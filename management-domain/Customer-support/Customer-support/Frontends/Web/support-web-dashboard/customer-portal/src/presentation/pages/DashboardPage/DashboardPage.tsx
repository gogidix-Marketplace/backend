import { Link } from 'react-router-dom';
import { MessageSquare, Plus, Search, Clock, CheckCircle, AlertCircle } from 'lucide-react';

interface Ticket {
  id: string;
  subject: string;
  status: 'open' | 'in-progress' | 'resolved' | 'closed';
  priority: 'low' | 'medium' | 'high' | 'urgent';
  createdAt: string;
  lastUpdated: string;
}

const mockTickets: Ticket[] = [
  {
    id: 'TKT-100234',
    subject: 'Unable to access my account after password reset',
    status: 'open',
    priority: 'high',
    createdAt: '2024-02-20T10:30:00',
    lastUpdated: '2024-02-20T10:30:00',
  },
  {
    id: 'TKT-100231',
    subject: 'Billing inquiry for March invoice',
    status: 'in-progress',
    priority: 'medium',
    createdAt: '2024-02-19T14:20:00',
    lastUpdated: '2024-02-20T09:15:00',
  },
  {
    id: 'TKT-100228',
    subject: 'Feature request: Dark mode support',
    status: 'open',
    priority: 'low',
    createdAt: '2024-02-18T16:45:00',
    lastUpdated: '2024-02-18T16:45:00',
  },
  {
    id: 'TKT-100225',
    subject: 'Integration API returning 500 errors',
    status: 'resolved',
    priority: 'urgent',
    createdAt: '2024-02-17T11:00:00',
    lastUpdated: '2024-02-19T15:30:00',
  },
  {
    id: 'TKT-100220',
    subject: 'Question about export functionality',
    status: 'closed',
    priority: 'low',
    createdAt: '2024-02-15T09:30:00',
    lastUpdated: '2024-02-16T10:00:00',
  },
];

function getStatusBadge(status: Ticket['status']) {
  const styles = {
    open: 'bg-blue-100 text-blue-800',
    'in-progress': 'bg-yellow-100 text-yellow-800',
    resolved: 'bg-green-100 text-green-800',
    closed: 'bg-gray-100 text-gray-800',
  };
  return (
    <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium ${styles[status]}`}>
      {status === 'open' && <AlertCircle className="w-3 h-3" />}
      {status === 'in-progress' && <Clock className="w-3 h-3" />}
      {status === 'resolved' && <CheckCircle className="w-3 h-3" />}
      {status.charAt(0).toUpperCase() + status.slice(1).replace('-', ' ')}
    </span>
  );
}

function getPriorityBadge(priority: Ticket['priority']) {
  const styles = {
    low: 'bg-gray-100 text-gray-700',
    medium: 'bg-blue-100 text-blue-700',
    high: 'bg-orange-100 text-orange-700',
    urgent: 'bg-red-100 text-red-700',
  };
  return (
    <span className={`inline-flex items-center px-2 py-1 rounded text-xs font-semibold ${styles[priority]}`}>
      {priority.toUpperCase()}
    </span>
  );
}

function formatDate(dateStr: string) {
  const date = new Date(dateStr);
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}

export function DashboardPage() {
  const openTickets = mockTickets.filter(t => t.status === 'open' || t.status === 'in-progress');
  const resolvedTickets = mockTickets.filter(t => t.status === 'resolved');

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">My Tickets</h1>
          <p className="text-gray-500 mt-1">View and manage your support requests</p>
        </div>
        <Link
          to="/new-ticket"
          className="inline-flex items-center gap-2 px-4 py-2 bg-primary-600 text-white font-medium rounded-lg hover:bg-primary-700 transition-colors"
        >
          <Plus className="w-4 h-4" />
          New Ticket
        </Link>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-500">Open Tickets</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{openTickets.length}</p>
            </div>
            <div className="p-3 bg-blue-50 rounded-lg">
              <MessageSquare className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-500">Resolved This Week</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{resolvedTickets.length}</p>
            </div>
            <div className="p-3 bg-green-50 rounded-lg">
              <CheckCircle className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-500">Avg Response Time</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">2.4h</p>
            </div>
            <div className="p-3 bg-purple-50 rounded-lg">
              <Clock className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </div>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
        <input
          type="text"
          placeholder="Search tickets by ID, subject, or keywords..."
          className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none"
        />
      </div>

      {/* Tickets Table */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100">
          <h2 className="text-lg font-semibold text-gray-900">Recent Tickets</h2>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ticket ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Subject
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Priority
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Last Updated
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Action
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {mockTickets.map((ticket) => (
                <tr key={ticket.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="text-sm font-mono text-gray-900">{ticket.id}</span>
                  </td>
                  <td className="px-6 py-4">
                    <p className="text-sm font-medium text-gray-900">{ticket.subject}</p>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getStatusBadge(ticket.status)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getPriorityBadge(ticket.priority)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatDate(ticket.lastUpdated)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right">
                    <Link
                      to={`/ticket/${ticket.id}`}
                      className="text-sm text-primary-600 hover:text-primary-700 font-medium"
                    >
                      View
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Quick Links */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Link
          to="/knowledge"
          className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow"
        >
          <h3 className="font-semibold text-gray-900 mb-2">Knowledge Base</h3>
          <p className="text-sm text-gray-500">Find answers to common questions and troubleshooting guides</p>
        </Link>
        <Link
          to="/new-ticket"
          className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow"
        >
          <h3 className="font-semibold text-gray-900 mb-2">Submit New Ticket</h3>
          <p className="text-sm text-gray-500">Create a new support request for our team to assist you</p>
        </Link>
      </div>
    </div>
  );
}
