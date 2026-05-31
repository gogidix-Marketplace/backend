import { Schema } from 'mongoose';

export const PayrollSchema = new Schema({
  tenantId: { type: String, required: true, index: true },
  periodStart: { type: Date, required: true },
  periodEnd: { type: Date, required: true },
  status: { type: String, enum: ['draft', 'pending', 'processing', 'completed', 'failed', 'cancelled'], default: 'pending' },
  totalEmployees: { type: Number, default: 0 },
  processedEmployees: { type: Number, default: 0 },
  totalGrossPay: { type: Number, default: 0 },
  totalTaxes: { type: Number, default: 0 },
  totalDeductions: { type: Number, default: 0 },
  totalNetPay: { type: Number, default: 0 },
  currency: { type: String, default: 'USD' },
}, { timestamps: true, collection: 'payrolls' });

PayrollSchema.index({ tenantId: 1, status: 1 });
PayrollSchema.index({ createdAt: -1 });
