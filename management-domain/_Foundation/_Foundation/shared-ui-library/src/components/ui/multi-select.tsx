import * as React from 'react'
import { Check, ChevronDown, X } from 'lucide-react'
import { cn } from '@lib/utils'
import { Button } from './button'
import { Popover, PopoverContent, PopoverTrigger } from './popover'

export interface MultiSelectOption {
  value: string
  label: string
  disabled?: boolean
}

export interface MultiSelectProps {
  options: MultiSelectOption[]
  value?: string[]
  onChange?: (value: string[]) => void
  placeholder?: string
  disabled?: boolean
  className?: string
  searchable?: boolean
  maxDisplay?: number
}

export function MultiSelect({
  options,
  value = [],
  onChange,
  placeholder = 'Select options',
  disabled = false,
  className,
  searchable = false,
  maxDisplay = 3,
}: MultiSelectProps) {
  const [isOpen, setIsOpen] = React.useState(false)
  const [searchQuery, setSearchQuery] = React.useState('')
  const triggerRef = React.useRef<HTMLButtonElement>(null)

  const selectedOptions = options.filter((option) => value.includes(option.value))
  const displayOptions = selectedOptions.slice(0, maxDisplay)
  const remainingCount = selectedOptions.length - maxDisplay

  const filteredOptions = React.useMemo(() => {
    if (!searchQuery) return options
    return options.filter((option) =>
      option.label.toLowerCase().includes(searchQuery.toLowerCase())
    )
  }, [options, searchQuery])

  const handleToggle = (optionValue: string) => {
    const newValue = value.includes(optionValue)
      ? value.filter((v) => v !== optionValue)
      : [...value, optionValue]
    onChange?.(newValue)
  }

  const handleRemove = (optionValue: string, e: React.MouseEvent) => {
    e.stopPropagation()
    onChange?.(value.filter((v) => v !== optionValue))
  }

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation()
    onChange?.([])
  }

  return (
    <Popover open={isOpen} onOpenChange={setIsOpen}>
      <PopoverTrigger asChild>
        <Button
          ref={triggerRef}
          type="button"
          variant="outline"
          disabled={disabled}
          className={cn(
            'w-full justify-between h-auto min-h-[42px] py-2',
            className
          )}
          onClick={() => setIsOpen(!isOpen)}
        >
          <div className="flex flex-wrap gap-1">
            {selectedOptions.length === 0 ? (
              <span className="text-gray-400">{placeholder}</span>
            ) : (
              <>
                {displayOptions.map((option) => (
                  <span
                    key={option.value}
                    className="inline-flex items-center gap-1 bg-primary-100 text-primary-700 px-2 py-1 rounded-md text-sm"
                  >
                    {option.label}
                    {!disabled && (
                      <button
                        type="button"
                        onClick={(e) => handleRemove(option.value, e)}
                        className="hover:text-primary-900"
                      >
                        <X className="h-3 w-3" />
                      </button>
                    )}
                  </span>
                ))}
                {remainingCount > 0 && (
                  <span className="inline-flex items-center bg-gray-100 text-gray-700 px-2 py-1 rounded-md text-sm">
                    +{remainingCount} more
                  </span>
                )}
              </>
            )}
          </div>
          <div className="flex items-center gap-2">
            {value.length > 0 && !disabled && (
              <button
                type="button"
                onClick={handleClear}
                className="text-gray-400 hover:text-gray-600"
              >
                <X className="h-4 w-4" />
              </button>
            )}
            <ChevronDown className="h-4 w-4 text-gray-400" />
          </div>
        </Button>
      </PopoverTrigger>
      <PopoverContent
        className="w-full p-0"
        align="start"
        style={{ width: triggerRef.current?.offsetWidth }}
      >
        {searchable && (
          <div className="p-2 border-b border-gray-200">
            <input
              type="text"
              placeholder="Search..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full px-3 py-2 text-sm border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
          </div>
        )}
        <div className="max-h-60 overflow-auto p-2">
          {filteredOptions.length === 0 ? (
            <div className="py-4 text-center text-sm text-gray-500">
              No options found
            </div>
          ) : (
            filteredOptions.map((option) => {
              const isSelected = value.includes(option.value)
              return (
                <button
                  key={option.value}
                  type="button"
                  onClick={() => handleToggle(option.value)}
                  disabled={option.disabled}
                  className={cn(
                    'w-full flex items-center gap-2 px-3 py-2 text-sm rounded-md transition-colors',
                    'hover:bg-gray-100',
                    option.disabled && 'opacity-50 cursor-not-allowed',
                    isSelected && 'bg-primary-50 text-primary-700 hover:bg-primary-100'
                  )}
                >
                  <div
                    className={cn(
                      'flex-shrink-0 w-4 h-4 border rounded flex items-center justify-center',
                      isSelected
                        ? 'border-primary-600 bg-primary-600 text-white'
                        : 'border-gray-300'
                    )}
                  >
                    {isSelected && <Check className="h-3 w-3" />}
                  </div>
                  <span className="flex-1 text-left">{option.label}</span>
                </button>
              )
            })
          )}
        </div>
      </PopoverContent>
    </Popover>
  )
}
