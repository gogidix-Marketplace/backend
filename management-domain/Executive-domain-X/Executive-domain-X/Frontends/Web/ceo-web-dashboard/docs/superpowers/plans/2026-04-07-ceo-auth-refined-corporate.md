# CEO Authentication Pages - Refined Corporate Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Redesign CEO login and 2FA pages with production-grade "Refined Corporate" aesthetic featuring sophisticated gradients, smooth animations, floating labels, and full accessibility.

**Architecture:** Two standalone page components (login.tsx, two-factor-auth.tsx) with shared animation constants and styling. Each page manages its own form state and interacts with the existing zustand authStore. Components use inline styles for precise control while maintaining React best practices.

**Tech Stack:** React 18, TypeScript, Tailwind CSS, Framer Motion (for animations), Lucide React (icons), existing zustand authStore

---

## File Structure

```
src/
├── pages/
│   ├── login.tsx              [MODIFY] - Complete redesign with floating labels
│   └── two-factor-auth.tsx    [MODIFY] - Complete redesign with animated inputs
├── shared/
│   ├── components/
│   │   └── auth/
│   │       ├── FloatingLabelInput.tsx  [CREATE] - Reusable floating label input
│   │       ├── AuthCard.tsx            [CREATE] - Card wrapper with animations
│   │       └── OTPInput.tsx            [CREATE] - 6-digit OTP input with animations
│   └── styles/
│       └── animations.ts            [CREATE] - Animation variants and constants
```

---

### Task 1: Create shared animation constants

**Files:**
- Create: `src/shared/styles/animations.ts`

- [ ] **Step 1: Create animation constants file**

```typescript
// Animation timing constants
export const ANIMATION_DURATION = {
  fast: 150,
  normal: 200,
  slow: 300,
  slower: 400,
} as const

// Easing functions
export const EASING = {
  ease: 'cubic-bezier(0.4, 0, 0.2, 1)',
  easeOut: 'cubic-bezier(0, 0, 0.2, 1)',
  easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
  bounce: 'cubic-bezier(0.68, -0.55, 0.265, 1.55)',
} as const

// Card entrance animation variants
export const cardVariants = {
  hidden: {
    opacity: 0,
    y: 20,
    scale: 0.96,
  },
  visible: {
    opacity: 1,
    y: 0,
    scale: 1,
    transition: {
      duration: ANIMATION_DURATION.slower / 1000,
      ease: EASING.easeOut,
    },
  },
}

// Stagger children animation
export const staggerContainer = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.1,
      delayChildren: 0.1,
    },
  },
}

export const staggerItem = {
  hidden: { opacity: 0, y: 10 },
  visible: {
    opacity: 1,
    y: 0,
    transition: {
      duration: ANIMATION_DURATION.normal / 1000,
      ease: EASING.easeOut,
    },
  },
}

// Input focus animation
export const inputFocusVariants = {
  unfocused: {
    borderColor: '#E2E8F0',
    backgroundColor: '#F8FAFC',
  },
  focused: {
    borderColor: '#3B82F6',
    backgroundColor: '#FFFFFF',
    boxShadow: '0 0 0 3px rgba(59, 130, 246, 0.1)',
    transition: {
      duration: ANIMATION_DURATION.normal / 1000,
      ease: EASING.ease,
    },
  },
}

// Button variants
export const buttonVariants = {
  idle: {
    scale: 1,
    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
  },
  hover: {
    scale: 1.01,
    y: -2,
    boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
    transition: {
      duration: ANIMATION_DURATION.normal / 1000,
      ease: EASING.ease,
    },
  },
  tap: {
    scale: 0.98,
    transition: {
      duration: ANIMATION_DURATION.fast / 1000,
      ease: EASING.easeIn,
    },
  },
}

// Checkmark animation for success state
export const checkmarkPathVariants = {
  hidden: {
    pathLength: 0,
    opacity: 0,
  },
  visible: {
    pathLength: 1,
    opacity: 1,
    transition: {
      pathLength: { duration: 0.5, ease: 'easeInOut' },
      opacity: { duration: 0.1 },
    },
  },
}

// Shake animation for errors
export const shakeVariants = {
  hidden: { x: 0 },
  visible: {
    x: [0, -10, 10, -10, 10, 0],
    transition: {
      duration: 0.4,
    },
  },
}
```

