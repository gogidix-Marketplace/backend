/**
 * Session Manager
 *
 * Handles:
 * - Session timeout with warning dialog
 * - Multi-tab synchronization
 * - Token refresh
 * - Logout handling
 * - "Keep me signed in" bypass
 */

// Using native BroadcastChannel API instead of external package

export type ConnectionStatus = 'connecting' | 'connected' | 'disconnected' | 'error'

export interface SessionConfig {
  timeoutMinutes: number
  warningMinutes: number
  keepSignedIn: boolean
}

export interface SessionState {
  lastActivity: number
  expiresAt: number | null
  warningShown: boolean
  keepSignedInUntil: number | null
}

const SESSION_CHANNEL = 'executive-session'
const DEFAULT_TIMEOUT = 15 // minutes
const WARNING_BEFORE = 2 // minutes

class SessionManager {
  private config: SessionConfig = {
    timeoutMinutes: DEFAULT_TIMEOUT,
    warningMinutes: WARNING_BEFORE,
    keepSignedIn: false,
  }

  private state: SessionState = {
    lastActivity: Date.now(),
    expiresAt: null,
    warningShown: false,
    keepSignedInUntil: null,
  }

  private timeoutId: ReturnType<typeof setTimeout> | null = null
  private warningId: ReturnType<typeof setTimeout> | null = null
  private channel: any | null = null // Native BroadcastChannel
  private listeners: Set<(event: SessionEvent) => void> = new Set()

  constructor() {
    this.setupBroadcastChannel()
    this.setupActivityListeners()
  }

  /**
   * Initialize session with user config
   */
  init(config: Partial<SessionConfig> = {}) {
    this.config = { ...this.config, ...config }

    // Check for "keep me signed in"
    if (this.config.keepSignedIn) {
      const until = Date.now() + 24 * 60 * 60 * 1000 // 24 hours
      this.state.keepSignedInUntil = until
      this.saveState()
      return
    }

    // Start session timeout
    this.startTimeout()

    // Load existing state from storage
    this.loadState()
  }

  /**
   * Update session timeout duration
   */
  setSessionTimeout(minutes: number) {
    this.config.timeoutMinutes = minutes
    this.resetTimeout()
  }

  /**
   * Show warning dialog before timeout
   */
  showWarningBeforeTimeout(minutes: number) {
    this.config.warningMinutes = minutes
  }

  /**
   * Extend the current session
   */
  extendSession() {
    this.state.lastActivity = Date.now()
    this.state.warningShown = false
    this.resetTimeout()
    this.emit({ type: 'session-extended' })
    this.saveState()
  }

  /**
   * Sync session across tabs
   */
  syncSessionAcrossTabs() {
    this.channel?.postMessage({
      type: 'sync',
      state: this.state,
    })
  }

  /**
   * Refresh auth token
   */
  async refreshToken(): Promise<void> {
    // In production, this would call the auth API
    // For now, just extend the session
    this.extendSession()
  }

  /**
   * Logout and clear session
   */
  logout(redirect = true) {
    this.clearTimeout()
    this.state = {
      lastActivity: Date.now(),
      expiresAt: null,
      warningShown: false,
      keepSignedInUntil: null,
    }
    this.clearState()
    this.emit({ type: 'logout', redirect })
  }

  /**
   * Add event listener
   */
  on(event: SessionEvent['type'], callback: (event: SessionEvent) => void) {
    this.listeners.add((e) => {
      if (e.type === event) callback(e)
    })
  }

  /**
   * Remove event listener
   */
  off(event: SessionEvent['type'], callback: (event: SessionEvent) => void) {
    // Implementation for removing specific listeners
  }

  /**
   * Get current session status
   */
  getStatus(): SessionStatus {
    const now = Date.now()

    // Check keep signed in
    if (this.state.keepSignedInUntil && now < this.state.keepSignedInUntil) {
      return {
        isActive: true,
        isKeepSignedIn: true,
        expiresAt: new Date(this.state.keepSignedInUntil),
      }
    }

    // Check timeout
    if (this.state.expiresAt && now > this.state.expiresAt) {
      return {
        isActive: false,
        isKeepSignedIn: false,
        expiresAt: null,
      }
    }

    return {
      isActive: true,
      isKeepSignedIn: false,
      expiresAt: this.state.expiresAt ? new Date(this.state.expiresAt) : null,
    }
  }

