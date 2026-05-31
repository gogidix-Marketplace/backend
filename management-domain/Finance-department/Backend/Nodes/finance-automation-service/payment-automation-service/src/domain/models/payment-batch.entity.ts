import { BaseEntity } from '@shared/base/base.entity';
import { Money } from './value-objects/money.value-object';
import { BatchStatus } from './enums/batch-status.enum';

export interface BatchSummary {
  totalPayments: number;
  totalAmount: Money;
  completedPayments: number;
  failedPayments: number;
  pendingPayments: number;
}

export class PaymentBatch extends BaseEntity {
  private _name: string;
  private _description: string;
  private _status: BatchStatus;
  private _paymentIds: string[];
  private _gateway: string;
  private _scheduledAt?: Date;
  private _processingStartedAt?: Date;
  private _completedAt?: Date;
  private _totalAmount: Money;
  private _currency: string;
  private _metadata: Record<string, any>;
  private _priority: number;
  private _maxConcurrent: number;
  private _createdBy: string;

  private constructor(
    name: string,
    gateway: string,
    currency: string,
    tenantId: string,
    createdBy: string,
  ) {
    super(tenantId);
    this._name = name;
    this._description = '';
    this._status = BatchStatus.DRAFT;
    this._paymentIds = [];
    this._gateway = gateway;
    this._totalAmount = Money.zero(currency);
    this._currency = currency;
    this._metadata = {};
    this._priority = 0;
    this._maxConcurrent = 10;
    this._createdBy = createdBy;
  }

  static create(
    name: string,
    gateway: string,
    currency: string,
    tenantId: string,
    createdBy: string,
  ): PaymentBatch {
    return new PaymentBatch(name, gateway, currency, tenantId, createdBy);
  }

  // Getters
  get name(): string {
    return this._name;
  }

  get description(): string {
    return this._description;
  }

  get status(): BatchStatus {
    return this._status;
  }

  get paymentIds(): string[] {
    return [...this._paymentIds];
  }

  get gateway(): string {
    return this._gateway;
  }

  get scheduledAt(): Date | undefined {
    return this._scheduledAt;
  }

  get processingStartedAt(): Date | undefined {
    return this._processingStartedAt;
  }

  get completedAt(): Date | undefined {
    return this._completedAt;
  }

  get totalAmount(): Money {
    return this._totalAmount;
  }

  get currency(): string {
    return this._currency;
  }

  get metadata(): Record<string, any> {
    return { ...this._metadata };
  }

  get priority(): number {
    return this._priority;
  }

  get maxConcurrent(): number {
    return this._maxConcurrent;
  }

  get createdBy(): string {
    return this._createdBy;
  }

  // Business methods
  addPayment(paymentId: string, amount: Money): void {
    if (this._status !== BatchStatus.DRAFT) {
      throw new Error('Payments can only be added to draft batches');
    }
    if (this._paymentIds.includes(paymentId)) {
      throw new Error('Payment already exists in batch');
    }
    if (amount.getCurrency() !== this._currency) {
      throw new Error('Payment currency must match batch currency');
    }
    this._paymentIds.push(paymentId);
    this._totalAmount = this._totalAmount.add(amount);
    this.updateTimestamp();
  }

  removePayment(paymentId: string, amount: Money): void {
    if (this._status !== BatchStatus.DRAFT) {
      throw new Error('Payments can only be removed from draft batches');
    }
    const index = this._paymentIds.indexOf(paymentId);
    if (index === -1) {
      throw new Error('Payment not found in batch');
    }
    this._paymentIds.splice(index, 1);
    this._totalAmount = this._totalAmount.subtract(amount);
    this.updateTimestamp();
  }

  setDescription(description: string): void {
    this._description = description;
    this.updateTimestamp();
  }

  setGateway(gateway: string): void {
    if (this._status !== BatchStatus.DRAFT) {
      throw new Error('Gateway can only be changed for draft batches');
    }
    this._gateway = gateway;
    this.updateTimestamp();
  }

  schedule(date: Date): void {
    if (this._status !== BatchStatus.DRAFT && this._status !== BatchStatus.PENDING) {
      throw new Error('Batch must be in draft or pending status to schedule');
    }
    this._scheduledAt = date;
    this._status = BatchStatus.PENDING;
    this.updateTimestamp();
  }

  startProcessing(): void {
    if (this._status !== BatchStatus.PENDING) {
      throw new Error('Only pending batches can start processing');
    }
    if (this._scheduledAt && this._scheduledAt > new Date()) {
      throw new Error('Batch cannot be processed before schedule date');
    }
    this._status = BatchStatus.PROCESSING;
    this._processingStartedAt = new Date();
    this.updateTimestamp();
  }