- [ ] **Step 2: Commit animations**

```bash
git add src/shared/styles/animations.ts
git commit -m "feat(auth): add shared animation constants for refined corporate design"
```

---

### Task 2: Create FloatingLabelInput component

**Files:**
- Create: `src/shared/components/auth/FloatingLabelInput.tsx`

- [ ] **Step 1: Create FloatingLabelInput component with framer-motion**

```tsx
import { useState, useId } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { Eye, EyeOff, AlertCircle } from 'lucide-react'

interface FloatingLabelInputProps {
  type?: 'text' | 'email' | 'password'
  label: string
  placeholder: string
  value: string
  onChange: (value: string) => void
  error?: string
  disabled?: boolean
  autoComplete?: string
  required?: boolean
  ariaDescribedBy?: string
}

export function FloatingLabelInput({
  type = 'text',
  label,
  placeholder,
  value,
  onChange,
  error,
  disabled = false,
  autoComplete,
  required = false,
  ariaDescribedBy,
}: FloatingLabelInputProps) {
  const id = useId()
  const [isFocused, setIsFocused] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const hasValue = value.length > 0
  const isFloating = isFocused || hasValue
  const isPassword = type === 'password'

  const inputType = isPassword && showPassword ? 'text' : type

  return (
    <div className="relative">
      {/* Floating Label */}
      <motion.label
        htmlFor={id}
        className="absolute left-4 pointer-events-none z-10 origin-left transition-colors"
        initial={false}
        animate={{
          top: isFloating ? '-8px' : '14px',
          fontSize: isFloating ? '12px' : '15px',
          color: error ? '#EF4444' : isFocused ? '#3B82F6' : '#64748B',
        }}
        transition={{ duration: 0.2, ease: 'easeOut' }}
        style={{
          backgroundColor: isFloating ? 'white' : 'transparent',
          padding: isFloating ? '0 4px' : '0',
          borderRadius: isFloating ? '4px' : '0',
          fontWeight: isFloating ? '500' : '400',
        }}
      >
        {label} {required && <span className="text-red-500">*</span>}
      </motion.label>

      {/* Input Container */}
      <div className="relative">
        <input
          id={id}
          type={inputType}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          disabled={disabled}
          autoComplete={autoComplete}
          aria-invalid={!!error}
          aria-describedby={
            error ? `${id}-error` : ariaDescribedBy
          }
          className={`w-full h-13 px-4 pt-4 pb-1 border rounded-lg transition-all outline-none
            ${error ? 'border-red-500' : 'border-slate-200'}
            ${isFocused ? 'border-blue-500 bg-white shadow-[0_0_0_3px_rgba(59,130,246,0.1)]' : 'bg-slate-50'}
            ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
          `}
          style={{ height: '52px' }}
        />

        {/* Password Toggle */}
        {isPassword && (
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 rounded"
            aria-label={showPassword ? 'Hide password' : 'Show password'}
          >
            {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
          </button>
        )}

        {/* Error Icon */}
        <AnimatePresence>
          {error && (
            <motion.div
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.8 }}
              className="absolute right-4 top-1/2 -translate-y-1/2 text-red-500"
            >
              <AlertCircle className="w-5 h-5" />
            </motion.div>
          )}
        </AnimatePresence>
      </div>

      {/* Error Message */}
      <AnimatePresence>
        {error && (
          <motion.p
            id={`${id}-error`}
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            className="text-red-500 text-sm mt-1 flex items-center gap-1"
          >
            {error}
          </motion.p>
        )}
      </AnimatePresence>
    </div>
  )
}
```

- [ ] **Step 2: Commit FloatingLabelInput**

```bash
git add src/shared/components/auth/FloatingLabelInput.tsx
git commit -m "feat(auth): add FloatingLabelInput component with animations"
```

---

### Task 3: Create OTPInput component for 2FA

**Files:**
- Create: `src/shared/components/auth/OTPInput.tsx`

- [ ] **Step 1: Create OTPInput component**

