import * as React from 'react'
import { Search, SlidersHorizontal, X } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Badge } from '@/components/ui/badge'
import { cn } from '@/lib/utils'

export interface FilterOption {
  label: string
  value: string
}

export interface FilterConfig {
  key: string
  label: string
  type: 'select' | 'multiselect' | 'date' | 'daterange'
  options?: FilterOption[]
  placeholder?: string
}

export interface FilterBarProps {
  searchPlaceholder?: string
  searchValue?: string
  onSearchChange?: (value: string) => void
  filters?: FilterConfig[]
  filterValues?: Record<string, string | string[]>
  onFilterChange?: (key: string, value: string | string[]) => void
  onClearFilters?: () => void
  className?: string
  showFilterToggle?: boolean
}

export function FilterBar({
  searchPlaceholder = 'Search...',
  searchValue = '',
  onSearchChange,
  filters = [],
  filterValues = {},
  onFilterChange,
  onClearFilters,
  className,
  showFilterToggle = true,
}: FilterBarProps) {
  const [filterDialogOpen, setFilterDialogOpen] = React.useState(false)

  const activeFilterCount = Object.values(filterValues).filter((v) => {
    if (Array.isArray(v)) return v.length > 0
    return v !== '' && v !== undefined
  }).length

  const handleClearFilter = (key: string) => {
    onFilterChange?.(key, Array.isArray(filterValues[key]) ? [] : '')
  }

  const handleClearAll = () => {
    onClearFilters?.()
  }

  return (
    <div className={cn('flex flex-col gap-4', className)}>
      {/* Search and filter toggle row */}
      <div className="flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            type="search"
            placeholder={searchPlaceholder}
            value={searchValue}
            onChange={(e) => onSearchChange?.(e.target.value)}
            className="pl-9"
          />
          {searchValue && (
            <Button
              variant="ghost"
              size="icon"
              className="absolute right-1 top-1/2 h-6 w-6 -translate-y-1/2"
              onClick={() => onSearchChange?.('')}
            >
              <X className="h-4 w-4" />
            </Button>
          )}
        </div>

        {showFilterToggle && filters.length > 0 && (
          <Dialog open={filterDialogOpen} onOpenChange={setFilterDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" className="relative">
                <SlidersHorizontal className="h-4 w-4 mr-2" />
                Filters
                {activeFilterCount > 0 && (
                  <Badge
                    variant="secondary"
                    className="ml-2 h-5 min-w-5 rounded-full px-1"
                  >
                    {activeFilterCount}
                  </Badge>
                )}
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
              <DialogHeader>
                <DialogTitle>Filters</DialogTitle>
                <DialogDescription>
                  Apply filters to refine your results
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                {filters.map((filter) => (
                  <div key={filter.key} className="grid gap-2">
                    <label className="text-sm font-medium">{filter.label}</label>
                    {filter.type === 'select' && (
                      <Select
                        value={(filterValues[filter.key] as string) || ''}
                        onValueChange={(value) => onFilterChange?.(filter.key, value)}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder={filter.placeholder || `Select ${filter.label}`} />
                        </SelectTrigger>
                        <SelectContent>
                          {filter.options?.map((option) => (
                            <SelectItem key={option.value} value={option.value}>
                              {option.label}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                    )}
                    {/* Additional filter types can be added here */}
                  </div>
                ))}
              </div>
              <DialogFooter>
                <Button variant="outline" onClick={handleClearAll}>
                  Clear All
                </Button>
                <Button onClick={() => setFilterDialogOpen(false)}>
                  Apply Filters
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        )}
      </div>

      {/* Active filters */}
      {activeFilterCount > 0 && (
        <div className="flex flex-wrap gap-2">
          {Object.entries(filterValues).map(([key, value]) => {
            if (!value || (Array.isArray(value) && value.length === 0)) return null

            const filterConfig = filters.find((f) => f.key === key)
            const displayValue = Array.isArray(value) ? value.join(', ') : value

            return (
              <Badge key={key} variant="secondary" className="gap-1 pr-1">
                <span>{filterConfig?.label || key}: {displayValue}</span>
                <Button
                  variant="ghost"
                  size="icon"
                  className="h-4 w-4 rounded-full p-0 hover:bg-destructive hover:text-destructive-foreground"
                  onClick={() => handleClearFilter(key)}
                >
                  <X className="h-3 w-3" />
                </Button>
              </Badge>
            )
          })}
          <Button
            variant="ghost"
            size="sm"
            className="h-7 text-xs"
            onClick={handleClearAll}
          >
            Clear all
          </Button>
        </div>
      )}
    </div>
  )
}
