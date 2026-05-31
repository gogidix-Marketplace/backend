import { Schema } from 'mongoose';

export const EmployeePayrollSchema = new Schema({
  payrollId: { type: String, required: true, index: true },
  employeeId: { type: String, required: true },
  tenantId: { type: String, required: true, index: true },
  basicSalary: { type: Number, required: true },
  dearnessAllowance: { type: Number, default: 0 },
  houseRentAllowance: { type: Number, default: 0 },
  transportAllowance: { type: Number, default: 0 },
  medicalAllowance: { type: Number, default: 0 },
  grossEarnings: { type: Number, required: true },
  incomeTax: { type: Number, default: 0 },
  professionalTax: { type: Number, default: 0 },
  providentFund: { type: Number, default: 0 },
  totalDeductions: { type: Number, default: 0 },
  netPay: { type: Number, required: true },
  taxRegime: { type: String, enum: ['old', 'new'], default: 'new' },
  currency: { type: String, default: 'USD' },
}, { timestamps: true, collection: 'employee_payrolls' });

EmployeePayrollSchema.index({ payrollId: 1, employeeId: 1 }, { unique: true });
EmployeePayrollSchema.index({ tenantId: 1 });
