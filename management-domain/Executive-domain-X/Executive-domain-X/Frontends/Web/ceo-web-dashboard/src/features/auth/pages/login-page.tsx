import { useState, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore, MOCK_PASSWORD } from '@shared/stores/authStore'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Checkbox } from '@shared/components/ui/checkbox'
import { Lock, Mail, AlertCircle, Eye, EyeOff, Loader2 } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * Enhanced Login Page with:
 * - Email validation (must be gogidix.com domain)
 * - Password strength validation
 * - Loading state
 * - Remember me option
 * - Forgot password link
 * - Demo credentials quick fill
 */

const EMAIL_DOMAIN = 'gogidix.com'
const PASSWORD_MIN_LENGTH = 8

interface ValidationError {
  email?: string
  password?: string
}

function validateEmail(email: string): string | undefined {
  if (!email) {
    return 'Email is required'
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email)) {
    return 'Please enter a valid email address'
  }
  if (!email.toLowerCase().endsWith(`@${EMAIL_DOMAIN}`)) {
    return `Email must be from @${EMAIL_DOMAIN} domain`
  }
  return undefined
}

function validatePassword(password: string): string | undefined {
  if (!password) {
    return 'Password is required'
  }
  if (password.length < PASSWORD_MIN_LENGTH) {
    return `Password must be at least ${PASSWORD_MIN_LENGTH} characters`
  }
  const hasUpperCase = /[A-Z]/.test(password)
  const hasLowerCase = /[a-z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  if (!hasUpperCase || !hasLowerCase || !hasNumber) {
    return 'Password must contain at least one uppercase letter, one lowercase letter, and one number'
  }
  return undefined
}

export default function LoginPage() {
  const navigate = useNavigate()
  const { login, isAuthenticated, _pendingAuth } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const [rememberMe, setRememberMe] = useState(false)
  const [validationErrors, setValidationErrors] = useState<ValidationError>({})
  const [touched, setTouched] = useState<{ email: boolean; password: boolean }>({
    email: false,
    password: false,
  })

  // If already authenticated, redirect to dashboard
  if (isAuthenticated) {
    navigate('/')
    return null
  }

  const validateField = useCallback(
    (field: 'email' | 'password', value: string) => {
      const error = field === 'email' ? validateEmail(value) : validatePassword(value)
      setValidationErrors((prev) => ({ ...prev, [field]: error }))
      return error
    },
    []
  )

  const handleEmailBlur = () => {
    setTouched((prev) => ({ ...prev, email: true }))
    validateField('email', email)
  }

  const handlePasswordBlur = () => {
    setTouched((prev) => ({ ...prev, password: true }))
    validateField('password', password)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')

    // Validate all fields
    const emailError = validateField('email', email)
    const passwordError = validateField('password', password)
    setTouched({ email: true, password: true })

    if (emailError || passwordError) {
      return
    }

    setIsLoading(true)

    try {
      await login(email, password)

      // Check authStore state to determine next step
      // If _pendingAuth exists, 2FA is required
      if (_pendingAuth) {
        navigate('/two-factor', { state: { email } })
      } else if (isAuthenticated) {
        // Check if first-time login (onboarding required)
        const isFirstTime = email === 'new-ceo@gogidix.com' // Mock new user
        if (isFirstTime) {
          navigate('/onboarding')
        } else {
          navigate('/')
        }
      }
    } catch (err) {
      setError('Invalid email or password. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  const fillDemoCredentials = (type: 'ceo' | 'acting' | 'new') => {
    const credentials = {
      ceo: { email: 'ceo@gogidix.com', password: MOCK_PASSWORD },
      acting: { email: 'acting-ceo@gogidix.com', password: MOCK_PASSWORD },
      new: { email: 'new-ceo@gogidix.com', password: MOCK_PASSWORD },
    }
    setEmail(credentials[type].email)
    setPassword(credentials[type].password)
    setError('')
    setValidationErrors({})
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 via-blue-50 to-slate-100 dark:from-slate-900 dark:via-blue-950 dark:to-slate-800 px-4 py-8">
      <Card className="w-full max-w-md shadow-xl border-0 bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm">
        <CardHeader className="space-y-4 text-center pb-2">
          <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-[#0D47A1] to-[#1565C0] shadow-lg">
            <span className="text-3xl font-bold text-white">G</span>
          </div>
          <div>
            <CardTitle className="text-2xl font-bold text-slate-900 dark:text-white">
              Executive Suite
            </CardTitle>
            <CardDescription className="text-base">
              Sign in to access your CEO dashboard
            </CardDescription>
          </div>
        </CardHeader>
        <CardContent className="pt-4">
          <form onSubmit={handleSubmit} className="space-y-5">
            {/* Error Message */}
            {error && (
              <div className="flex items-start gap-2 rounded-lg bg-red-50 p-3 text-sm text-red-700 dark:bg-red-900/20 dark:text-red-400 border border-red-200 dark:border-red-800">
                <AlertCircle className="h-5 w-5 mt-0.5 flex-shrink-0" />
                <span>{error}</span>
              </div>
            )}

            {/* Email Field */}
            <div className="space-y-2">
              <Label htmlFor="email" className="text-sm font-medium">
                Email Address
              </Label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  id="email"
                  type="email"
                  placeholder={`name@${EMAIL_DOMAIN}`}
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value)
                    if (touched.email) {
                      validateField('email', e.target.value)
                    }
                  }}
                  onBlur={handleEmailBlur}
                  className={cn(
                    'pl-10 h-11',
                    touched.email && validationErrors.email && 'border-red-500 focus-visible:ring-red-500'
                  )}
                  required
                  aria-invalid={touched.email && !!validationErrors.email}
                  aria-describedby={touched.email && validationErrors.email ? 'email-error' : undefined}
                />
              </div>
              {touched.email && validationErrors.email && (
                <p id="email-error" className="text-sm text-red-600 dark:text-red-400 flex items-center gap-1">
                  {validationErrors.email}
                </p>
              )}
            </div>

            {/* Password Field */}
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <Label htmlFor="password" className="text-sm font-medium">
                  Password
                </Label>
                <button
                  type="button"
                  onClick={() => navigate('/forgot-password')}
                  className="text-sm font-medium text-primary hover:underline"
                >
                  Forgot password?
                </button>
              </div>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => {
                    setPassword(e.target.value)
                    if (touched.password) {
                      validateField('password', e.target.value)
                    }
                  }}
                  onBlur={handlePasswordBlur}
                  className={cn(
                    'pl-10 pr-10 h-11',
                    touched.password && validationErrors.password && 'border-red-500 focus-visible:ring-red-500'
                  )}
                  required
                  aria-invalid={touched.password && !!validationErrors.password}
                  aria-describedby={
                    touched.password && validationErrors.password ? 'password-error' : undefined
                  }
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                >
                  {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                </button>
              </div>
              {touched.password && validationErrors.password && (
                <p id="password-error" className="text-sm text-red-600 dark:text-red-400">
                  {validationErrors.password}
                </p>
              )}
            </div>

            {/* Remember Me */}
            <div className="flex items-center space-x-2">
              <Checkbox
                id="remember"
                checked={rememberMe}
                onCheckedChange={(checked) => setRememberMe(checked === true)}
              />
              <Label
                htmlFor="remember"
                className="text-sm font-normal cursor-pointer select-none"
              >
                Keep me signed in for 24 hours
              </Label>
            </div>

            {/* Submit Button */}
            <Button
              type="submit"
              className="w-full h-11 bg-gradient-to-r from-[#0D47A1] to-[#1565C0] hover:from-[#0D3366] hover:to-[#0D47A1] text-white font-medium shadow-md"
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Signing in...
                </>
              ) : (
                'Sign In'
              )}
            </Button>

            {/* Demo Credentials */}
            <div className="relative my-6">
              <div className="absolute inset-0 flex items-center">
                <span className="w-full border-t border-slate-200 dark:border-slate-700" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-white dark:bg-slate-900 px-3 text-muted-foreground font-medium">
                  Demo Accounts
                </span>
              </div>
            </div>

            <div className="space-y-3 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50 p-4">
              <p className="text-center text-sm font-medium text-slate-700 dark:text-slate-300">
                Quick Fill Demo Account
              </p>
              <div className="grid grid-cols-3 gap-2">
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('ceo')}
                >
                  CEO (2FA)
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('acting')}
                >
                  Acting CEO
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('new')}
                >
                  New User
                </Button>
              </div>
              <div className="text-center text-xs text-muted-foreground">
                All CEO accounts require 2FA verification
                <br />
                Email: <span className="font-mono">ceo@gogidix.com</span>
                <br />
                Password: <span className="font-mono">{MOCK_PASSWORD}</span>
                <br />
                2FA Code: <span className="font-mono">123456</span> (demo)
              </div>
            </div>

            {/* Footer */}
            <div className="text-center text-sm text-muted-foreground">
              Protected by enterprise-grade security.
              <br />
              Need help? Contact{' '}
              <a href="mailto:support@gogidix.com" className="text-primary hover:underline">
                IT Support
              </a>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
