// Models
export * from './models';

// Enums (excluding InvoiceStatus which is in models)
export * from './enums/validation-rule-type.enum';
export * from './enums/ocr-engine.enum';

// Events
export * from './events';

// Ports - input first (excluding duplicates)
export type {
  IInvoiceCommand,
  CreateInvoiceCommand,
  UpdateInvoiceCommand,
  ProcessInvoiceCommand,
  ValidateInvoiceCommand,
  ApproveInvoiceCommand,
  RejectInvoiceCommand,
  CancelInvoiceCommand,
  RecordPaymentCommand,
  AddAttachmentCommand,
  InvoiceCommandResult,
} from './ports/input/invoice.command';

export type {
  IInvoiceQuery,
  GetByIdQuery,
  GetByInvoiceNumberQuery,
  FindAllQuery,
  FindByVendorQuery,
  FindByStatusQuery,
  FindByDateRangeQuery,
  SearchInvoicesQuery,
  GetStatisticsQuery,
  GetDashboardQuery,
  InvoiceDto,
  InvoiceItemDto,
  InvoiceStatistics,
  DashboardData,
  PaginatedResult,
} from './ports/input/invoice.query';

// Repositories
export type {
  IInvoiceRepository,
  IInvoiceValidationRepository,
  IOcrResultRepository,
} from './repositories/invoice-repository.interface';

// Output ports
export { IEventPublisher, EventTopic } from './ports/output/event-publisher.interface';
export type { IOcrService, OcrRequest, OcrProcessingResult } from './ports/output/ocr-service.interface';
export { OcrEngineType } from './enums/ocr-engine.enum';
