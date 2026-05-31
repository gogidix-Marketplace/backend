import { Link } from 'react-router-dom';
import { Ticket, Clock, CheckCircle, TrendingUp, AlertCircle } from 'lucide-react';

const stats = [
  { name: 'Assigned Tickets', value: 12, change: '+3', icon: Ticket, color: 'blue' },
  { name: 'Resolved Today', value: 8, change: '+2', icon: CheckCircle, color: 'green' },
  { name: 'Avg Resolution', value: '2.4h', change: '-0.3h', icon: Clock, color: 'purple' },
  { name: 'Customer Rating', value: '4.7', change: '+0.1', icon: TrendingUp, color: 'yellow' },
];

const recentTickets = [
  { id: 'TKT-100245', subject: 'Payment gateway integration error', customer: 'TechCorp Inc', priority: 'high', status: 'open', time: '5m ago' },
  { id: 'TKT-100244', subject: 'Unable to export report data', customer: 'Global Services', priority: 'medium', status: 'in-progress', time: '1h ago' },
  { id: 'TKT-100243', subject: 'User permissions not working', customer: 'Startup XYZ', priority: 'high', status: 'open', time: '2h ago' },
  { id: 'TKT-100242', subject: 'API rate limiting issue', customer: 'BigData Co', priority: 'urgent', status: 'pending', time: '3h ago' },
  { id: 'TKT-100241', subject: 'Billing question', customer: 'Small Business LLC', priority: 'low', status: 'open', time: '4h ago' },
];

function getStatusBadge(status: string) {
  const styles: Record<string, string> = {
    open: 'bg-blue-100 text-blue-800',
    'in-progress': 'bg-yellow-100 text-yellow-800',
    pending: 'bg-orange-100 text-orange-800',
    resolved: 'bg-green-100 text-green-800',
  };
  return (
    <span className={`inline-flex items-center px-2 py-1 rounded text-xs font-medium ${styles[status] || styles.open}`}>
      {status.charAt(0).toUpperCase() + status.slice(1)}
    </span>
  );
}

function getPriorityBadge(priority: string) {
  const styles: Record<string, string> = {
    low: 'bg-gray-100 text-gray-700',
    medium: 'bg-blue-100 text-blue-700',
    high: 'bg-orange-100 text-orange-700',
    urgent: 'bg-red-100 text-red-700',
  };
  return (
    <span className={`inline-flex items-center px-2 py-1 rounded text-xs font-semibold ${styles[priority] || styles.low}`}>
      {priority.toUpperCase()}
    </span>
  );
}

export function AgentDashboardPage() {
  return (
    <div className="space-y-6">
      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat) => (
          <div key={stat.name} className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <div className="flex items-start justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500">{stat.name}</p>
                <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value}</p>
              </div>
              <div className={`p-3 bg-${stat.color}-50 rounded-lg`}>
                <stat.icon className={`w-5 h-5 text-${stat.color}-600`} />
              </div>
            </div>
            <p className="text-xs text-gray-500 mt-2">
              <span className={`text-${stat.change?.startsWith('+') ? 'green' : 'red'}-600 font-medium`}>
                {stat.change}
              </span>{' '}
              from yesterday
            </p>
          </div>
        ))}
      </div>

      {/* Quick Actions */}
      <div className="bg-gradient-to-r from-primary-500 to-primary-600 rounded-xl p-6 text-white">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-lg font-semibold">Ready to help customers?</h2>
            <p className="text-primary-100 text-sm mt-1">You have 3 tickets waiting in the queue</p>
          </div>
          <Link
            to="/queue"
            className="px-6 py-3 bg-white text-primary-600 font-medium rounded-lg hover:bg-primary-50 transition-colors"
          >
            View Queue
          </Link>
        </div>
      </div>

      {/* Recent Tickets */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <h2 className="text-lg font-semibold text-gray-900">Assigned Tickets</h2>
          <Link to="/queue" className="text-sm text-primary-600 hover:text-primary-700 font-medium">
            View All
          </Link>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ticket
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Customer
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Priority
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Time
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {recentTickets.map((ticket) => (
                <tr key={ticket.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4">
                    <div>
                      <p className="text-sm font-mono text-gray-900">{ticket.id}</p>
                      <p className="text-sm text-gray-600 truncate max-w-xs">{ticket.subject}</p>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-900">{ticket.customer}</td>
                  <td className="px-6 py-4">{getPriorityBadge(ticket.priority)}</td>
                  <td className="px-6 py-4">{getStatusBadge(ticket.status)}</td>
                  <td className="px-6 py-4 text-right text-sm text-gray-500">{ticket.time}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Team Performance */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Team Status</h3>
          <div className="space-y-3">
            {[
              { name: 'John Smith', status: 'available', tickets: 3 },
              { name: 'Emily Davis', status: 'busy', tickets: 5 },
              { name: 'Michael Brown', status: 'away', tickets: 0 },
              { name: 'You', status: 'available', tickets: 12 },
            ].map((member) => (
              <div key={member.name} className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className={`w-2 h-2 rounded-full ${
                    member.status === 'available' ? 'bg-green-500' :
                    member.status === 'busy' ? 'bg-red-500' :
                    'bg-gray-400'
                  }`} />
                  <span className="text-sm font-medium text-gray-900">{member.name}</span>
                </div>
                <span className="text-sm text-gray-500">{member.tickets} tickets</span>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">SLA Alerts</h3>
          <div className="space-y-3">
            {[
              { ticket: 'TKT-100242', issue: 'At risk of breaching SLA', time: '45m left' },
              { ticket: 'TKT-100240', issue: 'Response overdue', time: '2h overdue' },
            ].map((alert) => (
              <div key={alert.ticket} className="flex items-start gap-3 p-3 bg-orange-50 rounded-lg">
                <AlertCircle className="w-5 h-5 text-orange-600 flex-shrink-0 mt-0.5" />
                <div className="flex-1">
                  <p className="text-sm font-medium text-gray-900">{alert.ticket}</p>
                  <p className="text-xs text-gray-600">{alert.issue}</p>
                </div>
                <span className="text-xs text-orange-600 font-medium">{alert.time}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
