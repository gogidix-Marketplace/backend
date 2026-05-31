/**
 * Two-Factor Authentication Service
 *
 * Supports:
 * - TOTP (Time-based One-Time Password) via authenticator app
 * - SMS verification
 * - Email verification
 * - Backup codes
 */

export type TwoFactorMethod = 'totp' | 'sms' | 'email' | 'backup'

export interface TwoFactorSetup {
  method: TwoFactorMethod
  secret?: string // For TOTP
  qrCodeUrl?: string // For TOTP setup
  backupCodes?: string[] // For backup codes
  phoneNumber?: string // For SMS
  email?: string // For email
}

export interface TwoFactorVerifyParams {
  method: TwoFactorMethod
  code: string
  backupCode?: string // For backup code verification
  trustDevice?: boolean
}

export interface TwoFactorState {
  enabled: boolean
  methods: TwoFactorMethod[]
  defaultMethod: TwoFactorMethod
  trustedDevices: TrustedDevice[]
}

export interface TrustedDevice {
  id: string
  name: string
  userAgent: string
  trustedAt: string
  expiresAt: string
}

class TwoFactorService {
  private baseUrl = '/api/auth/2fa' // In production, this would be your API endpoint

  /**
   * Check if 2FA is enabled for the current user
   */
  async isEnabled(): Promise<boolean> {
    // In production, this would call: GET /api/auth/2fa/status
    // For demo, we'll return false
    return false
  }

  /**
   * Get current 2FA state
   */
  async getState(): Promise<TwoFactorState> {
    // In production, this would call: GET /api/auth/2fa/state
    return {
      enabled: false,
      methods: [],
      defaultMethod: 'totp',
      trustedDevices: [],
    }
  }

  /**
   * Setup TOTP (authenticator app)
   */
  async setupTOTP(): Promise<TwoFactorSetup> {
    // In production, this would call: POST /api/auth/2fa/totp/setup
    // Response would include: { secret, qrCodeUrl }

    // Mock response for demo
    const mockSecret = this.generateSecret()
    const mockEmail = 'ceo@gogidix.com'

    return {
      method: 'totp',
      secret: mockSecret,
      qrCodeUrl: `otpauth://totp/Gogidix:${mockEmail}?secret=${mockSecret}&issuer=Gogidix`,
    }
  }

  /**
   * Enable TOTP after user verifies their code
   */
  async enableTOTP(code: string): Promise<{ success: boolean; backupCodes: string[] }> {
    // In production, this would call: POST /api/auth/2fa/totp/enable
    // Body: { code }
    // Response would include backup codes

    // Mock response for demo
    return {
      success: true,
      backupCodes: this.generateBackupCodes(),
    }
  }

  /**
   * Setup SMS verification
   */
  async setupSMS(phoneNumber: string): Promise<TwoFactorSetup> {
    // In production, this would call: POST /api/auth/2fa/sms/setup
    // Body: { phoneNumber }
    // This would send a verification code via SMS

    return {
      method: 'sms',
      phoneNumber,
    }
  }

  /**
   * Verify and enable SMS
   */
  async enableSMS(code: string): Promise<{ success: boolean }> {
    // In production, this would call: POST /api/auth/2fa/sms/enable
    // Body: { code }

    return { success: true }
  }

  /**
   * Setup email verification
   */
  async setupEmail(email: string): Promise<TwoFactorSetup> {
    // In production, this would call: POST /api/auth/2fa/email/setup
    // Body: { email }
    // This would send a verification code via email

    return {
      method: 'email',
      email,
    }
  }

  /**
   * Verify and enable email
   */
  async enableEmail(code: string): Promise<{ success: boolean }> {
    // In production, this would call: POST /api/auth/2fa/email/enable
    // Body: { code }

    return { success: true }
  }

  /**
   * Generate new backup codes
   */
  async generateBackupCodes(): Promise<string[]> {
    // In production, this would call: POST /api/auth/2fa/backup/regenerate

    const codes: string[] = []
    for (let i = 0; i < 10; i++) {
      codes.push(this.generateBackupCode())
    }
    return codes
  }

