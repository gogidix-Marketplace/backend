import { useState, useRef, useEffect, useCallback } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { Navigate } from 'react-router-dom'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Checkbox } from '@shared/components/ui/checkbox'
import { Shield, ArrowLeft, Loader2, RefreshCw } from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { useAuthStore } from '@shared/stores/authStore'

/**
 * Two-Factor Authentication Page
 *
 * Supports:
 * - TOTP (Authenticator app)
 * - SMS
 * - Email
 * - Backup codes
 *
 * Features:
 * - Auto-focus next input
 * - Code expiration countdown
 * - Resend code with cooldown
 * - Max 3 attempts before lockout
 * - Trust device option (30 days)
 */

interface TwoFactorState {
  code: string[]
  isLoading: boolean
  error: string
  attempts: number
  method: 'totp' | 'sms' | 'email'
  isResending: boolean
  resendCooldown: number
  canResendIn: number
  expiresAt: Date | null
  trustDevice: boolean
  showBackupCode: boolean
  backupCode: string
}

const CODE_LENGTH = 6
const MAX_ATTEMPTS = 3
const RESEND_COOLDOWN = 30 // seconds
const CODE_EXPIRY = 5 * 60 * 1000 // 5 minutes

export default function TwoFactorPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { complete2FA, _pendingAuth, isAuthenticated } = useAuthStore()
  const email = location.state?.email || 'ceo@gogidix.com'
  const inputRefs = useRef<(HTMLInputElement | null)[]>([])

  // Protection: Redirect to login if no pending authentication
  // User must have initiated login but not completed 2FA yet
  if (!_pendingAuth && !isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  // If already fully authenticated (2FA completed), redirect to dashboard
  if (isAuthenticated) {
    return <Navigate to="/" replace />
  }

  const [state, setState] = useState<TwoFactorState>({
    code: ['', '', '', '', '', ''],
    isLoading: false,
    error: '',
    attempts: 0,
    method: 'totp',
    isResending: false,
    resendCooldown: RESEND_COOLDOWN,
    canResendIn: 0,
    expiresAt: new Date(Date.now() + CODE_EXPIRY),
    trustDevice: false,
    showBackupCode: false,
    backupCode: '',
  })

  // Focus first input on mount
  useEffect(() => {
    inputRefs.current[0]?.focus()
  }, [])

  // Countdown timer for resend cooldown
  useEffect(() => {
    if (state.canResendIn > 0) {
      const timer = setTimeout(() => {
        setState((prev) => ({ ...prev, canResendIn: prev.canResendIn - 1 }))
      }, 1000)
      return () => clearTimeout(timer)
    }
  }, [state.canResendIn])

  // Start resend cooldown when code is sent
  const startResendCooldown = useCallback(() => {
    setState((prev) => ({ ...prev, canResendIn: state.resendCooldown }))
  }, [state.resendCooldown])

  const handleInputChange = (index: number, value: string) => {
    // Only allow digits
    const numericValue = value.replace(/\D/g, '').slice(0, 1)
    const newCode = [...state.code]
    newCode[index] = numericValue
    setState((prev) => ({ ...prev, code: newCode, error: '' }))

    // Auto-focus next input
    if (numericValue && index < CODE_LENGTH - 1) {
      inputRefs.current[index + 1]?.focus()
    }
  }

  const handleKeyDown = (index: number, key: string) => {
    // Handle backspace - focus previous input
    if (key === 'Backspace' && !state.code[index] && index > 0) {
      inputRefs.current[index - 1]?.focus()
    }
    // Handle paste
    if (key === 'Paste' || (key === 'v' && (navigator as any).clipboard)) {
      navigator.clipboard.readText().then((pastedValue) => {
        const numericValue = pastedValue.replace(/\D/g, '').slice(0, CODE_LENGTH)
        if (numericValue.length === CODE_LENGTH) {
          const newCode = numericValue.split('')
          setState((prev) => ({ ...prev, code: newCode, error: '' }))
        }
      })
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    const fullCode = state.code.join('')

    if (fullCode.length !== CODE_LENGTH) {
      setState((prev) => ({ ...prev, error: `Please enter all ${CODE_LENGTH} digits` }))
      return
    }

    setState((prev) => ({ ...prev, isLoading: true, error: '' }))

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // Mock validation - accept 123456 for demo
    const isValid = fullCode === '123456'

    if (isValid) {
      // Mark 2FA as complete
      complete2FA()
      // Navigate to dashboard
      navigate('/')
    } else {
      const newAttempts = state.attempts + 1
      if (newAttempts >= MAX_ATTEMPTS) {
        setState((prev) => ({
          ...prev,
          isLoading: false,
          error: 'Too many failed attempts. Please contact support.',
          attempts: newAttempts,
        }))
      } else {
        setState((prev) => ({
          ...prev,
          isLoading: false,
          error: `Invalid code. ${MAX_ATTEMPTS - newAttempts} attempts remaining.`,
          attempts: newAttempts,
          code: ['', '', '', '', '', ''],
        }))
        inputRefs.current[0]?.focus()
      }
    }
  }

  const handleResendCode = async () => {
    if (state.canResendIn > 0) return

    setState((prev) => ({ ...prev, isResending: true }))

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000))

    setState((prev) => ({
      ...prev,
      isResending: false,
      code: ['', '', '', '', '', ''],
      expiresAt: new Date(Date.now() + CODE_EXPIRY),
    }))
    startResendCooldown()
    inputRefs.current[0]?.focus()
  }

  const handleBackupCodeSubmit = async () => {
    if (!state.backupCode) {
      setState((prev) => ({ ...prev, error: 'Please enter a backup code' }))
      return
    }

    setState((prev) => ({ ...prev, isLoading: true }))

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // Mock validation - accept 'backup123' for demo
    const isValid = state.backupCode === 'backup123'

    if (isValid) {
      // Mark 2FA as complete
      complete2FA()
      // Navigate to dashboard
      navigate('/')
    } else {
      setState((prev) => ({
        ...prev,
        isLoading: false,
        error: 'Invalid backup code. Please try again.',
      }))
    }
  }

  const getMethodLabel = () => {
    switch (state.method) {
      case 'totp':
        return 'Authenticator App'
      case 'sms':
        return 'SMS'
      case 'email':
        return 'Email'
      default:
        return 'Authenticator'
    }
  }

  const getTimeRemaining = () => {
    if (!state.expiresAt) return null
    const now = new Date()
    const diff = state.expiresAt.getTime() - now.getTime()
    if (diff <= 0) return null
    const minutes = Math.floor(diff / 60000)
    const seconds = Math.floor((diff % 60000) / 1000)
    return `${minutes}:${seconds.toString().padStart(2, '0')}`
  }

  const isExpired = !getTimeRemaining()

  if (state.showBackupCode) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="space-y-2">
            <div className="flex items-center gap-2">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setState((prev) => ({ ...prev, showBackupCode: false }))}
              >
                <ArrowLeft className="h-4 w-4" />
              </Button>
              <Shield className="h-6 w-6 text-primary" />
            </div>
            <CardTitle>Use Backup Code</CardTitle>
            <CardDescription>
              Enter one of your backup codes to access your account
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleBackupCodeSubmit} className="space-y-4">
              {state.error && (
                <div className="rounded-lg bg-red-50 p-3 text-sm text-red-600 dark:bg-red-900/20">
                  {state.error}
                </div>
              )}

              <div className="space-y-2">
                <Label htmlFor="backup-code">Backup Code</Label>
                <Input
                  id="backup-code"
                  placeholder="xxxxx-xxxxx"
                  value={state.backupCode}
                  onChange={(e) =>
                    setState((prev) => ({ ...prev, backupCode: e.target.value, error: '' }))
                  }
                  className="font-mono"
                />
                <p className="text-xs text-muted-foreground">
                  Demo code: <code className="bg-slate-100 dark:bg-slate-800 px-1 rounded">backup123</code>
                </p>
              </div>

              <Button type="submit" className="w-full" disabled={state.isLoading}>
                {state.isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Verifying...
                  </>
                ) : (
                  'Verify Backup Code'
                )}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-2">
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => navigate('/login', { state: { email } })}
            >
              <ArrowLeft className="h-4 w-4" />
            </Button>
            <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-primary/10">
              <Shield className="h-6 w-6 text-primary" />
            </div>
          </div>
          <CardTitle className="text-center">Two-Factor Authentication</CardTitle>
          <CardDescription className="text-center">
            Enter the 6-digit code from your {getMethodLabel()}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Method Selector */}
            <div className="flex justify-center gap-2">
              <Button
                type="button"
                variant={state.method === 'totp' ? 'default' : 'outline'}
                size="sm"
                onClick={() => setState((prev) => ({ ...prev, method: 'totp' }))}
              >
                App
              </Button>
              <Button
                type="button"
                variant={state.method === 'sms' ? 'default' : 'outline'}
                size="sm"
                onClick={() => setState((prev) => ({ ...prev, method: 'sms' }))}
              >
                SMS
              </Button>
              <Button
                type="button"
                variant={state.method === 'email' ? 'default' : 'outline'}
                size="sm"
                onClick={() => setState((prev) => ({ ...prev, method: 'email' }))}
              >
                Email
              </Button>
            </div>

            {state.error && (
              <div className="rounded-lg bg-red-50 p-3 text-sm text-red-600 dark:bg-red-900/20">
                {state.error}
              </div>
            )}

            {/* Code Input */}
            <div className="space-y-2">
              <Label className="text-center text-sm">Enter verification code</Label>
              <div className="flex justify-center gap-2">
                {state.code.map((digit, index) => (
                  <Input
                    key={index}
                    ref={(el) => (inputRefs.current[index] = el)}
                    type="text"
                    inputMode="numeric"
                    maxLength={1}
                    value={digit}
                    onChange={(e) => handleInputChange(index, e.target.value)}
                    onKeyDown={(e) => handleKeyDown(index, e.key)}
                    className={cn(
                      'w-12 h-14 text-center text-2xl font-bold',
                      isExpired && 'border-red-500'
                    )}
                    disabled={state.isLoading || isExpired}
                  />
                ))}
              </div>

              {/* Timer */}
              {getTimeRemaining() && (
                <div className="text-center text-sm text-muted-foreground">
                  Code expires in <span className="font-mono">{getTimeRemaining()}</span>
                </div>
              )}
              {isExpired && (
                <div className="text-center text-sm text-red-600">Code has expired</div>
              )}
            </div>

            {/* Trust Device */}
            <div className="flex items-center justify-center space-x-2">
              <Checkbox
                id="trust"
                checked={state.trustDevice}
                onCheckedChange={(checked) =>
                  setState((prev) => ({ ...prev, trustDevice: checked === true }))
                }
              />
              <Label htmlFor="trust" className="text-sm font-normal cursor-pointer select-none">
                Remember this device for 30 days
              </Label>
            </div>

            {/* Submit Button */}
            <Button
              type="submit"
              className="w-full"
              disabled={state.isLoading || isExpired || state.code.some((d) => !d)}
            >
              {state.isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Verifying...
                </>
              ) : (
                'Verify'
              )}
            </Button>

            {/* Resend Code */}
            <div className="text-center">
              {state.canResendIn > 0 ? (
                <p className="text-sm text-muted-foreground">
                  Resend code in <span className="font-mono">{state.canResendIn}s</span>
                </p>
              ) : (
                <Button
                  type="button"
                  variant="link"
                  size="sm"
                  onClick={handleResendCode}
                  disabled={state.isResending}
                  className="text-sm"
                >
                  {state.isResending ? (
                    <>
                      <Loader2 className="mr-2 h-3 w-3 animate-spin" />
                      Sending...
                    </>
                  ) : (
                    <>
                      <RefreshCw className="mr-1 h-3 w-3" />
                      Resend Code
                    </>
                  )}
                </Button>
              )}
            </div>

            {/* Backup Code Link */}
            <div className="text-center">
              <Button
                type="button"
                variant="link"
                size="sm"
                onClick={() => setState((prev) => ({ ...prev, showBackupCode: true }))}
                className="text-sm text-muted-foreground"
              >
                Use a backup code instead
              </Button>
            </div>

            {/* Demo Hint */}
            <div className="text-center text-xs text-muted-foreground bg-slate-100 dark:bg-slate-800 p-2 rounded">
              Demo code: <code className="bg-slate-200 dark:bg-slate-700 px-1 rounded">123456</code>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
