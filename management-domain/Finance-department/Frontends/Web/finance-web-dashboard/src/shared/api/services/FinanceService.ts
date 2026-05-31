// ============================================
// FINANCE DEPARTMENT - FINANCE API SERVICE
// ============================================

import { apiClient, mockAdapter } from '../client'
import type {
  Budget,
  Expense,
  Invoice,
  Payment,
  FinanceDashboardData,
  BudgetFilters,
  ExpenseFilters,
  InvoiceFilters,
  PaginatedResponse,
} from '@domain'

const USE_MOCK = true // Set to false in production

export class FinanceService {
  private api = USE_MOCK ? mockAdapter : apiClient

  // Dashboard
  async getDashboardData(): Promise<FinanceDashboardData> {
    return this.api.get<FinanceDashboardData>('/dashboard')
  }

  // Budgets
  async getBudgets(
    page: number = 1,
    pageSize: number = 20,
    filters?: BudgetFilters
  ): Promise<PaginatedResponse<Budget>> {
    const params = new URLSearchParams({
      page: page.toString(),
      pageSize: pageSize.toString(),
    })

    if (filters) {
      if (filters.department) params.append('department', filters.department)
      if (filters.status) params.append('status', filters.status)
      if (filters.fiscalYear) params.append('fiscalYear', filters.fiscalYear.toString())
      if (filters.period) params.append('period', filters.period)
      if (filters.manager) params.append('manager', filters.manager)
      if (filters.search) params.append('search', filters.search)
    }

    return this.api.get<PaginatedResponse<Budget>>(`/budgets?${params.toString()}`)
  }

  async getBudget(id: string): Promise<Budget> {
    return this.api.get<Budget>(`/budgets/${id}`)
  }

  async createBudget(data: Partial<Budget>): Promise<Budget> {
    return this.api.post<Budget>('/budgets', data)
  }

  async updateBudget(id: string, data: Partial<Budget>): Promise<Budget> {
    return this.api.put<Budget>(`/budgets/${id}`, data)
  }

  async deleteBudget(id: string): Promise<void> {
    return this.api.delete<void>(`/budgets/${id}`)
  }

  async approveBudget(id: string, comment?: string): Promise<Budget> {
    return this.api.post<Budget>(`/budgets/${id}/approve`, { comment })
  }

  async rejectBudget(id: string, reason: string): Promise<Budget> {
    return this.api.post<Budget>(`/budgets/${id}/reject`, { reason })
  }

  // Expenses
  async getExpenses(
    page: number = 1,
    pageSize: number = 20,
    filters?: ExpenseFilters
  ): Promise<PaginatedResponse<Expense>> {
    const params = new URLSearchParams({
      page: page.toString(),
      pageSize: pageSize.toString(),
    })

    if (filters) {
      if (filters.department) params.append('department', filters.department)
      if (filters.category) params.append('category', filters.category)
      if (filters.status) params.append('status', filters.status)
      if (filters.submittedBy) params.append('submittedBy', filters.submittedBy)
      if (filters.dateFrom) params.append('dateFrom', filters.dateFrom)
      if (filters.dateTo) params.append('dateTo', filters.dateTo)
      if (filters.amountFrom) params.append('amountFrom', filters.amountFrom.toString())
      if (filters.amountTo) params.append('amountTo', filters.amountTo.toString())
      if (filters.reimbursable !== undefined) params.append('reimbursable', filters.reimbursable.toString())
      if (filters.search) params.append('search', filters.search)
    }

    return this.api.get<PaginatedResponse<Expense>>(`/expenses?${params.toString()}`)
  }

  async getExpense(id: string): Promise<Expense> {
    return this.api.get<Expense>(`/expenses/${id}`)
  }

  async createExpense(data: Partial<Expense>): Promise<Expense> {
    return this.api.post<Expense>('/expenses', data)
  }

  async updateExpense(id: string, data: Partial<Expense>): Promise<Expense> {
    return this.api.put<Expense>(`/expenses/${id}`, data)
  }

  async deleteExpense(id: string): Promise<void> {
    return this.api.delete<void>(`/expenses/${id}`)
  }

  async approveExpense(id: string, comment?: string): Promise<Expense> {
    return this.api.post<Expense>(`/expenses/${id}/approve`, { comment })
  }

  async rejectExpense(id: string, reason: string): Promise<Expense> {
    return this.api.post<Expense>(`/expenses/${id}/reject`, { reason })
  }

