import { useState, useLayoutEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@shared/stores/authStore'
import { Button } from '@shared/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Users, ShieldCheck, ArrowLeft } from 'lucide-react'

export default function TwoFactorAuthPage() {
  const navigate = useNavigate()
  const { complete2FA, user, _pendingAuth } = useAuthStore()
  const [otp, setOtp] = useState(['', '', '', '', '', ''])
  const [error, setError] = useState('')
  const [isVerifying, setIsVerifying] = useState(false)

  useLayoutEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => { document.body.style.overflow = '' }
  }, [])

  const handleChange = (index: number, value: string) => {
    if (!/^\d*$/.test(value)) return
    const newOtp = [...otp]
    newOtp[index] = value.slice(-1)
    setOtp(newOtp)
    setError('')
    if (value && index < 5) {
      const next = document.getElementById(`otp-${index + 1}`)
      next?.focus()
    }
  }

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !otp[index] && index > 0) {
      const prev = document.getElementById(`otp-${index - 1}`)
      prev?.focus()
    }
  }

  const handleVerify = async () => {
    const code = otp.join('')
    if (code.length < 6) {
      setError('Please enter all 6 digits')
      return
    }
    setIsVerifying(true)
    await new Promise(r => setTimeout(r, 800))
    complete2FA()
  }

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault()
    const pasted = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, 6)
    if (pasted.length > 0) {
      const newOtp = [...otp]
      for (let i = 0; i < pasted.length; i++) {
        newOtp[i] = pasted[i]
      }
      setOtp(newOtp)
      const nextIndex = Math.min(pasted.length, 5)
      document.getElementById(`otp-${nextIndex}`)?.focus()
    }
  }

  if (!_pendingAuth && !user) {
    navigate('/login')
    return null
  }

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
      <div className="w-full max-w-md px-4">
        <div className="flex flex-col items-center mb-8">
          <div className="flex h-16 w-16 items-center justify-center rounded-xl bg-[#2563EB] shadow-lg mb-4">
            <Users className="h-8 w-8 text-white" />
          </div>
          <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Two-Factor Authentication</h1>
          <p className="text-sm text-slate-600 dark:text-slate-400 mt-1">Verify your identity to continue</p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <ShieldCheck className="h-5 w-5 text-[#2563EB]" />
              Enter Verification Code
            </CardTitle>
            <CardDescription>
              Enter the 6-digit code from your authenticator app. (Demo: use any 6 digits)
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-6">
              <div className="flex justify-center gap-2">
                {otp.map((digit, index) => (
                  <input
                    key={index}
                    id={`otp-${index}`}
                    type="text"
                    inputMode="numeric"
                    maxLength={1}
                    value={digit}
                    onChange={(e) => handleChange(index, e.target.value)}
                    onKeyDown={(e) => handleKeyDown(index, e)}
                    onPaste={handlePaste}
                    className="h-12 w-12 rounded-lg border-2 border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-800 text-center text-xl font-bold text-slate-900 dark:text-white focus:border-[#2563EB] focus:outline-none focus:ring-2 focus:ring-[#2563EB]/20"
                  />
                ))}
              </div>

              {error && (
                <p className="text-center text-sm text-red-600">{error}</p>
              )}

              <Button
                className="w-full"
                variant="hr"
                onClick={handleVerify}
                disabled={isVerifying || otp.join('').length < 6}
              >
                {isVerifying ? 'Verifying...' : 'Verify & Continue'}
              </Button>

              <div className="text-center space-y-2">
                <button
                  className="text-sm text-[#2563EB] hover:underline"
                  onClick={() => {
                    setOtp(['', '', '', '', '', ''])
                    setError('')
                  }}
                >
                  Resend Code
                </button>
              </div>

              <div className="pt-2 border-t">
                <button
                  className="flex items-center gap-1 text-sm text-muted-foreground hover:text-slate-900 dark:hover:text-white"
                  onClick={() => {
                    const { logout } = useAuthStore.getState()
                    logout()
                    navigate('/login')
                  }}
                >
                  <ArrowLeft className="h-4 w-4" />
                  Back to Login
                </button>
              </div>
            </div>
          </CardContent>
        </Card>

        <p className="text-center text-xs text-slate-500 mt-6">
          Secured by Gogidix HR Platform
        </p>
      </div>
    </div>
  )
}
