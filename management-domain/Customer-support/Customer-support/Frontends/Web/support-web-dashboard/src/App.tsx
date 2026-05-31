import { Routes, Route, Link, useLocation } from 'react-router-dom'

function DashboardPage() {
  const kpis = [
    { title: 'Open Tickets', value: '234', change: '+12', positive: false },
    { title: 'Resolved Today', value: '89', change: '+23', positive: true },
    { title: 'Avg Response Time', value: '2.4h', change: '-0.5h', positive: true },
    { title: 'Customer Satisfaction', value: '94%', change: '+2%', positive: true },
  ]

  const tickets = [
    { id: 1, customer: 'Alice Johnson', subject: 'Login Issue', status: 'Open', priority: 'High', created: '2026-03-04' },
    { id: 2, customer: 'Bob Smith', subject: 'Payment Failed', status: 'In Progress', priority: 'High', created: '2026-03-04' },
    { id: 3, customer: 'Carol White', subject: 'Feature Request', status: 'Open', priority: 'Medium', created: '2026-03-03' },
    { id: 4, customer: 'David Brown', subject: 'Billing Question', status: 'Resolved', priority: 'Low', created: '2026-03-03' },
    { id: 5, customer: 'Eve Davis', subject: 'Account Update', status: 'In Progress', priority: 'Medium', created: '2026-03-02' },
  ]

  const getStatusClass = (status: string) => {
    switch (status) {
      case 'Open': return 'bg-red-100 text-red-800'
      case 'In Progress': return 'bg-yellow-100 text-yellow-800'
      case 'Resolved': return 'bg-green-100 text-green-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  const getPriorityClass = (priority: string) => {
    switch (priority) {
      case 'High': return 'bg-red-100 text-red-800'
      case 'Medium': return 'bg-yellow-100 text-yellow-800'
      case 'Low': return 'bg-green-100 text-green-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Customer Support Dashboard</h1>
          <p className="text-gray-600 mt-1">Ticket Management & Analytics</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          {kpis.map((kpi, index) => (
            <div key={index} className="bg-white rounded-lg shadow p-6">
              <p className="text-gray-600 text-sm font-medium">{kpi.title}</p>
              <p className="text-2xl font-bold text-gray-900 mt-2">{kpi.value}</p>
              <p className={`text-sm mt-2 ${kpi.positive ? 'text-green-600' : 'text-red-600'}`}>
                {kpi.change} from yesterday
              </p>
            </div>
          ))}
        </div>

        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-gray-900">Recent Tickets</h2>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Customer</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Subject</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Priority</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Created</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {tickets.map((ticket) => (
                  <tr key={ticket.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{ticket.customer}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">{ticket.subject}</td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs rounded-full ${getStatusClass(ticket.status)}`}>
                        {ticket.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs rounded-full ${getPriorityClass(ticket.priority)}`}>
                        {ticket.priority}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.created}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}

function App() {
  const location = useLocation()
  const isDashboard = location.pathname === '/' || location.pathname === '/dashboard'

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-cyan-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <span className="text-2xl mr-2">🎧</span>
              <span className="text-xl font-bold">Customer Support</span>
            </div>
            <div className="flex space-x-4">
              <Link to="/" className={`px-3 py-2 rounded-md text-sm font-medium ${isDashboard ? 'bg-cyan-700' : 'hover:bg-cyan-700'}`}>Dashboard</Link>
            </div>
          </div>
        </div>
      </nav>
      <Routes>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
      </Routes>
    </div>
  )
}

export default App
