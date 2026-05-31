import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAccountantStore } from '@shared/store'
import { Lock, Mail, Eye, EyeOff } from 'lucide-react'

export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const login = useAccountantStore((s) => s.login)
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    await new Promise((r) => setTimeout(r, 800))
    const success = login(email, password)
    setLoading(false)
    if (success) {
      navigate('/2fa')
    } else {
      setError('Invalid email or password. Please try again.')
    }
  }

  const handleDemoLogin = (demoEmail: string) => {
    setEmail(demoEmail)
    setPassword('password123')
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-teal-600 via-teal-700 to-teal-800 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white/10 backdrop-blur-sm rounded-2xl mb-4">
            <span className="text-2xl font-bold text-white">GA</span>
          </div>
          <h1 className="text-2xl font-bold text-white">Accountant Dashboard</h1>
          <p className="text-teal-200 mt-1">Gogidix Finance Department</p>
        </div>

        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <h2 className="text-xl font-semibold text-gray-800 mb-6">Sign in to your account</h2>

          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-sm text-red-700">{error}</div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <div className="relative">
                <Mail size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none transition-colors text-sm"
                  placeholder="Enter your email"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
              <div className="relative">
                <Lock size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full pl-10 pr-10 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none transition-colors text-sm"
                  placeholder="Enter your password"
                  required
                />
                <button type="button" onClick={() => setShowPassword(!showPassword)} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-2.5 bg-teal-600 hover:bg-teal-700 disabled:bg-teal-400 text-white font-medium rounded-lg transition-colors text-sm"
            >
              {loading ? (
                <span className="flex items-center justify-center gap-2">
                  <svg className="animate-spin h-4 w-4" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" /><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" /></svg>
                  Signing in...
                </span>
              ) : 'Sign In'}
            </button>
          </form>

          <div className="mt-6 pt-6 border-t border-gray-200">
            <p className="text-xs font-medium text-gray-500 uppercase tracking-wider mb-3">Demo Accounts</p>
            <div className="space-y-2">
              <button
                onClick={() => handleDemoLogin('accountant@gogidix.com')}
                className="w-full text-left p-3 bg-gray-50 hover:bg-teal-50 rounded-lg transition-colors"
              >
                <p className="text-sm font-medium text-gray-800">David Wilson</p>
                <p className="text-xs text-gray-500">Accountant — accountant@gogidix.com</p>
              </button>
              <button
                onClick={() => handleDemoLogin('senior-accountant@gogidix.com')}
                className="w-full text-left p-3 bg-gray-50 hover:bg-teal-50 rounded-lg transition-colors"
              >
                <p className="text-sm font-medium text-gray-800">Maria Santos</p>
                <p className="text-xs text-gray-500">Senior Accountant — senior-accountant@gogidix.com</p>
              </button>
            </div>
          </div>
        </div>

        <p className="text-center text-teal-300 text-xs mt-6">Gogidix &copy; 2024 — Finance Department Internal Tool</p>
      </div>
    </div>
  )
}
