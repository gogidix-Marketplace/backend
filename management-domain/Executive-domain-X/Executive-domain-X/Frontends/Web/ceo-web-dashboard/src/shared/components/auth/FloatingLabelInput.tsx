import { useState, useId } from 'react'
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
      <label
        htmlFor={id}
        className="absolute left-4 pointer-events-none z-10 origin-left transition-all duration-200 ease-out"
        style={{
          top: isFloating ? '-8px' : '15px',
          fontSize: isFloating ? '12px' : '15px',
          color: error ? '#EF4444' : isFocused ? '#3B82F6' : '#64748B',
          backgroundColor: isFloating ? 'white' : 'transparent',
          padding: isFloating ? '0 4px' : '0',
          fontWeight: isFloating ? 500 : 400,
          lineHeight: 1,
        }}
      >
        {label} {required && <span className="text-red-500">*</span>}
      </label>

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
          aria-describedby={error ? `${id}-error` : ariaDescribedBy}
          className={`w-full px-4 pt-4 pb-1 border rounded-lg transition-all duration-200 outline-none
            ${error ? 'border-red-500' : 'border-slate-200'}
            ${isFocused ? 'border-blue-500 bg-white ring-[3px] ring-blue-500/10' : 'bg-slate-50'}
            ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
          `}
          style={{ height: '52px' }}
          placeholder={isFocused ? placeholder : ''}
        />

        {isPassword && (
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 rounded p-1"
            aria-label={showPassword ? 'Hide password' : 'Show password'}
            tabIndex={0}
          >
            {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
          </button>
        )}

        {error && !isPassword && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-red-500">
            <AlertCircle className="w-5 h-5" />
          </div>
        )}
      </div>

      {error && (
        <p
          id={`${id}-error`}
          className="text-red-500 text-sm mt-1 flex items-center gap-1"
        >
          {error}
        </p>
      )}
    </div>
  )
}
