import { useState, useMemo } from 'react'
import type { PaginationParams } from '@/types'

interface UsePaginationResult extends PaginationParams {
  totalPages: number
  startIndex: number
  endIndex: number
  nextPage: () => void
  prevPage: () => void
  goToPage: (page: number) => void
  setLimit: (limit: number) => void
  canGoNext: boolean
  canGoPrev: boolean
}

export function usePagination(
  totalItems: number,
  initialLimit: number = 10
): UsePaginationResult {
  const [page, setPage] = useState(1)
  const [limit, setLimit] = useState(initialLimit)

  const totalPages = Math.ceil(totalItems / limit)

  const startIndex = (page - 1) * limit
  const endIndex = Math.min(startIndex + limit, totalItems)

  const nextPage = () => {
    setPage((p) => Math.min(p + 1, totalPages))
  }

  const prevPage = () => {
    setPage((p) => Math.max(p - 1, 1))
  }

  const goToPage = (newPage: number) => {
    setPage(Math.max(1, Math.min(newPage, totalPages)))
  }

  const canGoNext = page < totalPages
  const canGoPrev = page > 1

  return {
    page,
    limit,
    totalPages,
    startIndex,
    endIndex,
    nextPage,
    prevPage,
    goToPage,
    setLimit,
    canGoNext,
    canGoPrev,
    sortBy: undefined,
    sortOrder: undefined,
  }
}

export function usePaginatedData<T>(data: T[], itemsPerPage: number = 10) {
  const [currentPage, setCurrentPage] = useState(1)

  const totalPages = Math.ceil(data.length / itemsPerPage)

  const paginatedData = useMemo(() => {
    const startIndex = (currentPage - 1) * itemsPerPage
    const endIndex = startIndex + itemsPerPage
    return data.slice(startIndex, endIndex)
  }, [data, currentPage, itemsPerPage])

  const startIndex = (currentPage - 1) * itemsPerPage
  const endIndex = Math.min(startIndex + itemsPerPage, data.length)

  return {
    data: paginatedData,
    currentPage,
    totalPages,
    startIndex,
    endIndex,
    totalItems: data.length,
    nextPage: () => setCurrentPage((p) => Math.min(p + 1, totalPages)),
    prevPage: () => setCurrentPage((p) => Math.max(p - 1, 1)),
    goToPage: (page: number) => setCurrentPage(Math.max(1, Math.min(page, totalPages))),
    canGoNext: currentPage < totalPages,
    canGoPrev: currentPage > 1,
  }
}
