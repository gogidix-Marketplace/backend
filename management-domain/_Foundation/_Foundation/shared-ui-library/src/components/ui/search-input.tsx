import * as React from 'react'
import { Search, X } from 'lucide-react'
import { cn } from '@lib/utils'

export interface SearchInputProps
  extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'prefix' | 'suffix'> {
  onSearch?: (value: string) => void
  onClear?: () => void
  loading?: boolean
  debounceMs?: number
  showClearButton?: boolean
}

export function SearchInput({
  value,
  onChange,
  onSearch,
  onClear,
  loading = false,
  debounceMs = 300,
  showClearButton = true,
  className,
  ...props
}: SearchInputProps) {
  const [localValue, setLocalValue] = React.useState(value?.toString() || '')
  const debounceRef = React.useRef<ReturnType<typeof setTimeout>>()

  React.useEffect(() => {
    setLocalValue(value?.toString() || '')
  }, [value])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value
    setLocalValue(newValue)

    // Debounce search callback
    if (debounceRef.current) {
      clearTimeout(debounceRef.current)
    }

    debounceRef.current = setTimeout(() => {
      onChange?.(e)
      onSearch?.(newValue)
    }, debounceMs)
  }

  const handleClear = () => {
    setLocalValue('')
    onChange?.({ target: { value: '' } } as React.ChangeEvent<HTMLInputElement>)
    onSearch?.('')
    onClear?.()
  }

  return (
    <div className="relative">
      <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
        {loading ? (
          <div className="h-4 w-4 animate-spin rounded-full border-2 border-primary-600 border-t-transparent" />
        ) : (
          <Search className="h-4 w-4 text-gray-400" />
        )}
      </div>
      <input
        type="text"
        value={localValue}
        onChange={handleChange}
        className={cn(
          'flex h-10 w-full rounded-md border border-gray-300 bg-white py-2 pl-10 pr-10 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent disabled:cursor-not-allowed disabled:opacity-50',
          className
        )}
        {...props}
      />
      {showClearButton && localValue && (
        <button
          type="button"
          onClick={handleClear}
          className="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-400 hover:text-gray-600"
        >
          <X className="h-4 w-4" />
        </button>
      )}
    </div>
  )
}

// Search Filter Component with Category Selection
export interface SearchFilterProps extends SearchInputProps {
  categories?: string[]
  selectedCategory?: string
  onCategoryChange?: (category: string) => void
}

export function SearchFilter({
  categories = [],
  selectedCategory,
  onCategoryChange,
  className,
  ...searchProps
}: SearchFilterProps) {
  return (
    <div className={cn('flex gap-2', className)}>
      <SearchInput {...searchProps} className="flex-1" />
      {categories.length > 0 && (
        <select
          value={selectedCategory || ''}
          onChange={(e) => onCategoryChange?.(e.target.value)}
          className="h-10 rounded-md border border-gray-300 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
        >
          <option value="">All Categories</option>
          {categories.map((category) => (
            <option key={category} value={category}>
              {category}
            </option>
          ))}
        </select>
      )}
    </div>
  )
}
