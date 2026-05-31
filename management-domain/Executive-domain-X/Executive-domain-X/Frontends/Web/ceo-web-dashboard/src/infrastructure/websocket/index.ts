/**
 * WebSocket Infrastructure
 *
 * Real-time WebSocket infrastructure for live dashboard updates
 */

export {
  getWebSocketClient,
  type ConnectionStatus,
  type WSMessage,
  type WSMessageType,
  type TypedWSMessage,
  type KPIUpdateData,
  type AlertData,
  type AIInsightData,
  type StatusChangeData,
  type IncidentUpdateData,
  type ApprovalRequestData,
  type SessionExpiryData,
} from './websocket-client'

export {
  useWebSocketSubscription,
  useConnectionStatus,
  useDashboardUpdates,
  useWebSocketSend,
  useWebSocketConnection,
  type DashboardUpdates,
} from './hooks'

export { ConnectionStatusIndicator } from './connection-status'
export type { ConnectionStatusIndicatorProps } from './connection-status'
