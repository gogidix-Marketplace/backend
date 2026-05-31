import { useState } from 'react'
import { Link } from 'react-router-dom'
import {
  Search,
  Filter,
  Plus,
  MoreVertical,
  ArrowUpDown,
  User,
  Clock,
  AlertTriangle,
  CheckCircle,
  MessageSquare,
  Phone,
  Mail,
  Calendar,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@shared/components/ui/dropdown-menu'
import { cn, formatDateTime, formatDuration, getPriorityColor, getStatusColor } from '@shared/utils/cn'
import { mockTickets, mockAgents } from '@shared/data/mockData'
import type { TicketStatus, TicketPriority } from '@shared/types'

export default function TicketsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [statusFilter, setStatusFilter] = useState<TicketStatus | 'all'>('all')
  const [priorityFilter, setPriorityFilter] = useState<TicketPriority | 'all'>('all')
  const [assignedFilter, setAssignedFilter] = useState<string>('all')

  const channelIcons = {
    email: Mail,
    chat: MessageSquare,
    phone: Phone,
    web: MessageSquare,
    api: MessageSquare,
    social: MessageSquare,
  }

  // Filter tickets
  const filteredTickets = mockTickets.filter((ticket) => {
    const matchesSearch =
      searchQuery === '' ||
      ticket.subject.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.ticketNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.customerName.toLowerCase().includes(searchQuery.toLowerCase())

    const matchesStatus = statusFilter === 'all' || ticket.status === statusFilter
    const matchesPriority = priorityFilter === 'all' || ticket.priority === priorityFilter
    const matchesAssigned = assignedFilter === 'all' || ticket.assignedTo === assignedFilter

    return matchesSearch && matchesStatus && matchesPriority && matchesAssigned
  })

  const statusCounts = {
    all: mockTickets.length,
    open: mockTickets.filter(t => t.status === 'open').length,
    pending: mockTickets.filter(t => t.status === 'pending').length,
    in_progress: mockTickets.filter(t => t.status === 'in_progress').length,
    resolved: mockTickets.filter(t => t.status === 'resolved').length,
    closed: mockTickets.filter(t => t.status === 'closed').length,
    escalated: mockTickets.filter(t => t.status === 'escalated').length,
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Tickets</h1>
          <p className="page-description">
            Manage and track all support tickets
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          New Ticket
        </Button>
      </div>

      {/* Status Tabs */}
      <div className="flex gap-2 overflow-x-auto pb-2">
        <Button
          variant={statusFilter === 'all' ? 'default' : 'outline'}
          size="sm"
          onClick={() => setStatusFilter('all')}
        >
          All ({statusCounts.all})
        </Button>
        <Button
          variant={statusFilter === 'open' ? 'default' : 'outline'}
          size="sm"
          onClick={() => setStatusFilter('open')}
        >
          Open ({statusCounts.open})
        </Button>
        <Button
          variant={statusFilter === 'in_progress' ? 'default' : 'outline'}
          size="sm"
          onClick={() => setStatusFilter('in_progress')}
        >
          In Progress ({statusCounts.in_progress})
        </Button>
        <Button
          variant={statusFilter === 'pending' ? 'default' : 'outline'}
          size="sm"
          onClick={() => setStatusFilter('pending')}
        >
          Pending ({statusCounts.pending})
        </Button>
        <Button
          variant={statusFilter === 'escalated' ? 'default' : 'outline'}
          size="sm"
          onClick={() => setStatusFilter('escalated')}
        >
          Escalated ({statusCounts.escalated})
        </Button>
        <Button
          variant={statusFilter === 'resolved' ? 'default' : 'outline'}
          size="sm"
          onClick={() => setStatusFilter('resolved')}
        >
          Resolved ({statusCounts.resolved})
        </Button>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-col gap-4 md:flex-row md:items-center">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                placeholder="Search tickets..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <div className="flex gap-2">
              <Select value={priorityFilter} onValueChange={(v) => setPriorityFilter(v as TicketPriority | 'all')}>
                <SelectTrigger className="w-[140px]">
                  <SelectValue placeholder="Priority" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Priorities</SelectItem>
                  <SelectItem value="urgent">Urgent</SelectItem>
                  <SelectItem value="high">High</SelectItem>
                  <SelectItem value="medium">Medium</SelectItem>
                  <SelectItem value="low">Low</SelectItem>
                </SelectContent>
              </Select>
              <Select value={assignedFilter} onValueChange={setAssignedFilter}>
                <SelectTrigger className="w-[160px]">
                  <SelectValue placeholder="Assignee" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Agents</SelectItem>
                  {mockAgents.map((agent) => (
                    <SelectItem key={agent.id} value={agent.id}>
                      {agent.firstName} {agent.lastName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tickets Table */}
      <Card>
        <CardHeader>
          <CardTitle>Ticket Queue</CardTitle>
          <CardDescription>
            Showing {filteredTickets.length} of {mockTickets.length} tickets
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-[100px]">Ticket</TableHead>
                  <TableHead>Subject</TableHead>
                  <TableHead>Customer</TableHead>
                  <TableHead>Assigned To</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Priority</TableHead>
                  <TableHead>Channel</TableHead>
                  <TableHead>SLA</TableHead>
                  <TableHead>Created</TableHead>
                  <TableHead className="w-[50px]"></TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredTickets.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={10} className="text-center py-8 text-muted-foreground">
                      No tickets found matching your filters
                    </TableCell>
                  </TableRow>
                ) : (
                  filteredTickets.map((ticket) => {
                    const ChannelIcon = channelIcons[ticket.channel]
                    return (
                      <TableRow key={ticket.id} className="group">
                        <TableCell className="font-medium">
                          <Link
                            to={`/tickets/${ticket.id}`}
                            className="text-[#1976D2] hover:underline"
                          >
                            {ticket.ticketNumber}
                          </Link>
                        </TableCell>
                        <TableCell>
                          <div className="max-w-[200px]">
                            <p className="truncate font-medium">{ticket.subject}</p>
                            <p className="text-xs text-muted-foreground truncate">
                              {ticket.category}
                            </p>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center gap-2">
                            <div className="h-6 w-6 rounded-full bg-slate-200 flex items-center justify-center text-xs">
                              {ticket.customerName.split(' ').map(n => n[0]).join('')}
                            </div>
                            <div className="text-sm">
                              <p className="font-medium">{ticket.customerName}</p>
                              <p className="text-xs text-muted-foreground">{ticket.country}</p>
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          {ticket.assignedTo ? (
                            <div className="flex items-center gap-2">
                              <div className="h-6 w-6 rounded-full bg-[#1976D2] text-white flex items-center justify-center text-xs">
                                {ticket.assignedAgentName?.split(' ').map(n => n[0]).join('')}
                              </div>
                              <span className="text-sm">{ticket.assignedAgentName}</span>
                            </div>
                          ) : (
                            <span className="text-sm text-muted-foreground">Unassigned</span>
                          )}
                        </TableCell>
                        <TableCell>
                          <Badge
                            variant={
                              ticket.status === 'open' ? 'open' :
                              ticket.status === 'resolved' ? 'resolved' :
                              ticket.status === 'escalated' ? 'escalated' :
                              ticket.status === 'in_progress' ? 'in_progress' :
                              ticket.status === 'customer_reply' ? 'info' : 'secondary'
                            }
                          >
                            {ticket.status.replace('_', ' ')}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <Badge variant={ticket.priority === 'urgent' ? 'destructive' : ticket.priority === 'high' ? 'warning' : 'secondary'}>
                            {ticket.priority}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <ChannelIcon className="h-4 w-4 text-muted-foreground" />
                        </TableCell>
                        <TableCell>
                          {ticket.slaBreached ? (
                            <Badge variant="destructive" className="gap-1">
                              <AlertTriangle className="h-3 w-3" />
                              Breached
                            </Badge>
                          ) : ticket.slaWarning ? (
                            <Badge variant="warning" className="gap-1">
                              <Clock className="h-3 w-3" />
                              Warning
                            </Badge>
                          ) : (
                            <Badge variant="success" className="gap-1">
                              <CheckCircle className="h-3 w-3" />
                              OK
                            </Badge>
                          )}
                        </TableCell>
                        <TableCell>
                          <div className="text-sm">
                            <p>{formatDateTime(ticket.createdDate).split(',')[0]}</p>
                            <p className="text-xs text-muted-foreground">
                              {formatDateTime(ticket.createdDate).split(',')[1]}
                            </p>
                          </div>
                        </TableCell>
                        <TableCell>
                          <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                              <Button variant="ghost" size="icon" className="opacity-0 group-hover:opacity-100">
                                <MoreVertical className="h-4 w-4" />
                              </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                              <DropdownMenuLabel>Actions</DropdownMenuLabel>
                              <DropdownMenuSeparator />
                              <DropdownMenuItem>
                                <User className="mr-2 h-4 w-4" />
                                Assign to Agent
                              </DropdownMenuItem>
                              <DropdownMenuItem>
                                <MessageSquare className="mr-2 h-4 w-4" />
                                Send Reply
                              </DropdownMenuItem>
                              <DropdownMenuItem>
                                <Clock className="mr-2 h-4 w-4" />
                                Snooze
                              </DropdownMenuItem>
                              <DropdownMenuSeparator />
                              <DropdownMenuItem className="text-green-600">
                                <CheckCircle className="mr-2 h-4 w-4" />
                                Mark Resolved
                              </DropdownMenuItem>
                              <DropdownMenuItem className="text-red-600">
                                <AlertTriangle className="mr-2 h-4 w-4" />
                                Escalate
                              </DropdownMenuItem>
                            </DropdownMenuContent>
                          </DropdownMenu>
                        </TableCell>
                      </TableRow>
                    )
                  })
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>

      {/* Bulk Actions Bar */}
      {filteredTickets.length > 0 && (
        <div className="flex items-center justify-between rounded-lg border bg-slate-50 dark:bg-slate-800 p-4">
          <p className="text-sm text-muted-foreground">
            {filteredTickets.length} tickets selected
          </p>
          <div className="flex gap-2">
            <Button variant="outline" size="sm">
              Bulk Assign
            </Button>
            <Button variant="outline" size="sm">
              Change Status
            </Button>
            <Button variant="outline" size="sm">
              Change Priority
            </Button>
            <Button variant="outline" size="sm" className="text-red-600">
              Delete
            </Button>
          </div>
        </div>
      )}
    </div>
  )
}
