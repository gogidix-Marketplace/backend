import { Injectable, Logger, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Types } from 'mongoose';
import { Invoice } from '../../../domain/models/invoice.entity';
import { InvoiceItem } from '../../../domain/models/invoice-item.entity';
import {
  IInvoiceRepository,
  IInvoiceValidationRepository,
  FindOptions,
  SearchCriteria,
  InvoiceStatistics,
} from '../../../domain/ports/output/invoice-repository.interface';
import { InvoiceValidation } from '../../../domain/models/invoice-validation.entity';

/**
 * Mongoose schema for Invoice
 */
import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';

@Schema({ collection: 'invoices' })
export class InvoiceDocument {
  @Prop({ required: true })
  invoiceNumber!: string;

  @Prop()
  purchaseOrderNumber?: string;

  @Prop({ required: true })
  vendorId!: string;

  @Prop({ required: true })
  vendorName!: string;

  @Prop()
  vendorTaxId?: string;

  @Prop({ required: true })
  invoiceDate!: Date;

  @Prop({ required: true })
  dueDate!: Date;

  @Prop({ required: true })
  paymentTerms!: string;

  @Prop({ required: true, default: 'USD' })
  currency!: string;

  @Prop({ required: true, default: 0 })
  subtotalAmount!: number;

  @Prop({ required: true, default: 0 })
  taxAmount!: number;

  @Prop({ required: true, default: 0 })
  discountAmount!: number;

  @Prop({ required: true, default: 0 })
  totalAmount!: number;

  @Prop({ required: true, default: 0 })
  amountPaid!: number;

  @Prop({ required: true, default: 0 })
  outstandingAmount!: number;

  @Prop({ required: true })
  status!: string;

  @Prop({ type: [{ lineNumber: Number, description: String, quantity: Number, unitPrice: Number, taxRate: Number, taxAmount: Number, discountAmount: Number, totalAmount: Number, subtotal: Number, sku: String, unitOfMeasure: String }] })
  items!: Array<{
    lineNumber: number;
    description: string;
    quantity: number;
    unitPrice: number;
    taxRate: number;
    taxAmount: number;
    discountAmount: number;
    totalAmount: number;
    subtotal: number;
    sku?: string;
    unitOfMeasure: string;
  }>;

  @Prop()
  notes?: string;

  @Prop()
  internalNotes?: string;

  @Prop({ required: true })
  receivedDate!: Date;

  @Prop()
  processedDate?: Date;

  @Prop({ required: true })
  tenantId!: string;

  @Prop({ required: true })
  organizationId!: string;

  @Prop()
  category?: string;

  @Prop()
  glAccountCode?: string;

  @Prop()
  costCenter?: string;

  @Prop({ type: [{ name: String, url: String, type: String, size: Number }] })
  attachments!: Array<{ name: string; url: string; type: string; size: number }>;

  @Prop()
  approvedBy?: string;

  @Prop()
  approvedAt?: Date;

  @Prop()
  rejectedBy?: string;

  @Prop()
  rejectedAt?: Date;

  @Prop()
  rejectionReason?: string;

  @Prop({ required: true, default: false })
  ocrProcessed!: boolean;

  @Prop()
  ocrConfidence?: number;

  @Prop({ required: true })
  createdAt!: Date;

  @Prop({ required: true })
  updatedAt!: Date;

  @Prop({ required: true, default: 0 })
  version!: number;

  @Prop()
  deletedAt?: Date;
}

export const InvoiceSchema = SchemaFactory.createForClass(InvoiceDocument);

// Indexes
InvoiceSchema.index({ tenantId: 1, organizationId: 1 });
InvoiceSchema.index({ tenantId: 1, invoiceNumber: 1 }, { unique: true });
InvoiceSchema.index({ vendorId: 1 });
InvoiceSchema.index({ status: 1 });
InvoiceSchema.index({ invoiceDate: 1, dueDate: 1 });
InvoiceSchema.index({ tenantId: 1, status: 1, dueDate: 1 });

