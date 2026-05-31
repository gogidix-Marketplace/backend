import { useState, useEffect, useLayoutEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Shield, ArrowLeft } from 'lucide-react'
import { useAuthStore } from '@shared/stores/authStore'
import { AuthCard, OTPInput } from '@shared/components/auth'

type TwoFactorStep = 'verify' | 'success'
type ExecutiveRole = 'CEO' | 'CFO' | 'COO' | 'CTO'

interface RoleConfig {
  title: string
  dashboardName: string
}

const ROLE_CONFIGS: Record<ExecutiveRole, RoleConfig> = {
  CEO: { title: 'CEO', dashboardName: 'CEO' },
  CFO: { title: 'CFO', dashboardName: 'CFO' },
  COO: { title: 'COO', dashboardName: 'COO' },
  CTO: { title: 'CTO', dashboardName: 'CTO' },
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

export default function TwoFactorAuthPage() {
  const navigate = useNavigate()
  const { complete2FA, isAuthenticated, user } = useAuthStore()
  const role = getRoleFromPort()
  const redirectPath = getRedirectPath(role)
  const config = ROLE_CONFIGS[role]
  const [step, setStep] = useState<TwoFactorStep>('verify')
  const [code, setCode] = useState(['', '', '', '', '', ''])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  useLayoutEffect(() => {
    document.body.style.overflow = ''
    document.documentElement.style.overflow = ''
  }, [])

  useEffect(() => {
    if (isAuthenticated) {
      navigate(redirectPath)
    }
  }, [isAuthenticated, navigate, redirectPath])

  const handleVerify = async () => {
    setError('')
    const fullCode = code.join('')

    if (fullCode.length !== 6) {
      setError('Please enter the complete 6-digit code')
      return
    }

    setIsLoading(true)

    try {
      await new Promise((resolve) => setTimeout(resolve, 1500))

      if (fullCode === '123456') {
        complete2FA()
        setStep('success')
      } else {
        setError('Invalid code. Please try again.')
        setCode(['', '', '', '', '', ''])
      }
    } catch (err) {
      setError('Verification failed. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  if (step === 'success') {
    return (
      <div
        className="fixed inset-0 flex items-center justify-center p-4"
        style={{ background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)', zIndex: 9999 }}
      >
        <div
          className="w-full max-w-[480px] bg-white rounded-2xl p-10 text-center"
          style={{ boxShadow: '0 8px 32px rgba(0, 0, 0, 0.12)' }}
        >
          <div className="w-20 h-20 bg-emerald-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg className="w-10 h-10 text-emerald-600" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
              <polyline points="22 4 12 14.01 9 11.01" />
            </svg>
          </div>
          <h2
            className="text-2xl font-semibold mb-2"
            style={{ color: '#1E293B', letterSpacing: '-0.5px' }}
          >
            Verification Successful!
          </h2>
          <p className="mb-8" style={{ color: '#64748B' }}>
            You can now access your {config.dashboardName} dashboard.
          </p>
          <button
            onClick={() => navigate(redirectPath)}
            className="w-full text-white font-medium rounded-lg transition-colors hover:bg-slate-800"
            style={{
              backgroundColor: '#0F172A',
              height: '48px',
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
      <div className="w-full max-w-[480px]">
        <AuthCard
          title={
            <div className="flex items-center justify-center gap-2">
              <div
                className="w-6 h-6 rounded-lg flex items-center justify-center"
                style={{ backgroundColor: 'rgba(15, 23, 42, 0.1)' }}
              >
                <Shield className="w-4 h-4" style={{ color: '#0F172A' }} />
              </div>
              <span>Two-Factor Authentication</span>
            </div>
          }
          subtitle="Enter the 6-digit code from your authenticator app"
          showLogo={false}
        >
          <button
            onClick={() => navigate('/login')}
            className="flex items-center gap-2 text-sm mb-4 transition-colors hover:opacity-70"
            style={{ color: '#64748B' }}
          >
            <ArrowLeft className="w-4 h-4" />
            Back to Login
          </button>

          <form onSubmit={(e) => { e.preventDefault(); handleVerify() }} className="space-y-6">
            {error && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg flex items-start gap-2 text-sm">
                <svg className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
                <span style={{ color: '#DC2626' }}>{error}</span>
              </div>
            )}

            <div>
              <OTPInput
                value={code}
                onChange={setCode}
                error={!!error}
                disabled={isLoading}
              />
            </div>

            <button
              type="submit"
              disabled={isLoading || code.join('').length !== 6}
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
                  Verifying...
                </span>
              ) : (
                'Verify'
              )}
            </button>
          </form>

          <div className="mt-6 text-center space-y-2">
            <button
              type="button"
              className="text-sm font-medium hover:underline transition-colors"
              style={{ color: '#3B82F6' }}
            >
              Resend Code
            </button>
            <p className="text-sm" style={{ color: '#64748B' }}>
              or{' '}
              <button
                type="button"
                className="hover:underline transition-colors"
                style={{ color: '#64748B' }}
              >
                use backup code instead
              </button>
            </p>
          </div>

          <div
            className="mt-6 p-3 rounded-lg text-xs"
            style={{
              backgroundColor: '#F8FAFC',
              color: '#64748B',
              borderTop: '1px solid #E2E8F0',
              paddingTop: '16px',
            }}
          >
            <p>
              <strong style={{ color: '#475569' }}>Demo code: 123456</strong>
            </p>
            <p className="mt-1">Haven't set up 2FA? Use the demo code above.</p>
          </div>
        </AuthCard>
      </div>
    </div>
  )
}
