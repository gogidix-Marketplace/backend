import { useState, useEffect } from 'react'
import { dashboards, categories, getDashboardsByCategory, type Dashboard } from './data/dashboards'
import DashboardCard from './components/DashboardCard'
import StatusPanel from './components/StatusPanel'
import Header from './components/Header'

function App() {
  const [selectedCategory, setSelectedCategory] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')
  const [filteredDashboards, setFilteredDashboards] = useState(dashboards)
  const [showStatusPanel, setShowStatusPanel] = useState(false)
  const [dashboardStatus, setDashboardStatus] = useState<Record<string, 'running' | 'stopped' | 'pending'>>({})

  useEffect(() => {
    let filtered = getDashboardsByCategory(selectedCategory)

    if (searchQuery) {
      const query = searchQuery.toLowerCase()
      filtered = filtered.filter(
        d =>
          d.name.toLowerCase().includes(query) ||
          d.description.toLowerCase().includes(query) ||
          d.features.some(f => f.toLowerCase().includes(query))
      )
    }

    setFilteredDashboards(filtered)
  }, [selectedCategory, searchQuery])

  const checkStatus = async () => {
    const statuses: Record<string, 'running' | 'stopped' | 'pending'> = {}
    for (const dashboard of dashboards) {
      try {
        const response = await fetch(`http://localhost:${dashboard.port}`, {
          method: 'HEAD',
          mode: 'no-cors',
        })
        statuses[dashboard.id] = 'running'
      } catch {
        statuses[dashboard.id] = 'stopped'
      }
    }
    setDashboardStatus(statuses)
    return statuses
  }

  useEffect(() => {
    checkStatus()
    const interval = setInterval(checkStatus, 5000)
    return () => clearInterval(interval)
  }, [])

  const launchDashboard = (dashboard: Dashboard) => {
    window.open(`http://localhost:${dashboard.port}`, '_blank')
  }

  const startDashboard = async (dashboard: Dashboard) => {
    setDashboardStatus(prev => ({ ...prev, [dashboard.id]: 'pending' }))
    // This would trigger the backend to start the dashboard
    // For now, we'll just open it
    setTimeout(() => {
      launchDashboard(dashboard)
    }, 1000)
  }

  const runningCount = Object.values(dashboardStatus).filter(s => s === 'running').length
  const totalCount = dashboards.length

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950">
      <Header
        onRefresh={checkStatus}
        onShowStatus={() => setShowStatusPanel(true)}
        runningCount={runningCount}
        totalCount={totalCount}
      />

      <main className="container mx-auto px-6 py-8">
        {/* Category Tabs */}
        <div className="flex flex-wrap gap-3 mb-8">
          {categories.map(cat => (
            <button
              key={cat.id}
              onClick={() => setSelectedCategory(cat.id)}
              className={`px-5 py-2.5 rounded-lg font-medium transition-all duration-200 flex items-center gap-2 ${
                selectedCategory === cat.id
                  ? 'bg-executive-blue text-white shadow-lg shadow-executive-blue/30'
                  : 'bg-slate-800 text-slate-400 hover:bg-slate-700 hover:text-slate-200'
              }`}
            >
              <span className="text-lg">{cat.icon}</span>
              {cat.name}
              {cat.id !== 'all' && (
                <span className="ml-1 text-xs opacity-70">
                  ({getDashboardsByCategory(cat.id).length})
                </span>
              )}
            </button>
          ))}
        </div>

        {/* Search Bar */}
        <div className="mb-8">
          <div className="relative">
            <input
              type="text"
              placeholder="Search dashboards, features, or descriptions..."
              value={searchQuery}
              onChange={e => setSearchQuery(e.target.value)}
              className="w-full bg-slate-800/50 border border-slate-700 rounded-xl px-5 py-4 pl-12 text-slate-200 placeholder-slate-500 focus:outline-none focus:border-executive-blue focus:ring-2 focus:ring-executive-blue/20 transition-all"
            />
            <svg
              className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-500"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
        </div>

        {/* Results Summary */}
        <div className="mb-6 text-slate-400">
          <span className="font-semibold text-slate-200">{filteredDashboards.length}</span> dashboard
          {filteredDashboards.length !== 1 ? 's' : ''} found
          {selectedCategory !== 'all' && (
            <span> in <span className="text-executive-gold font-medium">{categories.find(c => c.id === selectedCategory)?.name}</span></span>
          )}
        </div>

        {/* Dashboard Grid */}
        {filteredDashboards.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredDashboards.map((dashboard, index) => (
              <DashboardCard
                key={dashboard.id}
                dashboard={dashboard}
                status={dashboardStatus[dashboard.id] || 'stopped'}
                onLaunch={() => launchDashboard(dashboard)}
                onStart={() => startDashboard(dashboard)}
                style={{ animationDelay: `${index * 50}ms` }}
              />
            ))}
          </div>
        ) : (
          <div className="text-center py-20">
            <div className="text-6xl mb-4">🔍</div>
            <h3 className="text-xl font-semibold text-slate-300 mb-2">No dashboards found</h3>
            <p className="text-slate-500">Try adjusting your search or category filter</p>
          </div>
        )}

        {/* Quick Actions */}
        <div className="mt-12 p-6 bg-slate-900/50 border border-slate-800 rounded-xl">
          <h3 className="text-lg font-semibold text-slate-200 mb-4">Quick Actions</h3>
          <div className="flex flex-wrap gap-4">
            <button className="btn-action" onClick={() => {
              const running = dashboards.filter(d => dashboardStatus[d.id] === 'running')
              running.forEach(d => window.open(`http://localhost:${d.port}`, '_blank'))
            }}>
              <span>🚀</span> Open All Running
            </button>
            <button className="btn-secondary" onClick={checkStatus}>
              <span>🔄</span> Refresh Status
            </button>
            <button className="btn-secondary" onClick={() => setShowStatusPanel(true)}>
              <span>📊</span> View Status Panel
            </button>
          </div>
        </div>
      </main>

      {/* Status Panel */}
      {showStatusPanel && (
        <StatusPanel
          dashboards={dashboards}
          status={dashboardStatus}
          onClose={() => setShowStatusPanel(false)}
          onRefresh={checkStatus}
        />
      )}

      {/* Footer */}
      <footer className="border-t border-slate-800 mt-16 py-8">
        <div className="container mx-auto px-6 text-center text-slate-500 text-sm">
          <p>Gogidix Management Domain Launcher v1.0.0</p>
          <p className="mt-1">
            Running locally on ports 3000-3015 |{' '}
            <a href="#" className="text-executive-blue hover:text-executive-blue-light transition-colors">
              Documentation
            </a>
          </p>
        </div>
      </footer>
    </div>
  )
}

export default App
