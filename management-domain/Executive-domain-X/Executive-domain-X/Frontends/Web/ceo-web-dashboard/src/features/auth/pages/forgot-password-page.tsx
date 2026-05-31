import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Mail, ArrowLeft, CheckCircle2, Loader2 } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * Forgot Password Page
 *
 * Flow:
 * 1. Enter email address
 * 2. Submit → Show confirmation message
 * 3. Email sent with reset link
 * 4. User clicks link → Reset password page
 */

type Screen = 'request' | 'check-email' | 'success'

interface State {
  screen: Screen
  email: string
  isLoading: boolean
  error: string
  resendCooldown: number
}

export default function ForgotPasswordPage() {
  const navigate = useNavigate()
  const [state, setState] = useState<State>({
    screen: 'request',
    email: '',
    isLoading: false,
    error: '',
    resendCooldown: 30,
  })

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setState((prev) => ({ ...prev, isLoading: true, error: '' }))

    // Validate email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(state.email)) {
      setState((prev) => ({ ...prev, isLoading: false, error: 'Please enter a valid email address' }))
      return
    }

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    // In production, this would call an API to send the reset email
    // For demo, we'll show the check-email screen
    setState((prev) => ({ ...prev, screen: 'check-email', isLoading: false }))
  }

  const handleResendEmail = async () => {
    setState((prev) => ({ ...prev, isLoading: true }))

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    setState((prev) => ({ ...prev, screen: 'success', isLoading: false }))
  }

  const handleBackToLogin = () => {
    navigate('/login')
  }

  if (state.screen === 'request') {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="space-y-2">
            <div className="flex items-center gap-2">
              <Button variant="ghost" size="sm" onClick={handleBackToLogin}>
                <ArrowLeft className="h-4 w-4" />
              </Button>
            </div>
            <CardTitle className="text-2xl">Forgot Password?</CardTitle>
            <CardDescription>
              Enter your email address and we'll send you a link to reset your password.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-5">
              {state.error && (
                <div className="rounded-lg bg-red-50 p-3 text-sm text-red-600 dark:bg-red-900/20">
                  {state.error}
                </div>
              )}

              <div className="space-y-2">
                <Label htmlFor="email">Email Address</Label>
                <div className="relative">
                  <Mail className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    id="email"
                    type="email"
                    placeholder="name@gogidix.com"
                    value={state.email}
                    onChange={(e) =>
                      setState((prev) => ({ ...prev, email: e.target.value, error: '' }))
                    }
                    className="pl-10"
                    required
                  />
                </div>
              </div>

              <Button type="submit" className="w-full" disabled={state.isLoading || !state.email}>
                {state.isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Sending...
                  </>
                ) : (
                  'Send Reset Link'
                )}
              </Button>

              <div className="text-center">
                <Button
                  type="button"
                  variant="link"
                  onClick={handleBackToLogin}
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

  if (state.screen === 'check-email') {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="space-y-2 text-center">
            <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-green-100 dark:bg-green-900/20">
              <Mail className="h-8 w-8 text-green-600 dark:text-green-400" />
            </div>
            <CardTitle className="text-2xl">Check Your Email</CardTitle>
            <CardDescription>
              We've sent a password reset link to
              <br />
              <span className="font-medium text-foreground">{state.email}</span>
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2 rounded-lg bg-slate-50 dark:bg-slate-800 p-4">
              <p className="text-sm text-muted-foreground">
                Click the link in the email to reset your password. The link will expire in 24
                hours.
              </p>
            </div>

            <div className="text-center text-sm text-muted-foreground">
              Didn't receive the email? Check your spam folder or
            </div>

            <Button
              variant="outline"
              className="w-full"
              onClick={handleResendEmail}
              disabled={state.isLoading || state.resendCooldown > 0}
            >
              {state.isLoading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Resending...
                </>
              ) : state.resendCooldown > 0 ? (
                `Resend in ${state.resendCooldown}s`
              ) : (
                'Resend Email'
              )}
            </Button>

            <div className="text-center">
              <Button
                type="button"
                variant="link"
                onClick={handleBackToLogin}
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

  if (state.screen === 'success') {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
        <Card className="w-full max-w-md shadow-lg">
          <CardHeader className="space-y-2 text-center">
            <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-green-100 dark:bg-green-900/20">
              <CheckCircle2 className="h-8 w-8 text-green-600 dark:text-green-400" />
            </div>
            <CardTitle className="text-2xl">Email Sent!</CardTitle>
            <CardDescription>
              A new password reset link has been sent to your email address.
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2 rounded-lg bg-slate-50 dark:bg-slate-800 p-4">
              <p className="text-sm text-muted-foreground">
                If you don't receive the email within a few minutes, please check your spam folder.
              </p>
            </div>

            <Button className="w-full" onClick={handleBackToLogin}>
              Back to Login
            </Button>
          </CardContent>
        </Card>
      </div>
    )
  }

  return null
}
