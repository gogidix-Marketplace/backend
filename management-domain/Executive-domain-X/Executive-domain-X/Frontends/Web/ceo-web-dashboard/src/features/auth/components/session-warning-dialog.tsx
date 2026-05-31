import { useEffect, useState } from 'react'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
import { Button } from '@shared/components/ui/button'
import { AlertCircle, Clock } from 'lucide-react'
import { getSessionManager, type SessionEvent } from '@infrastructure/auth/session-manager'

/**
 * Session Warning Dialog
 *
 * Shows a warning dialog 2 minutes before session timeout.
 * User can extend the session or log out.
 */

export function SessionWarningDialog() {
  const [isOpen, setIsOpen] = useState(false)
  const [minutesRemaining, setMinutesRemaining] = useState(0)
  const [isExtending, setIsExtending] = useState(false)

  useEffect(() => {
    const manager = getSessionManager()

    const handleWarning = (event: SessionEvent) => {
      if (event.type === 'session-warning') {
        setMinutesRemaining(event.minutesRemaining)
        setIsOpen(true)
      }
    }

    manager.on('session-warning', handleWarning)

    return () => {
      manager.off('session-warning', handleWarning)
    }
  }, [])

  const handleExtend = async () => {
    setIsExtending(true)
    const manager = getSessionManager()

    // Simulate API call for token refresh
    await new Promise((resolve) => setTimeout(resolve, 500))

    manager.extendSession()
    setIsExtending(false)
    setIsOpen(false)
  }

  const handleLogout = () => {
    const manager = getSessionManager()
    manager.logout(true)
    setIsOpen(false)
  }

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogContent className="sm:max-w-md" onPointerDownOutside={(e) => e.preventDefault()}>
        <DialogHeader>
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-full bg-amber-100 dark:bg-amber-900/20">
              <Clock className="h-5 w-5 text-amber-600 dark:text-amber-400" />
            </div>
            <div>
              <DialogTitle>Session Expiring Soon</DialogTitle>
            </div>
          </div>
          <DialogDescription className="pt-2">
            Your session will expire in {minutesRemaining} minute{minutesRemaining !== 1 ? 's' : ''}{' '}
            due to inactivity.
          </DialogDescription>
        </DialogHeader>

        <div className="rounded-lg bg-amber-50 dark:bg-amber-900/10 p-3 border border-amber-200 dark:border-amber-800">
          <div className="flex gap-2">
            <AlertCircle className="h-4 w-4 text-amber-600 dark:text-amber-400 mt-0.5 flex-shrink-0" />
            <p className="text-sm text-amber-800 dark:text-amber-200">
              For security purposes, you'll be automatically logged out if there's no activity.
              Extend your session to continue working.
            </p>
          </div>
        </div>

        <DialogFooter className="gap-2 sm:gap-0">
          <Button variant="outline" onClick={handleLogout}>
            Log Out Now
          </Button>
          <Button onClick={handleExtend} disabled={isExtending}>
            {isExtending ? 'Extending...' : 'Extend Session'}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

/**
 * Auto-logout component that shows when session has timed out
 */
export function SessionTimeoutDialog() {
  const [isOpen, setIsOpen] = useState(false)
  const [redirecting, setRedirecting] = useState(false)

  useEffect(() => {
    const manager = getSessionManager()

    const handleTimeout = (event: SessionEvent) => {
      if (event.type === 'session-timeout') {
        setIsOpen(true)
        // Auto-redirect after 3 seconds
        setTimeout(() => {
          setRedirecting(true)
          setTimeout(() => {
            window.location.href = '/login'
          }, 1000)
        }, 3000)
      }
    }

    manager.on('session-timeout', handleTimeout)

    return () => {
      manager.off('session-timeout', handleTimeout)
    }
  }, [])

  return (
    <Dialog open={isOpen} onOpenChange={() => {}}>
      <DialogContent
        className="sm:max-w-md"
        onPointerDownOutside={(e) => e.preventDefault()}
        showOverlay={true}
      >
        <DialogHeader>
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-full bg-red-100 dark:bg-red-900/20">
              <AlertCircle className="h-5 w-5 text-red-600 dark:text-red-400" />
            </div>
            <div>
              <DialogTitle>Session Expired</DialogTitle>
            </div>
          </div>
          <DialogDescription className="pt-2">
            {redirecting
              ? 'Redirecting to login...'
              : 'Your session has expired due to inactivity. Please login again to continue.'}
          </DialogDescription>
        </DialogHeader>

        <div className="flex justify-center">
          {redirecting ? (
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <div className="h-4 w-4 animate-spin rounded-full border-2 border-primary border-t-transparent" />
              Redirecting...
            </div>
          ) : (
            <Button onClick={() => (window.location.href = '/login')}>Go to Login</Button>
          )}
        </div>
      </DialogContent>
    </Dialog>
  )
}

/**
 * Combined session management provider
 * Includes both warning and timeout dialogs
 */
export function SessionManagerProvider() {
  return (
    <>
      <SessionWarningDialog />
      <SessionTimeoutDialog />
    </>
  )
}
