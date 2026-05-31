import {
  Controller,
  Post,
  UseInterceptors,
  UploadedFile,
  Body,
  BadRequestException,
  HttpStatus,
  HttpCode,
} from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { diskStorage } from 'multer';
import { extname } from 'path';
import { v4 as uuidv4 } from 'uuid';
import { InvoiceOcrService } from '../../application/services/invoice-ocr.service';
import { InvoiceCommandService } from '../../application/services/invoice-command.service';
import { UploadInvoiceDto, OcrOptionsDto } from '../../application/dto/requests';
import { RequestContext, RequestContextType, TenantIdInterceptor } from './interceptors/tenant.interceptor';
import { CreateInvoiceItemDto } from '../../application/dto/requests/create-invoice.dto';
import { InvoiceStatus } from '../../domain/models/invoice-validation.entity';
import { Invoice, PaymentTerms } from '../../domain/models/invoice.entity';
import { InvoiceItem } from '../../domain/models/invoice-item.entity';

/**
 * Multer configuration for file uploads
 */
const storage = diskStorage({
  destination: (req, file, cb) => {
    const uploadDir = process.env.UPLOAD_DIR || './uploads';
    cb(null, uploadDir);
  },
  filename: (req, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.random().toString(36).substring(2, 15);
    cb(null, `${file.fieldname}-${uniqueSuffix}${extname(file.originalname)}`);
  },
});

/**
 * File filter for allowed types
 */
const fileFilter = (req: any, file: any, cb: any) => {
  const allowedTypes = process.env.ALLOWED_FILE_TYPES?.split(',') || [
    'application/pdf',
    'image/jpeg',
    'image/png',
    'image/tiff',
  ];

  if (allowedTypes.includes(file.mimetype)) {
    cb(null, true);
  } else {
    cb(new BadRequestException(`File type ${file.mimetype} is not allowed`), false);
  }
};

/**
 * Controller for invoice file uploads and OCR processing
 */
@Controller('upload')
@UseInterceptors(TenantIdInterceptor)
export class UploadController {
  constructor(
    private readonly ocrService: InvoiceOcrService,
    private readonly commandService: InvoiceCommandService,
  ) {}

  /**
   * Upload an invoice file and optionally process with OCR
   */
  @Post('invoice')
  @UseInterceptors(
    FileInterceptor('file', {
      storage,
      fileFilter,
      limits: {
        fileSize: parseInt(process.env.MAX_FILE_SIZE || '10485760', 10), // 10MB default
      },
    }),
  )
  @HttpCode(HttpStatus.OK)
  async uploadInvoice(
    @UploadedFile() file: Express.Multer.File,
    @Body() body: UploadInvoiceDto & { ocrOptions?: string },
    @RequestContext() context: RequestContextType,
  ) {
    if (!file) {
      throw new BadRequestException('No file uploaded');
    }

    this.logUpload(file, context);

    // Process file with OCR if enabled
    const ocrOptions: OcrOptionsDto = body.ocrOptions
      ? JSON.parse(body.ocrOptions)
      : { engine: 'tesseract' as const };

    let ocrResult;
    let invoiceData: Partial<Invoice> = {};

    if (ocrOptions) {
      // Process OCR
      const result = await this.ocrService.processInvoiceFile(
        uuidv4(),
        file.originalname,
        file.mimetype,
        file.size,
        file.path,
      );

      if (result.success && result.extractedData) {
        invoiceData = this.extractInvoiceData(result.extractedData);
      }
    }

    // Create invoice from uploaded data
    const invoice = await this.createInvoiceFromUpload(
      file,
      body,
      invoiceData,
      context,
    );

    return {
      success: true,
      invoice: {
        id: invoice.invoiceId,
        invoiceNumber: invoice.invoiceNumber,
        status: invoice.status,
        ocrProcessed: ocrResult?.success || false,
        ocrConfidence: ocrResult?.confidence,
      },
      file: {
        originalName: file.originalname,
        filename: file.filename,
        size: file.size,
        mimetype: file.mimetype,
      },
    };
  }

