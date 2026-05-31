import { BaseEntity } from '@shared/base/base.entity';
import { Money } from './value-objects/money.value-object';
import { PaymentStatus } from './enums/payment-status.enum';

export interface VendorBankDetails {
  bankName: string;
  accountNumber: string;
  routingNumber?: string;
  swiftCode?: string;
  iban?: string;
  accountType?: 'CHECKING' | 'SAVINGS';
  address?: {
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  };
}

export interface VendorPaymentSettings {
  paymentTerms: number; // Days
  autoPayEnabled: boolean;
  preferredPaymentMethod: string;
  minimumPaymentAmount?: number;
  maximumPaymentAmount?: number;
  requireApprovalAbove?: number;
  currency: string;
}

export class VendorPayment extends BaseEntity {
  private _vendorId: string;
  private _vendorName: string;
  private _vendorCode: string;
  private _email: string;
  private _phone?: string;
  private _taxId?: string;
  private _bankDetails: VendorBankDetails;
  private _paymentSettings: VendorPaymentSettings;
  private _isActive: boolean;
  private _totalPaidAmount: Money;
  private _totalPaymentCount: number;
  private _lastPaymentDate?: Date;
  private _nextPaymentDate?: Date;
  private _metadata: Record<string, any>;
  private _creditScore?: number;
  private _paymentRating?: 'EXCELLENT' | 'GOOD' | 'AVERAGE' | 'POOR';

  private constructor(
    vendorId: string,
    vendorName: string,
    vendorCode: string,
    email: string,
    bankDetails: VendorBankDetails,
    paymentSettings: VendorPaymentSettings,
    tenantId: string,
  ) {
    super(tenantId);
    this._vendorId = vendorId;
    this._vendorName = vendorName;
    this._vendorCode = vendorCode;
    this._email = email;
    this._bankDetails = bankDetails;
    this._paymentSettings = paymentSettings;
    this._isActive = true;
    this._totalPaidAmount = Money.zero(paymentSettings.currency);
    this._totalPaymentCount = 0;
    this._metadata = {};
  }

  static create(
    vendorId: string,
    vendorName: string,
    vendorCode: string,
    email: string,
    bankDetails: VendorBankDetails,
    currency: string,
    tenantId: string,
  ): VendorPayment {
    const paymentSettings: VendorPaymentSettings = {
      paymentTerms: 30,
      autoPayEnabled: false,
      preferredPaymentMethod: 'BANK_TRANSFER',
      currency,
    };

    return new VendorPayment(
      vendorId,
      vendorName,
      vendorCode,
      email,
      bankDetails,
      paymentSettings,
      tenantId,
    );
  }

  // Getters
  get vendorId(): string {
    return this._vendorId;
  }

  get vendorName(): string {
    return this._vendorName;
  }

  get vendorCode(): string {
    return this._vendorCode;
  }

  get email(): string {
    return this._email;
  }

  get phone(): string | undefined {
    return this._phone;
  }

  get taxId(): string | undefined {
    return this._taxId;
  }

  get bankDetails(): VendorBankDetails {
    return { ...this._bankDetails };
  }

  get paymentSettings(): VendorPaymentSettings {
    return { ...this._paymentSettings };
  }

  get isActive(): boolean {
    return this._isActive;
  }

  get totalPaidAmount(): Money {
    return this._totalPaidAmount;
  }

  get totalPaymentCount(): number {
    return this._totalPaymentCount;
  }

  get lastPaymentDate(): Date | undefined {
    return this._lastPaymentDate;
  }

  get nextPaymentDate(): Date | undefined {
    return this._nextPaymentDate;
  }

  get metadata(): Record<string, any> {
    return { ...this._metadata };
  }

  get creditScore(): number | undefined {
    return this._creditScore;
  }

  get paymentRating(): 'EXCELLENT' | 'GOOD' | 'AVERAGE' | 'POOR' | undefined {
    return this._paymentRating;
  }

  // Business methods
  updateContactInfo(email: string, phone?: string): void {
    this._email = email;
    this._phone = phone;
    this.updateTimestamp();
  }

  updateBankDetails(bankDetails: VendorBankDetails): void {
    this._bankDetails = { ...this._bankDetails, ...bankDetails };
    this.updateTimestamp();
  }

  updatePaymentSettings(settings: Partial<VendorPaymentSettings>): void {
    this._paymentSettings = { ...this._paymentSettings, ...settings };
    this.updateTimestamp();
  }

  setPaymentTerms(days: number): void {
    if (days < 0 || days > 365) {
      throw new Error('Payment terms must be between 0 and 365 days');
    }
    this._paymentSettings.paymentTerms = days;
    this.updateTimestamp();
  }

  enableAutoPay(): void {
    this._paymentSettings.autoPayEnabled = true;
    this.updateTimestamp();
  }

  disableAutoPay(): void {
    this._paymentSettings.autoPayEnabled = false;
    this.updateTimestamp();
  }

  setPreferredPaymentMethod(method: string): void {
    this._paymentSettings.preferredPaymentMethod = method;
    this.updateTimestamp();
  }

