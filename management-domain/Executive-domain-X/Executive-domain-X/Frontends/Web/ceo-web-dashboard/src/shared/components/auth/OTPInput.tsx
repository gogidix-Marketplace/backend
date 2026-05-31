import { useRef, useEffect, forwardRef } from 'react'

interface OTPInputProps {
  length?: number
  value: string[]
  onChange: (value: string[]) => void
  error?: string
  disabled?: boolean
}

export const OTPInput = forwardRef<HTMLDivElement, OTPInputProps>(
  function OTPInput({ length = 6, value, onChange, error, disabled }, ref) {
    const inputsRef = useRef<(HTMLInputElement | null)[]>([])

    useEffect(() => {
      const firstEmptyIndex = value.findIndex((v) => v === '')
      if (firstEmptyIndex !== -1) {
        inputsRef.current[firstEmptyIndex]?.focus()
      }
    }, [])

    const handleChange = (index: number, char: string) => {
      if (!/^\d*$/.test(char)) return

      const newValue = [...value]
      newValue[index] = char

      onChange(newValue)

      if (char && index < length - 1) {
        inputsRef.current[index + 1]?.focus()
      }
    }

    const handleKeyDown = (
      index: number,
      e: React.KeyboardEvent<HTMLInputElement>
    ) => {
      if (e.key === 'Backspace' && !value[index] && index > 0) {
        const newValue = [...value]
        newValue[index - 1] = ''
        onChange(newValue)
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
      <div ref={ref} className="flex justify-center gap-3">
        {value.map((digit, index) => (
          <input
            key={index}
            ref={(el) => { inputsRef.current[index] = el }}
            type="text"
            inputMode="numeric"
            maxLength={1}
            value={digit}
            onChange={(e) => handleChange(index, e.target.value)}
            onKeyDown={(e) => handleKeyDown(index, e)}
            onPaste={index === 0 ? handlePaste : undefined}
            disabled={disabled}
            className={`w-13 h-13 text-center text-xl font-bold border-2 rounded-lg outline-none transition-all duration-200
              ${error ? 'border-red-500' : digit ? 'border-blue-500 bg-blue-50/50' : 'border-slate-200 bg-white'}
              focus:border-blue-500 focus:ring-[3px] focus:ring-blue-500/10 focus:bg-white
              ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
            `}
            style={{ width: '52px', height: '52px' }}
            aria-label={`Digit ${index + 1}`}
            aria-describedby={error ? 'otp-error' : undefined}
          />
        ))}
      </div>
    )
  }
)

OTPInput.displayName = 'OTPInput'
