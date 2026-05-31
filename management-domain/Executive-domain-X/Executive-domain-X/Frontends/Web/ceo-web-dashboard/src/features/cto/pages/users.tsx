import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOUsersPage() {
  const summary = [
    { label: 'Total Users', value: '2,847', change: '+23', positive: true },
    { label: 'Active', value: '2,612', change: '+18', positive: true },
    { label: 'Admins', value: '23', change: '+1', positive: true },
    { label: 'Locked', value: '8', change: '-2', positive: true },
  ]

  const users = [
    { name: 'Sarah Chen', email: 'sarah.chen@gogidix.com', role: 'Platform Admin', department: 'Engineering', lastLogin: '2 min ago', status: 'Active' },
    { name: 'James Wilson', email: 'james.w@gogidix.com', role: 'Developer', department: 'Platform', lastLogin: '15 min ago', status: 'Active' },
    { name: 'Maria Garcia', email: 'maria.g@gogidix.com', role: 'DBA', department: 'Infrastructure', lastLogin: '1h ago', status: 'Active' },
    { name: 'Alex Kumar', email: 'alex.k@gogidix.com', role: 'Security Admin', department: 'InfoSec', lastLogin: '3h ago', status: 'Active' },
    { name: 'Lisa Park', email: 'lisa.p@gogidix.com', role: 'Developer', department: 'E-Commerce', lastLogin: '5h ago', status: 'Active' },
    { name: 'David Brown', email: 'david.b@gogidix.com', role: 'DevOps', department: 'Infrastructure', lastLogin: '1d ago', status: 'Locked' },
    { name: 'Nina Patel', email: 'nina.p@gogidix.com', role: 'Developer', department: 'Courier', lastLogin: 'Never', status: 'Pending' },
    { name: 'Tom Harris', email: 'tom.h@gogidix.com', role: 'QA Engineer', department: 'Quality', lastLogin: '2h ago', status: 'Active' },
    { name: 'Emma Davis', email: 'emma.d@gogidix.com', role: 'Tech Lead', department: 'Platform', lastLogin: '30 min ago', status: 'Active' },
    { name: 'Ryan Lee', email: 'ryan.l@gogidix.com', role: 'Developer', department: 'Warehousing', lastLogin: '4h ago', status: 'Active' },
  ]

  const roleDistribution = [
    { role: 'Developer', count: 1240, max: 1240 },
    { role: 'DevOps', count: 185, max: 1240 },
    { role: 'QA Engineer', count: 312, max: 1240 },
    { role: 'Tech Lead', count: 89, max: 1240 },
    { role: 'DBA', count: 42, max: 1240 },
    { role: 'Admin', count: 23, max: 1240 },
  ]

  const statusVariant = (s: string) =>
    s === 'Active' ? 'success' : s === 'Locked' ? 'destructive' : 'warning'

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">User Management</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Technical team access and role management</p>
        </div>
        <Button size="sm">Add User</Button>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {summary.map((s) => (
          <Card key={s.label}>
            <CardContent className="p-4">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-1">{s.label}</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white">{s.value}</p>
              <span className={`text-xs font-semibold ${s.positive ? 'text-emerald-600' : 'text-red-600'}`}>
                {s.positive ? '↑' : '↓'} {s.change}
              </span>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Users</CardTitle>
          <CardDescription>All technical team members</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Name</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Email</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Role</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Department</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Last Login</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Status</th>
                  <th className="text-right py-2 px-3 text-xs font-medium text-slate-500">Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.email} className="border-b border-slate-100 dark:border-slate-800">
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{u.name}</td>
                    <td className="py-2.5 px-3 text-slate-500">{u.email}</td>
                    <td className="py-2.5 px-3"><Badge variant="outline" className="text-[10px]">{u.role}</Badge></td>
                    <td className="py-2.5 px-3 text-slate-500">{u.department}</td>
                    <td className="py-2.5 px-3 text-slate-500">{u.lastLogin}</td>
                    <td className="py-2.5 px-3"><Badge variant={statusVariant(u.status)} className="text-[10px]">{u.status}</Badge></td>
                    <td className="py-2.5 px-3 text-right">
                      <div className="flex justify-end gap-1">
                        <Button variant="ghost" size="sm" className="text-xs h-7">Edit</Button>
                        <Button variant="ghost" size="sm" className="text-xs h-7 text-red-600">Disable</Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Role Distribution</CardTitle>
          <CardDescription>Users per role across the platform</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {roleDistribution.map((r) => (
              <div key={r.role} className="flex items-center gap-3">
                <span className="text-sm font-medium text-slate-900 dark:text-white w-24">{r.role}</span>
                <div className="flex-1 h-4 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                  <div className="h-full bg-blue-500 rounded-full" style={{ width: `${(r.count / r.max) * 100}%` }} />
                </div>
                <span className="text-sm font-semibold text-slate-700 dark:text-slate-300 w-12 text-right">{r.count}</span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
