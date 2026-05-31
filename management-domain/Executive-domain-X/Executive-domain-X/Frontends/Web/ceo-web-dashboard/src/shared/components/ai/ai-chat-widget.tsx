import * as React from 'react'
import { Button } from '@shared/components/ui/button'
import { Card, CardContent } from '@shared/components/ui/card'
import { Input } from '@shared/components/ui/input'
import { Badge } from '@shared/components/ui/badge'
import { cn } from '@shared/utils/cn'
import {
  Sparkles,
  X,
  Send,
  Minimize2,
  Maximize2,
  Loader2,
} from 'lucide-react'

/**
 * AI Chat Widget - Floating chat button and window
 *
 * Features:
 * - Floating action button
 * - Chat interface with history
 * - Message input
 * - Typing indicator
 * - Minimizable/maximizable
 * - Purple gradient theme
 */

export interface ChatMessage {
  id: string
  role: 'user' | 'assistant' | 'system'
  content: string
  timestamp: Date
  confidence?: number
}

export interface AIChatWidgetProps {
  onSendMessage?: (message: string) => Promise<string>
  placeholder?: string
  position?: 'bottom-right' | 'bottom-left'
  initialOpen?: boolean
  title?: string
  welcomeMessage?: string
  className?: string
}

const aiGradient = 'from-[#7C4DFF] to-[#B388FF]'
const aiGlow = 'shadow-[0_0_20px_rgba(124,77,255,0.4)]'

