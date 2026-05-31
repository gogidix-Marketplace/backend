import { useState, useMemo } from 'react'
import type { FilterState } from '@/types'

export function useFilter<T extends Record<string, unknown>>(
  data: T[],
  initialFilter?: Partial<FilterState>
) {
  const [filter, setFilter] = useState<FilterState>({
    search: '',
    ...initialFilter,
  })

  const filteredData = useMemo(() => {
    return data.filter((item) => {
      // Search filter
      if (filter.search) {
        const searchLower = filter.search.toLowerCase()
        const searchableValues = Object.values(item).filter(
          (v) => typeof v === 'string' || typeof v === 'number'
        )
        const matchesSearch = searchableValues.some((v) =>
          String(v).toLowerCase().includes(searchLower)
        )
        if (!matchesSearch) return false
      }

      // Status filter
      if (filter.status && filter.status.length > 0) {
        if (!filter.status.includes((item.status as string))) {
          return false
        }
      }

      // Date range filter
      if (filter.dateRange) {
        const [start, end] = filter.dateRange
        const itemDate = new Date(item.createdAt as string)
        if (itemDate < start || itemDate > end) {
          return false
        }
      }

      return true
    })
  }, [data, filter])

  const updateFilter = (key: keyof FilterState, value: unknown) => {
    setFilter((prev) => ({ ...prev, [key]: value }))
  }

  const clearFilter = (key?: keyof FilterState) => {
    if (key) {
      setFilter((prev) => ({ ...prev, [key]: key === 'dateRange' ? undefined : '' }))
    } else {
      setFilter({ search: '' })
    }
  }

  return {
    filter,
    filteredData,
    updateFilter,
    clearFilter,
    setFilter,
  }
}

export function useSort<T>(data: T[]) {
  const [sortBy, setSortBy] = useState<keyof T | null>(null)
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc')

  const sortedData = useMemo(() => {
    if (!sortBy) return data

    return [...data].sort((a, b) => {
      const aVal = a[sortBy]
      const bVal = b[sortBy]

      if (aVal === bVal) return 0
      if (aVal === null || aVal === undefined) return 1
      if (bVal === null || bVal === undefined) return -1

      const comparison = aVal > bVal ? 1 : -1
      return sortOrder === 'asc' ? comparison : -comparison
    })
  }, [data, sortBy, sortOrder])

  const toggleSort = (key: keyof T) => {
    if (sortBy === key) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')
    } else {
      setSortBy(key)
      setSortOrder('asc')
    }
  }

  return {
    sortBy,
    sortOrder,
    sortedData,
    toggleSort,
    setSortBy,
    setSortOrder,
  }
}
