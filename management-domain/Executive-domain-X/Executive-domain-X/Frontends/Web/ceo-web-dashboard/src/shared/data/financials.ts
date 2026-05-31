import type { BusinessUnitRevenue } from '../types'

export const plData = {
  totalRevenue: 48600000,
  totalOpEx: 35465700,
  ebitda: 13134300,
  ebitdaMargin: 27.0,
  netIncome: 13131000,
  netMargin: 27.0,
  units: [
    { unitId: 'courier' as const, revenue: 8200000, growth: 12, margin: 28.5, opex: 5862000, netProfit: 2337000, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'ecommerce' as const, revenue: 9800000, growth: 22, margin: 34.2, opex: 6448400, netProfit: 3351600, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'warehousing' as const, revenue: 5100000, growth: 8, margin: 22.1, opex: 3972900, netProfit: 1127100, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'air-freight' as const, revenue: 3800000, growth: 5, margin: 18.3, opex: 3104600, netProfit: 695400, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'ocean-shipping' as const, revenue: 4200000, growth: 3, margin: 21.7, opex: 3288600, netProfit: 911400, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'haulage' as const, revenue: 12800000, growth: 18, margin: 31.4, opex: 8780800, netProfit: 4019200, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'procurement' as const, revenue: 3200000, growth: -5, margin: 15.8, opex: 2694400, netProfit: 505600, currency: 'USD', period: 'Q1 2026' },
    { unitId: 'admin-core' as const, revenue: 1500000, growth: 10, margin: 12.0, opex: 1320000, netProfit: 180000, currency: 'USD', period: 'Q1 2026' },
  ] satisfies BusinessUnitRevenue[],
}

export const settlementPipeline: Array<{
  unitId: string
  unitName: string
  pendingAmount: number
  pendingCount: number
  nextBatchTime: string
}> = [
  { unitId: 'ecommerce', unitName: 'Gogidix E-Commerce', pendingAmount: 1850000, pendingCount: 342, nextBatchTime: '2026-04-22T18:00:00Z' },
  { unitId: 'courier', unitName: 'Gogidix Courier', pendingAmount: 620000, pendingCount: 89, nextBatchTime: '2026-04-22T16:30:00Z' },
  { unitId: 'haulage', unitName: 'Gogidix Haulage', pendingAmount: 940000, pendingCount: 156, nextBatchTime: '2026-04-22T20:00:00Z' },
  { unitId: 'warehousing', unitName: 'Gogidix Warehousing', pendingAmount: 380000, pendingCount: 64, nextBatchTime: '2026-04-23T08:00:00Z' },
  { unitId: 'air-freight', unitName: 'Gogidix Air Freight', pendingAmount: 210000, pendingCount: 28, nextBatchTime: '2026-04-23T06:00:00Z' },
  { unitId: 'ocean-shipping', unitName: 'Gogidix Ocean Shipping', pendingAmount: 560000, pendingCount: 41, nextBatchTime: '2026-04-23T10:00:00Z' },
  { unitId: 'procurement', unitName: 'Gogidix Procurement', pendingAmount: 145000, pendingCount: 23, nextBatchTime: '2026-04-23T08:00:00Z' },
]

export const commissionTracker: Array<{
  unitId: string
  unitName: string
  totalCommission: number
  paidOut: number
  pending: number
}> = [
  { unitId: 'ecommerce', unitName: 'Gogidix E-Commerce', totalCommission: 2450000, paidOut: 2120000, pending: 330000 },
  { unitId: 'courier', unitName: 'Gogidix Courier', totalCommission: 820000, paidOut: 756000, pending: 64000 },
  { unitId: 'haulage', unitName: 'Gogidix Haulage', totalCommission: 1280000, paidOut: 1100000, pending: 180000 },
  { unitId: 'sales', unitName: 'Sales Department', totalCommission: 1520000, paidOut: 1340000, pending: 180000 },
  { unitId: 'procurement', unitName: 'Gogidix Procurement', totalCommission: 480000, paidOut: 420000, pending: 60000 },
]
