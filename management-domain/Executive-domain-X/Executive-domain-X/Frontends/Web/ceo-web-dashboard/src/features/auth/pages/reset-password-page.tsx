import { useState, useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Lock, Eye, EyeOff, CheckCircle2, Loader2, AlertCircle } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * Reset Password Page
 *
 * Flow:
 * 1. User clicks link from email with token
 * 2. Validate token (from URL params)
 * 3. Show reset form if valid, error if expired
 * 4. Enter new password + confirm
 * 5. Update password, invalidate existing sessions
 * 6. Redirect to login with success message
 */

interface PasswordStrength {
  score: number
  hasMinLength: boolean
  hasUpperCase: boolean
  hasLowerCase: boolean
  hasNumber: boolean
  hasSpecial: boolean
}

export default function ResetPasswordPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token')

  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)
  const [tokenValid, setTokenValid] = useState(true)
  const [validatingToken, setValidatingToken] = useState(true)

  // Validate token on mount
  useEffect(() => {
    const validateToken = async () => {
      setValidatingToken(true)
      // Simulate API call to validate token
      await new Promise((resolve) => setTimeout(resolve, 500))
      // For demo, any token is valid unless it's 'expired'
      if (token === 'expired') {
        setTokenValid(false)
      }
      setValidatingToken(false)
    }
    validateToken()
  }, [token])

  const calculatePasswordStrength = (pwd: string): PasswordStrength => {
    return {
      score: calculateScore(pwd),
      hasMinLength: pwd.length >= 8,
      hasUpperCase: /[A-Z]/.test(pwd),
      hasLowerCase: /[a-z]/.test(pwd),
      hasNumber: /[0-9]/.test(pwd),
      hasSpecial: /[!@#$%^&*(),.?":{}|<>]/.test(pwd),
    }
  }

  const calculateScore = (pwd: string): number => {
    let score = 0
    if (pwd.length >= 8) score += 1
    if (pwd.length >= 12) score += 1
    if (/[A-Z]/.test(pwd)) score += 1
    if (/[a-z]/.test(pwd)) score += 1
    if (/[0-9]/.test(pwd)) score += 1
    if (/[!@#$%^&*(),.?":{}|<>]/.test(pwd)) score += 1
    return Math.min(score, 5)
  }

  const getStrengthLabel = (score: number): { label: string; color: string } => {
    if (score < 2) return { label: 'Weak', color: 'bg-red-500' }
    if (score < 4) return { label: 'Fair', color: 'bg-yellow-500' }
    if (score < 5) return { label: 'Good', color: 'bg-blue-500' }
    return { label: 'Strong', color: 'bg-green-500' }
  }

  const strength = calculatePasswordStrength(password)
  const strengthInfo = getStrengthLabel(strength.score)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')

    // Validation
    if (!password) {
      setError('Please enter a new password')
      return
    }

    if (strength.score < 3) {
      setError('Password is not strong enough. Please improve it.')
      return
    }

    if (password !== confirmPassword) {
      setError('Passwords do not match')
      return
    }

    setIsLoading(true)

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    // Success
    setSuccess(true)
    setIsLoading(false)

    // Redirect to login after 2 seconds
    setTimeout(() => {
      navigate('/login', {
        state: {
          message: 'Your password has been successfully reset. Please login with your new password.',
        },
      })
    }, 2000)
  }

  if (validatingToken) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardContent className="pt-6">
            <div className="flex flex-col items-center gap-4">
              <Loader2 className="h-8 w-8 animate-spin text-primary" />
              <p className="text-muted-foreground">Verifying reset link...</p>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  if (!tokenValid) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="space-y-2 text-center">
            <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-red-100 dark:bg-red-900/20">
              <AlertCircle className="h-8 w-8 text-red-600 dark:text-red-400" />
            </div>
            <CardTitle className="text-2xl">Link Expired</CardTitle>
            <CardDescription>
              This password reset link has expired or is invalid.
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2 rounded-lg bg-slate-50 dark:bg-slate-800 p-4">
              <p className="text-sm text-muted-foreground">
                For security reasons, password reset links expire after 24 hours. Please request a
                new reset link.
              </p>
            </div>

            <Button
              className="w-full"
              onClick={() => navigate('/forgot-password')}
            >
              Request New Link
            </Button>

            <div className="text-center">
              <Button
                type="button"
                variant="link"
                onClick={() => navigate('/login')}
                className="text-sm"
              >
                Back to Login
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  if (success) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="space-y-2 text-center">
            <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-green-100 dark:bg-green-900/20">
              <CheckCircle2 className="h-8 w-8 text-green-600 dark:text-green-400" />
            </div>
            <CardTitle className="text-2xl">Password Reset!</CardTitle>
            <CardDescription>
              Your password has been successfully updated.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-2 rounded-lg bg-slate-50 dark:bg-slate-800 p-4">
              <p className="text-sm text-muted-foreground">
                Redirecting you to login...
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-2">
          <CardTitle className="text-2xl">Reset Password</CardTitle>
          <CardDescription>
            Enter your new password below.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-5">
            {error && (
              <div className="rounded-lg bg-red-50 p-3 text-sm text-red-600 dark:bg-red-900/20">
                {error}
              </div>
            )}

            {/* New Password */}
            <div className="space-y-2">
              <Label htmlFor="password">New Password</Label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="pl-10 pr-10"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                >
                  {showPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                </button>
              </div>

              {/* Password Strength Indicator */}
              {password && (
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="flex gap-1">
                      {[1, 2, 3, 4, 5].map((i) => (
                        <div
                          key={i}
                          className={cn(
                            'h-1 w-8 rounded-full transition-colors',
                            i <= strength.score ? strengthInfo.color : 'bg-slate-200 dark:bg-slate-700'
                          )}
                        />
                      ))}
                    </div>
                    <span className="text-xs text-muted-foreground">{strengthInfo.label}</span>
                  </div>

                  {/* Password Requirements */}
                  <div className="grid grid-cols-2 gap-1 text-xs">
                    <div className={cn(strength.hasMinLength ? 'text-green-600' : 'text-muted-foreground')}>
                      {strength.hasMinLength ? '✓' : '○'} At least 8 characters
                    </div>
                    <div className={cn(strength.hasUpperCase ? 'text-green-600' : 'text-muted-foreground')}>
                      {strength.hasUpperCase ? '✓' : '○'} Uppercase letter
                    </div>
                    <div className={cn(strength.hasLowerCase ? 'text-green-600' : 'text-muted-foreground')}>
                      {strength.hasLowerCase ? '✓' : '○'} Lowercase letter
                    </div>
                    <div className={cn(strength.hasNumber ? 'text-green-600' : 'text-muted-foreground')}>
                      {strength.hasNumber ? '✓' : '○'} Number
                    </div>
                    <div className={cn(strength.hasSpecial ? 'text-green-600' : 'text-muted-foreground')}>
                      {strength.hasSpecial ? '✓' : '○'} Special character
                    </div>
                  </div>
                </div>
              )}
            </div>

            {/* Confirm Password */}
            <div className="space-y-2">
              <Label htmlFor="confirm-password">Confirm New Password</Label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  id="confirm-password"
                  type={showConfirmPassword ? 'text' : 'password'}
                  placeholder="••••••••"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className={cn(
                    'pl-10 pr-10',
                    confirmPassword && password !== confirmPassword && 'border-red-500'
                  )}
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                >
                  {showConfirmPassword ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                </button>
              </div>
              {confirmPassword && password !== confirmPassword && (
                <p className="text-sm text-red-600">Passwords do not match</p>
              )}
              {confirmPassword && password === confirmPassword && (
                <p className="text-sm text-green-600">Passwords match</p>
              )}
            </div>

            <Button
              type="submit"
              className="w-full"
              disabled={isLoading || !password || !confirmPassword || password !== confirmPassword}
            >
              {isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Resetting...
                </>
              ) : (
                'Reset Password'
              )}
            </Button>

            <div className="text-center">
              <Button
                type="button"
                variant="link"
                onClick={() => navigate('/login')}
                className="text-sm"
              >
                Back to Login
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
