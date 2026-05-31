import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore, MOCK_PASSWORD } from '@shared/stores/authStore'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Lock, Mail, AlertCircle, Shield } from 'lucide-react'
import { cn } from '@shared/utils/cn'

export default function LoginPage() {
  const navigate = useNavigate()
  const { login, isAuthenticated } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  // If already authenticated, redirect to dashboard
  if (isAuthenticated) {
    navigate('/')
    return null
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      await login(email, password)
      navigate('/')
    } catch (err) {
      setError('Invalid email or password. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  const fillDemoCredentials = (role: 'admin' | 'superadmin' | 'security' | 'devops' | 'operator') => {
    const credentials = {
      admin: 'admin@gogidix.com',
      superadmin: 'superadmin@gogidix.com',
      security: 'security@gogidix.com',
      devops: 'devops@gogidix.com',
      operator: 'operator@gogidix.com',
    }
    setEmail(credentials[role])
    setPassword(MOCK_PASSWORD)
    setError('')
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800 px-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-4 text-center">
          <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-admin-navy">
            <Shield className="h-8 w-8 text-white" />
          </div>
          <div>
            <CardTitle className="text-2xl">Admin Portal Login</CardTitle>
            <CardDescription>
              Enter your credentials to access the system administrator dashboard
            </CardDescription>
          </div>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            {error && (
              <div className="flex items-center gap-2 rounded-lg bg-red-50 p-3 text-sm text-red-600 dark:bg-red-900/20">
                <AlertCircle className="h-4 w-4" />
                {error}
              </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  id="email"
                  type="email"
                  placeholder="admin@gogidix.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="pl-9"
                  required
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  id="password"
                  type="password"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="pl-9"
                  required
                />
              </div>
            </div>

            <Button
              type="submit"
              variant="admin"
              className="w-full"
              disabled={isLoading}
            >
              {isLoading ? 'Signing in...' : 'Sign In'}
            </Button>

            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <span className="w-full border-t" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-background px-2 text-muted-foreground">
                  Demo Credentials
                </span>
              </div>
            </div>

            <div className="space-y-2 rounded-lg border bg-slate-50 p-4 dark:bg-slate-800">
              <p className="text-center text-sm font-medium">Quick Fill Demo Accounts</p>
              <div className="grid grid-cols-2 gap-2">
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('admin')}
                >
                  System Admin
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('superadmin')}
                >
                  Super Admin
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('security')}
                >
                  Security Officer
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => fillDemoCredentials('devops')}
                >
                  DevOps Engineer
                </Button>
              </div>
              <div className="text-center text-xs text-muted-foreground">
                Password: <span className="font-mono">{MOCK_PASSWORD}</span>
              </div>
            </div>

            <div className="text-center text-sm text-muted-foreground">
              <a href="#" className="hover:text-primary underline">
                Forgot your password?
              </a>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}
