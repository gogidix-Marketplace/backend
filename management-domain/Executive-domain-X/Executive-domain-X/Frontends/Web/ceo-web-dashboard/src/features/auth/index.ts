/**
 * Auth Feature Module
 *
 * Exports all authentication-related components and services
 */

// Pages
export { default as LoginPage } from './pages/login-page'
export { default as TwoFactorPage } from './pages/two-factor-page'
export { default as ForgotPasswordPage } from './pages/forgot-password-page'
export { default as ResetPasswordPage } from './pages/reset-password-page'
export { default as OnboardingPage } from './pages/onboarding-page'
export { default as StaffActivationPage } from './pages/staff-activation-page'

// Components
export {
  SessionWarningDialog,
  SessionTimeoutDialog,
  SessionManagerProvider,
} from './components/session-warning-dialog'

// Services
export {
  getSessionManager,
  useSessionManager,
  type SessionConfig,
  type SessionState,
  type SessionStatus,
  type SessionEvent,
} from '@infrastructure/auth/session-manager'

export {
  getTwoFactorService,
  useTwoFactor,
  type TwoFactorMethod,
  type TwoFactorSetup,
  type TwoFactorVerifyParams,
  type TwoFactorState,
  type TrustedDevice,
} from '@infrastructure/auth/two-factor-service'
