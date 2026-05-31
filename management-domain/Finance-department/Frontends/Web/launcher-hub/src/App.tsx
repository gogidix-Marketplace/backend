import { useState, useEffect } from 'react'

const API_BASE = 'http://localhost:2999/api'

interface Dashboard {
  id: string
  name: string
  description: string
  icon: string
  port: number
  url: string
  status: 'online' | 'offline' | 'loading' | 'starting'
}

interface Department {
  id: string
  name: string
  shortName: string
  icon: string
  color: string
  dashboards: Dashboard[]
}

const initialDepartments: Department[] = [
  {
    id: 'executive',
    name: 'Executive Office',
    shortName: 'Executive',
    icon: '👔',
    color: 'purple',
    dashboards: [
      { id: 'ceo-dashboard', name: 'CEO Dashboard', description: 'Executive overview for CEO with company-wide KPIs', icon: '🎯', port: 3010, url: 'http://localhost:3010', status: 'offline' },
      { id: 'executive-dashboard', name: 'C-Suite Hub', description: 'Unified dashboard for C-level executives', icon: '🏢', port: 3011, url: 'http://localhost:3011', status: 'offline' }
    ]
  },
  {
    id: 'finance',
    name: 'Finance Department',
    shortName: 'Finance',
    icon: '💰',
    color: 'blue',
    dashboards: [
      { id: 'finance-hub', name: 'Finance Hub', description: 'Main financial dashboard with overview', icon: '🏦', port: 3020, url: 'http://localhost:3020', status: 'offline' },
      { id: 'accountant-dashboard', name: 'Accountant Workspace', description: 'Daily transactions and bookkeeping', icon: '📒', port: 3021, url: 'http://localhost:3021', status: 'offline' },
      { id: 'cfo-dashboard', name: 'CFO Executive', description: 'Strategic KPIs and executive insights', icon: '📊', port: 3022, url: 'http://localhost:3022', status: 'offline' },
      { id: 'reports-dashboard', name: 'Financial Reports', description: 'Generate comprehensive reports', icon: '📋', port: 3023, url: 'http://localhost:3023', status: 'offline' },
      { id: 'finance-portal', name: 'Finance Portal', description: 'Client and partner portal', icon: '🌐', port: 3024, url: 'http://localhost:3024', status: 'offline' }
    ]
  },
  {
    id: 'hr',
    name: 'Human Resources',
    shortName: 'HR',
    icon: '👥',
    color: 'pink',
    dashboards: [
      { id: 'hr-dashboard', name: 'HR Dashboard', description: 'Employee management and operations', icon: '👤', port: 3030, url: 'http://localhost:3030', status: 'offline' },
      { id: 'hr-portal', name: 'HR Portal', description: 'Employee self-service portal', icon: '🔐', port: 3031, url: 'http://localhost:3031', status: 'offline' }
    ]
  },
  {
    id: 'sales',
    name: 'Sales Department',
    shortName: 'Sales',
    icon: '📈',
    color: 'emerald',
    dashboards: [
      { id: 'sales-dashboard', name: 'Sales Dashboard', description: 'Sales pipeline and analytics', icon: '💹', port: 3040, url: 'http://localhost:3040', status: 'offline' },
      { id: 'sales-portal', name: 'Sales Portal', description: 'Customer sales portal', icon: '🛒', port: 3041, url: 'http://localhost:3041', status: 'offline' }
    ]
  },
  {
    id: 'marketing',
    name: 'Digital Marketing',
    shortName: 'Marketing',
    icon: '📣',
    color: 'orange',
    dashboards: [
      { id: 'global-marketing', name: 'Global Marketing', description: 'Worldwide campaigns and analytics', icon: '🌍', port: 3050, url: 'http://localhost:3050', status: 'offline' },
      { id: 'country-marketing', name: 'Country Marketing', description: 'Regional marketing operations', icon: '🏳️', port: 3051, url: 'http://localhost:3051', status: 'offline' },
      { id: 'marketing-portal', name: 'Marketing Portal', description: 'Public marketing content portal', icon: '🎨', port: 3052, url: 'http://localhost:3052', status: 'offline' },
      { id: 'corporate-admin', name: 'Corporate Admin', description: 'Corporate website management', icon: '⚙️', port: 3053, url: 'http://localhost:3053', status: 'offline' }
    ]
  },
  {
    id: 'support',
    name: 'Customer Support',
    shortName: 'Support',
    icon: '🎧',
    color: 'cyan',
    dashboards: [
      { id: 'support-dashboard', name: 'Support Dashboard', description: 'Ticket management and analytics', icon: '🎫', port: 3060, url: 'http://localhost:3060', status: 'offline' },
      { id: 'support-portal', name: 'Support Portal', description: 'Customer help desk portal', icon: '❓', port: 3061, url: 'http://localhost:3061', status: 'offline' }
    ]
  }
]

