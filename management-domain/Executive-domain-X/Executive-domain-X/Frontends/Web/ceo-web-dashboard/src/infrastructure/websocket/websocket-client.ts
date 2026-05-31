/**
 * WebSocket Client
 *
 * Handles:
 * - Connection management
 * - Subscription management
 * - Message handling
 * - Auto-reconnect with exponential backoff
 * - Connection status tracking
 */

import { Observable, BehaviorSubject, Subject } from 'rxjs'
import { filter, takeUntil, tap } from 'rxjs/operators'

export type ConnectionStatus =
  | 'connecting'
  | 'connected'
  | 'disconnected'
  | 'error'
  | 'reconnecting'

export interface WSMessage {
  type: string
  data: unknown
  timestamp?: string
}

export type WSMessageType =
  | 'kpi_update'
  | 'alert'
  | 'insight'
  | 'status_change'
  | 'incident_update'
  | 'approval_request'
  | 'session_expiry'

export interface TypedWSMessage<T extends WSMessageType> {
  type: T
  data: T extends 'kpi_update' ? KPIUpdateData
    : T extends 'alert' ? AlertData
    : T extends 'insight' ? AIInsightData
    : T extends 'status_change' ? StatusChangeData
    : T extends 'incident_update' ? IncidentUpdateData
    : T extends 'approval_request' ? ApprovalRequestData
    : T extends 'session_expiry' ? SessionExpiryData
    : unknown
  timestamp: string
}

export interface KPIUpdateData {
  kpiId: string
  value: number
  previousValue: number
  change: number
  timestamp: string
}

export interface AlertData {
  id: string
  severity: 'info' | 'warning' | 'critical'
  title: string
  message: string
  domain?: string
  region?: string
}

export interface AIInsightData {
  id: string
  type: 'opportunity' | 'risk' | 'prediction' | 'recommendation'
  title: string
  description: string
  confidence: number
}

export interface StatusChangeData {
  entityType: 'domain' | 'region' | 'service'
  entityId: string
  previousStatus: string
  newStatus: string
  reason?: string
}

export interface IncidentUpdateData {
  incidentId: string
  type: 'created' | 'updated' | 'resolved'
  severity: 'p1' | 'p2' | 'p3' | 'p4'
  title: string
  description: string
}

export interface ApprovalRequestData {
  requestId: string
  type: 'budget' | 'hiring' | 'initiative'
  title: string
  amount?: number
  requestedBy: string
  urgency: 'low' | 'medium' | 'high' | 'urgent'
}

export interface SessionExpiryData {
  expiresAt: string
  warningMinutes: number
}

interface WebSocketClientConfig {
  url: string
  reconnectInterval?: number
  maxReconnectAttempts?: number
  heartbeatInterval?: number
}

class WebSocketClient {
  private ws: WebSocket | null = null
  private config: Required<WebSocketClientConfig>
  private status$ = new BehaviorSubject<ConnectionStatus>('disconnected')
  private messages$ = new Subject<WSMessage>()
  private reconnectAttempts = 0
  private reconnectTimeout: ReturnType<typeof setTimeout> | null = null
  private heartbeatTimeout: ReturnType<typeof setInterval> | null = null
  private manualClose = false
  private subscriptions = new Set<string>()

  constructor(config: WebSocketClientConfig) {
    this.config = {
      url: config.url,
      reconnectInterval: config.reconnectInterval || 3000,
      maxReconnectAttempts: config.maxReconnectAttempts || 10,
      heartbeatInterval: config.heartbeatInterval || 30000,
    }
  }