  /**
   * Verify a 2FA code during login
   */
  async verify(params: TwoFactorVerifyParams): Promise<{
    success: boolean
    token?: string
    error?: string
  }> {
    // In production, this would call: POST /api/auth/2fa/verify
    // Body: { method, code, backupCode, trustDevice }

    // Mock verification for demo
    if (params.method === 'backup' && params.backupCode === 'backup123') {
      return { success: true }
    }

    if (params.code === '123456') {
      return { success: true, token: 'mock-jwt-token' }
    }

    return {
      success: false,
      error: 'Invalid verification code',
    }
  }

  /**
   * Disable 2FA (requires password confirmation)
   */
  async disable(password: string): Promise<{ success: boolean }> {
    // In production, this would call: POST /api/auth/2fa/disable
    // Body: { password }

    return { success: true }
  }

  /**
   * Send a new verification code (SMS/Email)
   */
  async sendCode(method: 'sms' | 'email'): Promise<{ success: boolean; expiresAt?: string }> {
    // In production, this would call: POST /api/auth/2fa/send-code
    // Body: { method }

    const expiresAt = new Date(Date.now() + 5 * 60 * 1000).toISOString()

    return { success: true, expiresAt }
  }

  /**
   * Add a trusted device
   */
  async addTrustedDevice(deviceName: string): Promise<TrustedDevice> {
    // In production, this would call: POST /api/auth/2fa/trusted-devices
    // Body: { deviceName }

    return {
      id: crypto.randomUUID(),
      name: deviceName,
      userAgent: navigator.userAgent,
      trustedAt: new Date().toISOString(),
      expiresAt: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(), // 30 days
    }
  }

  /**
   * Remove a trusted device
   */
  async removeTrustedDevice(deviceId: string): Promise<{ success: boolean }> {
    // In production, this would call: DELETE /api/auth/2fa/trusted-devices/:id

    return { success: true }
  }

  /**
   * Get all trusted devices
   */
  async getTrustedDevices(): Promise<TrustedDevice[]> {
    // In production, this would call: GET /api/auth/2fa/trusted-devices

    return []
  }

  // Private helper methods

  private generateSecret(): string {
    // Generate a 32-character base32-encoded secret
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ234567'
    let secret = ''
    for (let i = 0; i < 32; i++) {
      secret += chars[Math.floor(Math.random() * chars.length)]
    }
    return secret.match(/.{1,4}/g)?.join(' ') || secret
  }

  private generateBackupCode(): string {
    // Generate a backup code in format: XXXXX-XXXXX
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789' // No O, 0, I, 1 for clarity
    let code = ''
    for (let i = 0; i < 10; i++) {
      if (i === 5) code += '-'
      code += chars[Math.floor(Math.random() * chars.length)]
    }
    return code
  }
}

// Singleton instance
let twoFactorServiceInstance: TwoFactorService | null = null

export function getTwoFactorService(): TwoFactorService {
  if (!twoFactorServiceInstance) {
    twoFactorServiceInstance = new TwoFactorService()
  }
  return twoFactorServiceInstance
}

// React hook for 2FA
export function useTwoFactor() {
  const service = getTwoFactorService()

  return {
    isEnabled: () => service.isEnabled(),
    getState: () => service.getState(),
    setupTOTP: () => service.setupTOTP(),
    enableTOTP: (code: string) => service.enableTOTP(code),
    setupSMS: (phone: string) => service.setupSMS(phone),
    enableSMS: (code: string) => service.enableSMS(code),
    setupEmail: (email: string) => service.setupEmail(email),
    enableEmail: (code: string) => service.enableEmail(code),
    verify: (params: TwoFactorVerifyParams) => service.verify(params),
    disable: (password: string) => service.disable(password),
    sendCode: (method: 'sms' | 'email') => service.sendCode(method),
    generateBackupCodes: () => service.generateBackupCodes(),
  }
}