const departmentColors: any = {
  purple: { gradient: 'from-purple-500/20 to-pink-500/20 border-purple-500/30', accent: 'bg-purple-600 hover:bg-purple-700', text: 'text-purple-400' },
  blue: { gradient: 'from-blue-500/20 to-cyan-500/20 border-blue-500/30', accent: 'bg-blue-600 hover:bg-blue-700', text: 'text-blue-400' },
  pink: { gradient: 'from-pink-500/20 to-rose-500/20 border-pink-500/30', accent: 'bg-pink-600 hover:bg-pink-700', text: 'text-pink-400' },
  emerald: { gradient: 'from-emerald-500/20 to-green-500/20 border-emerald-500/30', accent: 'bg-emerald-600 hover:bg-emerald-700', text: 'text-emerald-400' },
  orange: { gradient: 'from-orange-500/20 to-amber-500/20 border-orange-500/30', accent: 'bg-orange-600 hover:bg-orange-700', text: 'text-orange-400' },
  cyan: { gradient: 'from-cyan-500/20 to-teal-500/20 border-cyan-500/30', accent: 'bg-cyan-600 hover:bg-cyan-700', text: 'text-cyan-400' }
}

function App() {
  const [departments, setDepartments] = useState<Department[]>(initialDepartments)
  const [serviceOnline, setServiceOnline] = useState(false)
  const [isStartingAll, setIsStartingAll] = useState(false)
  const [isStoppingAll, setIsStoppingAll] = useState(false)

  // Check service status
  useEffect(() => {
    const checkService = async () => {
      try {
        const res = await fetch(API_BASE + '/status')
        if (res.ok) {
          setServiceOnline(true)
        }
      } catch {
        setServiceOnline(false)
      }
    }
    checkService()
    const interval = setInterval(checkService, 5000)
    return () => clearInterval(interval)
  }, [])

  const startDashboard = async (dashboardId: string) => {
    if (!serviceOnline) {
      alert('Launcher service is not running. Start it with: cd launcher-service && npm start')
      return
    }
    setDepartments(prev => prev.map(dept => ({
      ...dept,
      dashboards: dept.dashboards.map((d: any) =>
        d.id === dashboardId ? { ...d, status: 'starting' } : d
      )
    })))
    try {
      await fetch(API_BASE + '/start/' + dashboardId, { method: 'POST' })
      setTimeout(() => {
        setDepartments((prev: any) => prev.map((dept: any) => ({
          ...dept,
          dashboards: dept.dashboards.map((d: any) =>
            d.id === dashboardId ? { ...d, status: 'online' } : d
          )
        })))
      }, 3000)
    } catch {
      // Error handling
    }
  }

  const startAllDashboards = async () => {
    if (!serviceOnline) {
      alert('Launcher service is not running')
      return
    }
    setIsStartingAll(true)
    try {
      await fetch(API_BASE + '/start-all', { method: 'POST' })
      // Update statuses after delay
      setTimeout(() => {
        setDepartments((prev: any) => prev.map((dept: any) => ({
          ...dept,
          dashboards: dept.dashboards.map((d: any) => ({ ...d, status: 'online' }))
        })))
      }, 5000)
    } catch {
      // Error handling
    }
    setIsStartingAll(false)
  }

  const stopAllDashboards = async () => {
    if (!serviceOnline) return
    setIsStoppingAll(true)
    try {
      await fetch(API_BASE + '/stop-all', { method: 'POST' })
      setDepartments((prev: any) => prev.map((dept: any) => ({
        ...dept,
        dashboards: dept.dashboards.map((d: any) => ({ ...d, status: 'offline' }))
      })))
    } catch {
      // Error handling
    }
    setIsStoppingAll(false)
  }

  const checkStatus = async (dashboard: Dashboard) => {
    try {
      await fetch(dashboard.url, { method: 'HEAD', mode: 'no-cors' })
      setDepartments((prev: any) => prev.map((dept: any) => ({
        ...dept,
        dashboards: dept.dashboards.map((d: any) =>
          d.id === dashboard.id ? { ...d, status: 'online' } : d
        )
      })))
    } catch {
      setDepartments((prev: any) => prev.map((dept: any) => ({
        ...dept,
        dashboards: dept.dashboards.map((d: any) =>
          d.id === dashboard.id ? { ...d, status: 'offline' } : d
        )
      })))
    }
  }

  const checkAllStatuses = () => {
    departments.forEach((dept: any) => {
      dept.dashboards.forEach((dash: any) => {
        checkStatus(dash)
      })
    })
  }

  const launchDashboard = (dashboard: Dashboard) => {
    window.open(dashboard.url, '_blank')
  }

  const allDashboards = departments.flatMap((d: any) => d.dashboards)
  const onlineCount = allDashboards.filter((d: any) => d.status === 'online').length
  const totalCount = allDashboards.length

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900">
      <header className="border-b border-slate-700/50 bg-slate-900/80 backdrop-blur-sm sticky top-0 z-10 shadow-lg">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-4">
              <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-blue-500 via-purple-500 to-pink-500 flex items-center justify-center text-2xl font-bold shadow-lg">
                G
              </div>
              <div>
                <h1 className="text-2xl font-bold text-white">Gogidix Ecosystem</h1>
                <p className="text-sm text-slate-400 flex items-center gap-2">
                  Unified Application Launcher
                  <span className={'px-2 py-0.5 rounded text-xs ' + (serviceOnline ? 'bg-emerald-500/20 text-emerald-400' : 'bg-red-500/20 text-red-400')}>
                    {serviceOnline ? '● Service Online' : '○ Service Offline'}
                  </span>
                </p>
              </div>
            </div>
            <div className="flex gap-2">
              <button
                onClick={startAllDashboards}
                disabled={isStartingAll || !serviceOnline}
                className="px-5 py-2.5 bg-gradient-to-r from-emerald-600 to-green-600 hover:from-emerald-700 hover:to-green-700 disabled:from-slate-600 disabled:to-slate-700 text-white rounded-lg transition-all flex items-center gap-2 font-medium shadow-lg disabled:opacity-50"
              >
                {isStartingAll ? <span className="animate-spin">⏳</span> : <span>▶️</span>}
                {isStartingAll ? 'Starting...' : 'Start All'}
              </button>
              <button
                onClick={stopAllDashboards}
                disabled={isStoppingAll || !serviceOnline}
                className="px-5 py-2.5 bg-gradient-to-r from-red-600 to-rose-600 hover:from-red-700 hover:to-rose-700 disabled:from-slate-600 disabled:to-slate-700 text-white rounded-lg transition-all flex items-center gap-2 font-medium shadow-lg disabled:opacity-50"
              >
                {isStoppingAll ? <span className="animate-spin">⏳</span> : <span>⏹️</span>}
                {isStoppingAll ? 'Stopping...' : 'Stop All'}
              </button>
              <button
                onClick={checkAllStatuses}
                className="px-5 py-2.5 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white rounded-lg transition-all flex items-center gap-2 font-medium shadow-lg"
              >
                <span>🔄</span> Refresh
              </button>
            </div>
          </div>

          <div className="grid grid-cols-4 gap-3">
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-lg p-3 border border-slate-700/50 text-center">
              <div className="text-2xl font-bold text-white">{totalCount}</div>
              <div className="text-xs text-slate-400">Total Apps</div>
            </div>
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-lg p-3 border border-slate-700/50 text-center">
              <div className="text-2xl font-bold text-emerald-400">{onlineCount}</div>
              <div className="text-xs text-slate-400">Running</div>
            </div>
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-lg p-3 border border-slate-700/50 text-center">
              <div className="text-2xl font-bold text-red-400">{totalCount - onlineCount}</div>
              <div className="text-xs text-slate-400">Stopped</div>
            </div>
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-lg p-3 border border-slate-700/50 text-center">
              <div className="text-2xl font-bold text-blue-400">{Math.round(onlineCount / totalCount * 100)}%</div>
              <div className="text-xs text-slate-400">Active</div>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        {!serviceOnline && (
          <div className="mb-8 bg-amber-500/10 border border-amber-500/30 rounded-xl p-6">
            <h3 className="text-lg font-semibold text-amber-400 mb-2 flex items-center gap-2">
              <span>⚠️</span> Launcher Service Offline
            </h3>
            <p className="text-slate-400 text-sm mb-4">
              To control dashboards from this launcher, start the launcher service first:
            </p>
            <code className="block bg-slate-800 p-3 rounded text-sm text-slate-300">
              cd launcher-service && npm start
            </code>
          </div>
        )}

        {departments.map((department: any) => {
          const colors = departmentColors[department.color]
          const deptOnlineCount = department.dashboards.filter((d: any) => d.status === 'online').length

          return (
            <div key={department.id} className="mb-10">
              <div className={'flex items-center justify-between mb-6 p-5 bg-gradient-to-r ' + colors.gradient + ' backdrop-blur-sm rounded-xl border shadow-lg'}>
                <div className="flex items-center gap-4">
                  <div className={'w-16 h-16 rounded-xl ' + colors.accent + ' flex items-center justify-center text-3xl shadow-lg'}>
                    {department.icon}
                  </div>
                  <div>
                    <h2 className="text-2xl font-bold text-white">{department.name}</h2>
                    <p className="text-sm text-slate-400">
                      {deptOnlineCount} of {department.dashboards.length} dashboards running
                    </p>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                {department.dashboards.map((dashboard: any) => {
                  const status = dashboard.status

                  return (
                    <div
                      key={dashboard.id}
                      className={'group relative bg-gradient-to-br ' + colors.gradient + ' backdrop-blur-sm rounded-xl p-5 border transition-all hover:scale-[1.02] hover:shadow-xl'}
                    >
                      <div className="absolute top-3 right-3">
                        <div className={'flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ' +
                          (status === 'online' ? 'bg-emerald-500/20 text-emerald-400' :
                           status === 'starting' ? 'bg-yellow-500/20 text-yellow-400' :
                           'bg-red-500/20 text-red-400')}>
                          <span className={'w-1.5 h-1.5 rounded-full ' +
                            (status === 'online' ? 'bg-emerald-400 animate-pulse' :
                             status === 'starting' ? 'bg-yellow-400 animate-spin' :
                             'bg-red-400')}></span>
                          {status === 'online' ? 'Running' : status === 'starting' ? 'Starting...' : 'Stopped'}
                        </div>
                      </div>

                      <div className="w-12 h-12 rounded-xl bg-slate-900/50 flex items-center justify-center text-2xl mb-3">
                        {dashboard.icon}
                      </div>

                      <h3 className="text-base font-bold text-white mb-1">{dashboard.name}</h3>
                      <p className="text-xs text-slate-400 mb-3 line-clamp-2">{dashboard.description}</p>

                      <div className={'text-xs ' + colors.text + ' mb-3 font-mono'}>
                        :{dashboard.port}
                      </div>

                      <div className="flex gap-2">
                        {status === 'online' ? (
                          <>
                            <button
                              onClick={() => launchDashboard(dashboard)}
                              className={'flex-1 py-2 rounded-lg text-xs font-medium ' + colors.accent + ' text-white'}
                            >
                              Open
                            </button>
                            <button
                              onClick={() => checkStatus(dashboard)}
                              className="px-3 py-2 bg-slate-700/50 hover:bg-slate-700 text-slate-300 rounded-lg"
                            >
                              🔍
                            </button>
                          </>
                        ) : (
                          <>
                            <button
                              onClick={() => startDashboard(dashboard.id)}
                              disabled={status === 'starting' || !serviceOnline}
                              className={'flex-1 py-2 rounded-lg text-xs font-medium transition-colors ' +
                                (status === 'starting' || !serviceOnline
                                  ? 'bg-slate-700/50 text-slate-500 cursor-not-allowed'
                                  : 'bg-emerald-600 hover:bg-emerald-700 text-white')}
                            >
                              {status === 'starting' ? '...' : 'Start'}
                            </button>
                            <button
                              onClick={() => checkStatus(dashboard)}
                              className="px-3 py-2 bg-slate-700/50 hover:bg-slate-700 text-slate-300 rounded-lg"
                            >
                              🔍
                            </button>
                          </>
                        )}
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
          )
        })}

        <div className="bg-gradient-to-r from-blue-500/10 via-purple-500/10 to-pink-500/10 backdrop-blur-sm rounded-2xl p-6 border border-blue-500/30">
          <h3 className="text-lg font-semibold text-white mb-4 flex items-center gap-2">
            <span>🚀</span> Quick Start
          </h3>
          <div className="grid md:grid-cols-2 gap-6 text-sm">
            <div>
              <p className="text-emerald-400 font-medium mb-2">Option 1: Use this launcher</p>
              <ol className="text-slate-400 space-y-1 list-decimal list-inside">
                <li>Start the launcher service: <code className="text-slate-300">cd launcher-service && npm start</code></li>
                <li>Click "Start All" button above</li>
                <li>Wait for dashboards to start</li>
                <li>Click "Open" on any dashboard</li>
              </ol>
            </div>
            <div>
              <p className="text-blue-400 font-medium mb-2">Option 2: Start manually</p>
              <ol className="text-slate-400 space-y-1 list-decimal list-inside">
                <li>Open terminal for each dashboard</li>
                <li>Navigate to dashboard folder</li>
                <li>Run: <code className="text-slate-300">npm run dev</code></li>
              </ol>
            </div>
          </div>
        </div>
      </main>

      <footer className="border-t border-slate-700/50 mt-12 py-6 text-center text-slate-500 text-sm">
        <p>Gogidix Ecosystem Launcher v2.0.0 • Full React Applications</p>
      </footer>
    </div>
  )
}

export default App
