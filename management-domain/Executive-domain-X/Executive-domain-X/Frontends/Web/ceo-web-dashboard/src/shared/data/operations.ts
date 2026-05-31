export const operationalKPIs: Array<{
  unitId: string
  unitName: string
  otdRate: number
  targetOtd: number
  slaCompliance: number
  activeIncidents: number
  volume24h: number
}> = [
  { unitId: 'courier', unitName: 'Gogidix Courier', otdRate: 94.2, targetOtd: 95, slaCompliance: 96.8, activeIncidents: 2, volume24h: 14280 },
  { unitId: 'ecommerce', unitName: 'Gogidix E-Commerce', otdRate: 91.5, targetOtd: 93, slaCompliance: 93.4, activeIncidents: 4, volume24h: 28540 },
  { unitId: 'warehousing', unitName: 'Gogidix Warehousing', otdRate: 97.1, targetOtd: 97, slaCompliance: 98.2, activeIncidents: 1, volume24h: 8960 },
  { unitId: 'air-freight', unitName: 'Gogidix Air Freight', otdRate: 88.3, targetOtd: 90, slaCompliance: 91.5, activeIncidents: 1, volume24h: 1240 },
  { unitId: 'ocean-shipping', unitName: 'Gogidix Ocean Shipping', otdRate: 92.7, targetOtd: 92, slaCompliance: 95.1, activeIncidents: 0, volume24h: 860 },
  { unitId: 'haulage', unitName: 'Gogidix Haulage', otdRate: 93.8, targetOtd: 94, slaCompliance: 95.6, activeIncidents: 3, volume24h: 18200 },
  { unitId: 'procurement', unitName: 'Gogidix Procurement', otdRate: 96.4, targetOtd: 95, slaCompliance: 97.3, activeIncidents: 0, volume24h: 3420 },
  { unitId: 'admin-core', unitName: 'Admin & Core Services', otdRate: 99.1, targetOtd: 99, slaCompliance: 99.5, activeIncidents: 0, volume24h: 0 },
]

export const activeIncidents: Array<{
  id: string
  severity: 'P1' | 'P2' | 'P3' | 'P4'
  title: string
  unitId: string
  department: string
  status: 'investigating' | 'mitigating' | 'resolved' | 'monitoring'
  assignee: string
  eta: string
  timeAgo: string
}> = [
  { id: 'INC-4821', severity: 'P1', title: 'E-Commerce checkout service latency spike', unitId: 'ecommerce', department: 'system-administrator', status: 'mitigating', assignee: 'Eng. Adewale Okonkwo', eta: '2026-04-22T16:00:00Z', timeAgo: '42 min ago' },
  { id: 'INC-4820', severity: 'P2', title: 'Haulage fleet tracking intermittent disconnects', unitId: 'haulage', department: 'system-administrator', status: 'investigating', assignee: 'Eng. Fatima Bello', eta: '2026-04-22T18:00:00Z', timeAgo: '1h 15min ago' },
  { id: 'INC-4819', severity: 'P2', title: 'Courier pricing engine degraded response times', unitId: 'courier', department: 'system-administrator', status: 'mitigating', assignee: 'Eng. Chinedu Eze', eta: '2026-04-22T17:30:00Z', timeAgo: '2h ago' },
  { id: 'INC-4818', severity: 'P3', title: 'Warehouse slot booking delays in Lagos hub', unitId: 'warehousing', department: 'system-administrator', status: 'monitoring', assignee: 'Eng. Amina Musa', eta: '2026-04-22T20:00:00Z', timeAgo: '3h ago' },
  { id: 'INC-4817', severity: 'P3', title: 'Air freight customs API timeout on AWB generation', unitId: 'air-freight', department: 'system-administrator', status: 'investigating', assignee: 'Eng. Obinna Nwankwo', eta: '2026-04-23T08:00:00Z', timeAgo: '4h ago' },
  { id: 'INC-4816', severity: 'P4', title: 'Procurement report generation slow for Q1 data', unitId: 'procurement', department: 'finance', status: 'investigating', assignee: 'Eng. Kemi Adeyemi', eta: '2026-04-23T12:00:00Z', timeAgo: '5h ago' },
  { id: 'INC-4815', severity: 'P2', title: 'Haulage driver app crash on Android 14 devices', unitId: 'haulage', department: 'system-administrator', status: 'mitigating', assignee: 'Eng. Bola Tinubu', eta: '2026-04-22T19:00:00Z', timeAgo: '1h 30min ago' },
  { id: 'INC-4814', severity: 'P3', title: 'Ocean shipping container status sync delay', unitId: 'ocean-shipping', department: 'system-administrator', status: 'monitoring', assignee: 'Eng. Yemi Osinbajo', eta: '2026-04-22T22:00:00Z', timeAgo: '6h ago' },
  { id: 'INC-4813', severity: 'P1', title: 'E-Commerce search index corruption affecting results', unitId: 'ecommerce', department: 'system-administrator', status: 'mitigating', assignee: 'Eng. Ngozi Obi', eta: '2026-04-22T15:30:00Z', timeAgo: '55 min ago' },
  { id: 'INC-4812', severity: 'P4', title: 'Admin portal slow load on dashboard page', unitId: 'admin-core', department: 'system-administrator', status: 'investigating', assignee: 'Eng. Segun Awolowo', eta: '2026-04-23T10:00:00Z', timeAgo: '8h ago' },
]

export const slaCompliance: Array<{
  unitId: string
  unitName: string
  slaTarget: number
  slaActual: number
  status: 'compliant' | 'at_risk' | 'non_compliant'
}> = [
  { unitId: 'courier', unitName: 'Gogidix Courier', slaTarget: 95.0, slaActual: 96.8, status: 'compliant' },
  { unitId: 'ecommerce', unitName: 'Gogidix E-Commerce', slaTarget: 93.0, slaActual: 93.4, status: 'compliant' },
  { unitId: 'warehousing', unitName: 'Gogidix Warehousing', slaTarget: 97.0, slaActual: 98.2, status: 'compliant' },
  { unitId: 'air-freight', unitName: 'Gogidix Air Freight', slaTarget: 90.0, slaActual: 91.5, status: 'compliant' },
  { unitId: 'ocean-shipping', unitName: 'Gogidix Ocean Shipping', slaTarget: 92.0, slaActual: 95.1, status: 'compliant' },
  { unitId: 'haulage', unitName: 'Gogidix Haulage', slaTarget: 94.0, slaActual: 95.6, status: 'compliant' },
  { unitId: 'procurement', unitName: 'Gogidix Procurement', slaTarget: 95.0, slaActual: 97.3, status: 'compliant' },
  { unitId: 'admin-core', unitName: 'Admin & Core Services', slaTarget: 99.0, slaActual: 99.5, status: 'compliant' },
]
