import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function COOApprovalsPage() {
  const filters = ['All', 'Pending', 'Approved', 'Rejected'] as const

  const approvals = [
    { id: 'APR-4021', type: 'Operational Change', title: 'Extend Courier night shift for Lagos Hub peak season', department: 'Courier Operations', requestedBy: 'A. Okafor', date: 'Apr 22, 2026', urgency: 'High', urgencyVariant: 'destructive' as const },
    { id: 'APR-4020', type: 'Resource Request', title: 'Approve 15 additional temp warehouse staff for inventory audit', department: 'Warehousing', requestedBy: 'B. Adeyemi', date: 'Apr 22, 2026', urgency: 'Medium', urgencyVariant: 'warning' as const },
    { id: 'APR-4019', type: 'Vendor Approval', title: 'New ocean freight vendor — Trans-Atlantic Shipping Ltd.', department: 'Ocean Freight', requestedBy: 'C. Nwosu', date: 'Apr 21, 2026', urgency: 'High', urgencyVariant: 'destructive' as const },
    { id: 'APR-4018', type: 'Process Change', title: 'Update e-commerce return handling SLA from 48h to 24h', department: 'E-Commerce', requestedBy: 'D. Balogun', date: 'Apr 21, 2026', urgency: 'Low', urgencyVariant: 'info' as const },
    { id: 'APR-4017', type: 'Resource Request', title: 'Budget approval for air cargo handler training program', department: 'Air Freight', requestedBy: 'E. Ibrahim', date: 'Apr 20, 2026', urgency: 'Medium', urgencyVariant: 'warning' as const },
    { id: 'APR-4016', type: 'Operational Change', title: 'Migrate courier routing engine to v3 algorithm', department: 'Courier Operations', requestedBy: 'F. Musa', date: 'Apr 20, 2026', urgency: 'High', urgencyVariant: 'destructive' as const },
    { id: 'APR-4015', type: 'Vendor Approval', title: 'Renew haulage fleet maintenance contract with AutoServ NG', department: 'Haulage', requestedBy: 'G. Oyelaran', date: 'Apr 19, 2026', urgency: 'Medium', urgencyVariant: 'warning' as const },
    { id: 'APR-4014', type: 'Process Change', title: 'Implement new procurement approval workflow for POs > $10K', department: 'Procurement', requestedBy: 'H. Adebayo', date: 'Apr 19, 2026', urgency: 'Low', urgencyVariant: 'info' as const },
  ]

  const history = [
    { id: 'APR-4013', title: 'Approve new warehouse security system upgrade', outcome: 'Approved', processedBy: 'COO', date: 'Apr 18, 2026' },
    { id: 'APR-4012', title: 'Emergency courier fleet expansion — 20 vehicles', outcome: 'Approved', processedBy: 'COO', date: 'Apr 17, 2026' },
    { id: 'APR-4011', title: 'Cross-train warehouse staff for air freight operations', outcome: 'Approved', processedBy: 'COO', date: 'Apr 16, 2026' },
    { id: 'APR-4010', title: 'Replace ocean freight insurance provider', outcome: 'Rejected', processedBy: 'COO', date: 'Apr 15, 2026' },
    { id: 'APR-4009', title: 'Implement automated sorting for Abuja satellite hub', outcome: 'Approved', processedBy: 'COO', date: 'Apr 14, 2026' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Pending Approvals</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Review and process operational approval requests</p>
        </div>
        <div className="flex gap-2">
          {filters.map((f) => (
            <Button key={f} variant={f === 'All' ? 'default' : 'outline'} size="sm">{f}</Button>
          ))}
        </div>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="text-lg">Approval Queue</CardTitle>
            <Badge variant="destructive">{approvals.length} pending</Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {approvals.map((a) => (
              <div key={a.id} className="flex flex-col sm:flex-row sm:items-center gap-3 p-4 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <Badge variant="outline" className="text-[10px]">{a.type}</Badge>
                    <Badge variant={a.urgencyVariant} className="text-[10px]">{a.urgency}</Badge>
                  </div>
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{a.title}</p>
                  <div className="flex items-center gap-2 mt-0.5">
                    <Badge variant="outline" className="text-[10px]">{a.department}</Badge>
                    <span className="text-xs text-slate-500">by {a.requestedBy}</span>
                    <span className="text-xs text-slate-400">• {a.date}</span>
                  </div>
                </div>
                <div className="flex gap-2 flex-shrink-0">
                  <Button variant="default" size="sm">Approve</Button>
                  <Button variant="outline" size="sm">Reject</Button>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Approval History</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {history.map((h) => (
              <div key={h.id} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-slate-900 dark:text-white">{h.title}</p>
                  <div className="flex items-center gap-2 mt-0.5">
                    <span className="text-xs text-slate-500">{h.processedBy} • {h.date}</span>
                  </div>
                </div>
                <Badge variant={h.outcome === 'Approved' ? 'success' : 'destructive'} className="text-xs">{h.outcome}</Badge>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
