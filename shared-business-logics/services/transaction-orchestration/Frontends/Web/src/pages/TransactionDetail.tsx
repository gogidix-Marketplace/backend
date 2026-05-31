import { useParams } from 'react-router-dom'
import { ArrowLeft, Clock, CheckCircle, AlertCircle } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function TransactionDetail() {
  const { id } = useParams()

  return (
    <div>
      <Link to="/transactions" className="flex items-center gap-2 text-primary-600 hover:text-primary-900 mb-6">
        <ArrowLeft className="w-4 h-4" />
        Back to Transactions
      </Link>

      <div className="card mb-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Transaction {id}</h1>
            <p className="mt-2 text-gray-600">Started on January 28, 2024 at 10:30 AM</p>
          </div>
          <span className="px-4 py-2 text-sm font-medium bg-green-100 text-green-800 rounded">
            Completed
          </span>
        </div>

        <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
          <div>
            <p className="text-sm font-medium text-gray-500">Type</p>
            <p className="mt-1 text-lg font-semibold">Payment</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-500">Amount</p>
            <p className="mt-1 text-lg font-semibold">$1,234.56</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-500">Duration</p>
            <p className="mt-1 text-lg font-semibold">2m 34s</p>
          </div>
        </div>
      </div>

      <div className="card mb-6">
        <h2 className="text-lg font-semibold mb-4">Progress Steps</h2>
        <div className="space-y-4">
          {[
            { name: 'Initialization', status: 'completed', time: '10:30:00' },
            { name: 'Validation', status: 'completed', time: '10:30:15' },
            { name: 'Payment Processing', status: 'completed', time: '10:31:00' },
            { name: 'Confirmation', status: 'completed', time: '10:32:34' },
          ].map((step, i) => (
            <div key={i} className="flex items-center gap-4">
              <div className={`p-2 rounded-full ${
                step.status === 'completed' ? 'bg-green-100' : 'bg-gray-100'
              }`}>
                {step.status === 'completed' ? (
                  <CheckCircle className="w-5 h-5 text-green-600" />
                ) : (
                  <Clock className="w-5 h-5 text-gray-400" />
                )}
              </div>
              <div className="flex-1">
                <p className="font-medium">{step.name}</p>
                <p className="text-sm text-gray-500">{step.time}</p>
              </div>
              <span className={`px-2 py-1 text-xs font-medium rounded ${
                step.status === 'completed' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
              }`}>
                {step.status}
              </span>
            </div>
          ))}
        </div>
      </div>

      <div className="card">
        <h2 className="text-lg font-semibold mb-4">Audit Trail</h2>
        <div className="space-y-3">
          {[
            { event: 'Transaction initiated', user: 'system', time: '10:30:00' },
            { event: 'Payment validated', user: 'system', time: '10:30:15' },
            { event: 'Payment processed', user: 'payment-service', time: '10:31:00' },
            { event: 'Confirmation sent', user: 'notification-service', time: '10:32:34' },
          ].map((log, i) => (
            <div key={i} className="flex items-start gap-3 p-3 bg-gray-50 rounded">
              <div className="p-2 bg-primary-100 rounded">
                <Clock className="w-4 h-4 text-primary-600" />
              </div>
              <div className="flex-1">
                <p className="font-medium">{log.event}</p>
                <p className="text-sm text-gray-500">by {log.user} at {log.time}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
