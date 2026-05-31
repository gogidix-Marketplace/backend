import { type Dashboard } from '../data/dashboards'

interface StatusPanelProps {
  dashboards: Dashboard[]
  status: Record<string, 'running' | 'stopped' | 'pending'>
  onClose: () => void
  onRefresh: () => void
}

const StatusPanel = ({ dashboards, status, onClose, onRefresh }: StatusPanelProps) => {
  const running = dashboards.filter(d => status[d.id] === 'running')
  const stopped = dashboards.filter(d => status[d.id] === 'stopped')
  const pending = dashboards.filter(d => status[d.id] === 'pending')

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4" onClick={onClose}>
      <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-4xl max-h-[80vh] overflow-hidden" onClick={e => e.stopPropagation()}>
        {/* Header */}
        <div className="p-6 border-b border-slate-800 flex items-center justify-between">
          <div>
            <h2 className="text-xl font-semibold text-slate-100">Dashboard Status</h2>
            <p className="text-slate-400 text-sm mt-1">
              {running.length} running, {stopped.length} stopped, {pending.length} pending
            </p>
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={onRefresh}
              className="p-2 bg-slate-800 hover:bg-slate-700 rounded-lg transition-colors text-slate-300"
              title="Refresh"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
            </button>
            <button
              onClick={onClose}
              className="p-2 bg-slate-800 hover:bg-slate-700 rounded-lg transition-colors text-slate-300"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="p-6 overflow-y-auto max-h-[60vh]">
          <div className="space-y-6">
            {/* Running */}
            {running.length > 0 && (
              <div>
                <h3 className="text-sm font-semibold text-emerald-400 uppercase tracking-wider mb-3 flex items-center gap-2">
                  <span className="w-2 h-2 bg-emerald-400 rounded-full animate-pulse" />
                  Running ({running.length})
                </h3>
                <div className="space-y-2">
                  {running.map(d => (
                    <div
                      key={d.id}
                      className="flex items-center justify-between p-3 bg-slate-800/50 rounded-lg hover:bg-slate-800 transition-colors"
                    >
                      <div className="flex items-center gap-3">
                        <span className="text-xl">{d.icon}</span>
                        <div>
                          <div className="text-slate-200 font-medium">{d.name}</div>
                          <div className="text-slate-500 text-xs">Port {d.port}</div>
                        </div>
                      </div>
                      <a
                        href={`http://localhost:${d.port}`}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="px-3 py-1.5 bg-emerald-600 hover:bg-emerald-500 text-white text-sm font-medium rounded-lg transition-colors"
                      >
                        Open
                      </a>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Stopped */}
            {stopped.length > 0 && (
              <div>
                <h3 className="text-sm font-semibold text-slate-500 uppercase tracking-wider mb-3 flex items-center gap-2">
                  <span className="w-2 h-2 bg-slate-500 rounded-full" />
                  Stopped ({stopped.length})
                </h3>
                <div className="space-y-2">
                  {stopped.map(d => (
                    <div
                      key={d.id}
                      className="flex items-center justify-between p-3 bg-slate-800/30 rounded-lg"
                    >
                      <div className="flex items-center gap-3">
                        <span className="text-xl opacity-50">{d.icon}</span>
                        <div>
                          <div className="text-slate-400 font-medium">{d.name}</div>
                          <div className="text-slate-600 text-xs">Port {d.port}</div>
                        </div>
                      </div>
                      <span className="text-slate-600 text-sm">Stopped</span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Pending */}
            {pending.length > 0 && (
              <div>
                <h3 className="text-sm font-semibold text-amber-400 uppercase tracking-wider mb-3 flex items-center gap-2">
                  <div className="w-2 h-2 border-2 border-amber-400 border-t-transparent rounded-full animate-spin" />
                  Starting ({pending.length})
                </h3>
                <div className="space-y-2">
                  {pending.map(d => (
                    <div
                      key={d.id}
                      className="flex items-center justify-between p-3 bg-slate-800/30 rounded-lg"
                    >
                      <div className="flex items-center gap-3">
                        <span className="text-xl">{d.icon}</span>
                        <div>
                          <div className="text-slate-300 font-medium">{d.name}</div>
                          <div className="text-slate-500 text-xs">Port {d.port}</div>
                        </div>
                      </div>
                      <div className="flex items-center gap-2 text-amber-400 text-sm">
                        <div className="w-3 h-3 border-2 border-current border-t-transparent rounded-full animate-spin" />
                        Starting...
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

export default StatusPanel