  // Private methods

  private setupBroadcastChannel() {
    try {
      // Using native BroadcastChannel API
      this.channel = new (window as any).BroadcastChannel(SESSION_CHANNEL)

      this.channel.onmessage = (event) => {
        const message = event.data

        switch (message.type) {
          case 'sync':
            // Sync state from other tab
            if (message.state) {
              this.state = message.state
            }
            this.emit({ type: 'session-synced' })
            break

          case 'logout':
            // Logout triggered from another tab
            this.emit({ type: 'logout', redirect: false })
            break

          case 'activity':
            // Activity in another tab - extend local session
            this.resetTimeout()
            break
        }
      }
    } catch (e) {
      console.warn('BroadcastChannel not supported:', e)
    }
  }

  private setupActivityListeners() {
    const events = [
      'mousedown',
      'keydown',
      'scroll',
      'touchstart',
      'click',
    ] as const

    const updateActivity = () => {
      this.state.lastActivity = Date.now()
      this.channel?.postMessage({ type: 'activity' })
    }

    events.forEach((event) => {
      document.addEventListener(event, updateActivity, { passive: true })
    })
  }

  private startTimeout() {
    const timeoutMs = this.config.timeoutMinutes * 60 * 1000
    const warningMs = (this.config.timeoutMinutes - this.config.warningMinutes) * 60 * 1000

    this.state.expiresAt = Date.now() + timeoutMs

    // Set warning timeout
    this.warningId = setTimeout(() => {
      if (!this.state.warningShown) {
        this.state.warningShown = true
        this.emit({
          type: 'session-warning',
          minutesRemaining: this.config.warningMinutes,
        })
      }
    }, warningMs)

    // Set logout timeout
    this.timeoutId = setTimeout(() => {
      this.emit({ type: 'session-timeout' })
      this.logout(true)
    }, timeoutMs)

    this.saveState()
  }

  private resetTimeout() {
    this.clearTimeout()
    this.startTimeout()
  }

  private clearTimeout() {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId)
      this.timeoutId = null
    }
    if (this.warningId) {
      clearTimeout(this.warningId)
      this.warningId = null
    }
  }

  private saveState() {
    try {
      localStorage.setItem('executive-session', JSON.stringify(this.state))
    } catch (e) {
      console.warn('Failed to save session state:', e)
    }
  }

  private loadState() {
    try {
      const saved = localStorage.getItem('executive-session')
      if (saved) {
        this.state = JSON.parse(saved)
      }
    } catch (e) {
      console.warn('Failed to load session state:', e)
    }
  }

  private clearState() {
    try {
      localStorage.removeItem('executive-session')
    } catch (e) {
      console.warn('Failed to clear session state:', e)
    }
  }

  private emit(event: SessionEvent) {
    this.listeners.forEach((listener) => listener(event))
  }
}

// Types

export type SessionEvent =
  | { type: 'session-warning'; minutesRemaining: number }
  | { type: 'session-timeout' }
  | { type: 'session-extended' }
  | { type: 'logout'; redirect: boolean }
  | { type: 'session-synced' }

export interface SessionStatus {
  isActive: boolean
  isKeepSignedIn: boolean
  expiresAt: Date | null
}

// Singleton instance
let sessionManagerInstance: SessionManager | null = null

export function getSessionManager(): SessionManager {
  if (!sessionManagerInstance) {
    sessionManagerInstance = new SessionManager()
  }
  return sessionManagerInstance
}

// React hook for session management
export function useSessionManager(config?: Partial<SessionConfig>) {
  const manager = getSessionManager()

  // Initialize on first use
  if (config) {
    manager.init(config)
  }

  return {
    manager,
    status: manager.getStatus(),
    extendSession: () => manager.extendSession(),
    logout: (redirect?: boolean) => manager.logout(redirect),
  }
}