  setPaymentThresholds(min?: number, max?: number, approvalThreshold?: number): void {
    if (min !== undefined && min < 0) {
      throw new Error('Minimum payment amount cannot be negative');
    }
    if (max !== undefined && min !== undefined && max < min) {
      throw new Error('Maximum payment amount must be greater than minimum');
    }
    this._paymentSettings.minimumPaymentAmount = min;
    this._paymentSettings.maximumPaymentAmount = max;
    this._paymentSettings.requireApprovalAbove = approvalThreshold;
    this.updateTimestamp();
  }

  recordPayment(amount: Money): void {
    if (amount.getCurrency() !== this._totalPaidAmount.getCurrency()) {
      throw new Error('Payment currency must match vendor currency');
    }
    this._totalPaidAmount = this._totalPaidAmount.add(amount);
    this._totalPaymentCount++;
    this._lastPaymentDate = new Date();
    this.calculateNextPaymentDate();
    this.updatePaymentRating();
    this.updateTimestamp();
  }

  calculateNextPaymentDate(): void {
    if (this._lastPaymentDate) {
      const nextDate = new Date(this._lastPaymentDate);
      nextDate.setDate(nextDate.getDate() + this._paymentSettings.paymentTerms);
      this._nextPaymentDate = nextDate;
    }
  }

  setCreditScore(score: number): void {
    if (score < 0 || score > 100) {
      throw new Error('Credit score must be between 0 and 100');
    }
    this._creditScore = score;
    this.updatePaymentRating();
    this.updateTimestamp();
  }

  private updatePaymentRating(): void {
    if (!this._creditScore) {
      this._paymentRating = undefined;
      return;
    }

    if (this._creditScore >= 90) {
      this._paymentRating = 'EXCELLENT';
    } else if (this._creditScore >= 75) {
      this._paymentRating = 'GOOD';
    } else if (this._creditScore >= 60) {
      this._paymentRating = 'AVERAGE';
    } else {
      this._paymentRating = 'POOR';
    }
  }

  activate(): void {
    this._isActive = true;
    this.updateTimestamp();
  }

  deactivate(): void {
    this._isActive = false;
    this.updateTimestamp();
  }

  isAutoPayEnabled(): boolean {
    return this._isActive && this._paymentSettings.autoPayEnabled;
  }

  requiresApproval(amount: Money): boolean {
    const threshold = this._paymentSettings.requireApprovalAbove;
    if (!threshold) return false;
    return amount.isGreaterThan(Money.create(threshold, this._paymentSettings.currency));
  }

  isWithinPaymentLimits(amount: Money): boolean {
    const min = this._paymentSettings.minimumPaymentAmount;
    const max = this._paymentSettings.maximumPaymentAmount;

    if (min && amount.isLessThan(Money.create(min, this._paymentSettings.currency))) {
      return false;
    }

    if (max && amount.isGreaterThan(Money.create(max, this._paymentSettings.currency))) {
      return false;
    }

    return true;
  }

  updateMetadata(metadata: Record<string, any>): void {
    this._metadata = { ...this._metadata, ...metadata };
    this.updateTimestamp();
  }

  toJSON(): Record<string, any> {
    return {
      id: this.id,
      vendorId: this._vendorId,
      vendorName: this._vendorName,
      vendorCode: this._vendorCode,
      email: this._email,
      phone: this._phone,
      taxId: this._taxId,
      bankDetails: this._bankDetails,
      paymentSettings: this._paymentSettings,
      isActive: this._isActive,
      totalPaidAmount: this._totalPaidAmount.toJSON(),
      totalPaymentCount: this._totalPaymentCount,
      lastPaymentDate: this._lastPaymentDate?.toISOString(),
      nextPaymentDate: this._nextPaymentDate?.toISOString(),
      metadata: this._metadata,
      creditScore: this._creditScore,
      paymentRating: this._paymentRating,
      createdAt: this.createdAt.toISOString(),
      updatedAt: this.updatedAt.toISOString(),
      tenantId: this.tenantId,
      version: this.version,
    };
  }

  static fromJSON(json: Record<string, any>): VendorPayment {
    const vendor = new VendorPayment(
      json.vendorId,
      json.vendorName,
      json.vendorCode,
      json.email,
      json.bankDetails,
      json.paymentSettings,
      json.tenantId,
    );

    vendor['id'] = json.id;
    vendor['_phone'] = json.phone;
    vendor['_taxId'] = json.taxId;
    vendor['_isActive'] = json.isActive;
    vendor['_totalPaidAmount'] = Money.fromJSON(json.totalPaidAmount);
    vendor['_totalPaymentCount'] = json.totalPaymentCount;
    vendor['_lastPaymentDate'] = json.lastPaymentDate ? new Date(json.lastPaymentDate) : undefined;
    vendor['_nextPaymentDate'] = json.nextPaymentDate ? new Date(json.nextPaymentDate) : undefined;
    vendor['_metadata'] = json.metadata;
    vendor['_creditScore'] = json.creditScore;
    vendor['_paymentRating'] = json.paymentRating;
    vendor['createdAt'] = new Date(json.createdAt);
    vendor['updatedAt'] = new Date(json.updatedAt);
    vendor['version'] = json.version;

    return vendor;
  }
}
