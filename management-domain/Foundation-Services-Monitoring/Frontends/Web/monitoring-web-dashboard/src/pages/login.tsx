import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore, MOCK_PASSWORD } from '@shared/stores/authStore'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Activity, AlertCircle } from 'lucide-react'

export default function LoginPage() {
  const navigate = useNavigate()
  const { login, isLoading } = useAuthStore()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')

    try {
      await login(email, password)
      navigate('/')
    } catch (err) {
      setError('Invalid credentials. Please try again.')
    }
  }

  const quickLogin = (roleEmail: string) => {
    setEmail(roleEmail)
    setPassword(MOCK_PASSWORD)
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4">
      <div className="w-full max-w-md">
        <div className="mb-8 flex flex-col items-center text-center">
          <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-[#0D47A1]">
            <Activity className="h-8 w-8 text-white" />
          </div>
          <h1 className="text-2xl font-bold text-slate-900">Monitoring Portal</h1>
          <p className="text-sm text-slate-600">Foundation Services Monitoring Dashboard</p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Sign In</CardTitle>
            <CardDescription>Enter your credentials to access the monitoring dashboard</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <div className="flex items-center gap-2 rounded-md bg-red-50 p-3 text-sm text-red-600">
                  <AlertCircle className="h-4 w-4" />
                  {error}
                </div>
              )}

              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="name@gogidix.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              <Button type="submit" className="w-full" variant="monitoring" disabled={isLoading}>
                {isLoading ? 'Signing in...' : 'Sign In'}
              </Button>
            </form>

            <div className="mt-6">
              <p className="mb-3 text-center text-xs text-slate-500">Quick login (demo):</p>
              <div className="grid grid-cols-2 gap-2">
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => quickLogin('devops@gogidix.com')}
                >
                  DevOps Lead
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => quickLogin('sre@gogidix.com')}
                >
                  SRE Engineer
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => quickLogin('analyst@gogidix.com')}
                >
                  Analyst
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="text-xs"
                  onClick={() => quickLogin('service-owner@gogidix.com')}
                >
                  Service Owner
                </Button>
              </div>
              <p className="mt-2 text-center text-xs text-slate-400">Password: {MOCK_PASSWORD}</p>
            </div>
          </CardContent>
        </Card>

        <p className="mt-6 text-center text-xs text-slate-500">
          Gogidix Foundation Services Monitoring v1.0.0
        </p>
      </div>
    </div>
  )
}