  async uploadExpenseReceipt(file: File): Promise<{ url: string; fileName: string }> {
    const formData = new FormData()
    formData.append('file', file)

    return apiClient.post<{ url: string; fileName: string }>('/expenses/receipts', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  }

  // Invoices
  async getInvoices(
    page: number = 1,
    pageSize: number = 20,
    filters?: InvoiceFilters
  ): Promise<PaginatedResponse<Invoice>> {
    const params = new URLSearchParams({
      page: page.toString(),
      pageSize: pageSize.toString(),
    })

    if (filters) {
      if (filters.type) params.append('type', filters.type)
      if (filters.status) params.append('status', filters.status)
      if (filters.vendor) params.append('vendor', filters.vendor)
      if (filters.customer) params.append('customer', filters.customer)
      if (filters.dateFrom) params.append('dateFrom', filters.dateFrom)
      if (filters.dateTo) params.append('dateTo', filters.dateTo)
      if (filters.amountFrom) params.append('amountFrom', filters.amountFrom.toString())
      if (filters.amountTo) params.append('amountTo', filters.amountTo.toString())
      if (filters.overdue !== undefined) params.append('overdue', filters.overdue.toString())
      if (filters.search) params.append('search', filters.search)
    }

    return this.api.get<PaginatedResponse<Invoice>>(`/invoices?${params.toString()}`)
  }

  async getInvoice(id: string): Promise<Invoice> {
    return this.api.get<Invoice>(`/invoices/${id}`)
  }

  async createInvoice(data: Partial<Invoice>): Promise<Invoice> {
    return this.api.post<Invoice>('/invoices', data)
  }

  async updateInvoice(id: string, data: Partial<Invoice>): Promise<Invoice> {
    return this.api.put<Invoice>(`/invoices/${id}`, data)
  }

  async deleteInvoice(id: string): Promise<void> {
    return this.api.delete<void>(`/invoices/${id}`)
  }

  async approveInvoice(id: string, comment?: string): Promise<Invoice> {
    return this.api.post<Invoice>(`/invoices/${id}/approve`, { comment })
  }

  async sendInvoice(id: string): Promise<Invoice> {
    return this.api.post<Invoice>(`/invoices/${id}/send`, {})
  }

  async markInvoicePaid(id: string, paymentData: Partial<Payment>): Promise<Invoice> {
    return this.api.post<Invoice>(`/invoices/${id}/pay`, paymentData)
  }

  // Payments
  async getPayments(
    page: number = 1,
    pageSize: number = 20,
    type?: 'incoming' | 'outgoing'
  ): Promise<PaginatedResponse<Payment>> {
    const params = new URLSearchParams({
      page: page.toString(),
      pageSize: pageSize.toString(),
    })

    if (type) params.append('type', type)

    return this.api.get<PaginatedResponse<Payment>>(`/payments?${params.toString()}`)
  }

  async getPayment(id: string): Promise<Payment> {
    return this.api.get<Payment>(`/payments/${id}`)
  }

  async createPayment(data: Partial<Payment>): Promise<Payment> {
    return this.api.post<Payment>('/payments', data)
  }

  async schedulePayment(data: Partial<Payment>): Promise<Payment> {
    return this.api.post<Payment>('/payments/schedule', data)
  }

  async cancelPayment(id: string): Promise<Payment> {
    return this.api.post<Payment>(`/payments/${id}/cancel`, {})
  }

  // Reports
  async generateReport(
    type: string,
    period: { start: string; end: string },
    format: 'pdf' | 'xlsx' | 'csv',
    options?: Record<string, unknown>
  ): Promise<{ reportId: string; status: string }> {
    return this.api.post<{ reportId: string; status: string }>('/reports/generate', {
      type,
      period,
      format,
      options,
    })
  }

  async getReports(): Promise<Array<{ id: string; name: string; type: string; createdAt: string; status: string }>> {
    return this.api.get('/reports')
  }

  async downloadReport(reportId: string): Promise<Blob> {
    return apiClient.get(`/reports/${reportId}/download`, { responseType: 'blob' })
  }

  // Currency
  async getCurrencyRates(baseCurrency: string = 'USD'): Promise<
    Array<{ currency: string; rate: number; timestamp: string }>
  > {
    return this.api.get(`/currencies/rates?base=${baseCurrency}`)
  }

  async convertCurrency(amount: number, from: string, to: string): Promise<{ amount: number; rate: number }> {
    return this.api.get(`/currencies/convert?amount=${amount}&from=${from}&to=${to}`)
  }
}

// Export singleton instance
export const financeService = new FinanceService()
