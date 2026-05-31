import { BaseEntity } from '@shared/base/base.entity';
import { Money } from './value-objects/money.value-object';
import { PaymentStatus } from './enums/payment-status.enum';
import { PaymentMethod } from './enums/payment-method.enum';

export interface PaymentMetadata {
  invoiceId?: string;
  invoiceNumber?: string;
  reference?: string;
  description?: string;
  category?: string;
  tags?: string[];
  attachments?: string[];
  notes?: string;
  [key: string]: any;
}

export class Payment extends BaseEntity {
  private _status: PaymentStatus;
  private _amount: Money;
  private _currency: string;
  private _paymentMethod: PaymentMethod;
  private _gateway: string;
  private _vendorId: string;
  private _vendorName: string;
  private _accountId: string;
  private _scheduledAt?: Date;
  private _processedAt?: Date;
  private _completedAt?: Date;
  private _failedAt?: Date;
  private _cancelledAt?: Date;
  private _metadata: PaymentMetadata;
  private _externalReference?: string;
  private _gatewayResponse?: any;
  private _failureReason?: string;
  private _retryCount: number;
  private _maxRetries: number;
  private _batchId?: string;
  private _reconciliationStatus: 'PENDING' | 'MATCHED' | 'UNMATCHED';
  private _reconciledAt?: Date;

  private constructor(
    amount: Money,
    vendorId: string,
    vendorName: string,
    accountId: string,
    paymentMethod: PaymentMethod,
    gateway: string,
    tenantId: string,
  ) {
    super(tenantId);
    this._amount = amount;
    this._currency = amount.getCurrency();
    this._vendorId = vendorId;
    this._vendorName = vendorName;
    this._accountId = accountId;
    this._paymentMethod = paymentMethod;
    this._gateway = gateway;
    this._status = PaymentStatus.PENDING;
    this._metadata = {};
    this._retryCount = 0;
    this._maxRetries = 3;
    this._reconciliationStatus = 'PENDING';
  }

  static create(
    amount: Money,
    vendorId: string,
    vendorName: string,
    accountId: string,
    paymentMethod: PaymentMethod,
    gateway: string,
    tenantId: string,
  ): Payment {
    return new Payment(
      amount,
      vendorId,
      vendorName,
      accountId,
      paymentMethod,
      gateway,
      tenantId,
    );
  }

  // Getters
  get status(): PaymentStatus {
    return this._status;
  }

  get amount(): Money {
    return this._amount;
  }

  get currency(): string {
    return this._currency;
  }

  get paymentMethod(): PaymentMethod {
    return this._paymentMethod;
  }

  get gateway(): string {
    return this._gateway;
  }

  get vendorId(): string {
    return this._vendorId;
  }

  get vendorName(): string {
    return this._vendorName;
  }

  get accountId(): string {
    return this._accountId;
  }

  get scheduledAt(): Date | undefined {
    return this._scheduledAt;
  }

  get processedAt(): Date | undefined {
    return this._processedAt;
  }

  get completedAt(): Date | undefined {
    return this._completedAt;
  }

  get failedAt(): Date | undefined {
    return this._failedAt;
  }

  get cancelledAt(): Date | undefined {
    return this._cancelledAt;
  }

  get metadata(): PaymentMetadata {
    return { ...this._metadata };
  }

  get externalReference(): string | undefined {
    return this._externalReference;
  }

  get gatewayResponse(): any {
    return this._gatewayResponse;
  }

  get failureReason(): string | undefined {
    return this._failureReason;
  }

  get retryCount(): number {
    return this._retryCount;
  }

  get maxRetries(): number {
    return this._maxRetries;
  }

  get batchId(): string | undefined {
    return this._batchId;
  }

  get reconciliationStatus(): 'PENDING' | 'MATCHED' | 'UNMATCHED' {
    return this._reconciliationStatus;
  }

  get reconciledAt(): Date | undefined {
    return this._reconciledAt;
  }

  // Business methods
  schedule(date: Date): void {
    if (this._status !== PaymentStatus.PENDING) {
      throw new Error('Only pending payments can be scheduled');
    }
    this._scheduledAt = date;
    this._status = PaymentStatus.SCHEDULED;
    this.updateTimestamp();
  }

  process(): void {
    if (this._status !== PaymentStatus.PENDING && this._status !== PaymentStatus.SCHEDULED) {
      throw new Error('Payment cannot be processed in current state');
    }
    if (this._scheduledAt && this._scheduledAt > new Date()) {
      throw new Error('Scheduled payment cannot be processed before schedule date');
    }
    this._status = PaymentStatus.PROCESSING;
    this._processedAt = new Date();
    this.updateTimestamp();
  }

  complete(externalReference: string, gatewayResponse?: any): void {
    if (this._status !== PaymentStatus.PROCESSING) {
      throw new Error('Only processing payments can be completed');
    }
    this._status = PaymentStatus.COMPLETED;
    this._completedAt = new Date();
    this._externalReference = externalReference;
    this._gatewayResponse = gatewayResponse;
    this.updateTimestamp();
  }

  fail(reason: string, gatewayResponse?: any): void {
    if (this._status !== PaymentStatus.PROCESSING) {
      throw new Error('Only processing payments can fail');
    }
    this._status = PaymentStatus.FAILED;
    this._failedAt = new Date();
    this._failureReason = reason;
    this._gatewayResponse = gatewayResponse;
    this.updateTimestamp();
  }

