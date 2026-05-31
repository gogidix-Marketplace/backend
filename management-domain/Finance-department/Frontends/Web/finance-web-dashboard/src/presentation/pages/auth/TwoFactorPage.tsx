import { useState, useRef, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '@store'
import { Shield, AlertCircle, Wallet } from 'lucide-react'

export function TwoFactorPage() {
  const navigate = useNavigate()
  const { user, verify2FA } = useAuthStore()
  const [code, setCode] = useState(['', '', '', '', '', ''])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const inputRefs = useRef<(HTMLInputElement | null)[]>([])

  useEffect(() => {
    if (!user) navigate('/login')
  }, [user, navigate])

  useEffect(() => {
    inputRefs.current[0]?.focus()
  }, [])

  const handleChange = (index: number, value: string) => {
    if (!/^\d*$/.test(value)) return
    const newCode = [...code]
    newCode[index] = value.slice(-1)
    setCode(newCode)
    if (value && index < 5) {
      inputRefs.current[index + 1]?.focus()
    }
  }

  const handleKeyDown = (index: number, e: React.KeyboardEvent) => {
    if (e.key === 'Backspace' && !code[index] && index > 0) {
      inputRefs.current[index - 1]?.focus()
    }
  }

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault()
    const pasted = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, 6)
    const newCode = [...code]
    for (let i = 0; i < pasted.length; i++) newCode[i] = pasted[i]
    setCode(newCode)
    if (pasted.length > 0) inputRefs.current[Math.min(pasted.length, 5)]?.focus()
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    await new Promise((r) => setTimeout(r, 500))
    const fullCode = code.join('')
    if (fullCode.length !== 6) {
      setError('Please enter all 6 digits')
      setLoading(false)
      return
    }
    const success = verify2FA(fullCode)
    if (success) {
      navigate('/dashboard')
    } else {
      setError('Invalid verification code. Use 123456 or 000000')
      setCode(['', '', '', '', '', ''])
      inputRefs.current[0]?.focus()
    }
    setLoading(false)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-blue-600 rounded-2xl mb-4">
            <Wallet size={32} className="text-white" />
          </div>
          <h1 className="text-2xl font-bold text-white">Two-Factor Authentication</h1>
          <p className="text-slate-400">Finance Dashboard</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="text-center mb-6">
            <div className="inline-flex items-center justify-center w-12 h-12 bg-blue-100 rounded-full mb-3">
              <Shield size={24} className="text-blue-600" />
            </div>
            <h2 className="text-lg font-semibold text-slate-900">Verify Your Identity</h2>
            <p className="text-sm text-slate-500 mt-1">
              Enter the 6-digit code sent to your authenticator app
            </p>
          </div>

          {error && (
            <div className="flex items-center gap-2 p-3 bg-red-50 border border-red-200 rounded-lg mb-4">
              <AlertCircle size={18} className="text-red-600" />
              <p className="text-sm text-red-700">{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="flex justify-center gap-2" onPaste={handlePaste}>
              {code.map((digit, index) => (
                <input
                  key={index}
                  ref={(el) => { inputRefs.current[index] = el }}
                  type="text"
                  inputMode="numeric"
                  maxLength={1}
                  value={digit}
                  onChange={(e) => handleChange(index, e.target.value)}
                  onKeyDown={(e) => handleKeyDown(index, e)}
                  className="w-12 h-14 text-center text-xl font-bold border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
              ))}
            </div>

            <button
              type="submit"
              disabled={loading || code.some((d) => !d)}
              className={`w-full py-2.5 rounded-lg font-medium text-white transition-colors ${
                loading || code.some((d) => !d)
                  ? 'bg-slate-300 cursor-not-allowed'
                  : 'bg-blue-600 hover:bg-blue-700'
              }`}
            >
              {loading ? 'Verifying...' : 'Verify'}
            </button>
          </form>

          <div className="mt-6 pt-6 border-t border-slate-200 text-center">
            <p className="text-xs text-slate-400">
              Demo code: <span className="font-mono font-medium text-slate-600">123456</span> or <span className="font-mono font-medium text-slate-600">000000</span>
            </p>
          </div>
        </div>

        <p className="text-center text-slate-500 text-sm mt-6">
          © 2025 Gogidix. All rights reserved.
        </p>
      </div>
    </div>
  )
}
