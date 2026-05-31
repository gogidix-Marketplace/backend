import { BaseEntity } from '../../shared/base/base.entity';
import { InvoiceItem } from './invoice-item.entity';
import { InvoiceStatus } from './invoice-validation.entity';

// Re-export InvoiceStatus for external use
export { InvoiceStatus };

/**
 * Payment terms enumeration
 */
export enum PaymentTerms {
  NET_15 = 'NET_15',
  NET_30 = 'NET_30',
  NET_45 = 'NET_45',
  NET_60 = 'NET_60',
  DUE_ON_RECEIPT = 'DUE_ON_RECEIPT',
  END_OF_MONTH = 'END_OF_MONTH',
  CUSTOM = 'CUSTOM',
}

/**
 * Invoice entity - core domain model
 * Represents an invoice in the system
 */
export class Invoice extends BaseEntity {
  private invoiceNumber: string;
  private purchaseOrderNumber?: string;
  private vendorId: string;
  private vendorName: string;
  private vendorTaxId?: string;
  private invoiceDate: Date;
  private dueDate: Date;
  private paymentTerms: PaymentTerms;
  private currency: string;
  private subtotalAmount: number;
  private taxAmount: number;
  private discountAmount: number;
  private totalAmount: number;
  private amountPaid: number;
  private outstandingAmount: number;
  private status: InvoiceStatus;
  private items: InvoiceItem[];
  private notes?: string;
  private internalNotes?: string;
  private receivedDate: Date;
  private processedDate?: Date;
  private tenantId: string;
  private organizationId: string;
  private category?: string;
  private glAccountCode?: string;
  private costCenter?: string;
  private attachments: Array<{ name: string; url: string; type: string; size: number }>;
  private approvedBy?: string;
  private approvedAt?: Date;
  private rejectedBy?: string;
  private rejectedAt?: Date;
  private rejectionReason?: string;
  private ocrProcessed: boolean;
  private ocrConfidence?: number;

  constructor(
    invoiceNumber: string,
    vendorId: string,
    vendorName: string,
    invoiceDate: Date,
    dueDate: Date,
    tenantId: string,
    organizationId: string,
  ) {
    super();
    this.invoiceNumber = invoiceNumber;
    this.vendorId = vendorId;
    this.vendorName = vendorName;
    this.invoiceDate = invoiceDate;
    this.dueDate = dueDate;
    this.paymentTerms = PaymentTerms.NET_30;
    this.currency = 'USD';
    this.subtotalAmount = 0;
    this.taxAmount = 0;
    this.discountAmount = 0;
    this.totalAmount = 0;
    this.amountPaid = 0;
    this.outstandingAmount = 0;
    this.status = InvoiceStatus.DRAFT;
    this.items = [];
    this.receivedDate = new Date();
    this.tenantId = tenantId;
    this.organizationId = organizationId;
    this.attachments = [];
    this.ocrProcessed = false;
  }

  // Status transitions
  transitionTo(status: InvoiceStatus, reason?: string): void {
    const validTransitions: Record<InvoiceStatus, InvoiceStatus[]> = {
      [InvoiceStatus.DRAFT]: [InvoiceStatus.RECEIVED, InvoiceStatus.CANCELLED],
      [InvoiceStatus.RECEIVED]: [InvoiceStatus.VALIDATING, InvoiceStatus.CANCELLED],
      [InvoiceStatus.VALIDATING]: [InvoiceStatus.VALIDATED, InvoiceStatus.FAILED, InvoiceStatus.CANCELLED],
      [InvoiceStatus.VALIDATED]: [InvoiceStatus.PROCESSING, InvoiceStatus.CANCELLED],
      [InvoiceStatus.PROCESSING]: [InvoiceStatus.PROCESSED, InvoiceStatus.FAILED, InvoiceStatus.CANCELLED],
      [InvoiceStatus.PROCESSED]: [], // Final state
      [InvoiceStatus.FAILED]: [InvoiceStatus.VALIDATING, InvoiceStatus.CANCELLED],
      [InvoiceStatus.CANCELLED]: [], // Final state
    };

    const allowedTransitions = validTransitions[this.status];
    if (!allowedTransitions.includes(status)) {
      throw new Error(
        `Invalid status transition from ${this.status} to ${status}. ` +
        `Allowed transitions: ${allowedTransitions.join(', ')}`,
      );
    }

    this.status = status;
    if (status === InvoiceStatus.PROCESSED) {
      this.processedDate = new Date();
    }
    this.markAsUpdated();
  }

