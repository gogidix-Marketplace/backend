import { useState, useRef, useEffect } from 'react'
import {
  Dialog,
  DialogContent,
  DialogHeader,
} from '@shared/components/ui/dialog'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Card } from '@shared/components/ui/card'
import { cn } from '@shared/utils/cn'
import { Send, Minimize2, Maximize2, X, Sparkles, User, Bot } from 'lucide-react'

/**
 * AIChatWindow - Interactive chat interface
 *
 * Floating chat interface for AI-powered insights
 */

export interface AIMessage {
  id: string
  role: 'user' | 'assistant'
  content: string
  timestamp: Date
}

export interface AIChatWindowProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  messages?: AIMessage[]
  onSendMessage?: (message: string) => void
  loading?: boolean
  title?: string
}

export function AIChatWindow({
  open,
  onOpenChange,
  messages = [],
  onSendMessage,
  loading = false,
  title = 'AI Assistant',
}: AIChatWindowProps) {
  const [input, setInput] = useState('')
  const [isMinimized, setIsMinimized] = useState(false)
  const messagesEndRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const handleSend = () => {
    if (input.trim() && !loading) {
      onSendMessage?.(input.trim())
      setInput('')
    }
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent
        className={cn(
          'fixed right-6 bottom-6 w-96 p-0 gap-0',
          isMinimized && 'h-14'
        )}
        onPointerDownOutside={(e) => e.preventDefault()}
      >
        {/* Header */}
        <div className="bg-gradient-to-r from-[#7C4DFF] to-[#B388FF] px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-2 text-white">
            <Bot className="h-5 w-5" />
            <span className="font-semibold">{title}</span>
          </div>
          <div className="flex items-center gap-1">
            <Button
              variant="ghost"
              size="sm"
              className="h-7 w-7 p-0 text-white hover:bg-white/20"
              onClick={() => setIsMinimized(!isMinimized)}
            >
              {isMinimized ? <Maximize2 className="h-4 w-4" /> : <Minimize2 className="h-4 w-4" />}
            </Button>
            <Button
              variant="ghost"
              size="sm"
              className="h-7 w-7 p-0 text-white hover:bg-white/20"
              onClick={() => onOpenChange(false)}
            >
              <X className="h-4 w-4" />
            </Button>
          </div>
        </div>

        {!isMinimized && (
          <>
            {/* Messages */}
            <div className="h-96 overflow-y-auto p-4 space-y-4">
              {messages.length === 0 ? (
                <div className="text-center py-8">
                  <Sparkles className="h-12 w-12 mx-auto text-purple-500 mb-3" />
                  <p className="text-sm text-muted-foreground">
                    Ask me anything about your dashboard data
                  </p>
                  <div className="mt-4 space-y-2">
                    <Button
                      variant="outline"
                      size="sm"
                      className="w-full justify-start text-xs"
                      onClick={() => onSendMessage?.('What are the key risks this quarter?')}
                    >
                      What are the key risks this quarter?
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      className="w-full justify-start text-xs"
                      onClick={() => onSendMessage?.('Show revenue trends by region')}
                    >
                      Show revenue trends by region
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      className="w-full justify-start text-xs"
                      onClick={() => onSendMessage?.('Which domains need attention?')}
                    >
                      Which domains need attention?
                    </Button>
                  </div>
                </div>
              ) : (
                messages.map((message) => (
                  <div
                    key={message.id}
                    className={cn(
                      'flex gap-3',
                      message.role === 'user' ? 'justify-end' : 'justify-start'
                    )}
                  >
                    {message.role === 'assistant' && (
                      <div className="flex h-8 w-8 items-center justify-center rounded-full bg-purple-100 dark:bg-purple-900/20 flex-shrink-0">
                        <Bot className="h-4 w-4 text-purple-600" />
                      </div>
                    )}
                    <Card
                      className={cn(
                        'max-w-[80%] px-3 py-2',
                        message.role === 'user'
                          ? 'bg-purple-600 text-white'
                          : 'bg-slate-100 dark:bg-slate-800'
                      )}
                    >
                      <p className="text-sm whitespace-pre-wrap">{message.content}</p>
                      <p
                        className={cn(
                          'text-xs mt-1',
                          message.role === 'user' ? 'text-white/70' : 'text-muted-foreground'
                        )}
                      >
                        {message.timestamp.toLocaleTimeString([], {
                          hour: '2-digit',
                          minute: '2-digit',
                        })}
                      </p>
                    </Card>
                    {message.role === 'user' && (
                      <div className="flex h-8 w-8 items-center justify-center rounded-full bg-slate-200 dark:bg-slate-700 flex-shrink-0">
                        <User className="h-4 w-4" />
                      </div>
                    )}
                  </div>
                ))
              )}
              {loading && (
                <div className="flex gap-3">
                  <div className="flex h-8 w-8 items-center justify-center rounded-full bg-purple-100 dark:bg-purple-900/20 flex-shrink-0">
                    <Bot className="h-4 w-4 text-purple-600" />
                  </div>
                  <Card className="px-3 py-2 bg-slate-100 dark:bg-slate-800">
                    <div className="flex gap-1">
                      <div className="h-2 w-2 bg-purple-500 rounded-full animate-bounce" style={{ animationDelay: '0ms' }} />
                      <div className="h-2 w-2 bg-purple-500 rounded-full animate-bounce" style={{ animationDelay: '150ms' }} />
                      <div className="h-2 w-2 bg-purple-500 rounded-full animate-bounce" style={{ animationDelay: '300ms' }} />
                    </div>
                  </Card>
                </div>
              )}
              <div ref={messagesEndRef} />
            </div>

            {/* Input */}
            <div className="border-t p-3">
              <div className="flex gap-2">
                <Input
                  placeholder="Ask AI..."
                  value={input}
                  onChange={(e) => setInput(e.target.value)}
                  onKeyPress={handleKeyPress}
                  disabled={loading}
                  className="flex-1"
                />
                <Button
                  size="icon"
                  className="h-9 w-9 bg-purple-600 hover:bg-purple-700"
                  onClick={handleSend}
                  disabled={!input.trim() || loading}
                >
                  <Send className="h-4 w-4" />
                </Button>
              </div>
              <p className="text-xs text-muted-foreground mt-2 text-center">
                AI can make mistakes. Verify important information.
              </p>
            </div>
          </>
        )}
      </DialogContent>
    </Dialog>
  )
}
