import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, User, Paperclip } from 'lucide-react';

export function TicketDetailsPage() {
  const { ticketId } = useParams<{ ticketId: string }>();
  const navigate = useNavigate();

  const messages = [
    {
      id: 1,
      author: 'You',
      role: 'customer',
      content: 'I am unable to access my account after performing a password reset. The system keeps saying "Invalid credentials" even though I just reset the password.',
      timestamp: '2024-02-20T10:30:00',
      attachments: [],
    },
    {
      id: 2,
      author: 'Support Team',
      role: 'agent',
      content: 'Hello! Thank you for reaching out. I understand you\'re having trouble accessing your account after a password reset. Let me help you with this.',
      timestamp: '2024-02-20T10:45:00',
      attachments: [],
    },
    {
      id: 3,
      author: 'Support Team',
      role: 'agent',
      content: 'I\'ve checked your account and can see that the password reset was successful. However, there might be a sync delay in our system. Could you please try clearing your browser cache and cookies, then attempt to log in again?',
      timestamp: '2024-02-20T10:47:00',
      attachments: [],
    },
  ];

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 text-gray-600 hover:text-gray-900"
        >
          <ArrowLeft className="w-4 h-4" />
          <span className="text-sm font-medium">Back to Tickets</span>
        </button>
        <div className="flex gap-2">
          <button className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50">
            Close Ticket
          </button>
        </div>
      </div>

      {/* Ticket Info */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
        <div className="flex items-start justify-between mb-4">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <h1 className="text-xl font-bold text-gray-900">{ticketId}</h1>
              <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                Open
              </span>
              <span className="inline-flex items-center px-2 py-1 rounded text-xs font-semibold bg-orange-100 text-orange-700">
                HIGH
              </span>
            </div>
            <h2 className="text-lg font-medium text-gray-900">Unable to access my account after password reset</h2>
          </div>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 pt-4 border-t border-gray-100">
          <div>
            <p className="text-xs text-gray-500">Created</p>
            <p className="text-sm font-medium text-gray-900">Feb 20, 2024</p>
          </div>
          <div>
            <p className="text-xs text-gray-500">Last Updated</p>
            <p className="text-sm font-medium text-gray-900">2 hours ago</p>
          </div>
          <div>
            <p className="text-xs text-gray-500">Category</p>
            <p className="text-sm font-medium text-gray-900">Account & Access</p>
          </div>
          <div>
            <p className="text-xs text-gray-500">Assigned To</p>
            <p className="text-sm font-medium text-gray-900">Sarah Johnson</p>
          </div>
        </div>
      </div>

      {/* Conversation */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold text-gray-900">Conversation</h3>

        {messages.map((message) => (
          <div
            key={message.id}
            className={`flex ${message.role === 'customer' ? 'justify-end' : 'justify-start'}`}
          >
            <div className={`max-w-2xl ${message.role === 'customer' ? 'order-2' : 'order-1'}`}>
              <div className={`flex items-start gap-3 ${message.role === 'customer' ? 'flex-row-reverse' : ''}`}>
                <div className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${
                  message.role === 'customer' ? 'bg-primary-100' : 'bg-gray-200'
                }`}>
                  <User className="w-4 h-4 text-gray-600" />
                </div>
                <div className={`flex-1 ${message.role === 'customer' ? 'text-right' : ''}`}>
                  <div className={`inline-block rounded-2xl px-4 py-3 ${
                    message.role === 'customer'
                      ? 'bg-primary-600 text-white rounded-br-sm'
                      : 'bg-gray-100 text-gray-900 rounded-bl-sm'
                  }`}>
                    <p className="text-sm whitespace-pre-wrap">{message.content}</p>
                  </div>
                  <p className="text-xs text-gray-400 mt-1">
                    {message.author} • {new Date(message.timestamp).toLocaleString()}
                  </p>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Reply Form */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Reply</h3>
        <textarea
          rows={4}
          placeholder="Type your message here..."
          className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none resize-none"
        />
        <div className="flex items-center justify-between mt-4">
          <button className="flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900">
            <Paperclip className="w-4 h-4" />
            Attach File
          </button>
          <button className="px-6 py-2 bg-primary-600 text-white font-medium rounded-lg hover:bg-primary-700 transition-colors">
            Send Reply
          </button>
        </div>
      </div>
    </div>
  );
}
