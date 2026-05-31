import type { BusinessUnitRevenue } from '../types'

export const monthlyRevenue: Array<{
  month: string
  courier: number
  ecommerce: number
  warehousing: number
  airFreight: number
  ocean: number
  haulage: number
  procurement: number
  admin: number
  total: number
}> = [
  { month: 'Apr 2026', courier: 820000, ecommerce: 980000, warehousing: 510000, airFreight: 380000, ocean: 420000, haulage: 1280000, procurement: 320000, admin: 150000, total: 4860000 },
  { month: 'Mar 2026', courier: 790000, ecommerce: 920000, warehousing: 490000, airFreight: 370000, ocean: 410000, haulage: 1210000, procurement: 330000, admin: 145000, total: 4665000 },
  { month: 'Feb 2026', courier: 750000, ecommerce: 870000, warehousing: 470000, airFreight: 360000, ocean: 400000, haulage: 1150000, procurement: 340000, admin: 140000, total: 4480000 },
  { month: 'Jan 2026', courier: 710000, ecommerce: 810000, warehousing: 450000, airFreight: 350000, ocean: 390000, haulage: 1090000, procurement: 350000, admin: 135000, total: 4285000 },
  { month: 'Dec 2025', courier: 830000, ecommerce: 1050000, warehousing: 530000, airFreight: 400000, ocean: 430000, haulage: 1320000, procurement: 310000, admin: 148000, total: 5018000 },
  { month: 'Nov 2025', courier: 780000, ecommerce: 960000, warehousing: 500000, airFreight: 385000, ocean: 415000, haulage: 1250000, procurement: 325000, admin: 142000, total: 4757000 },
  { month: 'Oct 2025', courier: 740000, ecommerce: 890000, warehousing: 480000, airFreight: 365000, ocean: 405000, haulage: 1180000, procurement: 335000, admin: 138000, total: 4533000 },
  { month: 'Sep 2025', courier: 700000, ecommerce: 830000, warehousing: 460000, airFreight: 345000, ocean: 395000, haulage: 1120000, procurement: 345000, admin: 133000, total: 4328000 },
  { month: 'Aug 2025', courier: 670000, ecommerce: 790000, warehousing: 440000, airFreight: 335000, ocean: 385000, haulage: 1070000, procurement: 340000, admin: 130000, total: 4160000 },
  { month: 'Jul 2025', courier: 650000, ecommerce: 760000, warehousing: 430000, airFreight: 330000, ocean: 380000, haulage: 1040000, procurement: 335000, admin: 128000, total: 4053000 },
  { month: 'Jun 2025', courier: 630000, ecommerce: 730000, warehousing: 420000, airFreight: 325000, ocean: 375000, haulage: 1010000, procurement: 330000, admin: 125000, total: 3945000 },
  { month: 'May 2025', courier: 610000, ecommerce: 700000, warehousing: 410000, airFreight: 320000, ocean: 370000, haulage: 980000, procurement: 325000, admin: 122000, total: 3837000 },
]

export const revenueByRegion: Array<{ region: string; revenue: number; percent: number }> = [
  { region: 'Nigeria', revenue: 28600000, percent: 64.8 },
  { region: 'West Africa', revenue: 5200000, percent: 11.8 },
  { region: 'East Africa', revenue: 3800000, percent: 8.6 },
  { region: 'Southern Africa', revenue: 2900000, percent: 6.6 },
  { region: 'North Africa', revenue: 1800000, percent: 4.1 },
  { region: 'Europe', revenue: 1200000, percent: 2.7 },
  { region: 'Asia', revenue: 600000, percent: 1.4 },
]

export const revenueByUnit: BusinessUnitRevenue[] = [
  { unitId: 'courier', revenue: 8200000, growth: 12, margin: 28.5, opex: 5862000, netProfit: 2337000, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'ecommerce', revenue: 9800000, growth: 22, margin: 34.2, opex: 6448400, netProfit: 3351600, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'warehousing', revenue: 5100000, growth: 8, margin: 22.1, opex: 3972900, netProfit: 1127100, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'air-freight', revenue: 3800000, growth: 5, margin: 18.3, opex: 3104600, netProfit: 695400, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'ocean-shipping', revenue: 4200000, growth: 3, margin: 21.7, opex: 3288600, netProfit: 911400, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'haulage', revenue: 12800000, growth: 18, margin: 31.4, opex: 8780800, netProfit: 4019200, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'procurement', revenue: 3200000, growth: -5, margin: 15.8, opex: 2694400, netProfit: 505600, currency: 'USD', period: 'Q1 2026' },
  { unitId: 'admin-core', revenue: 1500000, growth: 10, margin: 12.0, opex: 1320000, netProfit: 180000, currency: 'USD', period: 'Q1 2026' },
]