  /**
   * Connect to WebSocket server
   */
  connect(url?: string): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      return
    }

    this.manualClose = false
    this.status$.next('connecting')

    const wsUrl = url || this.config.url
    this.ws = new WebSocket(wsUrl)

    this.ws.onopen = () => {
      console.log('[WebSocket] Connected')
      this.status$.next('connected')
      this.reconnectAttempts = 0

      // Start heartbeat
      this.startHeartbeat()

      // Re-subscribe to all rooms
      this.subscriptions.forEach((room) => {
        this.send({ type: 'subscribe', data: { room } })
      })
    }

    this.ws.onclose = (event) => {
      console.log('[WebSocket] Disconnected', event.code, event.reason)

      if (!this.manualClose) {
        this.status$.next('reconnecting')
        this.scheduleReconnect()
      } else {
        this.status$.next('disconnected')
      }
    }

    this.ws.onerror = (error) => {
      console.error('[WebSocket] Error:', error)
      this.status$.next('error')
    }

    this.ws.onmessage = (event) => {
      try {
        const message: WSMessage = JSON.parse(event.data)
        this.messages$.next(message)
      } catch (error) {
        console.error('[WebSocket] Failed to parse message:', error)
      }
    }
  }

  /**
   * Disconnect from WebSocket server
   */
  disconnect(): void {
    this.manualClose = true
    this.clearReconnectTimeout()
    this.stopHeartbeat()

    if (this.ws) {
      this.ws.close(1000, 'Client disconnect')
      this.ws = null
    }

    this.status$.next('disconnected')
    this.subscriptions.clear()
  }

  /**
   * Reconnect to WebSocket server
   */
  reconnect(): void {
    this.disconnect()
    this.reconnectAttempts = 0
    this.connect()
  }

  /**
   * Get current connection status
   */
  getConnectionStatus(): ConnectionStatus {
    return this.status$.value
  }

  /**
   * Subscribe to connection status changes
   */
  onConnectionChange(callback: (status: ConnectionStatus) => void): () => void {
    const subscription = this.status$.subscribe(callback)
    return () => subscription.unsubscribe()
  }

  /**
   * Subscribe to a room/channel
   */
  subscribe(room: string): void {
    if (this.subscriptions.has(room)) {
      return
    }

    this.subscriptions.add(room)

    if (this.status$.value === 'connected') {
      this.send({ type: 'subscribe', data: { room } })
    }
  }

  /**
   * Unsubscribe from a room/channel
   */
  unsubscribe(room: string): void {
    if (!this.subscriptions.has(room)) {
      return
    }

    this.subscriptions.delete(room)

    if (this.status$.value === 'connected') {
      this.send({ type: 'unsubscribe', data: { room } })
    }
  }

  /**
   * Unsubscribe from all rooms
   */
  unsubscribeAll(): void {
    const rooms = Array.from(this.subscriptions)
    rooms.forEach((room) => this.unsubscribe(room))
  }

  /**
   * Subscribe to WebSocket messages
   */
  onMessage(callback: (message: WSMessage) => void): () => void {
    const subscription = this.messages$.subscribe(callback)
    return () => subscription.unsubscribe()
  }

  /**
   * Subscribe to specific message type
   */
  onMessageType<T extends WSMessageType>(
    type: T,
    callback: (message: TypedWSMessage<T>) => void
  ): () => void {
    const subscription = this.messages$
      .pipe(
        filter((m): m is TypedWSMessage<T> => m.type === type)
      )
      .subscribe(callback)

    return () => subscription.unsubscribe()
  }

  /**
   * Send a message to the server
   */
  send(message: WSMessage): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(message))
    } else {
      console.warn('[WebSocket] Cannot send message, not connected')
    }
  }

  // Private methods

  private scheduleReconnect(): void {
    if (this.reconnectAttempts >= this.config.maxReconnectAttempts) {
      console.error('[WebSocket] Max reconnect attempts reached')
      this.status$.next('error')
      return
    }

    const delay = this.config.reconnectInterval * Math.pow(2, this.reconnectAttempts)
    console.log(`[WebSocket] Reconnecting in ${delay}ms (attempt ${this.reconnectAttempts + 1})`)

    this.clearReconnectTimeout()
    this.reconnectTimeout = setTimeout(() => {
      this.reconnectAttempts++
      this.connect()
    }, delay)
  }

  private clearReconnectTimeout(): void {
    if (this.reconnectTimeout) {
      clearTimeout(this.reconnectTimeout)
      this.reconnectTimeout = null
    }
  }

  private startHeartbeat(): void {
    this.stopHeartbeat()

    this.heartbeatTimeout = setInterval(() => {
      if (this.ws?.readyState === WebSocket.OPEN) {
        this.send({ type: 'ping', data: { timestamp: Date.now() } })
      }
    }, this.config.heartbeatInterval)
  }

  private stopHeartbeat(): void {
    if (this.heartbeatTimeout) {
      clearInterval(this.heartbeatTimeout)
      this.heartbeatTimeout = null
    }
  }
}

// Singleton instance
let wsClientInstance: WebSocketClient | null = null

export function getWebSocketClient(config?: WebSocketClientConfig): WebSocketClient {
  if (!wsClientInstance && config) {
    wsClientInstance = new WebSocketClient(config)
  }
  if (!wsClientInstance) {
    throw new Error('WebSocket client not initialized. Provide config on first call.')
  }
  return wsClientInstance
}
