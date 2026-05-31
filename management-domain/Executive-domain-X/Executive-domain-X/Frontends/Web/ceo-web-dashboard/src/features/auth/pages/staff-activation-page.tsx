import { useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { CheckCircle2, Eye, EyeOff, Shield, User, Lock, Building2, Sparkles, ChevronRight } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * Staff Activation Page - New staff activation flow
 * HR requests access -> CTO approves -> Activation link sent to new staff
 * This page handles the activation process
 */

type ActivationStep = 'verify' | 'create-password' | 'complete'

interface StaffInfo {
  name: string
  email: string
  role: string
  department: string
}

export default function StaffActivationPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  // Token would be used in production to validate activation
  void searchParams.get('token')

  const [currentStep, setCurrentStep] = useState<ActivationStep>('verify')
  const [isLoading, setIsLoading] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [passwordError, setPasswordError] = useState('')

  // Mock staff info - in production, this would come from API
  const [staffInfo] = useState<StaffInfo>({
    name: 'Michael Brown',
    email: 'michael.brown@gogidix.com',
    role: 'Senior Developer',
    department: 'Backend',
  })

  const [activationCode, setActivationCode] = useState(['', '', '', '', '', ''])

  const handleCodeChange = (index: number, value: string) => {
    if (value.length <= 1) {
      const newCode = [...activationCode]
      newCode[index] = value
      setActivationCode(newCode)

      // Auto-focus next input
      if (value && index < 5) {
        const nextInput = document.getElementById(`code-${index + 1}`) as HTMLInputElement
        nextInput?.focus()
      }
    }
  }

  const handleVerifyCode = async () => {
    setIsLoading(true)
    // Simulate API verification
    await new Promise(resolve => setTimeout(resolve, 1000))
    setIsLoading(false)
    setCurrentStep('create-password')
  }

  const handleKeyDown = (index: number, e: React.KeyboardEvent) => {
    if (e.key === 'Backspace' && !activationCode[index] && index > 0) {
      const prevInput = document.getElementById(`code-${index - 1}`) as HTMLInputElement
      prevInput?.focus()
    }
  }

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault()
    const pastedData = e.clipboardData.getData('text').slice(0, 6).split('')
    const newCode = [...activationCode]
    pastedData.forEach((char, i) => {
      if (i < 6) newCode[i] = char
    })
    setActivationCode(newCode)
  }

  const validatePassword = (pwd: string): string => {
    if (pwd.length < 8) return 'Password must be at least 8 characters'
    if (!/[A-Z]/.test(pwd)) return 'Password must contain at least one uppercase letter'
    if (!/[a-z]/.test(pwd)) return 'Password must contain at least one lowercase letter'
    if (!/[0-9]/.test(pwd)) return 'Password must contain at least one number'
    return ''
  }

  const handleCreatePassword = async () => {
    const error = validatePassword(password)
    if (error) {
      setPasswordError(error)
      return
    }

    if (password !== confirmPassword) {
      setPasswordError('Passwords do not match')
      return
    }

    setIsLoading(true)
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1500))
    setIsLoading(false)
    setCurrentStep('complete')
  }

  const handleGoToLogin = () => {
    navigate('/login')
  }

  const getPasswordStrength = (pwd: string) => {
    let strength = 0
    if (pwd.length >= 8) strength++
    if (pwd.length >= 12) strength++
    if (/[A-Z]/.test(pwd)) strength++
    if (/[a-z]/.test(pwd)) strength++
    if (/[0-9]/.test(pwd)) strength++
    if (/[^A-Za-z0-9]/.test(pwd)) strength++
    return strength
  }

  const passwordStrength = getPasswordStrength(password)

  return (
    <div className="min-h-screen flex items-center justify-center p-4 py-8 bg-gradient-to-br from-[#0D47A1] via-[#1565C0] to-[#0D47A1]">
      {/* Decorative Elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-20 left-10 w-64 h-64 bg-[#FFA000] opacity-5 rounded-full blur-3xl animate-pulse" />
        <div className="absolute bottom-20 right-10 w-96 h-96 bg-white opacity-5 rounded-full blur-3xl animate-pulse" />
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-[#7C4DFF] opacity-5 rounded-full blur-3xl" />
      </div>

      <div className="w-full max-w-lg relative z-10">
        <Card className="border-0 shadow-2xl overflow-hidden">
          {/* Header */}
          <div className="bg-gradient-to-r from-[#7C4DFF] to-[#6200EA] p-6 text-white">
            <div className="flex items-center gap-3">
              <div className="h-12 w-12 rounded-full bg-white/20 flex items-center justify-center">
                <Building2 className="h-6 w-6" />
              </div>
              <div>
                <h1 className="text-xl font-bold">Gogidix Ecosystem</h1>
                <p className="text-sm text-white/80">Staff Account Activation</p>
              </div>
            </div>
          </div>

          <CardContent className="p-8">
            {/* Step Indicator */}
            <div className="flex items-center justify-center gap-2 mb-8">
              <div className={cn(
                "h-2 w-8 rounded-full transition-all",
                currentStep === 'verify' ? 'bg-[#7C4DFF]' : 'bg-emerald-500'
              )} />
              <div className="h-0.5 w-12 bg-slate-200" />
              <div className={cn(
                "h-2 w-8 rounded-full transition-all",
                currentStep === 'create-password' || currentStep === 'complete' ? 'bg-[#7C4DFF]' : 'bg-slate-200'
              )} />
              <div className="h-0.5 w-12 bg-slate-200" />
              <div className={cn(
                "h-2 w-8 rounded-full transition-all",
                currentStep === 'complete' ? 'bg-[#7C4DFF]' : 'bg-slate-200'
              )} />
            </div>

            {/* Step 1: Verify Code */}
            {currentStep === 'verify' && (
              <div className="space-y-6 animate-fade-in-up">
                <div className="text-center">
                  <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-[#7C4DFF]/10 mb-4">
                    <Shield className="h-8 w-8 text-[#7C4DFF]" />
                  </div>
                  <h2 className="text-2xl font-bold text-slate-900 dark:text-white mb-2">Verify Your Identity</h2>
                  <p className="text-slate-600 dark:text-slate-400">
                    Enter the 6-digit activation code sent to your email
                  </p>
                </div>

                {/* Staff Info Preview */}
                <div className="p-4 rounded-lg bg-slate-50 dark:bg-slate-900/50 border border-slate-200 dark:border-slate-700">
                  <div className="flex items-center gap-3 mb-3">
                    <div className="h-10 w-10 rounded-full bg-[#7C4DFF]/10 flex items-center justify-center">
                      <User className="h-5 w-5 text-[#7C4DFF]" />
                    </div>
                    <div>
                      <p className="font-medium text-slate-900 dark:text-white">{staffInfo.name}</p>
                      <p className="text-xs text-slate-500">{staffInfo.email}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant="secondary">{staffInfo.role}</Badge>
                    <Badge variant="outline">{staffInfo.department}</Badge>
                  </div>
                </div>

                {/* Activation Code Input */}
                <div className="space-y-4">
                  <Label className="text-sm font-medium text-slate-700 dark:text-slate-300 text-center">
                    Activation Code
                  </Label>
                  <div className="flex items-center justify-center gap-2">
                    {activationCode.map((digit, index) => (
                      <input
                        key={index}
                        id={`code-${index}`}
                        type="text"
                        inputMode="numeric"
                        maxLength={1}
                        value={digit}
                        onChange={(e) => handleCodeChange(index, e.target.value)}
                        onKeyDown={(e) => handleKeyDown(index, e)}
                        onPaste={handlePaste}
                        className="w-12 h-14 text-center text-2xl font-bold border-2 border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-900 text-slate-900 dark:text-white focus:border-[#7C4DFF] focus:ring-2 focus:ring-[#7C4DFF]/20 outline-none transition-all"
                      />
                    ))}
                  </div>
                  <p className="text-xs text-center text-slate-500">
                    Didn't receive the code? <button className="text-[#7C4DFF] hover:underline">Resend</button>
                  </p>
                </div>

                <Button
                  size="lg"
                  className="w-full h-12 bg-gradient-to-r from-[#7C4DFF] to-[#6200EA] hover:from-[#6200EA] hover:to-[#5000CA] text-white font-medium shadow-lg"
                  onClick={handleVerifyCode}
                  disabled={activationCode.some(d => !d) || isLoading}
                >
                  {isLoading ? 'Verifying...' : 'Verify & Continue'}
                  <ChevronRight className="h-5 w-5 ml-2" />
                </Button>

                <p className="text-xs text-center text-slate-500">
                  This activation link is valid for 7 days. Contact HR if you need assistance.
                </p>
              </div>
            )}

            {/* Step 2: Create Password */}
            {currentStep === 'create-password' && (
              <div className="space-y-6 animate-fade-in-up">
                <div className="text-center">
                  <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-emerald-100 dark:bg-emerald-900/30 mb-4">
                    <Lock className="h-8 w-8 text-emerald-600 dark:text-emerald-400" />
                  </div>
                  <h2 className="text-2xl font-bold text-slate-900 dark:text-white mb-2">Create Your Password</h2>
                  <p className="text-slate-600 dark:text-slate-400">
                    Secure your account with a strong password
                  </p>
                </div>

                <div className="space-y-4">
                  <div className="space-y-2">
                    <Label htmlFor="password" className="text-sm font-medium text-slate-700 dark:text-slate-300">
                      Password
                    </Label>
                    <div className="relative">
                      <Input
                        id="password"
                        type={showPassword ? 'text' : 'password'}
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => { setPassword(e.target.value); setPasswordError('') }}
                        className="h-12 pr-12"
                      />
                      <button
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                      >
                        {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                      </button>
                    </div>
                  </div>

                  {/* Password Strength Indicator */}
                  {password && (
                    <div className="space-y-2">
                      <div className="flex gap-1">
                        {[1, 2, 3, 4, 5, 6].map((level) => (
                          <div
                            key={level}
                            className={cn(
                              "h-1 flex-1 rounded-full transition-all",
                              level <= passwordStrength
                                ? passwordStrength <= 2
                                  ? 'bg-red-500'
                                  : passwordStrength <= 4
                                  ? 'bg-amber-500'
                                  : 'bg-emerald-500'
                                : 'bg-slate-200 dark:bg-slate-700'
                            )}
                          />
                        ))}
                      </div>
                      <p className="text-xs text-slate-500">
                        {passwordStrength <= 2 && 'Weak password'}
                        {passwordStrength > 2 && passwordStrength <= 4 && 'Medium strength'}
                        {passwordStrength > 4 && 'Strong password'}
                      </p>
                    </div>
                  )}

                  <div className="space-y-2">
                    <Label htmlFor="confirmPassword" className="text-sm font-medium text-slate-700 dark:text-slate-300">
                      Confirm Password
                    </Label>
                    <div className="relative">
                      <Input
                        id="confirmPassword"
                        type={showConfirmPassword ? 'text' : 'password'}
                        placeholder="Confirm your password"
                        value={confirmPassword}
                        onChange={(e) => { setConfirmPassword(e.target.value); setPasswordError('') }}
                        className="h-12 pr-12"
                      />
                      <button
                        type="button"
                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                      >
                        {showConfirmPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                      </button>
                    </div>
                  </div>

                  {passwordError && (
                    <p className="text-sm text-red-600">{passwordError}</p>
                  )}

                  {/* Password Requirements */}
                  <div className="p-4 rounded-lg bg-slate-50 dark:bg-slate-900/50 border border-slate-200 dark:border-slate-700">
                    <p className="text-xs font-medium text-slate-700 dark:text-slate-300 mb-2">Password requirements:</p>
                    <ul className="space-y-1">
                      <li className={cn("flex items-center gap-2 text-xs", password.length >= 8 ? "text-emerald-600" : "text-slate-500")}>
                        {password.length >= 8 ? <CheckCircle2 className="h-3 w-3" /> : <div className="h-3 w-3 rounded-full border border-slate-400" />}
                        At least 8 characters
                      </li>
                      <li className={cn("flex items-center gap-2 text-xs", /[A-Z]/.test(password) ? "text-emerald-600" : "text-slate-500")}>
                        {/[A-Z]/.test(password) ? <CheckCircle2 className="h-3 w-3" /> : <div className="h-3 w-3 rounded-full border border-slate-400" />}
                        One uppercase letter
                      </li>
                      <li className={cn("flex items-center gap-2 text-xs", /[a-z]/.test(password) ? "text-emerald-600" : "text-slate-500")}>
                        {/[a-z]/.test(password) ? <CheckCircle2 className="h-3 w-3" /> : <div className="h-3 w-3 rounded-full border border-slate-400" />}
                        One lowercase letter
                      </li>
                      <li className={cn("flex items-center gap-2 text-xs", /[0-9]/.test(password) ? "text-emerald-600" : "text-slate-500")}>
                        {/[0-9]/.test(password) ? <CheckCircle2 className="h-3 w-3" /> : <div className="h-3 w-3 rounded-full border border-slate-400" />}
                        One number
                      </li>
                    </ul>
                  </div>
                </div>

                <Button
                  size="lg"
                  className="w-full h-12 bg-gradient-to-r from-[#7C4DFF] to-[#6200EA] hover:from-[#6200EA] hover:to-[#5000CA] text-white font-medium shadow-lg"
                  onClick={handleCreatePassword}
                  disabled={!password || !confirmPassword || isLoading}
                >
                  {isLoading ? 'Creating Account...' : 'Activate Account'}
                  <ChevronRight className="h-5 w-5 ml-2" />
                </Button>
              </div>
            )}

            {/* Step 3: Complete */}
            {currentStep === 'complete' && (
              <div className="space-y-6 text-center animate-fade-in-up">
                <div className="flex justify-center">
                  <div className="relative">
                    <div className="absolute inset-0 bg-emerald-500 opacity-20 blur-xl rounded-full animate-pulse" />
                    <div className="relative flex h-24 w-24 items-center justify-center rounded-full bg-gradient-to-br from-emerald-500 to-emerald-600 shadow-2xl">
                      <CheckCircle2 className="h-12 w-12 text-white" />
                    </div>
                  </div>
                </div>

                <div>
                  <h2 className="text-3xl font-bold text-white mb-3">Welcome to Gogidix!</h2>
                  <p className="text-lg text-blue-100">
                    Your account has been successfully activated
                  </p>
                </div>

                <div className="grid grid-cols-2 gap-3 max-w-xs mx-auto">
                  {[
                    { icon: '✓', label: 'Account created' },
                    { icon: '✓', label: 'Password set' },
                    { icon: '✓', label: 'Email verified' },
                    { icon: '✓', label: 'Ready to login' },
                  ].map((item, i) => (
                    <div
                      key={i}
                      className="flex items-center gap-2 p-3 rounded-lg bg-white/10 border border-white/20"
                    >
                      <span className="text-white">{item.icon}</span>
                      <span className="text-white text-sm">{item.label}</span>
                    </div>
                  ))}
                </div>

                <div className="p-4 rounded-lg bg-white/10 border border-white/20">
                  <p className="text-sm text-blue-100 mb-2">
                    <span className="font-medium text-white">Email:</span> {staffInfo.email}
                  </p>
                  <p className="text-sm text-blue-100 mb-2">
                    <span className="font-medium text-white">Role:</span> {staffInfo.role}
                  </p>
                  <p className="text-sm text-blue-100">
                    <span className="font-medium text-white">Department:</span> {staffInfo.department}
                  </p>
                </div>

                <Button
                  size="lg"
                  className="h-14 px-8 bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white font-medium shadow-lg hover:shadow-xl transition-all duration-300"
                  onClick={handleGoToLogin}
                >
                  <span className="flex items-center justify-center gap-2 text-lg">
                    Go to Login <Sparkles className="h-5 w-5" />
                  </span>
                </Button>

                <p className="text-sm text-blue-200 mt-4">
                  You can now access the Gogidix ecosystem with your credentials
                </p>
              </div>
            )}
          </CardContent>
        </Card>

        {/* Footer */}
        <p className="text-center text-sm text-white/60 mt-6">
          Need help? Contact <a href="mailto:hr@gogidix.com" className="text-white/80 hover:underline">HR Support</a> or <a href="mailto:it@gogidix.com" className="text-white/80 hover:underline">IT Support</a>
        </p>
      </div>
    </div>
  )
}
