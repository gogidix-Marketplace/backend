// ============================================
// DIGITAL MARKETING - LOGIN PAGE
// ============================================

import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@shared/store'
import { BarChart3, Mail, Lock, AlertCircle } from 'lucide-react'

export function MarketingLoginPage() {
  const navigate = useNavigate()
  const { login } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      await login(email, password)
      navigate('/dashboard')
    } catch (err) {
      setError('Invalid email or password')
    } finally {
      setLoading(false)
    }
  }

  const demoUsers = [
    { email: 'marketing.manager@gogidix.com', name: 'Marketing Manager' },
    { email: 'content.manager@gogidix.com', name: 'Content Manager' },
    { email: 'social.manager@gogidix.com', name: 'Social Media Manager' },
  ]

  const fillDemo = (email: string) => {
    setEmail(email)
    setPassword('password123')
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-900 via-indigo-900 to-blue-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white/20 rounded-2xl mb-4 backdrop-blur">
            <BarChart3 size={32} className="text-white" />
          </div>
          <h1 className="text-2xl font-bold text-white">Marketing Dashboard</h1>
          <p className="text-purple-200">Digital Marketing Department - Port 3050</p>
        </div>

        {/* Login Form */}
        <div className="bg-white rounded-2xl shadow-xl p-8">
          <h2 className="text-xl font-semibold text-slate-900 mb-6">Sign In</h2>

          {error && (
            <div className="flex items-center gap-2 p-3 bg-red-50 border border-red-200 rounded-lg mb-4">
              <AlertCircle size={18} className="text-red-600" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Email</label>
              <div className="relative">
                <Mail size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="your.email@gogidix.com"
                  required
                  className="w-full pl-10 pr-4 py-2.5 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Password</label>
              <div className="relative">
                <Lock size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  required
                  className="w-full pl-10 pr-4 py-2.5 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className={`w-full py-2.5 rounded-lg font-medium text-white transition-colors ${
                loading
                  ? 'bg-slate-300 cursor-not-allowed'
                  : 'bg-blue-600 hover:bg-blue-700'
              }`}
            >
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>

          {/* Demo Accounts */}
          <div className="mt-6 pt-6 border-t border-slate-200">
            <p className="text-sm text-slate-500 mb-3">Demo Accounts (click to autofill):</p>
            <div className="space-y-2">
              {demoUsers.map((user) => (
                <button
                  key={user.email}
                  onClick={() => fillDemo(user.email)}
                  className="w-full flex items-center justify-between p-2 border border-slate-200 rounded-lg hover:bg-slate-50 transition-colors text-left"
                >
                  <span className="text-sm font-medium text-slate-700">{user.name}</span>
                  <span className="text-xs text-slate-500">{user.email}</span>
                </button>
              ))}
            </div>
            <p className="text-xs text-slate-400 mt-3">Password: password123</p>
          </div>
        </div>

        {/* Footer */}
        <p className="text-center text-purple-200 text-sm mt-6">
          © 2025 Gogidix. All rights reserved.
        </p>
      </div>
    </div>
  )
}