/**
 * Mongoose schema for InvoiceValidation
 */
@Schema({ collection: 'invoice_validations' })
export class InvoiceValidationDocument {
  @Prop({ required: true, unique: true })
  invoiceId!: string;

  @Prop({ required: true })
  validatedAt!: Date;

  @Prop({ required: true })
  validatedBy!: string;

  @Prop({ type: [{ ruleName: String, passed: Boolean, message: String, severity: String }] })
  validationResults!: Array<{
    ruleName: string;
    passed: boolean;
    message: string;
    severity: string;
  }>;

  @Prop({ required: true, enum: ['PASSED', 'FAILED', 'WARNING'] })
  overallStatus!: 'PASSED' | 'FAILED' | 'WARNING';

  @Prop({ required: true })
  validatorVersion!: string;

  @Prop({ type: Map })
  validationContext!: Map<string, unknown>;

  @Prop({ required: true })
  createdAt!: Date;

  @Prop({ required: true })
  updatedAt!: Date;
}

export const InvoiceValidationSchema = SchemaFactory.createForClass(InvoiceValidationDocument);

InvoiceValidationSchema.index({ invoiceId: 1, validatedAt: -1 });

/**
 * MongoDB implementation of InvoiceRepository
 */
@Injectable()
export class MongoInvoiceRepository implements IInvoiceRepository {
  private readonly logger = new Logger(MongoInvoiceRepository.name);

  constructor(
    @InjectModel(InvoiceDocument.name)
    private readonly invoiceModel: Model<InvoiceDocument>,
  ) {}

  async save(invoice: Invoice): Promise<Invoice> {
    this.logger.log(`Saving invoice ${invoice.getInvoiceNumber()}`);

    const doc = new this.invoiceModel(invoice.toObject());
    const savedDoc = await doc.save();

    return this.documentToEntity(savedDoc);
  }

  async update(invoice: Invoice): Promise<Invoice> {
    this.logger.log(`Updating invoice ${invoice.getInvoiceNumber()}`);

    const updateData = { ...invoice.toObject() };
    delete updateData._id; // Don't update the ID

    const updatedDoc = await this.invoiceModel
      .findByIdAndUpdate(invoice.id, updateData, { new: true })
      .exec();

    if (!updatedDoc) {
      throw new NotFoundException('Invoice not found for update');
    }

    return this.documentToEntity(updatedDoc);
  }

