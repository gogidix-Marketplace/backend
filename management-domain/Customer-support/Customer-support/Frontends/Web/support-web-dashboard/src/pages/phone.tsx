import { useState } from 'react'
import {
  Phone,
  PhoneIncoming,
  PhoneOutgoing,
  Mic,
  MicOff,
  Volume2,
  VolumeX,
  Pause,
  Play,
  Transfer,
  EndCall,
  Clock,
  User,
  Headphones,
  Zap,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Avatar, AvatarFallback } from '@shared/components/ui/avatar'
import { cn, formatDuration, formatDateTime } from '@shared/utils/cn'
import { mockPhoneCalls, mockAgents } from '@shared/data/mockData'

export default function PhonePage() {
  const [activeCall, setActiveCall] = useState<typeof mockPhoneCalls[0] | null>(mockPhoneCalls.find(c => c.status === 'in_progress') || null)
  const [isMuted, setIsMuted] = useState(false)
  const [isOnHold, setIsOnHold] = useState(false)

  const queuedCalls = mockPhoneCalls.filter(c => c.status === 'queued')
  const completedCalls = mockPhoneCalls.filter(c => c.status === 'completed')
  const availableAgents = mockAgents.filter(a => a.status === 'online')

  const formatCallDuration = (seconds?: number) => {
    if (!seconds) return '0:00'
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${mins}:${secs.toString().padStart(2, '0')}`
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Phone Support</h1>
        <p className="page-description">
          Manage incoming and outgoing calls
        </p>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-green-100 dark:bg-green-900 p-2">
                <PhoneIncoming className="h-5 w-5 text-green-600 dark:text-green-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{queuedCalls.length}</p>
                <p className="text-sm text-muted-foreground">In Queue</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 dark:bg-blue-900 p-2">
                <Phone className="h-5 w-5 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{availableAgents.length}</p>
                <p className="text-sm text-muted-foreground">Available</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 dark:bg-purple-900 p-2">
                <Clock className="h-5 w-5 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">3:45</p>
                <p className="text-sm text-muted-foreground">Avg Wait</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-yellow-100 dark:bg-yellow-900 p-2">
                <Zap className="h-5 w-5 text-yellow-600 dark:text-yellow-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">94%</p>
                <p className="text-sm text-muted-foreground">Service Level</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Active Call Panel */}
        <div className="lg:col-span-2 space-y-4">
          {activeCall ? (
            <Card className="border-green-200 dark:border-green-800">
              <CardHeader className="bg-green-50 dark:bg-green-900/20">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="h-12 w-12 rounded-full bg-green-500 animate-pulse flex items-center justify-center">
                      <Phone className="h-6 w-6 text-white" />
                    </div>
                    <div>
                      <CardTitle>Active Call</CardTitle>
                      <CardDescription>
                        {formatCallDuration(activeCall.duration)} • {activeCall.queueName}
                      </CardDescription>
                    </div>
                  </div>
                  <Badge variant="success" className="gap-1">
                    <span className="relative flex h-2 w-2">
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-white opacity-75"></span>
                      <span className="relative inline-flex rounded-full h-2 w-2 bg-white"></span>
                    </span>
                    Live
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="p-6">
                <div className="text-center space-y-4">
                  <Avatar className="h-20 w-20 mx-auto">
                    <AvatarFallback className="text-2xl bg-[#1976D2] text-white">
                      {activeCall.customerName.split(' ').map(n => n[0]).join('')}
                    </AvatarFallback>
                  </Avatar>
                  <div>
                    <h3 className="text-xl font-semibold">{activeCall.customerName}</h3>
                    <p className="text-muted-foreground">{activeCall.customerPhone}</p>
                    <Badge variant="outline" className="mt-2">{activeCall.country}</Badge>
                  </div>

                  {/* Call Controls */}
                  <div className="flex items-center justify-center gap-2 pt-4">
                    <Button
                      variant="outline"
                      size="icon"
                      className="h-14 w-14 rounded-full"
                      onClick={() => setIsMuted(!isMuted)}
                    >
                      {isMuted ? <MicOff className="h-6 w-6" /> : <Mic className="h-6 w-6" />}
                    </Button>
                    <Button
                      variant="outline"
                      size="icon"
                      className="h-14 w-14 rounded-full"
                      onClick={() => setIsOnHold(!isOnHold)}
                    >
                      {isOnHold ? <Play className="h-6 w-6" /> : <Pause className="h-6 w-6" />}
                    </Button>
                    <Button
                      variant="outline"
                      size="icon"
                      className="h-14 w-14 rounded-full"
                    >
                      <Volume2 className="h-6 w-6" />
                    </Button>
                    <Button
                      variant="outline"
                      size="icon"
                      className="h-14 w-14 rounded-full"
                    >
                      <Transfer className="h-6 w-6" />
                    </Button>
                    <Button
                      variant="destructive"
                      size="icon"
                      className="h-16 w-16 rounded-full"
                      onClick={() => setActiveCall(null)}
                    >
                      <EndCall className="h-6 w-6" />
                    </Button>
                  </div>

                  {isOnHold && (
                    <p className="text-sm text-muted-foreground">Call is on hold</p>
                  )}
                  {isMuted && (
                    <p className="text-sm text-muted-foreground">Microphone is muted</p>
                  )}
                </div>

                {/* Quick Actions */}
                <div className="grid grid-cols-3 gap-2 mt-6 pt-6 border-t">
                  <Button variant="outline" className="gap-2">
                    <User className="h-4 w-4" />
                    View Profile
                  </Button>
                  <Button variant="outline" className="gap-2">
                    <Headphones className="h-4 w-4" />
                    Create Ticket
                  </Button>
                  <Button variant="outline" className="gap-2">
                    <Transfer className="h-4 w-4" />
                    Transfer
                  </Button>
                </div>
              </CardContent>
            </Card>
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <Headphones className="h-16 w-16 mx-auto mb-4 text-muted-foreground" />
                <h3 className="text-lg font-medium mb-2">No Active Call</h3>
                <p className="text-sm text-muted-foreground mb-4">
                  {queuedCalls.length > 0
                    ? 'There are calls waiting in the queue'
                    : 'Waiting for incoming calls...'}
                </p>
                {queuedCalls.length > 0 && (
                  <Button>Answer Next Call</Button>
                )}
              </CardContent>
            </Card>
          )}

          {/* Call Queue */}
          <Card>
            <CardHeader>
              <CardTitle>Call Queue</CardTitle>
              <CardDescription>{queuedCalls.length} calls waiting</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                {queuedCalls.map((call) => (
                  <div
                    key={call.id}
                    className="flex items-center justify-between rounded-lg border border-yellow-200 bg-yellow-50 dark:bg-yellow-900/20 dark:border-yellow-800 p-4"
                  >
                    <div className="flex items-center gap-3">
                      <Avatar>
                        <AvatarFallback>
                          {call.customerName.split(' ').map(n => n[0]).join('')}
                        </AvatarFallback>
                      </Avatar>
                      <div>
                        <p className="font-medium">{call.customerName}</p>
                        <p className="text-sm text-muted-foreground">{call.customerPhone}</p>
                      </div>
                    </div>
                    <div className="flex items-center gap-3">
                      <div className="text-right">
                        <p className="text-sm text-yellow-600 font-medium">
                          Waiting {formatDuration(Math.floor((Date.now() - new Date(call.startedAt).getTime()) / 60000))}
                        </p>
                        <Badge variant="outline" className="text-xs">{call.queueName}</Badge>
                      </div>
                      <Button
                        size="sm"
                        onClick={() => setActiveCall({ ...call, status: 'in_progress' as const })}
                      >
                        Answer
                      </Button>
                    </div>
                  </div>
                ))}
                {queuedCalls.length === 0 && (
                  <div className="text-center py-8 text-muted-foreground">
                    <CheckCircle className="h-12 w-12 mx-auto mb-3 text-green-500" />
                    <p>Queue is clear</p>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-4">
          {/* Available Agents */}
          <Card>
            <CardHeader>
              <CardTitle className="text-base">Available Agents</CardTitle>
            </CardHeader>
            <CardContent className="p-2">
              <div className="space-y-1">
                {availableAgents.map((agent) => (
                  <div
                    key={agent.id}
                    className="flex items-center gap-3 rounded-lg p-2 hover:bg-slate-100 dark:hover:bg-slate-800 cursor-pointer"
                  >
                    <div className="relative">
                      <Avatar className="h-8 w-8">
                        <AvatarFallback className="text-xs bg-green-500 text-white">
                          {agent.firstName[0]}{agent.lastName[0]}
                        </AvatarFallback>
                      </Avatar>
                      <span className="absolute bottom-0 right-0 h-2.5 w-2.5 rounded-full border-2 border-white bg-green-500" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium truncate">{agent.firstName} {agent.lastName}</p>
                      <p className="text-xs text-muted-foreground">{agent.country || 'Global'}</p>
                    </div>
                    <Button variant="outline" size="icon" className="h-8 w-8">
                      <Phone className="h-3 w-3" />
                    </Button>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Recent Calls */}
          <Card>
            <CardHeader>
              <CardTitle className="text-base">Recent Calls</CardTitle>
            </CardHeader>
            <CardContent className="p-2">
              <div className="space-y-2">
                {completedCalls.map((call) => (
                  <div
                    key={call.id}
                    className="flex items-center gap-3 rounded-lg p-2 hover:bg-slate-100 dark:hover:bg-slate-800"
                  >
                    <div className="h-8 w-8 rounded-full bg-slate-100 dark:bg-slate-800 flex items-center justify-center">
                      {call.direction === 'inbound' ? (
                        <PhoneIncoming className="h-4 w-4 text-green-500" />
                      ) : (
                        <PhoneOutgoing className="h-4 w-4 text-blue-500" />
                      )}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium truncate">{call.customerName}</p>
                      <p className="text-xs text-muted-foreground">
                        {formatDuration(call.duration)} • {call.disposition}
                      </p>
                    </div>
                    {call.satisfactionRating && (
                      <Badge variant="success" className="text-xs">
                        ★ {call.satisfactionRating}
                      </Badge>
                    )}
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Quick Stats */}
          <Card>
            <CardHeader>
              <CardTitle className="text-base">Today's Stats</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-sm text-muted-foreground">Total Calls</span>
                <span className="font-medium">47</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-muted-foreground">Answered</span>
                <span className="font-medium text-green-600">42</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-muted-foreground">Missed</span>
                <span className="font-medium text-red-600">5</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-muted-foreground">Avg Duration</span>
                <span className="font-medium">4:32</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-muted-foreground">Avg CSAT</span>
                <span className="font-medium">4.6 ★</span>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
