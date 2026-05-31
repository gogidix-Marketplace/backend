import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, CheckCircle } from 'lucide-react';

export function NewTicketPage() {
  const navigate = useNavigate();
  const [isSubmitted, setIsSubmitted] = useState(false);

  if (isSubmitted) {
    return (
      <div className="max-w-lg mx-auto">
        <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <CheckCircle className="w-8 h-8 text-green-600" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Ticket Submitted!</h2>
          <p className="text-gray-500 mb-6">Your support request has been created. You will receive a confirmation email shortly.</p>
          <div className="bg-gray-50 rounded-lg p-4 mb-6">
            <p className="text-sm text-gray-500">Ticket ID</p>
            <p className="text-lg font-mono font-semibold text-gray-900">TKT-{Math.floor(Math.random() * 900000) + 100000}</p>
          </div>
          <button
            onClick={() => navigate('/')}
            className="w-full py-3 px-4 bg-primary-600 text-white font-medium rounded-lg hover:bg-primary-700 transition-colors"
          >
            Back to My Tickets
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto">
      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6"
      >
        <ArrowLeft className="w-4 h-4" />
        <span className="text-sm font-medium">Back</span>
      </button>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 md:p-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">Submit New Ticket</h1>
        <p className="text-gray-500 mb-6">Fill out the form below and our support team will assist you.</p>

        <form onSubmit={(e) => { e.preventDefault(); setIsSubmitted(true); }} className="space-y-6">
          <div>
            <label htmlFor="category" className="block text-sm font-medium text-gray-700 mb-2">
              Category <span className="text-danger-500">*</span>
            </label>
            <select
              id="category"
              required
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none"
            >
              <option value="">Select a category</option>
              <option value="technical">Technical Issue</option>
              <option value="billing">Billing & Payments</option>
              <option value="account">Account & Access</option>
              <option value="feature">Feature Request</option>
              <option value="other">Other</option>
            </select>
          </div>

          <div>
            <label htmlFor="subject" className="block text-sm font-medium text-gray-700 mb-2">
              Subject <span className="text-danger-500">*</span>
            </label>
            <input
              id="subject"
              type="text"
              required
              placeholder="Brief description of your issue"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none"
            />
          </div>

          <div>
            <label htmlFor="priority" className="block text-sm font-medium text-gray-700 mb-2">
              Priority
            </label>
            <select
              id="priority"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none"
            >
              <option value="low">Low</option>
              <option value="medium" selected>Medium</option>
              <option value="high">High</option>
              <option value="urgent">Urgent</option>
            </select>
          </div>

          <div>
            <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-2">
              Description <span className="text-danger-500">*</span>
            </label>
            <textarea
              id="description"
              required
              rows={6}
              placeholder="Please provide detailed information about your issue..."
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none resize-none"
            />
          </div>

          <div>
            <label htmlFor="attachments" className="block text-sm font-medium text-gray-700 mb-2">
              Attachments
            </label>
            <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-primary-400 transition-colors cursor-pointer">
              <p className="text-sm text-gray-500">Drag and drop files here, or click to browse</p>
              <p className="text-xs text-gray-400 mt-1">PNG, JPG, PDF up to 10MB</p>
            </div>
          </div>

          <div className="flex gap-4 pt-4">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="flex-1 py-3 px-4 border border-gray-300 text-gray-700 font-medium rounded-lg hover:bg-gray-50 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex-1 py-3 px-4 bg-primary-600 text-white font-medium rounded-lg hover:bg-primary-700 transition-colors"
            >
              Submit Ticket
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
