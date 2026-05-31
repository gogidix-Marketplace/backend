import { Invoice } from '../../../domain/models/invoice.entity';
import { InvoiceItem } from '../../../domain/models/invoice-item.entity';
import { InvoiceStatus } from '../../../domain/models/invoice-validation.entity';
import { PaymentTerms } from '../../../domain/models/invoice.entity';
import { NotFoundException } from '../../../shared/exceptions/not-found.exception';
import { ValidationException } from '../../../shared/exceptions/validation.exception';

describe('Invoice Entity', () => {
  let invoice: Invoice;
  const tenantId = 'tenant-001';
  const organizationId = 'org-001';
  const invoiceNumber = 'INV-2024-001';
  const vendorId = 'vendor-001';
  const vendorName = 'Acme Supplies';
  const invoiceDate = new Date('2024-01-15');
  const dueDate = new Date('2024-02-15');

  beforeEach(() => {
    invoice = new Invoice(
      invoiceNumber,
      vendorId,
      vendorName,
      invoiceDate,
      dueDate,
      tenantId,
      organizationId
    );
  });

  describe('Creation', () => {
    it('should create invoice with default values', () => {
      expect(invoice.getInvoiceNumber()).toBe(invoiceNumber);
      expect(invoice.getVendorId()).toBe(vendorId);
      expect(invoice.getVendorName()).toBe(vendorName);
      expect(invoice.getInvoiceDate()).toEqual(invoiceDate);
      expect(invoice.getDueDate()).toEqual(dueDate);
      expect(invoice.getStatus()).toBe(InvoiceStatus.DRAFT);
      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.NET_30);
      expect(invoice.getCurrency()).toBe('USD');
      expect(invoice.getSubtotalAmount()).toBe(0);
      expect(invoice.getTaxAmount()).toBe(0);
      expect(invoice.getTotalAmount()).toBe(0);
      expect(invoice.getAmountPaid()).toBe(0);
      expect(invoice.getOutstandingAmount()).toBe(0);
      expect(invoice.getItems()).toEqual([]);
      expect(invoice.getOcrProcessed()).toBe(false);
    });

    it('should set tenant and organization IDs', () => {
      expect(invoice.getTenantId()).toBe(tenantId);
      expect(invoice.getOrganizationId()).toBe(organizationId);
    });

    it('should initialize with received date', () => {
      expect(invoice.getReceivedDate()).toBeInstanceOf(Date);
    });
  });

  describe('Status Transitions', () => {
    it('should transition from DRAFT to RECEIVED', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);

      expect(invoice.getStatus()).toBe(InvoiceStatus.RECEIVED);
    });

    it('should transition from RECEIVED to VALIDATING', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);

      expect(invoice.getStatus()).toBe(InvoiceStatus.VALIDATING);
    });

    it('should transition from VALIDATING to VALIDATED', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.VALIDATED);

      expect(invoice.getStatus()).toBe(InvoiceStatus.VALIDATED);
    });

    it('should transition from VALIDATED to PROCESSING', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.VALIDATED);
      invoice.transitionTo(InvoiceStatus.PROCESSING);

      expect(invoice.getStatus()).toBe(InvoiceStatus.PROCESSING);
    });

    it('should transition from PROCESSING to PROCESSED and set processed date', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.VALIDATED);
      invoice.transitionTo(InvoiceStatus.PROCESSING);
      invoice.transitionTo(InvoiceStatus.PROCESSED);

      expect(invoice.getStatus()).toBe(InvoiceStatus.PROCESSED);
      expect(invoice.getProcessedDate()).toBeInstanceOf(Date);
    });

    it('should allow transition from FAILED to VALIDATING', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.FAILED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);

      expect(invoice.getStatus()).toBe(InvoiceStatus.VALIDATING);
    });

    it('should transition to CANCELLED from any state', () => {
      invoice.transitionTo(InvoiceStatus.CANCELLED);

      expect(invoice.getStatus()).toBe(InvoiceStatus.CANCELLED);
    });

    it('should throw error for invalid status transition', () => {
      expect(() => {
        invoice.transitionTo(InvoiceStatus.PROCESSED);
      }).toThrow('Invalid status transition');
    });

    it('should not allow transitions from PROCESSED', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.VALIDATED);
      invoice.transitionTo(InvoiceStatus.PROCESSING);
      invoice.transitionTo(InvoiceStatus.PROCESSED);

      expect(() => {
        invoice.transitionTo(InvoiceStatus.VALIDATED);
      }).toThrow();
    });

    it('should not allow transitions from CANCELLED', () => {
      invoice.transitionTo(InvoiceStatus.CANCELLED);

      expect(() => {
        invoice.transitionTo(InvoiceStatus.RECEIVED);
      }).toThrow();
    });
  });

  describe('Item Management', () => {
    it('should add item to invoice in DRAFT status', () => {
      const item = new InvoiceItem(1, 'Office Supplies', 10, 50, 10);

      invoice.addItem(item);

      const items = invoice.getItems();
      expect(items).toHaveLength(1);
      expect(items[0].getLineNumber()).toBe(1);
      expect(items[0].getDescription()).toBe('Office Supplies');
      expect(invoice.getSubtotalAmount()).toBe(500);
      expect(invoice.getTaxAmount()).toBe(50);
      expect(invoice.getTotalAmount()).toBe(550);
    });

    it('should add item to invoice in RECEIVED status', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);

      const item = new InvoiceItem(1, 'Services', 5, 100, 0);

      invoice.addItem(item);

      expect(invoice.getItems()).toHaveLength(1);
    });

    it('should throw error when adding item to non-draft/received invoice', () => {
      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);

      const item = new InvoiceItem(1, 'Item', 1, 100, 0);

      expect(() => {
        invoice.addItem(item);
      }).toThrow('Cannot add items to invoice in current status');
    });

    it('should remove item from invoice', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 0);

      invoice.addItem(item);
      expect(invoice.getItems()).toHaveLength(1);

      invoice.removeItem(1);

      expect(invoice.getItems()).toHaveLength(0);
    });

    it('should renumber items after removal', () => {
      const item1 = new InvoiceItem(1, 'Item 1', 1, 100, 0);

      const item2 = new InvoiceItem(2, 'Item 2', 1, 200, 0);

      invoice.addItem(item1);
      invoice.addItem(item2);

      invoice.removeItem(1);

      const items = invoice.getItems();
      expect(items).toHaveLength(1);
      expect(items[0].getLineNumber()).toBe(1);
    });

    it('should recalculate amounts when items added', () => {
      const item1 = new InvoiceItem(1, 'Item 1', 2, 100, 10);

      const item2 = new InvoiceItem(2, 'Item 2', 3, 50, 5);

      invoice.addItem(item1);
      invoice.addItem(item2);

      expect(invoice.getSubtotalAmount()).toBe(350); // 2*100 + 3*50
      expect(invoice.getTaxAmount()).toBe(27.5); // 20 + 7.5
      expect(invoice.getTotalAmount()).toBe(377.5); // 350 + 27.5
    });
  });

  describe('Approval Workflow', () => {
    it('should approve validated invoice', () => {
      const approver = 'manager@example.com';

      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.VALIDATED);

      invoice.approve(approver);

      expect(invoice.getStatus()).toBe(InvoiceStatus.PROCESSING);
      expect(invoice.getApprovedBy()).toBe(approver);
      expect(invoice.getApprovedAt()).toBeInstanceOf(Date);
    });

    it('should throw error when approving non-validated invoice', () => {
      expect(() => {
        invoice.approve('manager@example.com');
      }).toThrow('Cannot approve invoice that is not validated');
    });

    it('should reject invoice with reason', () => {
      const rejecter = 'manager@example.com';
      const reason = 'Incorrect amount';

      invoice.transitionTo(InvoiceStatus.RECEIVED);
      invoice.transitionTo(InvoiceStatus.VALIDATING);
      invoice.transitionTo(InvoiceStatus.VALIDATED);
      invoice.transitionTo(InvoiceStatus.PROCESSING);

      invoice.reject(rejecter, reason);

      expect(invoice.getStatus()).toBe(InvoiceStatus.FAILED);
      expect(invoice.getRejectedBy()).toBe(rejecter);
      expect(invoice.getRejectedAt()).toBeInstanceOf(Date);
      expect(invoice.getRejectionReason()).toBe(reason);
    });
  });

  describe('Payment Tracking', () => {
    beforeEach(() => {
      const item = new InvoiceItem(1, 'Item 1', 1, 1000, 10);
      invoice.addItem(item);
    });

    it('should record payment and update outstanding amount', () => {
      const paymentAmount = 500;
      const paymentDate = new Date();

      invoice.recordPayment(paymentAmount, paymentDate);

      expect(invoice.getAmountPaid()).toBe(500);
      expect(invoice.getOutstandingAmount()).toBe(600); // 1100 - 500
    });

    it('should throw error when payment amount is negative', () => {
      expect(() => {
        invoice.recordPayment(-100, new Date());
      }).toThrow('Payment amount must be positive');
    });

    it('should throw error when payment exceeds outstanding balance', () => {
      expect(() => {
        invoice.recordPayment(2000, new Date());
      }).toThrow('Payment amount exceeds outstanding balance');
    });
  });

  describe('Validation', () => {
    it('should return true for valid invoice', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 0);

      invoice.addItem(item);

      expect(invoice.isValid()).toBe(true);
    });

    it('should return false for invoice without invoice number', () => {
      expect(invoice.isValid()).toBe(false); // No items added
    });

    it('should return false for invoice without items', () => {
      expect(invoice.isValid()).toBe(false);
    });

    it('should return false for invoice without items', () => {
      expect(invoice.isValid()).toBe(false);
    });

    it('should throw error when setting invalid invoice dates', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 0);
      invoice.addItem(item);

      expect(() => {
        invoice.setInvoiceDate(new Date('2024-02-15'));
        invoice.setDueDate(new Date('2024-01-15'));
      }).toThrow();
    });

    it('should throw error for invalid discount amount', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 10);
      invoice.addItem(item);

      expect(() => {
        invoice.setDiscountAmount(200);
      }).toThrow('Discount cannot exceed invoice total');
    });
  });

  describe('OCR Processing', () => {
    it('should mark invoice as OCR processed', () => {
      const confidence = 0.95;

      invoice.markOcrProcessed(confidence);

      expect(invoice.getOcrProcessed()).toBe(true);
      expect(invoice.getOcrConfidence()).toBe(confidence);
    });
  });

  describe('Setters with Validation', () => {
    it('should set invoice number', () => {
      invoice.setInvoiceNumber('INV-2024-002');

      expect(invoice.getInvoiceNumber()).toBe('INV-2024-002');
    });

    it('should throw error when setting empty invoice number', () => {
      expect(() => {
        invoice.setInvoiceNumber('');
      }).toThrow('Invoice number cannot be empty');
    });

    it('should set purchase order number', () => {
      invoice.setPurchaseOrderNumber('PO-12345');

      expect(invoice.getPurchaseOrderNumber()).toBe('PO-12345');
    });

    it('should set vendor tax ID', () => {
      invoice.setVendorTaxId('12-3456789');

      expect(invoice.getVendorTaxId()).toBe('12-3456789');
    });

    it('should set currency with valid ISO code', () => {
      invoice.setCurrency('EUR');

      expect(invoice.getCurrency()).toBe('EUR');
    });

    it('should throw error for invalid currency code', () => {
      expect(() => {
        invoice.setCurrency('US');
      }).toThrow('Currency must be a valid ISO 4217 code');
    });

    it('should throw error for lowercase currency code', () => {
      expect(() => {
        invoice.setCurrency('usd');
      }).toThrow('Currency must be a valid ISO 4217 code');
    });

    it('should set discount amount', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 10);
      invoice.addItem(item);

      invoice.setDiscountAmount(50);

      expect(invoice.getDiscountAmount()).toBe(50);
      expect(invoice.getTotalAmount()).toBe(60); // 100 - 50 + 10 (tax)
    });

    it('should throw error for negative discount', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 0);
      invoice.addItem(item);

      expect(() => {
        invoice.setDiscountAmount(-10);
      }).toThrow('Discount amount cannot be negative');
    });

    it('should set payment terms', () => {
      invoice.setPaymentTerms(PaymentTerms.NET_60);

      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.NET_60);
    });
  });

  describe('Attachments', () => {
    it('should add attachment', () => {
      const attachment = {
        name: 'invoice.pdf',
        url: 'https://storage.example.com/invoice.pdf',
        type: 'application/pdf',
        size: 12345
      };

      invoice.addAttachment(attachment);

      const attachments = invoice.getAttachments();
      expect(attachments).toHaveLength(1);
      expect(attachments[0]).toEqual(attachment);
    });

    it('should remove attachment by URL', () => {
      const attachment = {
        name: 'invoice.pdf',
        url: 'https://storage.example.com/invoice.pdf',
        type: 'application/pdf',
        size: 12345
      };

      invoice.addAttachment(attachment);
      invoice.removeAttachment('https://storage.example.com/invoice.pdf');

      expect(invoice.getAttachments()).toHaveLength(0);
    });
  });

  describe('Factory Methods', () => {
    it('should create invoice from object', () => {
      const obj = {
        invoiceNumber: 'INV-001',
        vendorId: 'vendor-001',
        vendorName: 'Test Vendor',
        invoiceDate: new Date('2024-01-15'),
        dueDate: new Date('2024-02-15'),
        tenantId: 'tenant-001',
        organizationId: 'org-001',
        items: [
          {
            lineNumber: 1,
            description: 'Item 1',
            quantity: 1,
            unitPrice: 100,
            taxRate: 10,
            subtotalAmount: 100,
            taxAmount: 10,
            totalAmount: 110
          }
        ],
        currency: 'USD'
      };

      const createdInvoice = Invoice.fromObject(obj);

      expect(createdInvoice.getInvoiceNumber()).toBe('INV-001');
      expect(createdInvoice.getItems()).toHaveLength(1);
      expect(createdInvoice.getCurrency()).toBe('USD');
    });

    it('should convert invoice to object', () => {
      const item = new InvoiceItem(1, 'Item 1', 1, 100, 0);
      invoice.addItem(item);

      const obj = invoice.toObject();

      expect(obj.invoiceNumber).toBe(invoiceNumber);
      expect(obj.vendorId).toBe(vendorId);
      expect(obj.vendorName).toBe(vendorName);
      expect(obj.items).toHaveLength(1);
    });
  });

  describe('Notes and Internal Notes', () => {
    it('should set notes', () => {
      invoice.setNotes('Please process by EOM');

      expect(invoice.getNotes()).toBe('Please process by EOM');
    });

    it('should set internal notes', () => {
      invoice.setInternalNotes('For review by manager');

      expect(invoice.getInternalNotes()).toBe('For review by manager');
    });
  });

  describe('Categorization', () => {
    it('should set category', () => {
      invoice.setCategory('Office Supplies');

      expect(invoice.getCategory()).toBe('Office Supplies');
    });

    it('should set GL account code', () => {
      invoice.setGlAccountCode('6000');

      expect(invoice.getGlAccountCode()).toBe('6000');
    });

    it('should set cost center', () => {
      invoice.setCostCenter('CC-001');

      expect(invoice.getCostCenter()).toBe('CC-001');
    });
  });

  describe('Date Validation', () => {
    it('should throw error when invoice date is after due date', () => {
      expect(() => {
        invoice.setInvoiceDate(new Date('2024-03-01'));
      }).toThrow('Invoice date cannot be after due date');
    });

    it('should throw error when due date is before invoice date', () => {
      expect(() => {
        invoice.setDueDate(new Date('2024-01-01'));
      }).toThrow('Due date cannot be before invoice date');
    });
  });

  describe('All Payment Terms', () => {
    it('should support all payment terms', () => {
      invoice.setPaymentTerms(PaymentTerms.NET_15);
      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.NET_15);

      invoice.setPaymentTerms(PaymentTerms.NET_45);
      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.NET_45);

      invoice.setPaymentTerms(PaymentTerms.DUE_ON_RECEIPT);
      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.DUE_ON_RECEIPT);

      invoice.setPaymentTerms(PaymentTerms.END_OF_MONTH);
      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.END_OF_MONTH);

      invoice.setPaymentTerms(PaymentTerms.CUSTOM);
      expect(invoice.getPaymentTerms()).toBe(PaymentTerms.CUSTOM);
    });
  });
});