  // Item management
  addItem(item: InvoiceItem): void {
    if (this.status !== InvoiceStatus.DRAFT && this.status !== InvoiceStatus.RECEIVED) {
      throw new Error('Cannot add items to invoice in current status');
    }
    item.setLineNumber(this.items.length + 1);
    this.items.push(item);
    this.recalculateAmounts();
    this.markAsUpdated();
  }

  removeItem(lineNumber: number): void {
    if (this.status !== InvoiceStatus.DRAFT && this.status !== InvoiceStatus.RECEIVED) {
      throw new Error('Cannot remove items from invoice in current status');
    }
    this.items = this.items.filter(item => item.getLineNumber() !== lineNumber);
    // Renumber items
    this.items.forEach((item, index) => item.setLineNumber(index + 1));
    this.recalculateAmounts();
    this.markAsUpdated();
  }

  private recalculateAmounts(): void {
    this.subtotalAmount = this.items.reduce((sum, item) => sum + item.getSubtotalAmount(), 0);
    this.taxAmount = this.items.reduce((sum, item) => sum + item.getTaxAmount(), 0);
    this.totalAmount = this.subtotalAmount + this.taxAmount - this.discountAmount;
    this.outstandingAmount = this.totalAmount - this.amountPaid;
  }

  // Approval workflow
  approve(approvedBy: string): void {
    if (this.status !== InvoiceStatus.VALIDATED) {
      throw new Error('Cannot approve invoice that is not validated');
    }
    this.approvedBy = approvedBy;
    this.approvedAt = new Date();
    this.transitionTo(InvoiceStatus.PROCESSING);
  }

  reject(rejectedBy: string, reason: string): void {
    this.rejectedBy = rejectedBy;
    this.rejectedAt = new Date();
    this.rejectionReason = reason;
    this.transitionTo(InvoiceStatus.FAILED);
  }

  // Payment tracking
  recordPayment(amount: number, paymentDate: Date): void {
    if (amount <= 0) {
      throw new Error('Payment amount must be positive');
    }
    if (this.amountPaid + amount > this.totalAmount) {
      throw new Error('Payment amount exceeds outstanding balance');
    }
    this.amountPaid += amount;
    this.outstandingAmount = this.totalAmount - this.amountPaid;
    this.markAsUpdated();
  }

  // OCR processing
  markOcrProcessed(confidence: number): void {
    this.ocrProcessed = true;
    this.ocrConfidence = confidence;
    this.markAsUpdated();
  }

  // Validation
  isValid(): boolean {
    return (
      this.invoiceNumber.length > 0 &&
      this.vendorId.length > 0 &&
      this.vendorName.length > 0 &&
      this.items.length > 0 &&
      this.totalAmount >= 0 &&
      this.invoiceDate <= this.dueDate
    );
  }

  // Getters
  get id(): string | undefined {
    return this._id;
  }

  set id(value: string | undefined) {
    this._id = value;
  }

  getInvoiceNumber(): string {
    return this.invoiceNumber;
  }

  getPurchaseOrderNumber(): string | undefined {
    return this.purchaseOrderNumber;
  }

  getVendorId(): string {
    return this.vendorId;
  }

  getVendorName(): string {
    return this.vendorName;
  }

  getVendorTaxId(): string | undefined {
    return this.vendorTaxId;
  }

  getInvoiceDate(): Date {
    return this.invoiceDate;
  }

  getDueDate(): Date {
    return this.dueDate;
  }

  getPaymentTerms(): PaymentTerms {
    return this.paymentTerms;
  }

