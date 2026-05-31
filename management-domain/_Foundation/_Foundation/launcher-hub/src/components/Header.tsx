interface HeaderProps {
  onRefresh: () => void
  onShowStatus: () => void
  runningCount: number
  totalCount: number
}

const Header = ({ onRefresh, onShowStatus, runningCount, totalCount }: HeaderProps) => {
  return (
    <header className="border-b border-slate-800 bg-slate-900/50 backdrop-blur-sm sticky top-0 z-40">
      <div className="container mx-auto px-6 py-4">
        <div className="flex items-center justify-between">
          {/* Logo & Title */}
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-gradient-to-br from-executive-blue to-executive-blue-dark rounded-xl flex items-center justify-center pulse-glow">
              <span className="text-2xl">🎯</span>
            </div>
            <div>
              <h1 className="text-2xl font-bold text-slate-100">
                Gogidix <span className="text-executive-gold">Management Domain</span>
              </h1>
              <p className="text-slate-400 text-sm">Unified Dashboard Launcher</p>
            </div>
          </div>

          {/* Status Bar */}
          <div className="flex items-center gap-6">
            {/* Running Services */}
            <div className="hidden md:flex items-center gap-3 bg-slate-800/50 px-4 py-2 rounded-lg">
              <div className="flex items-center gap-2">
                <div className="w-2 h-2 bg-emerald-400 rounded-full animate-pulse" />
                <span className="text-slate-300 font-medium">{runningCount}</span>
                <span className="text-slate-500 text-sm">Running</span>
              </div>
              <div className="w-px h-4 bg-slate-700" />
              <div className="text-slate-500 text-sm">
                of {totalCount} total
              </div>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-2">
              <button
                onClick={onRefresh}
                className="p-2.5 bg-slate-800 hover:bg-slate-700 rounded-lg transition-colors text-slate-300"
                title="Refresh Status"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
              </button>
              <button
                onClick={onShowStatus}
                className="px-4 py-2.5 bg-executive-blue hover:bg-executive-blue-dark text-white rounded-lg font-medium transition-colors flex items-center gap-2"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                <span className="hidden sm:inline">Status</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  )
}

export default Header
