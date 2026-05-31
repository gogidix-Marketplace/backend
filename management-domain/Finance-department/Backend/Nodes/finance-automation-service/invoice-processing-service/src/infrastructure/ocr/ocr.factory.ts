import { Injectable, Inject } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { IOcrService } from '../../domain/ports/output/ocr-service.interface';
import { OcrEngineType } from '../../domain/enums/ocr-engine.enum';
import { TesseractOcrService } from './tesseract-ocr.service';
import { GoogleVisionOcrService } from './google-vision-ocr.service';

/**
 * Factory for creating OCR service instances
 * Allows switching between OCR engines based on configuration
 */
@Injectable()
export class OcrServiceFactory {
  private engines: Map<OcrEngineType, IOcrService> = new Map();

  constructor(
    private readonly configService: ConfigService,
    private readonly tesseractService: TesseractOcrService,
    private readonly googleVisionService: GoogleVisionOcrService,
  ) {
    this.registerEngines();
  }

  private registerEngines(): void {
    this.engines.set(OcrEngineType.TESSERACT, this.tesseractService);
    this.engines.set(OcrEngineType.GOOGLE_VISION, this.googleVisionService);
  }

  /**
   * Get OCR service by engine type
   */
  getService(engineType?: OcrEngineType): IOcrService {
    const requestedType = engineType || this.getDefaultEngineType();
    const service = this.engines.get(requestedType);

    if (!service) {
      throw new Error(`OCR engine not found: ${requestedType}`);
    }

    if (!service.isAvailable()) {
      // Fallback to Tesseract if requested engine is not available
      if (requestedType !== OcrEngineType.TESSERACT && this.tesseractService.isAvailable()) {
        return this.tesseractService;
      }
      throw new Error(`OCR engine not available: ${requestedType}`);
    }

    return service;
  }

  /**
   * Get the default OCR engine type from configuration
   */
  getDefaultEngineType(): OcrEngineType {
    const config = this.configService.get<string>('OCR_ENGINE', 'tesseract');
    return config as OcrEngineType || OcrEngineType.TESSERACT;
  }

  /**
   * Check if an engine is available
   */
  isEngineAvailable(engineType: OcrEngineType): boolean {
    const service = this.engines.get(engineType);
    return service ? service.isAvailable() : false;
  }

  /**
   * Get all available engines
   */
  getAvailableEngines(): OcrEngineType[] {
    const available: OcrEngineType[] = [];
    for (const [type, service] of this.engines) {
      if (service.isAvailable()) {
        available.push(type);
      }
    }
    return available;
  }

  /**
   * Get supported file types across all engines
   */
  getSupportedFileTypes(): string[] {
    const fileTypes = new Set<string>();
    for (const service of this.engines.values()) {
      if (service.isAvailable()) {
        service.getSupportedFileTypes().forEach(type => fileTypes.add(type));
      }
    }
    return Array.from(fileTypes);
  }
}