  getCurrency(): string {
    return this.currency;
  }

  getSubtotalAmount(): number {
    return this.subtotalAmount;
  }

  getTaxAmount(): number {
    return this.taxAmount;
  }

  getDiscountAmount(): number {
    return this.discountAmount;
  }

  getTotalAmount(): number {
    return this.totalAmount;
  }

  getAmountPaid(): number {
    return this.amountPaid;
  }

  getOutstandingAmount(): number {
    return this.outstandingAmount;
  }

  getStatus(): InvoiceStatus {
    return this.status;
  }

  getItems(): InvoiceItem[] {
    return [...this.items];
  }

  getNotes(): string | undefined {
    return this.notes;
  }

  getInternalNotes(): string | undefined {
    return this.internalNotes;
  }

  getReceivedDate(): Date {
    return this.receivedDate;
  }

  getProcessedDate(): Date | undefined {
    return this.processedDate;
  }

  getTenantId(): string {
    return this.tenantId;
  }

  getOrganizationId(): string {
    return this.organizationId;
  }

  getCategory(): string | undefined {
    return this.category;
  }

  getGlAccountCode(): string | undefined {
    return this.glAccountCode;
  }

  getCostCenter(): string | undefined {
    return this.costCenter;
  }

  getAttachments(): Array<{ name: string; url: string; type: string; size: number }> {
    return [...this.attachments];
  }

  getApprovedBy(): string | undefined {
    return this.approvedBy;
  }

  getApprovedAt(): Date | undefined {
    return this.approvedAt;
  }

  getRejectedBy(): string | undefined {
    return this.rejectedBy;
  }

  getRejectedAt(): Date | undefined {
    return this.rejectedAt;
  }

  getRejectionReason(): string | undefined {
    return this.rejectionReason;
  }

  getOcrProcessed(): boolean {
    return this.ocrProcessed;
  }

  getOcrConfidence(): number | undefined {
    return this.ocrConfidence;
  }

  // Setters with validation
  setInvoiceNumber(invoiceNumber: string): void {
    if (!invoiceNumber || invoiceNumber.trim().length === 0) {
      throw new Error('Invoice number cannot be empty');
    }
    this.invoiceNumber = invoiceNumber;
    this.markAsUpdated();
  }

  setPurchaseOrderNumber(poNumber: string): void {
    this.purchaseOrderNumber = poNumber;
    this.markAsUpdated();
  }

  setVendorId(vendorId: string): void {
    if (!vendorId || vendorId.trim().length === 0) {
      throw new Error('Vendor ID cannot be empty');
    }
    this.vendorId = vendorId;
    this.markAsUpdated();
  }

  setVendorName(vendorName: string): void {
    if (!vendorName || vendorName.trim().length === 0) {
      throw new Error('Vendor name cannot be empty');
    }
    this.vendorName = vendorName;
    this.markAsUpdated();
  }

  setVendorTaxId(taxId: string): void {
    this.vendorTaxId = taxId;
    this.markAsUpdated();
  }

  setInvoiceDate(date: Date): void {
    if (date > this.dueDate) {
      throw new Error('Invoice date cannot be after due date');
    }
    this.invoiceDate = date;
    this.markAsUpdated();
  }

  setDueDate(date: Date): void {
    if (date < this.invoiceDate) {
      throw new Error('Due date cannot be before invoice date');
    }
    this.dueDate = date;
    this.markAsUpdated();
  }

  setPaymentTerms(terms: PaymentTerms): void {
    this.paymentTerms = terms;
    this.markAsUpdated();
  }

  setCurrency(currency: string): void {
    if (!/^[A-Z]{3}$/.test(currency)) {
      throw new Error('Currency must be a valid ISO 4217 code');
    }
    this.currency = currency;
    this.markAsUpdated();
  }

  setDiscountAmount(amount: number): void {
    if (amount < 0) {
      throw new Error('Discount amount cannot be negative');
    }
    if (amount > this.subtotalAmount + this.taxAmount) {
      throw new Error('Discount cannot exceed invoice total');
    }
    this.discountAmount = amount;
    this.recalculateAmounts();
    this.markAsUpdated();
  }

