import { useState, useRef, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useCfoStore } from '@shared/store'
import { Shield, DollarSign } from 'lucide-react'

export default function TwoFactorPage() {
  const navigate = useNavigate()
  const user = useCfoStore((s) => s.user)
  const verify2FA = useCfoStore((s) => s.verify2FA)
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
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-700 via-indigo-800 to-violet-900 px-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-white/10 backdrop-blur-sm mb-4">
            <DollarSign className="w-9 h-9 text-white" />
          </div>
          <h1 className="text-3xl font-bold text-white mb-1">Two-Factor Authentication</h1>
          <p className="text-indigo-200 text-sm">CFO Dashboard</p>
        </div>

        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <div className="text-center mb-6">
            <div className="inline-flex items-center justify-center w-12 h-12 bg-indigo-100 rounded-full mb-3">
              <Shield size={24} className="text-indigo-600" />
            </div>
            <h2 className="text-lg font-semibold text-gray-800">Verify Your Identity</h2>
            <p className="text-sm text-gray-500 mt-1">
              Enter the 6-digit code from your authenticator app
            </p>
          </div>

          {error && (
            <div className="mb-4 p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm">
              {error}
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
                  className="w-12 h-14 text-center text-xl font-bold border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition-shadow"
                />
              ))}
            </div>

            <button
              type="submit"
              disabled={loading || code.some((d) => !d)}
              className={`w-full py-2.5 px-4 rounded-lg font-medium text-white transition-colors text-sm ${
                loading || code.some((d) => !d)
                  ? 'bg-gray-300 cursor-not-allowed'
                  : 'bg-indigo-600 hover:bg-indigo-700'
              }`}
            >
              {loading ? 'Verifying...' : 'Verify'}
            </button>
          </form>

          <div className="mt-6 pt-5 border-t border-gray-100 text-center">
            <p className="text-xs text-gray-400">
              Demo code: <span className="font-mono font-medium text-gray-600">123456</span> or <span className="font-mono font-medium text-gray-600">000000</span>
            </p>
          </div>
        </div>

        <p className="text-center text-xs text-indigo-300 mt-6">
          &copy; 2026 Gogidix Corporation. All rights reserved.
        </p>
      </div>
    </div>
  )
}