export function AIChatWidget({
  onSendMessage,
  placeholder = 'Ask AI anything about your data...',
  position = 'bottom-right',
  initialOpen = false,
  title = 'AI Assistant',
  welcomeMessage = 'Hello! I\'m your AI assistant. Ask me anything about your executive dashboard data.',
  className,
}: AIChatWidgetProps) {
  const [isOpen, setIsOpen] = React.useState(initialOpen)
  const [isMinimized, setIsMinimized] = React.useState(false)
  const [messages, setMessages] = React.useState<ChatMessage[]>([])
  const [input, setInput] = React.useState('')
  const [isLoading, setIsLoading] = React.useState(false)
  const messagesEndRef = React.useRef<HTMLDivElement>(null)
  const inputRef = React.useRef<HTMLInputElement>(null)

  // Add welcome message on first open
  React.useEffect(() => {
    if (isOpen && messages.length === 0) {
      setMessages([
        {
          id: 'welcome',
          role: 'assistant',
          content: welcomeMessage,
          timestamp: new Date(),
        },
      ])
    }
  }, [isOpen, messages.length, welcomeMessage])

  // Auto-scroll to bottom
  React.useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  // Focus input when opened
  React.useEffect(() => {
    if (isOpen && !isMinimized) {
      setTimeout(() => inputRef.current?.focus(), 100)
    }
  }, [isOpen, isMinimized])

  const handleSend = async () => {
    const message = input.trim()
    if (!message || isLoading) return

    const userMessage: ChatMessage = {
      id: Date.now().toString(),
      role: 'user',
      content: message,
      timestamp: new Date(),
    }

    setMessages((prev) => [...prev, userMessage])
    setInput('')
    setIsLoading(true)

    try {
      if (onSendMessage) {
        const response = await onSendMessage(message)
        const aiMessage: ChatMessage = {
          id: (Date.now() + 1).toString(),
          role: 'assistant',
          content: response,
          timestamp: new Date(),
        }
        setMessages((prev) => [...prev, aiMessage])
      }
    } catch (error) {
      const errorMessage: ChatMessage = {
        id: (Date.now() + 1).toString(),
        role: 'system',
        content: 'Sorry, I encountered an error. Please try again.',
        timestamp: new Date(),
      }
      setMessages((prev) => [...prev, errorMessage])
    } finally {
      setIsLoading(false)
    }
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  const positionClass = position === 'bottom-right' ? 'bottom-4 right-4' : 'bottom-4 left-4'

  return (
    <>
      {/* Portal to render at document body level for proper z-index stacking */}
      {!isOpen && (
        <Button
          onClick={() => setIsOpen(true)}
          className={cn(
            'fixed z-[100] h-14 w-14 rounded-full ai-glow',
            'bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700',
            'text-white shadow-lg transition-all hover:scale-110',
            'flex items-center justify-center',
            positionClass,
            className
          )}
        >
          <Sparkles className="h-6 w-6" />
          <span className="sr-only">Open AI Chat</span>
        </Button>
      )}

      {/* Chat Window */}
      {isOpen && (
        <Card
          className={cn(
            'fixed z-[100] w-96 max-w-[calc(100vw-2rem)] shadow-2xl',
            'flex flex-col max-h-[600px]',
            'bg-gradient-to-br from-white to-purple-50/50 dark:from-slate-900 dark:to-purple-950/20',
            'border-purple-200 dark:border-purple-800',
            positionClass,
            isMinimized && 'h-14'
          )}
        >
          {/* Header */}
          <div className={cn('flex items-center justify-between px-4 py-3 border-b', 'border-purple-200 dark:border-purple-800')}>
            <div className="flex items-center gap-2">
              <div className="flex h-8 w-8 items-center justify-center rounded-full bg-gradient-to-r from-purple-600 to-indigo-600">
                <Sparkles className="h-4 w-4 text-white" />
              </div>
              <div>
                <h3 className="font-semibold text-sm">{title}</h3>
                {!isMinimized && messages.length > 0 && (
                  <p className="text-xs text-muted-foreground">
                    {messages.filter(m => m.role === 'assistant').length} responses
                  </p>
                )}
              </div>
            </div>
            <div className="flex items-center gap-1">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setIsMinimized(!isMinimized)}
                className="h-8 w-8 p-0"
              >
                {isMinimized ? <Maximize2 className="h-4 w-4" /> : <Minimize2 className="h-4 w-4" />}
              </Button>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setIsOpen(false)}
                className="h-8 w-8 p-0"
              >
                <X className="h-4 w-4" />
              </Button>
            </div>
          </div>

          {/* Messages */}
          {!isMinimized && (
            <>
              <CardContent className="flex-1 overflow-y-auto p-4 space-y-4 min-h-[300px]">
                {messages.map((message) => (
                  <div
                    key={message.id}
                    className={cn(
                      'flex gap-3',
                      message.role === 'user' && 'flex-row-reverse'
                    )}
                  >
                    {message.role === 'assistant' && (
                      <div className="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-gradient-to-r from-purple-600 to-indigo-600">
                        <Sparkles className="h-4 w-4 text-white" />
                      </div>
                    )}
                    {message.role === 'system' && (
                      <div className="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-amber-100 dark:bg-amber-900/20">
                        <X className="h-4 w-4 text-amber-600 dark:text-amber-400" />
                      </div>
                    )}
                    <div
                      className={cn(
                        'rounded-lg px-3 py-2 max-w-[80%]',
                        message.role === 'user' && 'bg-primary text-primary-foreground',
                        message.role === 'assistant' && 'bg-purple-100 dark:bg-purple-900/30 text-purple-900 dark:text-purple-100',
                        message.role === 'system' && 'bg-amber-100 dark:bg-amber-900/30 text-amber-900 dark:text-amber-100'
                      )}
                    >
                      <p className="text-sm whitespace-pre-wrap">{message.content}</p>
                      {message.confidence && (
                        <div className="mt-2 flex items-center gap-2">
                          <div className="h-1 w-16 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                            <div
                              className="h-full bg-purple-500"
                              style={{ width: `${message.confidence}%` }}
                            />
                          </div>
                          <span className="text-xs text-muted-foreground">{message.confidence}%</span>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
                {isLoading && (
                  <div className="flex gap-3">
                    <div className="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full bg-gradient-to-r from-purple-600 to-indigo-600">
                      <Sparkles className="h-4 w-4 text-white" />
                    </div>
                    <div className="rounded-lg px-3 py-2 bg-purple-100 dark:bg-purple-900/30">
                      <Loader2 className="h-4 w-4 animate-spin text-purple-600 dark:text-purple-400" />
                    </div>
                  </div>
                )}
                <div ref={messagesEndRef} />
              </CardContent>

              {/* Input */}
              <div className="border-t border-purple-200 dark:border-purple-800 p-4">
                <div className="flex gap-2">
                  <Input
                    ref={inputRef}
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder={placeholder}
                    disabled={isLoading}
                    className="flex-1"
                  />
                  <Button
                    onClick={handleSend}
                    disabled={!input.trim() || isLoading}
                    size="icon"
                    className="h-10 w-10 bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700"
                  >
                    {isLoading ? (
                      <Loader2 className="h-4 w-4 animate-spin" />
                    ) : (
                      <Send className="h-4 w-4" />
                    )}
                  </Button>
                </div>
              </div>
            </>
          )}
        </Card>
      )}
    </>
  )
}
