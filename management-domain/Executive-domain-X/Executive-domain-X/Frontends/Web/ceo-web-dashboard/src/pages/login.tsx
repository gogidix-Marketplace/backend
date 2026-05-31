import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore, MOCK_PASSWORD } from '@shared/stores/authStore'
import { AuthCard, FloatingLabelInput } from '@shared/components/auth'

type ExecutiveRole = 'CEO' | 'CFO' | 'COO' | 'CTO'

interface RoleConfig {
  title: string
  dashboardName: string
  demoEmail: string
}

const ROLE_CONFIGS: Record<ExecutiveRole, RoleConfig> = {
  CEO: { title: 'CEO', dashboardName: 'CEO', demoEmail: 'ceo@gogidix.com' },
  CFO: { title: 'CFO', dashboardName: 'CFO', demoEmail: 'cfo@gogidix.com' },
  COO: { title: 'COO', dashboardName: 'COO', demoEmail: 'coo@gogidix.com' },
  CTO: { title: 'CTO', dashboardName: 'CTO', demoEmail: 'cto@gogidix.com' },
}

function getRoleFromPort(): ExecutiveRole {
  const port = window.location.port || import.meta.env.VITE_PORT || ''
  if (port === '3012') return 'CFO'
  if (port === '3013') return 'COO'
  if (port === '3014') return 'CTO'
  return 'CEO'
}

function getRedirectPath(role: ExecutiveRole): string {
  return role === 'CEO' ? '/' : `/${role.toLowerCase()}`
}

export default function LoginPage() {
  const navigate = useNavigate()
  const { login, isAuthenticated, user } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [rememberMe, setRememberMe] = useState(false)

  const role = getRoleFromPort()
  const config = ROLE_CONFIGS[role]
  const redirectPath = getRedirectPath(role)

  useEffect(() => {
    if (isAuthenticated) {
      const timer = setTimeout(() => navigate(redirectPath), 1500)
      return () => clearTimeout(timer)
    }
  }, [isAuthenticated, navigate, redirectPath])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      await login(email, password)
      const authStore = useAuthStore.getState()
      if (authStore.isAuthenticated) {
        navigate(redirectPath)
      } else if (authStore._pendingAuth) {
        navigate('/two-factor')
      }
    } catch (err) {
      setError('Invalid email or password')
    } finally {
      setIsLoading(false)
    }
  }

  const fillDemoCredentials = () => {
    setEmail(config.demoEmail)
    setPassword(MOCK_PASSWORD)
    setError('')
  }

  if (isAuthenticated && user) {
    return (
      <div
        className="min-h-screen flex items-center justify-center p-4"
        style={{ background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)' }}
      >
        <div
          className="w-full max-w-[480px] bg-white rounded-2xl p-10 text-center"
          style={{ boxShadow: '0 8px 32px rgba(0, 0, 0, 0.12)' }}
        >
          <div className="w-16 h-16 bg-emerald-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg className="w-8 h-8 text-emerald-600" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
              <polyline points="22 4 12 14.01 9 11.01" />
            </svg>
          </div>
          <h1 className="text-xl font-semibold mb-2" style={{ color: '#1E293B' }}>
            Already Logged In
          </h1>
          <p className="mb-6" style={{ color: '#64748B' }}>
            Logged in as{' '}
            <span className="font-semibold" style={{ color: '#3B82F6' }}>
              {user.displayName || user.email}
            </span>
          </p>
          <p className="text-sm mb-6" style={{ color: '#64748B' }}>
            Redirecting to dashboard...
          </p>
          <button
            onClick={() => navigate(redirectPath)}
            className="w-full py-3 text-white font-medium rounded-lg transition-colors hover:bg-slate-800"
            style={{
              backgroundColor: '#0F172A',
              minHeight: '48px',
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
            }}
          >
            Continue to Dashboard
          </button>
        </div>
      </div>
    )
  }

  return (
    <div
      className="fixed inset-0 flex items-center justify-center p-4"
      style={{ background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)', zIndex: 9999 }}
    >
      <AuthCard
        title="Welcome Back"
        subtitle={`Sign in to access your ${config.dashboardName} dashboard`}
      >
        <form onSubmit={handleSubmit} className="space-y-5">
          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-lg flex items-start gap-2 text-sm">
              <svg className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
              </svg>
              <span style={{ color: '#DC2626' }}>{error}</span>
            </div>
          )}

          <FloatingLabelInput
            type="email"
            label="Email Address"
            placeholder="Enter your email"
            value={email}
            onChange={setEmail}
            error={!email && error ? 'Email is required' : undefined}
            autoComplete="email"
            required
          />

          <FloatingLabelInput
            type="password"
            label="Password"
            placeholder="Enter your password"
            value={password}
            onChange={setPassword}
            error={!password && error ? 'Password is required' : undefined}
            autoComplete="current-password"
            required
          />

          <div className="flex items-center justify-between">
            <label className="flex items-center gap-2 cursor-pointer group">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                className="w-4 h-4 rounded border-slate-300"
                style={{ accentColor: '#0F172A' }}
              />
              <span className="text-sm group-hover:text-slate-700" style={{ color: '#64748B' }}>
                Remember me
              </span>
            </label>
            <a
              href="/forgot-password"
              className="text-sm font-medium hover:underline"
              style={{ color: '#3B82F6' }}
            >
              Forgot password?
            </a>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full text-white font-medium rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed"
            style={{
              backgroundColor: '#0F172A',
              height: '48px',
              boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
            }}
          >
            {isLoading ? (
              <span className="flex items-center justify-center gap-2">
                <svg className="animate-spin h-5 w-5" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                </svg>
                Signing in...
              </span>
            ) : (
              'Sign In'
            )}
          </button>
        </form>

        <div
          className="mt-6 pt-6 text-center"
          style={{ borderTop: '1px solid #E2E8F0' }}
        >
          <button
            type="button"
            onClick={fillDemoCredentials}
            className="text-sm font-medium transition-colors hover:opacity-80"
            style={{ color: '#64748B' }}
          >
            Fill demo credentials
          </button>
          <div
            className="mt-3 p-3 rounded-lg text-xs"
            style={{ backgroundColor: '#F8FAFC', color: '#64748B' }}
          >
            <p>
              <strong style={{ color: '#475569' }}>Demo Account:</strong>
            </p>
            <p className="mt-1">Email: {config.demoEmail}</p>
            <p>Password: {MOCK_PASSWORD}</p>
          </div>
        </div>
      </AuthCard>
    </div>
  )
}