  setNotes(notes: string): void {
    this.notes = notes;
    this.markAsUpdated();
  }

  setInternalNotes(notes: string): void {
    this.internalNotes = notes;
    this.markAsUpdated();
  }

  setCategory(category: string): void {
    this.category = category;
    this.markAsUpdated();
  }

  setGlAccountCode(code: string): void {
    this.glAccountCode = code;
    this.markAsUpdated();
  }

  setCostCenter(costCenter: string): void {
    this.costCenter = costCenter;
    this.markAsUpdated();
  }

  addAttachment(attachment: { name: string; url: string; type: string; size: number }): void {
    this.attachments.push(attachment);
    this.markAsUpdated();
  }

  removeAttachment(url: string): void {
    this.attachments = this.attachments.filter(a => a.url !== url);
    this.markAsUpdated();
  }

  // Factory method
  static fromObject(obj: Record<string, unknown>): Invoice {
    const invoice = new Invoice(
      obj.invoiceNumber as string,
      obj.vendorId as string,
      obj.vendorName as string,
      new Date(obj.invoiceDate as Date),
      new Date(obj.dueDate as Date),
      obj.tenantId as string,
      obj.organizationId as string,
    );

    if (obj.id) invoice.id = obj.id as string;
    if (obj.purchaseOrderNumber) invoice.setPurchaseOrderNumber(obj.purchaseOrderNumber as string);
    if (obj.vendorTaxId) invoice.setVendorTaxId(obj.vendorTaxId as string);
    if (obj.paymentTerms) invoice.setPaymentTerms(obj.paymentTerms as PaymentTerms);
    if (obj.currency) invoice.setCurrency(obj.currency as string);
    if (obj.discountAmount) invoice.setDiscountAmount(obj.discountAmount as number);
    if (obj.notes) invoice.setNotes(obj.notes as string);
    if (obj.internalNotes) invoice.setInternalNotes(obj.internalNotes as string);
    if (obj.category) invoice.setCategory(obj.category as string);
    if (obj.glAccountCode) invoice.setGlAccountCode(obj.glAccountCode as string);
    if (obj.costCenter) invoice.setCostCenter(obj.costCenter as string);

    if (obj.items && Array.isArray(obj.items)) {
      (obj.items as Record<string, unknown>[]).forEach(itemObj => {
        invoice.addItem(InvoiceItem.fromObject(itemObj));
      });
    }

    return invoice;
  }

  toObject(): Record<string, unknown> {
    return {
      _id: this.id,
      invoiceNumber: this.invoiceNumber,
      purchaseOrderNumber: this.purchaseOrderNumber,
      vendorId: this.vendorId,
      vendorName: this.vendorName,
      vendorTaxId: this.vendorTaxId,
      invoiceDate: this.invoiceDate,
      dueDate: this.dueDate,
      paymentTerms: this.paymentTerms,
      currency: this.currency,
      subtotalAmount: this.subtotalAmount,
      taxAmount: this.taxAmount,
      discountAmount: this.discountAmount,
      totalAmount: this.totalAmount,
      amountPaid: this.amountPaid,
      outstandingAmount: this.outstandingAmount,
      status: this.status,
      items: this.items.map(item => item.toObject()),
      notes: this.notes,
      internalNotes: this.internalNotes,
      receivedDate: this.receivedDate,
      processedDate: this.processedDate,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      category: this.category,
      glAccountCode: this.glAccountCode,
      costCenter: this.costCenter,
      attachments: this.attachments,
      approvedBy: this.approvedBy,
      approvedAt: this.approvedAt,
      rejectedBy: this.rejectedBy,
      rejectedAt: this.rejectedAt,
      rejectionReason: this.rejectionReason,
      ocrProcessed: this.ocrProcessed,
      ocrConfidence: this.ocrConfidence,
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
      version: this.getVersion,
    };
  }
}