  async findById(id: string | Types.ObjectId): Promise<Invoice | null> {
    const doc = await this.invoiceModel.findById(id as unknown as Types.ObjectId).exec();
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByInvoiceNumber(invoiceNumber: string, tenantId: string): Promise<Invoice | null> {
    const doc = await this.invoiceModel
      .findOne({ invoiceNumber, tenantId, deletedAt: null })
      .exec();
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByTenantId(tenantId: string, options?: FindOptions): Promise<Invoice[]> {
    const query = this.invoiceModel.find({ tenantId, deletedAt: null });

    this.applyFindOptions(query, options);

    const docs = await query.lean().exec();
    return docs.map(doc => this.documentToEntity(doc));
  }

  async findByVendorId(vendorId: string, tenantId: string, options?: FindOptions): Promise<Invoice[]> {
    const query = this.invoiceModel.find({ vendorId, tenantId, deletedAt: null });

    this.applyFindOptions(query, options);

    const docs = await query.lean().exec();
    return docs.map(doc => this.documentToEntity(doc));
  }

  async findByStatus(status: string, tenantId: string, options?: FindOptions): Promise<Invoice[]> {
    const query = this.invoiceModel.find({ status, tenantId, deletedAt: null });

    this.applyFindOptions(query, options);

    const docs = await query.lean().exec();
    return docs.map(doc => this.documentToEntity(doc));
  }

  async findByDateRange(
    startDate: Date,
    endDate: Date,
    tenantId: string,
    dateField: 'invoiceDate' | 'dueDate' | 'receivedDate' = 'invoiceDate',
    options?: FindOptions,
  ): Promise<Invoice[]> {
    const query = this.invoiceModel.find({
      tenantId,
      deletedAt: null,
      [dateField]: { $gte: startDate, $lte: endDate },
    });

    this.applyFindOptions(query, options);

    const docs = await query.lean().exec();
    return docs.map(doc => this.documentToEntity(doc));
  }

  async search(criteria: SearchCriteria, options?: FindOptions): Promise<Invoice[]> {
    const filter: Record<string, unknown> = {
      tenantId: criteria.tenantId,
      deletedAt: null,
    };

    if (criteria.organizationId) {
      filter.organizationId = criteria.organizationId;
    }
    if (criteria.vendorId) {
      filter.vendorId = criteria.vendorId;
    }
    if (criteria.status) {
      filter.status = criteria.status;
    }
    if (criteria.category) {
      filter.category = criteria.category;
    }
    if (criteria.costCenter) {
      filter.costCenter = criteria.costCenter;
    }
    if (criteria.glAccountCode) {
      filter.glAccountCode = criteria.glAccountCode;
    }
    if (criteria.startDate || criteria.endDate) {
      filter.invoiceDate = {};
      if (criteria.startDate) {
        filter.invoiceDate = { ...filter.invoiceDate as object, $gte: criteria.startDate };
      }
      if (criteria.endDate) {
        filter.invoiceDate = { ...filter.invoiceDate as object, $lte: criteria.endDate };
      }
    }
    if (criteria.minAmount !== undefined || criteria.maxAmount !== undefined) {
      filter.totalAmount = {};
      if (criteria.minAmount !== undefined) {
        filter.totalAmount = { ...filter.totalAmount as object, $gte: criteria.minAmount };
      }
      if (criteria.maxAmount !== undefined) {
        filter.totalAmount = { ...filter.totalAmount as object, $lte: criteria.maxAmount };
      }
    }

    // Text search
    if (criteria.searchTerm) {
      filter.$or = [
        { invoiceNumber: { $regex: criteria.searchTerm, $options: 'i' } },
        { vendorName: { $regex: criteria.searchTerm, $options: 'i' } },
        { notes: { $regex: criteria.searchTerm, $options: 'i' } },
        { purchaseOrderNumber: { $regex: criteria.searchTerm, $options: 'i' } },
      ];
    }

    const query = this.invoiceModel.find(filter);
    this.applyFindOptions(query, options);

    const docs = await query.lean().exec();
    return docs.map(doc => this.documentToEntity(doc));
  }

  async count(criteria: SearchCriteria): Promise<number> {
    const filter: Record<string, unknown> = {
      tenantId: criteria.tenantId,
      deletedAt: null,
    };

    if (criteria.organizationId) {
      filter.organizationId = criteria.organizationId;
    }
    if (criteria.vendorId) {
      filter.vendorId = criteria.vendorId;
    }
    if (criteria.status) {
      filter.status = criteria.status;
    }

    return this.invoiceModel.countDocuments(filter).exec();
  }

  async delete(id: string | Types.ObjectId): Promise<void> {
    await this.invoiceModel.findByIdAndUpdate(id as unknown as Types.ObjectId, { deletedAt: new Date() }).exec();
  }

  async existsByInvoiceNumber(invoiceNumber: string, tenantId: string): Promise<boolean> {
    const count = await this.invoiceModel
      .countDocuments({ invoiceNumber, tenantId, deletedAt: null })
      .exec();
    return count > 0;
  }

  async getStatistics(tenantId: string, organizationId?: string): Promise<InvoiceStatistics> {
    const matchFilter: Record<string, unknown> = {
      tenantId,
      deletedAt: null,
    };

    if (organizationId) {
      matchFilter.organizationId = organizationId;
    }

    const [
      totalResult,
      statusBreakdown,
      vendorBreakdown,
    ] = await Promise.all([
      this.invoiceModel.aggregate([
        { $match: matchFilter },
        {
          $group: {
            _id: null,
            totalInvoices: { $sum: 1 },
            totalAmount: { $sum: '$totalAmount' },
            outstandingAmount: { $sum: '$outstandingAmount' },
          },
        },
      ]).exec(),
      this.invoiceModel.aggregate([
        { $match: matchFilter },
        {
          $group: {
            _id: '$status',
            count: { $sum: 1 },
          },
        },
      ]).exec(),
      this.invoiceModel.aggregate([
        { $match: matchFilter },
        {
          $group: {
            _id: { vendorId: '$vendorId', vendorName: '$vendorName' },
            count: { $sum: 1 },
            totalAmount: { $sum: '$totalAmount' },
          },
        },
        { $sort: { totalAmount: -1 } },
        { $limit: 20 },
      ]).exec(),
    ]);

    const totalData = totalResult[0] || { totalInvoices: 0, totalAmount: 0, outstandingAmount: 0 };

    const statusCounts: Record<string, number> = {};
    statusBreakdown.forEach((item: { _id: string; count: number }) => {
      statusCounts[item._id] = item.count;
    });

    const vendorCounts = vendorBreakdown.map((item: { _id: { vendorId: string; vendorName: string }; count: number; totalAmount: number }) => ({
      vendorId: item._id.vendorId,
      vendorName: item._id.vendorName,
      count: item.count,
      totalAmount: item.totalAmount,
    }));

    // Calculate overdue amount (simplified)
    const today = new Date();
    const overdueResult = await this.invoiceModel.aggregate([
      {
        $match: {
          ...matchFilter,
          dueDate: { $lt: today },
          status: { $in: ['RECEIVED', 'VALIDATING', 'VALIDATED'] },
        },
      },
      {
        $group: {
          _id: null,
          overdueAmount: { $sum: '$outstandingAmount' },
        },
      },
    ]).exec();

    return {
      totalInvoices: totalData.totalInvoices,
      totalAmount: totalData.totalAmount,
      outstandingAmount: totalData.outstandingAmount,
      overdueAmount: overdueResult[0]?.overdueAmount || 0,
      averageAmount: totalData.totalInvoices > 0 ? totalData.totalAmount / totalData.totalInvoices : 0,
      statusCounts,
      vendorCounts,
    };
  }

  private applyFindOptions(query: any, options?: FindOptions): void {
    if (!options) return;

    if (options.sortBy) {
      const sortOrder = options.sortOrder === 'ASC' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    }

    if (options.page && options.pageSize) {
      query.skip((options.page - 1) * options.pageSize).limit(options.pageSize);
    } else if (options.pageSize) {
      query.limit(options.pageSize);
    }
  }

  private documentToEntity(doc: any): Invoice {
    const invoice = new Invoice(
      doc.invoiceNumber,
      doc.vendorId,
      doc.vendorName,
      new Date(doc.invoiceDate),
      new Date(doc.dueDate),
      doc.tenantId,
      doc.organizationId,
    );

    invoice.id = doc._id;
    invoice.setPurchaseOrderNumber(doc.purchaseOrderNumber);
    invoice.setVendorTaxId(doc.vendorTaxId);
    invoice.setPaymentTerms(doc.paymentTerms as any);
    invoice.setCurrency(doc.currency);
    invoice.setDiscountAmount(doc.discountAmount);
    invoice.setNotes(doc.notes);
    invoice.setInternalNotes(doc.internalNotes);
    invoice.setCategory(doc.category);
    invoice.setGlAccountCode(doc.glAccountCode);
    invoice.setCostCenter(doc.costCenter);

    // Add items
    if (doc.items && Array.isArray(doc.items)) {
      doc.items.forEach((itemData: any) => {
        const item = new InvoiceItem(
          itemData.lineNumber,
          itemData.description,
          itemData.quantity,
          itemData.unitPrice,
          itemData.taxRate,
          itemData.unitOfMeasure || 'EA',
          itemData.sku,
        );
        if (itemData.discountAmount) {
          item.setDiscountAmount(itemData.discountAmount);
        }
        invoice.addItem(item);
      });
    }

    // Direct property setting for internal state
    (invoice as any)._id = doc._id;
    (invoice as any).createdAt = new Date(doc.createdAt);
    (invoice as any).updatedAt = new Date(doc.updatedAt);
    (invoice as any).version = doc.version;
    (invoice as any).deletedAt = doc.deletedAt ? new Date(doc.deletedAt) : undefined;
    (invoice as any).status = doc.status;
    (invoice as any).amountPaid = doc.amountPaid;
    (invoice as any).receivedDate = new Date(doc.receivedDate);
    (invoice as any).processedDate = doc.processedDate ? new Date(doc.processedDate) : undefined;
    (invoice as any).attachments = doc.attachments || [];
    (invoice as any).approvedBy = doc.approvedBy;
    (invoice as any).approvedAt = doc.approvedAt ? new Date(doc.approvedAt) : undefined;
    (invoice as any).rejectedBy = doc.rejectedBy;
    (invoice as any).rejectedAt = doc.rejectedAt ? new Date(doc.rejectedAt) : undefined;
    (invoice as any).rejectionReason = doc.rejectionReason;
    (invoice as any).ocrProcessed = doc.ocrProcessed;
    (invoice as any).ocrConfidence = doc.ocrConfidence;

    return invoice;
  }
}

/**
 * MongoDB implementation of InvoiceValidationRepository
 */
@Injectable()
export class MongoInvoiceValidationRepository implements IInvoiceValidationRepository {
  private readonly logger = new Logger(MongoInvoiceValidationRepository.name);

  constructor(
    @InjectModel(InvoiceValidationDocument.name)
    private readonly validationModel: Model<InvoiceValidationDocument>,
  ) {}

  async save(validation: InvoiceValidation): Promise<InvoiceValidation> {
    this.logger.log(`Saving validation for invoice ${validation.getInvoiceId()}`);

    const doc = new this.validationModel({
      invoiceId: validation.getInvoiceId(),
      validatedAt: validation.getValidatedAt(),
      validatedBy: validation.getValidatedBy(),
      validationResults: validation.getValidationResults(),
      overallStatus: validation.getOverallStatus(),
      validatorVersion: validation.getValidatorVersion(),
      validationContext: validation.getValidationContext(),
      createdAt: validation.createdAtDate,
      updatedAt: validation.updatedAtDate,
    });

    const savedDoc = await doc.save();
    return this.documentToEntity(savedDoc);
  }

  async findByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null> {
    const doc = await this.validationModel
      .findOne({ invoiceId })
      .sort({ validatedAt: -1 })
      .exec();
    return doc ? this.documentToEntity(doc) : null;
  }

  async findLatestByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null> {
    return this.findByInvoiceId(invoiceId);
  }

  async findAllByInvoiceId(invoiceId: string): Promise<InvoiceValidation[]> {
    const docs = await this.validationModel
      .find({ invoiceId })
      .sort({ validatedAt: -1 })
      .exec();
    return docs.map(doc => this.documentToEntity(doc));
  }

  private documentToEntity(doc: any): InvoiceValidation {
    const validation = new InvoiceValidation(
      doc.invoiceId,
      doc.validatedBy,
    );

    validation.addValidationResults(doc.validationResults);
    validation.setValidatorVersion(doc.validatorVersion);
    validation.setValidationContext(doc.validationContext as Record<string, unknown>);

    // Direct property setting for internal state
    (validation as any)._id = doc._id;
    (validation as any).createdAt = new Date(doc.createdAt);
    (validation as any).updatedAt = new Date(doc.updatedAt);

    return validation;
  }
}
