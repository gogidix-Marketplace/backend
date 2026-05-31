import { cn, formatDate, formatMinutes, formatHours, getPriorityColor, getStatusColor, getInitials, calculatePercentage, truncateText } from '../lib/utils'

describe('Utils', () => {
  describe('cn', () => {
    it('should merge class names correctly', () => {
      expect(cn('text-red-500', 'bg-blue-500')).toBe('text-red-500 bg-blue-500')
    })

    it('should handle conditional classes', () => {
      expect(cn('text-red-500', false && 'bg-blue-500', 'font-bold')).toBe('text-red-500 font-bold')
    })
  })

  describe('formatDate', () => {
    it('should format date string correctly', () => {
      const date = '2024-01-15T10:30:00'
      const result = formatDate(date)
      expect(result).toContain('Jan')
      expect(result).toContain('15')
    })

    it('should return "-" for null date', () => {
      expect(formatDate(null as any)).toBe('-')
    })
  })

  describe('formatMinutes', () => {
    it('should format minutes less than 60', () => {
      expect(formatMinutes(30)).toBe('30m')
    })

    it('should format minutes more than 60', () => {
      expect(formatMinutes(90)).toBe('1h 30m')
    })

    it('should format hours only', () => {
      expect(formatMinutes(120)).toBe('2h')
    })
  })

  describe('formatHours', () => {
    it('should format hours less than 24', () => {
      expect(formatHours(5)).toBe('5h')
    })

    it('should format hours more than 24', () => {
      expect(formatHours(30)).toBe('1d 6h')
    })

    it('should format days only', () => {
      expect(formatHours(48)).toBe('2d')
    })
  })

  describe('getPriorityColor', () => {
    it('should return danger colors for CRITICAL priority', () => {
      const result = getPriorityColor('CRITICAL')
      expect(result).toContain('text-danger-600')
      expect(result).toContain('bg-danger-50')
    })

    it('should return warning colors for HIGH priority', () => {
      const result = getPriorityColor('HIGH')
      expect(result).toContain('text-warning-600')
    })

    it('should return primary colors for MEDIUM priority', () => {
      const result = getPriorityColor('MEDIUM')
      expect(result).toContain('text-primary-600')
    })
  })

  describe('getStatusColor', () => {
    it('should return blue colors for OPEN status', () => {
      const result = getStatusColor('OPEN')
      expect(result).toContain('text-blue-600')
    })

    it('should return green colors for RESOLVED status', () => {
      const result = getStatusColor('RESOLVED')
      expect(result).toContain('text-green-600')
    })
  })

  describe('getInitials', () => {
    it('should return initials from full name', () => {
      expect(getInitials('John Doe')).toBe('JD')
    })

    it('should handle single name', () => {
      expect(getInitials('John')).toBe('J')
    })

    it('should limit to 2 characters', () => {
      expect(getInitials('John Middle Doe')).toBe('JM')
    })
  })

  describe('calculatePercentage', () => {
    it('should calculate percentage correctly', () => {
      expect(calculatePercentage(50, 100)).toBe(50)
      expect(calculatePercentage(25, 200)).toBe(13)
    })

    it('should return 0 for zero total', () => {
      expect(calculatePercentage(50, 0)).toBe(0)
    })
  })

  describe('truncateText', () => {
    it('should return text as is if under max length', () => {
      expect(truncateText('Hello', 10)).toBe('Hello')
    })

    it('should truncate text and add ellipsis', () => {
      expect(truncateText('Hello World', 8)).toBe('Hello ...')
    })
  })
})
