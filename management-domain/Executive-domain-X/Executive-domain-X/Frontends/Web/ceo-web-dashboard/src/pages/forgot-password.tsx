import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Mail, ArrowLeft, CheckCircle2, AlertCircle } from 'lucide-react'

type ForgotPasswordStep = 'request' | 'check-email' | 'success'

export default function ForgotPasswordPage() {
  const navigate = useNavigate()
  const [step, setStep] = useState<ForgotPasswordStep>('request')
  const [email, setEmail] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1500))

      if (!email || !email.includes('@')) {
        setError('Please enter a valid email address')
        setIsLoading(false)
        return
      }

      setStep('check-email')
    } catch (err) {
      setError('An error occurred. Please try again.')
      setIsLoading(false)
    }
  }

  const renderRequestStep = () => (
    <div className="fixed inset-0 flex items-center justify-center bg-slate-100 p-4" style={{ zIndex: 9999 }}>
      <Card className="w-full max-w-md">
        <CardHeader>
          <button
            onClick={() => navigate('/login')}
            className="flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900 mb-4"
          >
            <ArrowLeft className="w-4 h-4" />
            Back to Login
          </button>
          <CardTitle>Forgot Password?</CardTitle>
          <CardDescription>
            Enter your email address and we'll send you a link to reset your password.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="reset-email">Email Address</Label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                <Input
                  id="reset-email"
                  type="email"
                  placeholder="your.email@gogidix.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="pl-9"
                  required
                />
              </div>
            </div>

            {error && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg flex items-start gap-2 text-sm">
                <AlertCircle className="w-4 h-4 text-red-600 flex-shrink-0 mt-0.5" />
                <span className="text-red-700">{error}</span>
              </div>
            )}

            <Button
              type="submit"
              disabled={isLoading}
              className="w-full bg-[#0D47A1] hover:bg-[#0A3D6E]"
            >
              {isLoading ? 'Sending...' : 'Send Reset Link'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  )

  const renderCheckEmailStep = () => (
    <div className="fixed inset-0 flex items-center justify-center bg-slate-100 p-4" style={{ zIndex: 9999 }}>
      <Card className="w-full max-w-md text-center">
        <CardContent className="pt-8">
          <div className="w-16 h-16 bg-emerald-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <Mail className="w-8 h-8 text-emerald-600" />
          </div>
          <h2 className="text-2xl font-bold text-slate-900 mb-2">Check Your Email</h2>
          <p className="text-slate-600 mb-6">
            We've sent a password reset link to
          </p>
          <p className="font-medium text-slate-900 mb-8">{email}</p>
          <p className="text-sm text-slate-500 mb-8">
            Didn't receive the email? Check your spam folder or request a new link.
          </p>
          <div className="space-y-3">
            <Button
              onClick={() => {
                setStep('request')
                setIsLoading(false)
              }}
              variant="outline"
              className="w-full"
            >
              Request New Link
            </Button>
            <Button
              onClick={() => navigate('/login')}
              variant="ghost"
              className="w-full"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              Back to Login
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  )

  if (step === 'request') {
    return renderRequestStep()
  }

  return renderCheckEmailStep()
}
