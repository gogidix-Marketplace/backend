/**
 * WebSocket React Hooks
 *
 * React hooks for WebSocket integration:
 * - useWebSocketSubscription - Subscribe to WebSocket messages
 * - useConnectionStatus - Connection status
 * - useDashboardUpdates - Auto-subscribe to dashboard updates
 */

import { useEffect, useState, useCallback, useRef } from 'react'
import {
  getWebSocketClient,
  ConnectionStatus,
  WSMessage,
  WSMessageType,
  TypedWSMessage,
} from './websocket-client'

/**
 * Subscribe to WebSocket messages
 */
export function useWebSocketSubscription<T extends WSMessageType>(
  room: string,
  messageType: T
): TypedWSMessage<T> | null {
  const [message, setMessage] = useState<TypedWSMessage<T> | null>(null)
  const clientRef = useRef<ReturnType<typeof getWebSocketClient> | null>(null)

  useEffect(() => {
    try {
      // In production, initialize with actual WebSocket URL
      // const client = getWebSocketClient({ url: 'ws://localhost:8080/ws' })
      // For now, we'll skip actual connection in this example
      clientRef.current = null
    } catch (error) {
      console.error('Failed to initialize WebSocket client:', error)
    }

    // When client is available:
    // client.subscribe(room)
    // const unsubscribe = client.onMessageType(messageType, setMessage)

    return () => {
      // Cleanup
      // if (clientRef.current) {
      //   clientRef.current.unsubscribe(room)
      // }
    }
  }, [room, messageType])

  return message
}

/**
 * Get connection status
 */
export function useConnectionStatus(): ConnectionStatus {
  const [status, setStatus] = useState<ConnectionStatus>('disconnected')

  useEffect(() => {
    try {
      const client = getWebSocketClient()
      const unsubscribe = client.onConnectionChange(setStatus)
      return unsubscribe
    } catch (error) {
      // Client not initialized yet
      return () => {}
    }
  }, [])

  return status
}

/**
 * Dashboard updates interface
 */
export interface DashboardUpdates {
  kpiUpdates: Array<{
    kpiId: string
    value: number
    change: number
  }>
  alerts: Array<{
    id: string
    severity: 'info' | 'warning' | 'critical'
    title: string
    message: string
  }>
  insights: Array<{
    id: string
    type: 'opportunity' | 'risk' | 'prediction'
    title: string
    description: string
  }>
  incidents: Array<{
    id: string
    severity: 'p1' | 'p2' | 'p3'
    title: string
  }>
  approvals: Array<{
    id: string
    type: 'budget' | 'hiring'
    title: string
  }>
}

/**
 * Auto-subscribe to dashboard updates for a specific role
 */
export function useDashboardUpdates(
  role: 'CEO' | 'CFO' | 'COO' | 'CTO'
): DashboardUpdates {
  const [updates, setUpdates] = useState<DashboardUpdates>({
    kpiUpdates: [],
    alerts: [],
    insights: [],
    incidents: [],
    approvals: [],
  })

  const room = `${role.toLowerCase()}-dashboard`

  useEffect(() => {
    // Mock implementation - in production, this would connect to actual WebSocket
    const mockUpdateInterval = setInterval(() => {
      // Simulate random updates for demo
      if (Math.random() > 0.95) {
        const updateTypes = ['kpiUpdates', 'alerts', 'insights'] as const
        const type = updateTypes[Math.floor(Math.random() * updateTypes.length)]

        setUpdates((prev) => {
          const newItem = {
            kpiUpdates: {
              id: Date.now().toString(),
              kpiId: 'revenue',
              value: 42000000 + Math.random() * 1000000,
              change: 8 + Math.random() * 2,
            },
            alerts: {
              id: Date.now().toString(),
              severity: Math.random() > 0.5 ? 'warning' : 'info',
              title: 'System Alert',
              message: 'This is a simulated alert for demo purposes',
            },
            insights: {
              id: Date.now().toString(),
              type: Math.random() > 0.5 ? 'opportunity' : 'risk',
              title: 'AI Insight',
              description: 'This is a simulated AI insight for demo purposes',
            },
          }[type]

          return {
            ...prev,
            [type]: [...prev[type], newItem].slice(-5), // Keep last 5
          }
        })
      }
    }, 5000)

    return () => clearInterval(mockUpdateInterval)
  }, [role])

  return updates
}

/**
 * Hook for sending WebSocket messages
 */
export function useWebSocketSend() {
  const send = useCallback((message: WSMessage) => {
    try {
      const client = getWebSocketClient()
      client.send(message)
    } catch (error) {
      console.error('Failed to send WebSocket message:', error)
    }
  }, [])

  return { send }
}

/**
 * Hook for manual WebSocket connection control
 */
export function useWebSocketConnection() {
  const [status, setStatus] = useState<ConnectionStatus>('disconnected')

  const connect = useCallback(() => {
    try {
      const client = getWebSocketClient({ url: 'ws://localhost:8080/ws' })
      client.connect()

      const unsubscribe = client.onConnectionChange(setStatus)

      return () => {
        unsubscribe()
        client.disconnect()
      }
    } catch (error) {
      console.error('Failed to connect WebSocket:', error)
      setStatus('error')
      return () => {}
    }
  }, [])

  const disconnect = useCallback(() => {
    try {
      const client = getWebSocketClient()
      client.disconnect()
    } catch (error) {
      console.error('Failed to disconnect WebSocket:', error)
    }
  }, [])

  const reconnect = useCallback(() => {
    try {
      const client = getWebSocketClient()
      client.reconnect()
    } catch (error) {
      console.error('Failed to reconnect WebSocket:', error)
    }
  }, [])

  return { status, connect, disconnect, reconnect }
}