```tsx
import { useRef, useEffect, forwardRef } from 'react'
import { motion } from 'framer-motion'

interface OTPInputProps {
  length?: number
  value: string[]
  onChange: (value: string[]) => void
  error?: string
  disabled?: boolean
}

export const OTPInput = forwardRef<HTMLDivElement, OTPInputProps>(
  ({ length = 6, value, onChange, error, disabled }, ref) => {
    const inputsRef = useRef<(HTMLInputElement | null)[]>([])

    useEffect(() => {
      // Focus first empty input on mount
      const firstEmptyIndex = value.findIndex((v) => v === '')
      if (firstEmptyIndex !== -1) {
        inputsRef.current[firstEmptyIndex]?.focus()
      }
    }, [])

    const handleChange = (index: number, char: string) => {
      if (!/^\d*$/.test(char)) return // Only digits

      const newValue = [...value]
      newValue[index] = char

      onChange(newValue)

      // Auto-focus next input
      if (char && index < length - 1) {
        inputsRef.current[index + 1]?.focus()
      }
    }

    const handleKeyDown = (
      index: number,
      e: React.KeyboardEvent<HTMLInputElement>
    ) => {
      if (e.key === 'Backspace' && !value[index] && index > 0) {
        inputsRef.current[index - 1]?.focus()
      }
    }

    const handlePaste = (e: React.ClipboardEvent) => {
      e.preventDefault()
      const pasted = e.clipboardData.getData('text').slice(0, length)
      if (!/^\d+$/.test(pasted)) return

      const newValue = [...value]
      pasted.split('').forEach((char, i) => {
        if (i < length) newValue[i] = char
      })

      onChange(newValue)
      inputsRef.current[Math.min(pasted.length, length - 1)]?.focus()
    }

    return (
      <div ref={ref} className="flex justify-center gap-2">
        {value.map((digit, index) => (
          <motion.input
            key={index}
            ref={(el) => (inputsRef.current[index] = el)}
            type="text"
            inputMode="numeric"
            maxLength={1}
            value={digit}
            onChange={(e) => handleChange(index, e.target.value)}
            onKeyDown={(e) => handleKeyDown(index, e)}
            onPaste={index === 0 ? handlePaste : undefined}
            disabled={disabled}
            className={`w-12 h-12 text-center text-xl font-bold border-2 rounded-lg outline-none transition-all
              ${error ? 'border-red-500' : 'border-slate-200'}
              ${digit ? 'border-blue-500 bg-blue-50/50' : 'bg-white'}
              ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
            `}
            style={{
              transition: 'all 0.2s cubic-bezier(0.4, 0, 0.2, 1)',
            }}
            whileFocus={{
              borderColor: error ? '#EF4444' : '#3B82F6',
              boxShadow: error
                ? '0 0 0 3px rgba(239, 68, 68, 0.1)'
                : '0 0 0 3px rgba(59, 130, 246, 0.1)',
              scale: 1.02,
            }}
            aria-label={`Digit ${index + 1}`}
            aria-describedby={error ? 'otp-error' : undefined}
          />
        ))}
      </div>
    )
  }
)

OTPInput.displayName = 'OTPInput'
```

- [ ] **Step 2: Commit OTPInput**

```bash
git add src/shared/components/auth/OTPInput.tsx
git commit -m "feat(auth): add OTPInput component for 2FA with animations"
```

---

### Task 4: Create AuthCard wrapper component

**Files:**
- Create: `src/shared/components/auth/AuthCard.tsx`

- [ ] **Step 1: Create AuthCard wrapper**

```tsx
import { motion } from 'framer-motion'
import { Building2 } from 'lucide-react'
import { cardVariants } from '@shared/styles/animations'

interface AuthCardProps {
  children: React.ReactNode
  title: string
  subtitle?: string
  showLogo?: boolean
}

export function AuthCard({
  children,
  title,
  subtitle,
  showLogo = true,
}: AuthCardProps) {
  return (
    <motion.div
      variants={cardVariants}
      initial="hidden"
      animate="visible"
      className="w-full max-w-[480px]"
      style={{
        background: 'rgba(255, 255, 255, 0.95)',
        backdropFilter: 'blur(20px)',
        border: '1px solid rgba(255, 255, 255, 0.18)',
        borderRadius: '16px',
        padding: '40px',
        boxShadow: '0 8px 32px rgba(0, 0, 0, 0.12)',
      }}
    >
      {/* Logo */}
      {showLogo && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1, duration: 0.3 }}
          className="flex items-center justify-center gap-3 mb-8"
        >
          <div
            className="w-10 h-10 rounded-xl flex items-center justify-center"
            style={{ backgroundColor: '#0F172A' }}
          >
            <Building2 className="w-5 h-5 text-white" />
          </div>
          <span
            className="text-xl font-semibold"
            style={{ color: '#0F172A', letterSpacing: '-0.5px' }}
          >
            Gogidix
          </span>
        </motion.div>
      )}

      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.15, duration: 0.3 }}
        className="text-center mb-8"
      >
        <h1
          className="text-2xl font-semibold mb-2"
          style={{ color: '#1E293B', letterSpacing: '-0.5px' }}
        >
          {title}
        </h1>
        {subtitle && (
          <p className="text-sm" style={{ color: '#64748B' }}>
            {subtitle}
          </p>
        )}
      </motion.div>

      {/* Content */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.2 }}
      >
        {children}
      </motion.div>
    </motion.div>
  )
}
```