  complete(): void {
    if (this._status !== BatchStatus.PROCESSING) {
      throw new Error('Only processing batches can be completed');
    }
    this._status = BatchStatus.COMPLETED;
    this._completedAt = new Date();
    this.updateTimestamp();
  }

  markPartiallyCompleted(): void {
    if (this._status !== BatchStatus.PROCESSING) {
      throw new Error('Only processing batches can be marked as partially completed');
    }
    this._status = BatchStatus.PARTIALLY_COMPLETED;
    this.updateTimestamp();
  }

  fail(): void {
    if (this._status !== BatchStatus.PROCESSING) {
      throw new Error('Only processing batches can fail');
    }
    this._status = BatchStatus.FAILED;
    this.updateTimestamp();
  }

  cancel(): void {
    if (this._status === BatchStatus.COMPLETED || this._status === BatchStatus.PROCESSING) {
      throw new Error('Completed or processing batches cannot be cancelled');
    }
    this._status = BatchStatus.CANCELLED;
    this.updateTimestamp();
  }

  setPriority(priority: number): void {
    if (priority < 0 || priority > 100) {
      throw new Error('Priority must be between 0 and 100');
    }
    this._priority = priority;
    this.updateTimestamp();
  }

  setMaxConcurrent(max: number): void {
    if (max < 1 || max > 100) {
      throw new Error('Max concurrent must be between 1 and 100');
    }
    this._maxConcurrent = max;
    this.updateTimestamp();
  }

  updateMetadata(metadata: Record<string, any>): void {
    this._metadata = { ...this._metadata, ...metadata };
    this.updateTimestamp();
  }

  isEmpty(): boolean {
    return this._paymentIds.length === 0;
  }

  getPaymentCount(): number {
    return this._paymentIds.length;
  }

  isDraft(): boolean {
    return this._status === BatchStatus.DRAFT;
  }

  isPending(): boolean {
    return this._status === BatchStatus.PENDING;
  }

  isProcessing(): boolean {
    return this._status === BatchStatus.PROCESSING;
  }

  isCompleted(): boolean {
    return this._status === BatchStatus.COMPLETED;
  }

  isFinalState(): boolean {
    return [BatchStatus.COMPLETED, BatchStatus.FAILED, BatchStatus.CANCELLED].includes(
      this._status,
    );
  }

  getSummary(completionRate: number, failureRate: number): BatchSummary {
    return {
      totalPayments: this._paymentIds.length,
      totalAmount: this._totalAmount,
      completedPayments: Math.floor(this._paymentIds.length * completionRate),
      failedPayments: Math.floor(this._paymentIds.length * failureRate),
      pendingPayments:
        this._paymentIds.length -
        Math.floor(this._paymentIds.length * completionRate) -
        Math.floor(this._paymentIds.length * failureRate),
    };
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this._name,
      description: this._description,
      status: this._status,
      paymentIds: this._paymentIds,
      gateway: this._gateway,
      scheduledAt: this._scheduledAt?.toISOString(),
      processingStartedAt: this._processingStartedAt?.toISOString(),
      completedAt: this._completedAt?.toISOString(),
      totalAmount: this._totalAmount.toJSON(),
      currency: this._currency,
      metadata: this._metadata,
      priority: this._priority,
      maxConcurrent: this._maxConcurrent,
      createdBy: this._createdBy,
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): PaymentBatch {
    const batch = new PaymentBatch(
      json.name,
      json.gateway,
      json.currency,
      json.tenantId,
      json.createdBy,
    );

    batch['id'] = json.id;
    batch['_description'] = json.description;
    batch['_status'] = json.status;
    batch['_paymentIds'] = json.paymentIds;
    batch['_scheduledAt'] = json.scheduledAt ? new Date(json.scheduledAt) : undefined;
    batch['_processingStartedAt'] = json.processingStartedAt
      ? new Date(json.processingStartedAt)
      : undefined;
    batch['_completedAt'] = json.completedAt ? new Date(json.completedAt) : undefined;
    batch['_totalAmount'] = Money.fromJSON(json.totalAmount);
    batch['_metadata'] = json.metadata;
    batch['_priority'] = json.priority;
    batch['_maxConcurrent'] = json.maxConcurrent;
    batch['createdAt'] = new Date(json.createdAt);
    batch['updatedAt'] = new Date(json.updatedAt);
    batch['version'] = json.version;

    return batch;
  }
}