  /**
   * Process only OCR for an uploaded file
   */
  @Post('ocr-only')
  @UseInterceptors(
    FileInterceptor('file', {
      storage,
      fileFilter,
      limits: {
        fileSize: parseInt(process.env.MAX_FILE_SIZE || '10485760', 10),
      },
    }),
  )
  @HttpCode(HttpStatus.OK)
  async processOcrOnly(
    @UploadedFile() file: Express.Multer.File,
    @Body() body: { ocrOptions?: string },
  ) {
    if (!file) {
      throw new BadRequestException('No file uploaded');
    }

    const ocrOptions: OcrOptionsDto = body.ocrOptions
      ? JSON.parse(body.ocrOptions)
      : { engine: 'tesseract' as const };

    const result = await this.ocrService.processInvoiceFile(
      uuidv4(),
      file.originalname,
      file.mimetype,
      file.size,
      file.path,
    );

    return {
      success: result.success,
      confidence: result.confidence,
      extractedData: result.extractedData,
      rawText: result.rawText?.substring(0, 1000), // First 1000 chars
      errors: result.errors,
    };
  }

  private async createInvoiceFromUpload(
    file: Express.Multer.File,
    body: UploadInvoiceDto,
    extractedData: any,
    context: RequestContextType,
  ) {
    // Generate invoice number if not provided
    const invoiceNumber = `INV-${Date.now()}`;

    // Create invoice items (empty or from OCR)
    const items: CreateInvoiceItemDto[] = extractedData.lineItems || [];

    // If no items from OCR, create a placeholder item
    if (items.length === 0) {
      items.push({
        description: 'Uploaded invoice - pending review',
        quantity: 1,
        unitPrice: 0,
        taxRate: 0,
      });
    }

    const command = {
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      invoiceNumber,
      vendorId: body.vendorId,
      vendorName: body.vendorName,
      invoiceDate: extractedData.invoiceDate || new Date(),
      dueDate: extractedData.dueDate || new Date(Date.now() + 30 * 24 * 60 * 60 * 1000), // +30 days
      paymentTerms: PaymentTerms.NET_30,
      currency: 'USD',
      category: body.category,
      glAccountCode: body.glAccountCode,
      costCenter: body.costCenter,
      items,
      notes: body.notes,
      internalNotes: 'Created from file upload',
    };

    const result = await this.commandService.createInvoice(command);

    // Add attachment
    if (result.invoiceId) {
      await this.commandService.addAttachment({
        invoiceId: result.invoiceId,
        tenantId: context.tenantId,
        organizationId: context.organizationId,
        userId: context.userId,
        correlationId: context.correlationId,
        attachment: {
          name: file.originalname,
          url: file.path,
          type: file.mimetype,
          size: file.size,
        },
      });
    }

    return result;
  }

  private extractInvoiceData(ocrData: any): Partial<any> {
    return {
      invoiceNumber: ocrData.invoiceNumber,
      vendorName: ocrData.vendorName,
      invoiceDate: ocrData.invoiceDate,
      dueDate: ocrData.dueDate,
      totalAmount: ocrData.totalAmount,
      taxAmount: ocrData.taxAmount,
      lineItems: ocrData.lineItems?.map((item: any) => ({
        description: item.description,
        quantity: item.quantity,
        unitPrice: item.unitPrice,
        taxRate: 0,
      })) || [],
    };
  }

  private logUpload(file: Express.Multer.File, context: RequestContextType): void {
    console.log({
      event: 'file_upload',
      tenantId: context.tenantId,
      userId: context.userId,
      fileName: file.originalname,
      fileSize: file.size,
      mimeType: file.mimetype,
      timestamp: new Date().toISOString(),
    });
  }
}
