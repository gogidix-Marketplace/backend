import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOApprovalsPage() {
  const pendingApprovals = [
    { id: 'APP-2401', type: 'Deployment Approval', title: 'search-service v3.2.1 → Production', requester: 'Sarah Chen', date: 'Apr 22, 10:30 AM', priority: 'High', risk: 'Medium' },
    { id: 'APP-2402', type: 'Access Request', title: 'Admin access to MongoDB cluster', requester: 'James Wilson', date: 'Apr 22, 09:15 AM', priority: 'Medium', risk: 'Low' },
    { id: 'APP-2403', type: 'Infrastructure Change', title: 'Scale Redis cluster from 5 to 7 nodes', requester: 'Maria Garcia', date: 'Apr 22, 08:45 AM', priority: 'High', risk: 'Medium' },
    { id: 'APP-2404', type: 'Security Exception', title: 'Allow legacy TLS 1.1 for partner API', requester: 'Alex Kumar', date: 'Apr 21, 04:20 PM', priority: 'High', risk: 'High' },
    { id: 'APP-2405', type: 'Vendor Approval', title: 'Datadog APM enterprise license renewal', requester: 'Lisa Park', date: 'Apr 21, 02:00 PM', priority: 'Medium', risk: 'Low' },
    { id: 'APP-2406', type: 'Deployment Approval', title: 'notification-engine v1.5.3 → Staging', requester: 'Tom Harris', date: 'Apr 21, 11:30 AM', priority: 'Low', risk: 'Low' },
    { id: 'APP-2407', type: 'Infrastructure Change', title: 'Enable auto-scaling for Kubernetes cluster', requester: 'Emma Davis', date: 'Apr 21, 10:00 AM', priority: 'High', risk: 'Medium' },
    { id: 'APP-2408', type: 'Access Request', title: 'Production DB read-only access for analytics', requester: 'Ryan Lee', date: 'Apr 20, 03:15 PM', priority: 'Medium', risk: 'Low' },
  ]

  const approvalHistory = [
    { id: 'APP-2395', type: 'Deployment Approval', title: 'route-optimization v2.0.0 → Staging', requester: 'Emma Davis', date: 'Apr 20', decision: 'Approved', by: 'CTO' },
    { id: 'APP-2394', type: 'Infrastructure Change', title: 'Upgrade Kafka to 3.6.1', requester: 'Maria Garcia', date: 'Apr 20', decision: 'Approved', by: 'CTO' },
    { id: 'APP-2393', type: 'Security Exception', title: 'Extended session timeout for mobile app', requester: 'Alex Kumar', date: 'Apr 19', decision: 'Rejected', by: 'CTO' },
    { id: 'APP-2392', type: 'Vendor Approval', title: 'CloudFlare enterprise plan upgrade', requester: 'Lisa Park', date: 'Apr 19', decision: 'Approved', by: 'CTO' },
    { id: 'APP-2391', type: 'Access Request', title: 'Root access to staging environment', requester: 'David Brown', date: 'Apr 18', decision: 'Rejected', by: 'CTO' },
  ]

  const typeVariant = (t: string) =>
    t === 'Deployment Approval' ? 'info' :
    t === 'Access Request' ? 'warning' :
    t === 'Infrastructure Change' ? 'default' :
    t === 'Security Exception' ? 'destructive' : 'secondary'

  const priorityVariant = (p: string) =>
    p === 'High' ? 'destructive' : p === 'Medium' ? 'warning' : 'outline'

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Approvals</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Technical approvals and change requests</p>
        </div>
        <Badge variant="destructive" className="px-3 py-1.5 text-xs font-medium self-start">
          {pendingApprovals.length} Pending
        </Badge>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Pending Approvals</CardTitle>
          <CardDescription>Awaiting your review and decision</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {pendingApprovals.map((a) => (
              <div key={a.id} className="p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 flex-wrap">
                      <span className="text-[10px] font-mono text-slate-400">{a.id}</span>
                      <Badge variant={typeVariant(a.type)} className="text-[10px]">{a.type}</Badge>
                      <Badge variant={priorityVariant(a.priority)} className="text-[10px]">{a.priority}</Badge>
                      <Badge variant="outline" className="text-[10px]">Risk: {a.risk}</Badge>
                    </div>
                    <p className="text-sm font-semibold text-slate-900 dark:text-white mt-1">{a.title}</p>
                    <div className="flex items-center gap-2 mt-1">
                      <span className="text-xs text-slate-500">{a.requester}</span>
                      <span className="text-[10px] text-slate-400">{a.date}</span>
                    </div>
                  </div>
                  <div className="flex gap-2 flex-shrink-0">
                    <Button size="sm" className="text-xs h-8">Approve</Button>
                    <Button variant="outline" size="sm" className="text-xs h-8 text-red-600">Reject</Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Approval History</CardTitle>
          <CardDescription>Recently processed requests</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">ID</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Title</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Type</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Requester</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Date</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Decision</th>
                </tr>
              </thead>
              <tbody>
                {approvalHistory.map((h) => (
                  <tr key={h.id} className="border-b border-slate-100 dark:border-slate-800">
                    <td className="py-2.5 px-3 font-mono text-[10px] text-slate-400">{h.id}</td>
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{h.title}</td>
                    <td className="py-2.5 px-3"><Badge variant={typeVariant(h.type)} className="text-[10px]">{h.type}</Badge></td>
                    <td className="py-2.5 px-3 text-slate-500">{h.requester}</td>
                    <td className="py-2.5 px-3 text-slate-500">{h.date}</td>
                    <td className="py-2.5 px-3"><Badge variant={h.decision === 'Approved' ? 'success' : 'destructive'} className="text-[10px]">{h.decision}</Badge></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