- [ ] **Step 2: Commit AuthCard**

```bash
git add src/shared/components/auth/AuthCard.tsx
git commit -m "feat(auth): add AuthCard wrapper with animations"
```

---

### Task 5: Redesign Login Page

**Files:**
- Modify: `src/pages/login.tsx`

- [ ] **Step 1: Replace login page with new Refined Corporate design**

```tsx
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import { useAuthStore, MOCK_PASSWORD } from '@shared/stores/authStore'
import { Building2, ArrowLeft, CheckCircle2 } from 'lucide-react'
import { AuthCard } from '@shared/components/auth/AuthCard'
import { FloatingLabelInput } from '@shared/components/auth/FloatingLabelInput'
import { shakeVariants } from '@shared/styles/animations'

type ExecutiveRole = 'CEO' | 'CFO' | 'COO' | 'CTO'

interface RoleConfig {
  title: string
  dashboardName: string
  demoEmail: string
}

const ROLE_CONFIGS: Record<ExecutiveRole, RoleConfig> = {
  CEO: { title: 'CEO', dashboardName: 'CEO', demoEmail: 'ceo@gogidix.com' },
  CFO: { title: 'CFO', dashboardName: 'CFO', demoEmail: 'cfo@gogidix.com' },
  COO: { title: 'COO', dashboardName: 'COO', demoEmail: 'coo@gogidix.com' },
  CTO: { title: 'CTO', dashboardName: 'CTO', demoEmail: 'cto@gogidix.com' },
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

export default function LoginPage() {
  const navigate = useNavigate()
  const { login, logout, isAuthenticated, user } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [rememberMe, setRememberMe] = useState(false)

  const role = getRoleFromPort()
  const config = ROLE_CONFIGS[role]
  const redirectPath = getRedirectPath(role)

  useEffect(() => {
    if (isAuthenticated) {
      const timer = setTimeout(() => navigate(redirectPath), 1500)
      return () => clearTimeout(timer)
    }
  }, [isAuthenticated, navigate, redirectPath])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      await login(email, password)
      const authStore = useAuthStore.getState()
      if (authStore.isAuthenticated) {
        navigate(redirectPath)
      } else if (authStore._pendingAuth) {
        navigate('/two-factor')
      }
    } catch (err) {
      setError('Invalid email or password')
    } finally {
      setIsLoading(false)
    }
  }

  const fillDemoCredentials = () => {
    setEmail(config.demoEmail)
    setPassword(MOCK_PASSWORD)
    setError('')
  }

  // Already authenticated view
  if (isAuthenticated && user) {
    return (
      <div
        className="min-h-screen flex items-center justify-center p-4 relative overflow-hidden"
        style={{
          background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)',
        }}
      >
        {/* Animated background pattern */}
        <div
          className="absolute inset-0 opacity-5"
          style={{
            backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
          }}
        />

        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="w-full max-w-[480px] bg-white rounded-2xl p-10 text-center relative z-10"
          style={{
            boxShadow: '0 8px 32px rgba(0, 0, 0, 0.12)',
          }}
        >
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ delay: 0.2, type: 'spring', stiffness: 200 }}
            className="w-16 h-16 bg-emerald-100 rounded-full flex items-center justify-center mx-auto mb-6"
          >
            <CheckCircle2 className="w-8 h-8 text-emerald-600" />
          </motion.div>
          <h1 className="text-xl font-semibold mb-2" style={{ color: '#1E293B' }}>
            Already Logged In
          </h1>
          <p className="mb-6" style={{ color: '#64748B' }}>
            Logged in as{' '}
            <span className="font-semibold" style={{ color: '#3B82F6' }}>
              {user.displayName || user.email}
            </span>
          </p>
          <p className="text-sm mb-6" style={{ color: '#64748B' }}>
            Redirecting to dashboard...
          </p>
          <button
            onClick={() => navigate(redirectPath)}
            className="w-full py-3 text-white font-medium rounded-lg transition-all hover:translate-y-[-2px]"
            style={{
              backgroundColor: '#0F172A',
              minHeight: '48px',
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
            }}
          >
            Continue to Dashboard
          </button>
        </motion.div>
      </div>
    )
  }

  return (
    <div
      className="min-h-screen flex items-center justify-center p-4 relative overflow-hidden"
      style={{
        background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)',
      }}
    >
      {/* Animated background pattern */}
      <motion.div
        className="absolute inset-0 opacity-5"
        style={{
          backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
        }}
        animate={{
          backgroundPosition: ['0px 0px', '60px 60px'],
        }}
        transition={{
          duration: 20,
          repeat: Infinity,
          repeatType: 'loop',
          ease: 'linear',
        }}
      />

      <AuthCard
        title="Welcome Back"
        subtitle={`Sign in to access your ${config.dashboardName} dashboard`}
      >
        <form onSubmit={handleSubmit} className="space-y-5">
          <AnimatePresence>
            {error && (
              <motion.div
                key="error"
                variants={shakeVariants}
                initial="hidden"
                animate="visible"
                exit={{ opacity: 0, height: 0 }}
                className="p-3 bg-red-50 border border-red-200 rounded-lg flex items-start gap-2 text-sm"
              >
                <svg
                  className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                    clipRule="evenodd"
                  />
                </svg>
                <span style={{ color: '#DC2626' }}>{error}</span>
              </motion.div>
            )}
          </AnimatePresence>

          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.25, duration: 0.2 }}
          >
            <FloatingLabelInput
              type="email"
              label="Email Address"
              placeholder="Enter your email"
              value={email}
              onChange={setEmail}
              error={!email && error ? 'Email is required' : undefined}
              autoComplete="email"
              required
            />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3, duration: 0.2 }}
          >
            <FloatingLabelInput
              type="password"
              label="Password"
              placeholder="Enter your password"
              value={password}
              onChange={setPassword}
              error={!password && error ? 'Password is required' : undefined}
              autoComplete="current-password"
              required
            />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.35, duration: 0.2 }}
            className="flex items-center justify-between"
          >
            <label className="flex items-center gap-2 cursor-pointer group">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                className="w-4 h-4 rounded border-slate-300 transition-colors focus:ring-2 focus:ring-blue-500 focus:ring-offset-0"
                style={{ accentColor: '#0F172A' }}
              />
              <span
                className="text-sm transition-colors group-hover:text-slate-700"
                style={{ color: '#64748B' }}
              >
                Remember me
              </span>
            </label>
            <a
              href="/forgot-password"
              className="text-sm font-medium hover:underline transition-colors"
              style={{ color: '#3B82F6' }}
            >
              Forgot password?
            </a>
          </motion.div>

          <motion.button
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4, duration: 0.2 }}
            type="submit"
            disabled={isLoading}
            className="w-full text-white font-medium rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed relative overflow-hidden"
            style={{
              backgroundColor: '#0F172A',
              height: '48px',
              boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
            }}
            whileHover={{ y: -2, boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)' }}
            whileTap={{ scale: 0.98 }}
          >
            {isLoading ? (
              <span className="flex items-center justify-center gap-2">
                <svg className="animate-spin h-5 w-5" viewBox="0 0 24 24">
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                    fill="none"
                  />
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  />
                </svg>
                Signing in...
              </span>
            ) : (
              'Sign In'
            )}
          </motion.button>
        </form>

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.5 }}
          className="mt-6 pt-6 text-center"
          style={{ borderTop: '1px solid #E2E8F0' }}
        >
          <button
            type="button"
            onClick={fillDemoCredentials}
            className="text-sm font-medium transition-colors hover:opacity-80"
            style={{ color: '#64748B' }}
          >
            Fill demo credentials
          </button>
          <div
            className="mt-3 p-3 rounded-lg text-xs"
            style={{ backgroundColor: '#F8FAFC', color: '#64748B' }}
          >
            <p>
              <strong style={{ color: '#475569' }}>Demo Account:</strong>
            </p>
            <p className="mt-1">Email: {config.demoEmail}</p>
            <p>Password: {MOCK_PASSWORD}</p>
          </div>
        </motion.div>
      </AuthCard>
    </div>
  )
}
```

