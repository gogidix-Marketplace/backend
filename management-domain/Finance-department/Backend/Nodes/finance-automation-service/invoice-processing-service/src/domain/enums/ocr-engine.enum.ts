/**
 * OCR engine types supported by the invoice processing service
 */
export enum OcrEngineType {
  TESSERACT = 'tesseract',
  GOOGLE_VISION = 'google-vision',
  AZURE_FORM_RECOGNIZER = 'azure-form-recognizer',
  AWS_TEXTRACT = 'aws-textract',
}

/**
 * OCR processing status
 */
export enum OcrStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  PARTIAL = 'PARTIAL',
}

/**
 * Supported file formats for OCR processing
 */
export enum OcrFileFormat {
  PDF = 'pdf',
  PNG = 'png',
  JPEG = 'jpeg',
  JPG = 'jpg',
  TIFF = 'tiff',
  BMP = 'bmp',
  GIF = 'gif',
}
