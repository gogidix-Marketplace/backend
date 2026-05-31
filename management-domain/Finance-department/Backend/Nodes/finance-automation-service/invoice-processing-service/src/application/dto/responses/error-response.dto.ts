/**
 * Standard error response DTO
 */
export interface ErrorResponseDto {
  statusCode: number;
  message: string;
  errorCode: string;
  timestamp: string;
  path?: string;
  context?: Record<string, unknown>;
  violations?: ValidationErrorDto[];
}

/**
 * Validation error DTO
 */
export interface ValidationErrorDto {
  field: string;
  message: string;
  value?: unknown;
}

/**
 * Success response wrapper
 */
export interface SuccessResponseDto<T = unknown> {
  success: true;
  data: T;
  message?: string;
  timestamp: string;
}

/**
 * Command response DTO
 */
export interface CommandResponseDto {
  success: boolean;
  invoiceId?: string;
  invoiceNumber?: string;
  status?: string;
  message?: string;
  errors?: ValidationErrorDto[];
}
