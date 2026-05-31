import { ConnectionState } from '../enums/connection-state.enum';

export interface ConnectionInfoProps {
  id: string;
  ip: string;
  userAgent?: string;
  connectedAt: number;
  lastActivity: number;
  state: ConnectionState;
  tenantId: string | null;
  userId: string | null;
  executiveLevel: string | null;
  rooms: Set<string>;
  messagesSent: number;
  messagesReceived: number;
  bytesSent: number;
  bytesReceived: number;
  isAlive: boolean;
  missedPings: number;
}

export class ConnectionInfo {
  readonly props: ConnectionInfoProps;

  constructor(id: string, ip: string) {
    const now = Date.now();
    this.props = {
      id, ip, connectedAt: now, lastActivity: now,
      state: ConnectionState.CONNECTING,
      tenantId: null, userId: null, executiveLevel: null,
      rooms: new Set(), messagesSent: 0, messagesReceived: 0,
      bytesSent: 0, bytesReceived: 0, isAlive: true, missedPings: 0,
    };
  }

  updateActivity() { this.props.lastActivity = Date.now(); }
  isIdle(timeoutMs = 300000): boolean { return Date.now() - this.props.lastActivity > timeoutMs; }
  getDuration(): number { return Date.now() - this.props.connectedAt; }

  authenticate(userInfo: { tenantId: string; userId: string; executiveLevel: string; roles?: string[] }) {
    this.props.tenantId = userInfo.tenantId;
    this.props.userId = userInfo.userId;
    this.props.executiveLevel = userInfo.executiveLevel;
    this.props.state = ConnectionState.AUTHENTICATED;
  }

  toJSON() {
    return { id: this.props.id, ip: this.props.ip, tenantId: this.props.tenantId, userId: this.props.userId, executiveLevel: this.props.executiveLevel, state: this.props.state, connectedAt: this.props.connectedAt, lastActivity: this.props.lastActivity, duration: this.getDuration(), rooms: Array.from(this.props.rooms), messagesSent: this.props.messagesSent, messagesReceived: this.props.messagesReceived };
  }
}