  cancel(reason: string): void {
    if (this._status === PaymentStatus.COMPLETED) {
      throw new Error('Completed payments cannot be cancelled');
    }
    this._status = PaymentStatus.CANCELLED;
    this._cancelledAt = new Date();
    this._metadata.cancelReason = reason;
    this.updateTimestamp();
  }

  retry(): void {
    if (this._status !== PaymentStatus.FAILED) {
      throw new Error('Only failed payments can be retried');
    }
    if (this._retryCount >= this._maxRetries) {
      throw new Error('Maximum retry attempts reached');
    }
    this._status = PaymentStatus.PENDING;
    this._retryCount++;
    this._failureReason = undefined;
    this.updateTimestamp();
  }

  canRetry(): boolean {
    return this._status === PaymentStatus.FAILED && this._retryCount < this._maxRetries;
  }

  assignToBatch(batchId: string): void {
    if (this._status !== PaymentStatus.PENDING) {
      throw new Error('Only pending payments can be assigned to a batch');
    }
    this._batchId = batchId;
    this.updateTimestamp();
  }

  setGateway(gateway: string): void {
    if (this._status !== PaymentStatus.PENDING) {
      throw new Error('Gateway can only be changed for pending payments');
    }
    this._gateway = gateway;
    this.updateTimestamp();
  }

  setPaymentMethod(method: PaymentMethod): void {
    if (this._status !== PaymentStatus.PENDING) {
      throw new Error('Payment method can only be changed for pending payments');
    }
    this._paymentMethod = method;
    this.updateTimestamp();
  }

  updateMetadata(metadata: Partial<PaymentMetadata>): void {
    this._metadata = { ...this._metadata, ...metadata };
    this.updateTimestamp();
  }

  setMaxRetries(maxRetries: number): void {
    if (maxRetries < 0 || maxRetries > 10) {
      throw new Error('Max retries must be between 0 and 10');
    }
    this._maxRetries = maxRetries;
    this.updateTimestamp();
  }

  markAsReconciled(matched: boolean): void {
    this._reconciliationStatus = matched ? 'MATCHED' : 'UNMATCHED';
    this._reconciledAt = new Date();
    this.updateTimestamp();
  }

  resetReconciliationStatus(): void {
    this._reconciliationStatus = 'PENDING';
    this._reconciledAt = undefined;
    this.updateTimestamp();
  }

  isPending(): boolean {
    return this._status === PaymentStatus.PENDING;
  }

  isScheduled(): boolean {
    return this._status === PaymentStatus.SCHEDULED;
  }

  isProcessing(): boolean {
    return this._status === PaymentStatus.PROCESSING;
  }

  isCompleted(): boolean {
    return this._status === PaymentStatus.COMPLETED;
  }

  isFailed(): boolean {
    return this._status === PaymentStatus.FAILED;
  }

  isCancelled(): boolean {
    return this._status === PaymentStatus.CANCELLED;
  }

  isFinalState(): boolean {
    return [PaymentStatus.COMPLETED, PaymentStatus.CANCELLED].includes(this._status);
  }

  isReconciled(): boolean {
    return this._reconciliationStatus === 'MATCHED';
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      status: this._status,
      amount: this._amount.toJSON(),
      currency: this._currency,
      paymentMethod: this._paymentMethod,
      gateway: this._gateway,
      vendorId: this._vendorId,
      vendorName: this._vendorName,
      accountId: this._accountId,
      scheduledAt: this._scheduledAt?.toISOString(),
      processedAt: this._processedAt?.toISOString(),
      completedAt: this._completedAt?.toISOString(),
      failedAt: this._failedAt?.toISOString(),
      cancelledAt: this._cancelledAt?.toISOString(),
      metadata: this._metadata,
      externalReference: this._externalReference,
      gatewayResponse: this._gatewayResponse,
      failureReason: this._failureReason,
      retryCount: this._retryCount,
      maxRetries: this._maxRetries,
      batchId: this._batchId,
      reconciliationStatus: this._reconciliationStatus,
      reconciledAt: this._reconciledAt?.toISOString(),
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): Payment {
    const money = Money.fromJSON(json.amount);
    const payment = new Payment(
      money,
      json.vendorId,
      json.vendorName,
      json.accountId,
      json.paymentMethod,
      json.gateway,
      json.tenantId,
    );

    payment['id'] = json.id;
    payment['_status'] = json.status;
    payment['_scheduledAt'] = json.scheduledAt ? new Date(json.scheduledAt) : undefined;
    payment['_processedAt'] = json.processedAt ? new Date(json.processedAt) : undefined;
    payment['_completedAt'] = json.completedAt ? new Date(json.completedAt) : undefined;
    payment['_failedAt'] = json.failedAt ? new Date(json.failedAt) : undefined;
    payment['_cancelledAt'] = json.cancelledAt ? new Date(json.cancelledAt) : undefined;
    payment['_metadata'] = json.metadata;
    payment['_externalReference'] = json.externalReference;
    payment['_gatewayResponse'] = json.gatewayResponse;
    payment['_failureReason'] = json.failureReason;
    payment['_retryCount'] = json.retryCount;
    payment['_maxRetries'] = json.maxRetries;
    payment['_batchId'] = json.batchId;
    payment['_reconciliationStatus'] = json.reconciliationStatus;
    payment['_reconciledAt'] = json.reconciledAt ? new Date(json.reconciledAt) : undefined;
    payment['createdAt'] = new Date(json.createdAt);
    payment['updatedAt'] = new Date(json.updatedAt);
    payment['version'] = json.version;

    return payment;
  }
}
