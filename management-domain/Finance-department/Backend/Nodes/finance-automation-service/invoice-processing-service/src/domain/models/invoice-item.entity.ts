import { BaseEntity } from '../../shared/base/base.entity';

/**
 * Invoice item entity representing a line item on an invoice
 */
export class InvoiceItem extends BaseEntity {
  private lineNumber: number;
  private description: string;
  private quantity: number;
  private unitPrice: number;
  private taxRate: number;
  private taxAmount: number;
  private discountAmount: number;
  private totalAmount: number;
  private sku?: string;
  private unitOfMeasure: string;

  constructor(
    lineNumber: number,
    description: string,
    quantity: number,
    unitPrice: number,
    taxRate: number,
    unitOfMeasure: string = 'EA',
    sku?: string,
  ) {
    super();
    this.lineNumber = lineNumber;
    this.description = description;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.taxRate = taxRate;
    this.unitOfMeasure = unitOfMeasure;
    this.sku = sku;
    this.discountAmount = 0;
    this.calculateAmounts();
  }

  private calculateAmounts(): void {
    const subtotal = this.quantity * this.unitPrice;
    this.taxAmount = subtotal * (this.taxRate / 100);
    this.totalAmount = subtotal + this.taxAmount - this.discountAmount;
  }

  // Getters
  getLineNumber(): number {
    return this.lineNumber;
  }

  setLineNumber(lineNumber: number): void {
    this.lineNumber = lineNumber;
  }

  getDescription(): string {
    return this.description;
  }

  getQuantity(): number {
    return this.quantity;
  }

  getUnitPrice(): number {
    return this.unitPrice;
  }

  getTaxRate(): number {
    return this.taxRate;
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

  getSubtotalAmount(): number {
    return this.quantity * this.unitPrice;
  }

  getSku(): string | undefined {
    return this.sku;
  }

  getUnitOfMeasure(): string {
    return this.unitOfMeasure;
  }

  // Setters with validation
  setDescription(description: string): void {
    if (!description || description.trim().length === 0) {
      throw new Error('Description cannot be empty');
    }
    this.description = description;
    this.markAsUpdated();
  }

  setQuantity(quantity: number): void {
    if (quantity <= 0) {
      throw new Error('Quantity must be greater than 0');
    }
    this.quantity = quantity;
    this.calculateAmounts();
    this.markAsUpdated();
  }

  setUnitPrice(unitPrice: number): void {
    if (unitPrice < 0) {
      throw new Error('Unit price cannot be negative');
    }
    this.unitPrice = unitPrice;
    this.calculateAmounts();
    this.markAsUpdated();
  }

  setTaxRate(taxRate: number): void {
    if (taxRate < 0 || taxRate > 100) {
      throw new Error('Tax rate must be between 0 and 100');
    }
    this.taxRate = taxRate;
    this.calculateAmounts();
    this.markAsUpdated();
  }

  setDiscountAmount(discountAmount: number): void {
    if (discountAmount < 0) {
      throw new Error('Discount amount cannot be negative');
    }
    const maxDiscount = this.getSubtotalAmount() + this.taxAmount;
    if (discountAmount > maxDiscount) {
      throw new Error('Discount cannot exceed line item total');
    }
    this.discountAmount = discountAmount;
    this.calculateAmounts();
    this.markAsUpdated();
  }

  // Factory method
  static fromObject(obj: Record<string, unknown>): InvoiceItem {
    const item = new InvoiceItem(
      obj.lineNumber as number,
      obj.description as string,
      obj.quantity as number,
      obj.unitPrice as number,
      obj.taxRate as number,
      obj.unitOfMeasure as string || 'EA',
      obj.sku as string | undefined,
    );
    if (obj.discountAmount) {
      item.setDiscountAmount(obj.discountAmount as number);
    }
    return item;
  }

  toObject(): Record<string, unknown> {
    return {
      lineNumber: this.lineNumber,
      description: this.description,
      quantity: this.quantity,
      unitPrice: this.unitPrice,
      taxRate: this.taxRate,
      taxAmount: this.taxAmount,
      discountAmount: this.discountAmount,
      totalAmount: this.totalAmount,
      sku: this.sku,
      unitOfMeasure: this.unitOfMeasure,
      subtotal: this.getSubtotalAmount(),
    };
  }
}
