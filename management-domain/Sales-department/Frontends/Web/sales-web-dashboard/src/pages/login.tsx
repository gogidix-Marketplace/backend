import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore, MOCK_PASSWORD } from '@shared/stores/authStore'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { TrendingUp, Award, Users, Target } from 'lucide-react'

export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()
  const { login } = useAuthStore()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      await login(email, password)
      navigate('/')
    } catch (err) {
      setError('Invalid credentials. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  const quickLoginUsers = [
    { email: 'vp.sales@gogidix.com', name: 'VP of Sales', role: 'VP_SALES' },
    { email: 'director.ng@gogidix.com', name: 'Nigeria Director', role: 'COUNTRY_DIRECTOR' },
    { email: 'manager.west@gogidix.com', name: 'Sales Manager', role: 'SALES_MANAGER' },
    { email: 'rep.john@gogidix.com', name: 'Sales Rep', role: 'SALES_REPRESENTATIVE' },
    { email: 'analyst.sales@gogidix.com', name: 'Sales Analyst', role: 'SALES_ANALYST' },
  ]

  const handleQuickLogin = (loginEmail: string) => {
    setEmail(loginEmail)
    setPassword(MOCK_PASSWORD)
  }

  return (
    <div className="min-h-screen flex">
      {/* Left Side - Branding */}
      <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-blue-900 to-blue-700 p-12 flex-col justify-between">
        <div>
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-white/20">
              <span className="text-2xl font-bold text-white">S</span>
            </div>
            <span className="text-2xl font-bold text-white">Gogidix Sales</span>
          </div>
        </div>

        <div className="space-y-8">
          <h1 className="text-5xl font-bold text-white">
            Sales Management Dashboard
          </h1>
          <p className="text-xl text-blue-100">
            Manage your pipeline, track performance, and close more deals with our comprehensive sales platform.
          </p>

          <div className="grid grid-cols-2 gap-6">
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-white/20">
                <TrendingUp className="h-5 w-5 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white">Pipeline Management</h3>
                <p className="text-sm text-blue-100">Track deals from lead to close</p>
              </div>
            </div>

            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-white/20">
                <Users className="h-5 w-5 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white">Team Performance</h3>
                <p className="text-sm text-blue-100">Monitor individual and team metrics</p>
              </div>
            </div>

            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-white/20">
                <Target className="h-5 w-5 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white">Forecasting</h3>
                <p className="text-sm text-blue-100">Predict revenue with AI-powered insights</p>
              </div>
            </div>

            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-white/20">
                <Award className="h-5 w-5 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-white">Commission Tracking</h3>
                <p className="text-sm text-blue-100">Transparent commission calculations</p>
              </div>
            </div>
          </div>
        </div>

        <div className="text-sm text-blue-200">
          &copy; 2024 Gogidix. All rights reserved.
        </div>
      </div>

      {/* Right Side - Login Form */}
      <div className="flex w-full lg:w-1/2 items-center justify-center p-8">
        <div className="w-full max-w-md space-y-8">
          <div className="lg:hidden text-center mb-8">
            <div className="flex items-center justify-center gap-3 mb-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-blue-600">
                <span className="text-2xl font-bold text-white">S</span>
              </div>
              <span className="text-2xl font-bold text-gray-900">Gogidix Sales</span>
            </div>
          </div>

          <Card>
            <CardHeader className="space-y-1">
              <CardTitle className="text-2xl">Sign in to your account</CardTitle>
              <CardDescription>
                Enter your email and password to access the Sales Dashboard
              </CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-4">
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

                {error && (
                  <div className="p-3 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md">
                    {error}
                  </div>
                )}

                <Button
                  type="submit"
                  className="w-full"
                  variant="sales"
                  disabled={isLoading}
                >
                  {isLoading ? 'Signing in...' : 'Sign in'}
                </Button>
              </form>

              <div className="mt-6">
                <div className="relative">
                  <div className="absolute inset-0 flex items-center">
                    <span className="w-full border-t" />
                  </div>
                  <div className="relative flex justify-center text-xs uppercase">
                    <span className="bg-white px-2 text-muted-foreground">
                      Demo Quick Login
                    </span>
                  </div>
                </div>

                <div className="mt-4 grid grid-cols-1 gap-2">
                  {quickLoginUsers.map((user) => (
                    <button
                      key={user.email}
                      type="button"
                      onClick={() => handleQuickLogin(user.email)}
                      className="flex items-center justify-between p-3 text-sm border rounded-md hover:bg-gray-50 transition-colors"
                    >
                      <span className="font-medium">{user.name}</span>
                      <span className="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded">
                        {user.role.replace(/_/g, ' ')}
                      </span>
                    </button>
                  ))}
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
