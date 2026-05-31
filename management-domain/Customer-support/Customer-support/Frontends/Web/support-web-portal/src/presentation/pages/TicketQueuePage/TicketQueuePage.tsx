import { useState } from 'react';
import { Filter, Search, User, Clock } from 'lucide-react';

const tickets = [
  { id: 'TKT-100245', subject: 'Payment gateway integration error', customer: 'TechCorp Inc', customerEmail: 'support@techcorp.com', priority: 'high', status: 'new', assignedTo: null, created: '5m ago', sla: '2h 55m left' },
  { id: 'TKT-100244', subject: 'Unable to export report data', customer: 'Global Services', customerEmail: 'it@globalservices.com', priority: 'medium', status: 'in-progress', assignedTo: 'You', created: '1h ago', sla: '4h left' },
  { id: 'TKT-100243', subject: 'User permissions not working', customer: 'Startup XYZ', customerEmail: 'admin@startupxyz.com', priority: 'high', status: 'new', assignedTo: null, created: '2h ago', sla: '3h left' },
  { id: 'TKT-100242', subject: 'API rate limiting issue', customer: 'BigData Co', customerEmail: 'dev@bigdata.co', priority: 'urgent', status: 'pending', assignedTo: 'John Smith', created: '3h ago', sla: '45m left (at risk)' },
  { id: 'TKT-100241', subject: 'Billing question', customer: 'Small Business LLC', customerEmail: 'finance@smallbusiness.com', priority: 'low', status: 'new', assignedTo: null, created: '4h ago', sla: '20h left' },
  { id: 'TKT-100240', subject: 'Account login failure', customer: 'Enterprise Inc', customerEmail: 'tech@enterprise.com', priority: 'high', status: 'escalated', assignedTo: 'You', created: '5h ago', sla: 'OVERDUE' },
];

function getPriorityBadge(priority: string) {
  const styles: Record<string, string> = {
    low: 'bg-gray-100 text-gray-700 border-gray-200',
    medium: 'bg-blue-100 text-blue-700 border-blue-200',
    high: 'bg-orange-100 text-orange-700 border-orange-200',
    urgent: 'bg-red-100 text-red-700 border-red-200',
  };
  return (
    <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-semibold border ${styles[priority] || styles.low}`}>
      {priority.toUpperCase()}
    </span>
  );
}

function getStatusBadge(status: string) {
  const styles: Record<string, string> = {
    new: 'bg-blue-50 text-blue-700 border-blue-200',
    'in-progress': 'bg-yellow-50 text-yellow-700 border-yellow-200',
    pending: 'bg-orange-50 text-orange-700 border-orange-200',
    escalated: 'bg-purple-50 text-purple-700 border-purple-200',
  };
  const labels: Record<string, string> = {
    new: 'NEW',
    'in-progress': 'IN PROGRESS',
    pending: 'PENDING',
    escalated: 'ESCALATED',
  };
  return (
    <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium border ${styles[status] || styles.new}`}>
      {labels[status] || status.toUpperCase()}
    </span>
  );
}

export function TicketQueuePage() {
  const [selectedFilter, setSelectedFilter] = useState('all');

  const filteredTickets = tickets.filter(ticket => {
    if (selectedFilter === 'all') return true;
    if (selectedFilter === 'unassigned') return !ticket.assignedTo;
    if (selectedFilter === 'mine') return ticket.assignedTo === 'You';
    if (selectedFilter === 'high-priority') return ticket.priority === 'high' || ticket.priority === 'urgent';
    return true;
  });

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Ticket Queue</h1>
          <p className="text-gray-500 mt-1">{filteredTickets.length} tickets in queue</p>
        </div>
        <div className="flex items-center gap-3">
          <button className="flex items-center gap-2 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 text-sm font-medium">
            <Filter className="w-4 h-4" />
            Filters
          </button>
          <button className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 text-sm font-medium">
            Auto-Assign Next
          </button>
        </div>
      </div>

      {/* Search and Filter Bar */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search tickets..."
            className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none"
          />
        </div>
        <div className="flex gap-2">
          {['all', 'unassigned', 'mine', 'high-priority'].map((filter) => (
            <button
              key={filter}
              onClick={() => setSelectedFilter(filter)}
              className={`px-4 py-3 rounded-lg text-sm font-medium whitespace-nowrap ${
                selectedFilter === filter
                  ? 'bg-primary-600 text-white'
                  : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50'
              }`}
            >
              {filter.split('-').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')}
            </button>
          ))}
        </div>
      </div>

      {/* Ticket List */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
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
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Assigned To
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  SLA
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Action
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filteredTickets.map((ticket) => (
                <tr key={ticket.id} className="hover:bg-gray-50 cursor-pointer">
                  <td className="px-6 py-4">
                    <div>
                      <p className="text-sm font-mono text-gray-900">{ticket.id}</p>
                      <p className="text-sm text-gray-600 truncate max-w-xs">{ticket.subject}</p>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-8 bg-gray-100 rounded-full flex items-center justify-center">
                        <User className="w-4 h-4 text-gray-500" />
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">{ticket.customer}</p>
                        <p className="text-xs text-gray-500">{ticket.customerEmail}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">{getPriorityBadge(ticket.priority)}</td>
                  <td className="px-6 py-4">{getStatusBadge(ticket.status)}</td>
                  <td className="px-6 py-4">
                    {ticket.assignedTo ? (
                      <span className="text-sm text-gray-900">{ticket.assignedTo}</span>
                    ) : (
                      <span className="text-sm text-gray-400">Unassigned</span>
                    )}
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-1">
                      <Clock className="w-3 h-3 text-gray-400" />
                      <span className={`text-sm font-medium ${
                        ticket.sla === 'OVERDUE' ? 'text-red-600' :
                        ticket.sla.includes('at risk') ? 'text-orange-600' :
                        'text-gray-600'
                      }`}>
                        {ticket.sla}
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button className="text-sm text-primary-600 hover:text-primary-700 font-medium">
                      View
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
