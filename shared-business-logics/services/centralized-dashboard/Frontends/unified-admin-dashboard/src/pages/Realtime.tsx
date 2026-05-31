import { useState, useEffect, useRef } from 'react'
import { useQuery } from '@tanstack/react-query'
import { websocketApi } from '@/lib/api'
import wsService from '@/lib/websocket'

interface WSMessage {
  type: string
  topic?: string
  data?: unknown
  timestamp: string
}

export default function Realtime() {
  const [messages, setMessages] = useState<WSMessage[]>([])
  const [connected, setConnected] = useState(false)
  const [selectedTopic, setSelectedTopic] = useState<string>('all')
  const messagesEndRef = useRef<HTMLDivElement>(null)

  const { data: stats } = useQuery({
    queryKey: ['ws-stats'],
    queryFn: websocketApi.getStatistics,
    refetchInterval: 5000,
  })

  const topics = ['dashboard-updates', 'saga-events', 'chart-updates', 'monitoring-alerts']

  useEffect(() => {
    wsService.connect()

    const unsubscribeConnection = wsService.onConnectionChange((isConnected) => {
      setConnected(isConnected)
    })

    const unsubscribeMessage = wsService.onMessage((message) => {
      const wsMessage = message as WSMessage
      setMessages((prev) => {
        const newMessages = [...prev, wsMessage]
        // Keep only last 100 messages
        if (newMessages.length > 100) {
          return newMessages.slice(-100)
        }
        return newMessages
      })
    })

    // Subscribe to all topics
    topics.forEach((topic) => wsService.subscribe(topic))

    return () => {
      unsubscribeConnection()
      unsubscribeMessage()
      wsService.disconnect()
    }
  }, [])

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const filteredMessages =
    selectedTopic === 'all'
      ? messages
      : messages.filter((msg) => msg.topic === selectedTopic || msg.type.includes(selectedTopic))

  const handleSendMessage = () => {
    websocketApi.broadcast({ test: 'message', timestamp: new Date().toISOString() })
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-secondary-900">Real-time Updates</h1>
        <p className="text-secondary-600">Live WebSocket feed and connection status</p>
      </div>

      {/* Connection Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="card">
          <div className="flex items-center gap-3">
            <span className={`h-3 w-3 rounded-full ${connected ? 'bg-green-500' : 'bg-red-500'}`} />
            <div>
              <p className="text-sm font-medium text-secondary-600">Status</p>
              <p className="text-lg font-semibold">{connected ? 'Connected' : 'Disconnected'}</p>
            </div>
          </div>
        </div>
        <div className="card">
          <p className="text-sm font-medium text-secondary-600">Connections</p>
          <p className="text-lg font-semibold">{stats?.totalConnections || 0}</p>
        </div>
        <div className="card">
          <p className="text-sm font-medium text-secondary-600">Tenants</p>
          <p className="text-lg font-semibold">{stats?.tenants || 0}</p>
        </div>
        <div className="card">
          <p className="text-sm font-medium text-secondary-600">Topics</p>
          <p className="text-lg font-semibold">{stats?.topics || 0}</p>
        </div>
      </div>

      {/* Topic Filter */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold">Filter by Topic</h3>
          <button onClick={handleSendMessage} className="btn-primary">
            Send Test Message
          </button>
        </div>
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => setSelectedTopic('all')}
            className={`px-4 py-2 rounded-lg ${
              selectedTopic === 'all'
                ? 'bg-primary-600 text-white'
                : 'bg-secondary-200 text-secondary-700'
            }`}
          >
            All Topics
          </button>
          {topics.map((topic) => (
            <button
              key={topic}
              onClick={() => setSelectedTopic(topic)}
              className={`px-4 py-2 rounded-lg ${
                selectedTopic === topic
                  ? 'bg-primary-600 text-white'
                  : 'bg-secondary-200 text-secondary-700'
              }`}
            >
              {topic}
            </button>
          ))}
        </div>
      </div>

      {/* Messages */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold">Message Feed</h3>
          <span className="text-sm text-secondary-500">
            {filteredMessages.length} messages
          </span>
        </div>
        <div className="bg-secondary-900 rounded-lg p-4 h-96 overflow-y-auto font-mono text-sm">
          {filteredMessages.length === 0 ? (
            <p className="text-secondary-500">Waiting for messages...</p>
          ) : (
            <>
              {filteredMessages.map((msg, idx) => (
                <div key={idx} className="mb-2 text-green-400">
                  <span className="text-secondary-500">
                    [{new Date(msg.timestamp).toLocaleTimeString()}]
                  </span>{' '}
                  <span className="text-yellow-400">{msg.type}</span>
                  {msg.topic && <span className="text-blue-400"> @{msg.topic}</span>}
                  {msg.data && (
                    <pre className="ml-4 text-secondary-300">{JSON.stringify(msg.data, null, 2)}</pre>
                  )}
                </div>
              ))}
              <div ref={messagesEndRef} />
            </>
          )}
        </div>
      </div>
    </div>
  )
}