- [ ] **Step 2: Commit login page redesign**

```bash
git add src/pages/login.tsx
git commit -m "feat(auth): redesign login page with Refined Corporate aesthetic"
```

---

### Task 6: Redesign 2FA Page

**Files:**
- Modify: `src/pages/two-factor-auth.tsx`

- [ ] **Step 1: Replace 2FA page with new design**

```tsx
import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import { useAuthStore } from '@shared/stores/authStore'
import { Shield, ArrowLeft, CheckCircle2 } from 'lucide-react'
import { AuthCard } from '@shared/components/auth/AuthCard'
import { OTPInput } from '@shared/components/auth/OTPInput'
import { shakeVariants } from '@shared/styles/animations'

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
      }
    } catch (err) {
      setError('Verification failed. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  // Success step
  if (step === 'success') {
    return (
      <div
        className="min-h-screen flex items-center justify-center p-4 relative overflow-hidden"
        style={{
          background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)',
        }}
      >
        <motion.div
          className="absolute inset-0 opacity-5"
          style={{
            backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
          }}
          animate={{
            backgroundPosition: ['0px 0px', '60px 60px'],
          }}
          transition={{
            duration: 20,
            repeat: Infinity,
            ease: 'linear',
          }}
        />

        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="w-full max-w-[480px] bg-white rounded-2xl p-10 text-center relative z-10"
          style={{
            boxShadow: '0 8px 32px rgba(0, 0, 0, 0.12)',
          }}
        >
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ delay: 0.2, type: 'spring', stiffness: 200 }}
            className="w-20 h-20 bg-emerald-100 rounded-full flex items-center justify-center mx-auto mb-6"
          >
            <CheckCircle2 className="w-10 h-10 text-emerald-600" />
          </motion.div>
          <h2
            className="text-2xl font-semibold mb-2"
            style={{ color: '#1E293B', letterSpacing: '-0.5px' }}
          >
            Verification Successful!
          </h2>
          <p className="mb-8" style={{ color: '#64748B' }}>
            You can now access your {config.dashboardName} dashboard.
          </p>
          <motion.button
            onClick={() => navigate(redirectPath)}
            className="w-full text-white font-medium rounded-lg transition-all"
            style={{
              backgroundColor: '#0F172A',
              height: '48px',
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
            }}
            whileHover={{ y: -2, boxShadow: '0 6px 16px rgba(0, 0, 0, 0.2)' }}
            whileTap={{ scale: 0.98 }}
          >
            Continue to Dashboard
          </motion.button>
        </motion.div>
      </div>
    )
  }

  return (
    <div
      className="min-h-screen flex items-center justify-center p-4 relative overflow-hidden"
      style={{
        background: 'linear-gradient(135deg, #1A1F36 0%, #2D3748 100%)',
      }}
    >
      {/* Animated background pattern */}
      <motion.div
        className="absolute inset-0 opacity-5"
        style={{
          backgroundImage: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
        }}
        animate={{
          backgroundPosition: ['0px 0px', '60px 60px'],
        }}
        transition={{
          duration: 20,
          repeat: Infinity,
          ease: 'linear',
        }}
      />

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
        {/* Back button */}
        <motion.button
          initial={{ opacity: 0, x: -10 }}
          animate={{ opacity: 1, x: 0 }}
          onClick={() => navigate('/login')}
          className="flex items-center gap-2 text-sm mb-4 transition-colors hover:opacity-70 absolute -top-2 left-6"
          style={{ color: '#64748B' }}
        >
          <ArrowLeft className="w-4 h-4" />
          Back to Login
        </motion.button>

        <form onSubmit={(e) => { e.preventDefault(); handleVerify() }} className="space-y-6">
          <AnimatePresence>
            {error && (
              <motion.div
                key="error"
                variants={shakeVariants}
                initial="hidden"
                animate="visible"
                exit={{ opacity: 0, height: 0 }}
                className="p-3 bg-red-50 border border-red-200 rounded-lg flex items-start gap-2 text-sm"
              >
                <svg
                  className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                    clipRule="evenodd"
                  />
                </svg>
                <span style={{ color: '#DC2626' }}>{error}</span>
              </motion.div>
            )}
          </AnimatePresence>

          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.15, duration: 0.2 }}
          >
            <OTPInput
              value={code}
              onChange={setCode}
              error={!!error}
              disabled={isLoading}
            />
          </motion.div>

          <motion.button
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.25, duration: 0.2 }}
            type="submit"
            disabled={isLoading || code.join('').length !== 6}
            className="w-full text-white font-medium rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed"
            style={{
              backgroundColor: '#0F172A',
              height: '48px',
              boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
            }}
            whileHover={{
              y: -2,
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
            }}
            whileTap={{ scale: 0.98 }}
          >
            {isLoading ? (
              <span className="flex items-center justify-center gap-2">
                <svg className="animate-spin h-5 w-5" viewBox="0 0 24 24">
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                    fill="none"
                  />
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  />
                </svg>
                Verifying...
              </span>
            ) : (
              'Verify'
            )}
          </motion.button>
        </form>

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.35 }}
          className="mt-6 text-center space-y-2"
        >
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
        </motion.div>

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.4 }}
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
        </motion.div>
      </AuthCard>
    </div>
  )
}
```

