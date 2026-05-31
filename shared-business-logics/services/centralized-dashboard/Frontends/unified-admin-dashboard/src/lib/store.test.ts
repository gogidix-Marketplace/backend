/**
 * Tests for Zustand store
 * Tests cover state management, actions, and notifications
 */

import { renderHook, act } from '@testing-library/react'
import { useDashboardStore, Notification } from './store'

describe('Dashboard Store', () => {
  beforeEach(() => {
    // Reset store state before each test
    useDashboardStore.setState({
      selectedTenant: 'default',
      sidebarOpen: true,
      notifications: [],
      realTimeEnabled: true
    })
  })

  describe('selectedTenant', () => {
    it('should have default tenant as "default"', () => {
      const { result } = renderHook(() => useDashboardStore())

      expect(result.current.selectedTenant).toBe('default')
    })

    it('should update selected tenant', () => {
      const { result } = renderHook(() => useDashboardStore())

      act(() => {
        result.current.setSelectedTenant('tenant-1')
      })

      expect(result.current.selectedTenant).toBe('tenant-1')
    })

    it('should persist tenant change across renders', () => {
      const { result, rerender } = renderHook(() => useDashboardStore())

      act(() => {
        result.current.setSelectedTenant('tenant-2')
      })

      rerender()

      expect(result.current.selectedTenant).toBe('tenant-2')
    })
  })

  describe('sidebarOpen', () => {
    it('should have sidebar open by default', () => {
      const { result } = renderHook(() => useDashboardStore())

      expect(result.current.sidebarOpen).toBe(true)
    })

    it('should toggle sidebar state', () => {
      const { result } = renderHook(() => useDashboardStore())

      expect(result.current.sidebarOpen).toBe(true)

      act(() => {
        result.current.toggleSidebar()
      })

      expect(result.current.sidebarOpen).toBe(false)

      act(() => {
        result.current.toggleSidebar()
      })

      expect(result.current.sidebarOpen).toBe(true)
    })
  })

  describe('notifications', () => {
    it('should start with empty notifications array', () => {
      const { result } = renderHook(() => useDashboardStore())

      expect(result.current.notifications).toEqual([])
    })

    it('should add notification with generated id and timestamp', () => {
      const { result } = renderHook(() => useDashboardStore())

      const notification = {
        type: 'success' as const,
        title: 'Test Notification',
        message: 'Test message'
      }

      act(() => {
        result.current.addNotification(notification)
      })

      const notifications = result.current.notifications
      expect(notifications).toHaveLength(1)
      expect(notifications[0].type).toBe('success')
      expect(notifications[0].title).toBe('Test Notification')
      expect(notifications[0].message).toBe('Test message')
      expect(notifications[0].id).toBeDefined()
      expect(notifications[0].timestamp).toBeInstanceOf(Date)
    })

    it('should add multiple notifications', () => {
      const { result } = renderHook(() => useDashboardStore())

      act(() => {
        result.current.addNotification({
          type: 'info',
          title: 'Info 1',
          message: 'Message 1'
        })
        result.current.addNotification({
          type: 'warning',
          title: 'Warning 1',
          message: 'Message 2'
        })
      })

      expect(result.current.notifications).toHaveLength(2)
    })

    it('should remove notification by id', () => {
      const { result } = renderHook(() => useDashboardStore())

      let notificationId: string

      act(() => {
        result.current.addNotification({
          type: 'info',
          title: 'Test',
          message: 'Test'
        })
        notificationId = result.current.notifications[0].id
      })

      expect(result.current.notifications).toHaveLength(1)

      act(() => {
        result.current.removeNotification(notificationId)
      })

      expect(result.current.notifications).toHaveLength(0)
    })

    it('should only remove the specified notification', () => {
      const { result } = renderHook(() => useDashboardStore())

      let id1: string, id2: string

      act(() => {
        result.current.addNotification({
          type: 'info',
          title: 'Notification 1',
          message: 'Message 1'
        })
        result.current.addNotification({
          type: 'warning',
          title: 'Notification 2',
          message: 'Message 2'
        })
        id1 = result.current.notifications[0].id
        id2 = result.current.notifications[1].id
      })

      act(() => {
        result.current.removeNotification(id1)
      })

      expect(result.current.notifications).toHaveLength(1)
      expect(result.current.notifications[0].id).toBe(id2)
    })

    it('should handle removing non-existent notification', () => {
      const { result } = renderHook(() => useDashboardStore())

      act(() => {
        result.current.addNotification({
          type: 'info',
          title: 'Test',
          message: 'Test'
        })
      })

      const beforeLength = result.current.notifications.length

      act(() => {
        result.current.removeNotification('non-existent-id')
      })

      expect(result.current.notifications).toHaveLength(beforeLength)
    })

    it('should support all notification types', () => {
      const { result } = renderHook(() => useDashboardStore())

      const types: Array<'info' | 'success' | 'warning' | 'error'> = ['info', 'success', 'warning', 'error']

      act(() => {
        types.forEach((type, index) => {
          result.current.addNotification({
            type,
            title: `${type} notification`,
            message: `Message ${index}`
          })
        })
      })

      expect(result.current.notifications).toHaveLength(4)
      result.current.notifications.forEach((notification, index) => {
        expect(notification.type).toBe(types[index])
      })
    })
  })

  describe('realTimeEnabled', () => {
    it('should have real-time enabled by default', () => {
      const { result } = renderHook(() => useDashboardStore())

      expect(result.current.realTimeEnabled).toBe(true)
    })

    it('should toggle real-time state', () => {
      const { result } = renderHook(() => useDashboardStore())

      expect(result.current.realTimeEnabled).toBe(true)

      act(() => {
        result.current.toggleRealTime()
      })

      expect(result.current.realTimeEnabled).toBe(false)

      act(() => {
        result.current.toggleRealTime()
      })

      expect(result.current.realTimeEnabled).toBe(true)
    })
  })

  describe('state persistence', () => {
    it('should maintain state across multiple operations', () => {
      const { result } = renderHook(() => useDashboardStore())

      act(() => {
        result.current.setSelectedTenant('custom-tenant')
        result.current.toggleSidebar()
        result.current.addNotification({
          type: 'success',
          title: 'Test',
          message: 'Test message'
        })
        result.current.toggleRealTime()
      })

      expect(result.current.selectedTenant).toBe('custom-tenant')
      expect(result.current.sidebarOpen).toBe(false)
      expect(result.current.notifications).toHaveLength(1)
      expect(result.current.realTimeEnabled).toBe(false)
    })
  })
})
