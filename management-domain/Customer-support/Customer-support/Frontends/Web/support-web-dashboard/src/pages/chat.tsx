import { useState } from 'react'
import {
  Send,
  Phone,
  MoreVertical,
  User,
  Clock,
  Smile,
  Paperclip,
  Video,
  Monitor,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Textarea } from '@shared/components/ui/textarea'
import { Avatar, AvatarFallback, AvatarImage } from '@shared/components/ui/avatar'
import { cn, formatDateTime, formatDuration } from '@shared/utils/cn'
import { mockChatSessions, mockAgents } from '@shared/data/mockData'

interface Message {
  id: string
  content: string
  sender: 'agent' | 'customer' | 'system'
  senderName: string
  timestamp: string
  isRead: boolean
  attachments?: Array<{ name: string; url: string; type: string }>
}

export default function ChatPage() {
  const [selectedSession, setSelectedSession] = useState(mockChatSessions[0])
  const [messageInput, setMessageInput] = useState('')
  const [showCannedResponses, setShowCannedResponses] = useState(false)

  const activeSessions = mockChatSessions.filter(s => s.status === 'active')
  const waitingSessions = mockChatSessions.filter(s => s.status === 'waiting')

  const messages: Message[] = selectedSession?.messages.map(m => ({
    id: m.id,
    content: m.content,
    sender: m.senderType === 'agent' ? 'agent' : m.senderType === 'system' ? 'system' : 'customer',
    senderName: m.senderName,
    timestamp: m.sentAt,
    isRead: m.isRead,
  })) || []

  const cannedResponses = [
    { label: 'Greeting', text: 'Hello! Thank you for contacting Gogidix Support. How can I help you today?' },
    { label: 'Hold', text: 'Thank you for your patience. I\'m looking into this for you right now.' },
    { label: 'Resolution', text: 'Great! I\'m glad we could resolve this for you. Is there anything else I can help with?' },
    { label: 'Escalation', text: 'I\'m going to escalate this to our specialist team who can better assist you.' },
    { label: 'Closing', text: 'Thank you for contacting Gogidix Support. Have a great day!' },
  ]

  const handleSendMessage = () => {
    if (!messageInput.trim()) return
    // In real app, this would send the message
    console.log('Sending message:', messageInput)
    setMessageInput('')
  }

  const handleJoinQueue = (sessionId: string) => {
    const session = mockChatSessions.find(s => s.id === sessionId)
    if (session) {
      setSelectedSession({
        ...session,
        status: 'active',
        agentId: 'agt-us-001',
        agentName: 'Emily Chen',
      })
    }
  }

  return (
    <div className="h-[calc(100vh-180px)]">
      <div className="page-header mb-4">
        <h1 className="page-title">Live Chat</h1>
        <p className="page-description">
          Real-time chat support with customers
        </p>
      </div>

      <div className="grid gap-4 lg:grid-cols-4 h-[calc(100%-60px)]">
        {/* Sidebar - Queue */}
        <Card className="lg:col-span-1 flex flex-col">
          <CardHeader className="pb-3">
            <CardTitle className="text-base">Chat Queue</CardTitle>
            <CardDescription className="flex items-center justify-between">
              <span>Active Chats</span>
              <Badge variant="success">{activeSessions.length}</Badge>
            </CardDescription>
          </CardHeader>
          <CardContent className="flex-1 overflow-y-auto p-2 space-y-2">
            {/* Active Sessions */}
            <div className="space-y-2">
              <p className="text-xs font-medium text-muted-foreground px-2">ACTIVE</p>
              {activeSessions.map((session) => (
                <div
                  key={session.id}
                  className={cn(
                    'flex items-center gap-3 rounded-lg p-3 cursor-pointer transition-colors',
                    selectedSession?.id === session.id
                      ? 'bg-[#1976D2] text-white'
                      : 'bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700'
                  )}
                  onClick={() => setSelectedSession(session)}
                >
                  <div className="relative">
                    <Avatar className="h-10 w-10">
                      <AvatarFallback className={selectedSession?.id === session.id ? 'bg-white/20' : ''}>
                        {session.customerName.split(' ').map(n => n[0]).join('')}
                      </AvatarFallback>
                    </Avatar>
                    <span className="absolute bottom-0 right-0 h-3 w-3 rounded-full border-2 border-white bg-green-500" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="font-medium truncate">{session.customerName}</p>
                    <p className="text-xs opacity-70 truncate">
                      {session.messages[session.messages.length - 1]?.content.substring(0, 30)}...
                    </p>
                  </div>
                  <Badge variant="outline" className={selectedSession?.id === session.id ? 'bg-white/20' : ''}>
                    {session.country}
                  </Badge>
                </div>
              ))}
            </div>

            {/* Waiting Sessions */}
            <div className="space-y-2">
              <p className="text-xs font-medium text-muted-foreground px-2">WAITING QUEUE</p>
              {waitingSessions.map((session) => (
                <div
                  key={session.id}
                  className="flex items-center gap-3 rounded-lg bg-yellow-50 dark:bg-yellow-900/20 border border-yellow-200 dark:border-yellow-800 p-3 cursor-pointer hover:bg-yellow-100 dark:hover:bg-yellow-900/30"
                  onClick={() => handleJoinQueue(session.id)}
                >
                  <div className="relative">
                    <Avatar className="h-10 w-10">
                      <AvatarFallback>
                        {session.customerName.split(' ').map(n => n[0]).join('')}
                      </AvatarFallback>
                    </Avatar>
                    <span className="absolute bottom-0 right-0 h-3 w-3 rounded-full border-2 border-white bg-yellow-500" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="font-medium truncate">{session.customerName}</p>
                    <p className="text-xs text-muted-foreground">
                      Waiting {formatDuration(Math.floor((Date.now() - new Date(session.startedAt).getTime()) / 60000))}
                    </p>
                  </div>
                  <Badge variant="warning">Join</Badge>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Main Chat Area */}
        <Card className="lg:col-span-3 flex flex-col">
          {/* Chat Header */}
          {selectedSession && (
            <CardHeader className="pb-3 border-b">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <Avatar>
                    <AvatarFallback>
                      {selectedSession.customerName.split(' ').map(n => n[0]).join('')}
                    </AvatarFallback>
                  </Avatar>
                  <div>
                    <CardTitle className="text-base">{selectedSession.customerName}</CardTitle>
                    <CardDescription className="flex items-center gap-2">
                      <span>{selectedSession.country}</span>
                      <span>•</span>
                      <span>Started {formatDateTime(selectedSession.startedAt)}</span>
                      {selectedSession.status === 'active' && (
                        <>
                          <span>•</span>
                          <span className="text-green-600">Active for {formatDuration(45)}</span>
                        </>
                      )}
                    </CardDescription>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <Button variant="outline" size="icon" title="View Profile">
                    <User className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="icon" title="Voice Call">
                    <Phone className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="icon" title="Video Call">
                    <Video className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="icon" title="Screen Share">
                    <Monitor className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="icon">
                    <MoreVertical className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            </CardHeader>
          )}

          {/* Messages */}
          {selectedSession && (
            <CardContent className="flex-1 overflow-y-auto p-4 space-y-4">
              {messages.map((message) => (
                <div
                  key={message.id}
                  className={cn(
                    'flex gap-3',
                    message.sender === 'agent' ? 'justify-end' : 'justify-start'
                  )}
                >
                  {message.sender === 'customer' && (
                    <Avatar className="h-8 w-8">
                      <AvatarFallback className="text-xs">
                        {selectedSession.customerName.split(' ').map(n => n[0]).join('')}
                      </AvatarFallback>
                    </Avatar>
                  )}
                  <div
                    className={cn(
                      'max-w-[70%] rounded-lg px-4 py-2',
                      message.sender === 'agent'
                        ? 'bg-[#1976D2] text-white'
                        : message.sender === 'system'
                        ? 'bg-slate-100 dark:bg-slate-800 text-muted-foreground text-center text-sm'
                        : 'bg-slate-100 dark:bg-slate-800'
                    )}
                  >
                    {message.sender === 'agent' && (
                      <p className="text-xs opacity-70 mb-1">You</p>
                    )}
                    <p className="text-sm">{message.content}</p>
                    <p
                      className={cn(
                        'text-xs mt-1',
                        message.sender === 'agent' ? 'opacity-70' : 'text-muted-foreground'
                      )}
                    >
                      {formatDateTime(message.timestamp).split(',')[1]}{' '}
                      {message.sender === 'agent' && message.isRead && '• Read'}
                    </p>
                  </div>
                  {message.sender === 'agent' && (
                    <Avatar className="h-8 w-8">
                      <AvatarFallback className="text-xs bg-[#1976D2] text-white">EC</AvatarFallback>
                    </Avatar>
                  )}
                </div>
              ))}

              {/* Typing Indicator */}
              {selectedSession.status === 'active' && (
                <div className="flex gap-3">
                  <Avatar className="h-8 w-8">
                    <AvatarFallback className="text-xs">
                      {selectedSession.customerName.split(' ').map(n => n[0]).join('')}
                    </AvatarFallback>
                  </Avatar>
                  <div className="bg-slate-100 dark:bg-slate-800 rounded-lg px-4 py-2">
                    <div className="flex gap-1">
                      <span className="h-2 w-2 rounded-full bg-slate-400 animate-bounce" style={{ animationDelay: '0ms' }} />
                      <span className="h-2 w-2 rounded-full bg-slate-400 animate-bounce" style={{ animationDelay: '150ms' }} />
                      <span className="h-2 w-2 rounded-full bg-slate-400 animate-bounce" style={{ animationDelay: '300ms' }} />
                    </div>
                  </div>
                </div>
              )}
            </CardContent>
          )}

          {/* Message Input */}
          {selectedSession && selectedSession.status === 'active' && (
            <div className="border-t p-4 space-y-3">
              {/* Canned Responses */}
              {showCannedResponses && (
                <div className="border rounded-lg p-2 space-y-1">
                  <p className="text-xs font-medium text-muted-foreground px-2">Quick Responses</p>
                  {cannedResponses.map((response) => (
                    <button
                      key={response.label}
                      className="w-full text-left px-3 py-2 text-sm rounded-md hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                      onClick={() => {
                        setMessageInput(response.text)
                        setShowCannedResponses(false)
                      }}
                    >
                      <span className="font-medium">{response.label}:</span> {response.text.substring(0, 50)}...
                    </button>
                  ))}
                </div>
              )}

              <div className="flex gap-2">
                <div className="flex-1 relative">
                  <Textarea
                    placeholder="Type your message..."
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter' && !e.shiftKey) {
                        e.preventDefault()
                        handleSendMessage()
                      }
                    }}
                    className="min-h-[60px] resize-none pr-20"
                  />
                  <div className="absolute right-2 bottom-2 flex items-center gap-1">
                    <Button
                      variant="ghost"
                      size="icon"
                      className="h-8 w-8"
                      title="Canned Responses"
                      onClick={() => setShowCannedResponses(!showCannedResponses)}
                    >
                      <Smile className="h-4 w-4" />
                    </Button>
                    <Button variant="ghost" size="icon" className="h-8 w-8" title="Attach File">
                      <Paperclip className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
                <Button onClick={handleSendMessage} className="h-[60px]">
                  <Send className="h-4 w-4" />
                </Button>
              </div>
            </div>
          )}

          {/* No Session Selected */}
          {!selectedSession && (
            <CardContent className="flex-1 flex items-center justify-center">
              <div className="text-center">
                <MessageSquare className="h-16 w-16 mx-auto mb-4 text-muted-foreground" />
                <h3 className="text-lg font-medium mb-2">No Chat Selected</h3>
                <p className="text-sm text-muted-foreground">
                  Select a chat from the queue or join a waiting customer
                </p>
              </div>
            </CardContent>
          )}
        </Card>
      </div>
    </div>
  )
}