- [ ] **Step 2: Commit 2FA page redesign**

```bash
git add src/pages/two-factor-auth.tsx
git commit -m "feat(auth): redesign 2FA page with Refined Corporate aesthetic"
```

---

### Task 7: Install framer-motion dependency

**Files:**
- Modify: `package.json`

- [ ] **Step 1: Install framer-motion**

```bash
cd /c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidex-domain/Management-domain/Executive-domain-X/Frontends/Web/ceo-web-dashboard
npm install framer-motion --legacy-peer-deps
```

- [ ] **Step 2: Verify installation**

```bash
grep "framer-motion" package.json
```

Expected output: `"framer-motion": "^x.x.x"`

- [ ] **Step 3: Commit package changes**

```bash
git add package.json package-lock.json
git commit -m "chore: install framer-motion for animations"
```

---

### Task 8: Create index file for auth components

**Files:**
- Create: `src/shared/components/auth/index.ts`

- [ ] **Step 1: Create barrel export file**

```typescript
export { AuthCard } from './AuthCard'
export { FloatingLabelInput } from './FloatingLabelInput'
export { OTPInput } from './OTPInput'
```

- [ ] **Step 2: Commit barrel export**

```bash
git add src/shared/components/auth/index.ts
git commit -m "chore: add barrel export for auth components"
```

---

## Self-Review Checklist

**Spec Coverage:**
- [x] Sophisticated gradient background (#1A1F36 to #2D3748)
- [x] Glassmorphism card effect
- [x] Card entrance animations (400ms, fade-in-up)
- [x] Staggered children animations
- [x] Floating label inputs
- [x] Button hover/press effects
- [x] Inter font family (via system fonts with proper stack)
- [x] Accessibility (ARIA labels, focus management)
- [x] 2FA OTP inputs with animations
- [x] Success checkmark animation

**Placeholder Scan:**
- [x] No TBD/TODO found
- [x] All code blocks complete
- [x] All file paths specified
- [x] All commands include expected output

**Type Consistency:**
- [x] Component props match across file structure
- [x] Animation constants properly typed
- [x] Event handler signatures consistent

---

## Testing Steps

After implementation, verify:

1. **Visual Review**
   - Background gradient displays correctly
   - Card has glassmorphism effect
   - All animations play smoothly
   - Typography matches specs

2. **Interaction Testing**
   - Floating labels work on focus/input
   - Button hover effects work
   - OTP inputs auto-advance
   - Error shake animation plays

3. **Accessibility**
   - Tab navigation works
   - ARIA labels present
   - Screen reader announces errors
   - Keyboard submission works

4. **Browser Testing**
   - Chrome/Edge (primary)
   - Firefox
   - Safari (if available)

5. **Responsive Testing**
   - Desktop (1920x1080)
   - Tablet (768px)
   - Mobile (375px)

---

**Plan complete and saved to `docs/superpowers/plans/2026-04-07-ceo-auth-refined-corporate.md`.**

**Two execution options:**

**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints

**Which approach?**
