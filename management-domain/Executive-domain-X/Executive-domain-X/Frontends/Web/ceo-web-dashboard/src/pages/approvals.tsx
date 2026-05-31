import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
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
import { Textarea } from '@shared/components/ui/textarea'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
import { Label } from '@shared/components/ui/label'
import { cn, formatCurrency, formatDateTime } from '@shared/utils/cn'
import { CheckCircle2, XCircle, Clock, Calendar, Filter } from 'lucide-react'

// Mock data
const pendingApprovals = [
  {
    id: '1',
    type: 'budget',
    title: 'Q3 Marketing Budget Revision',
    description: 'Additional budget required for digital marketing campaigns in new markets',
    department: 'Digital Marketing',
    amount: 2500000,
    requestedBy: 'Sarah Chen',
    requestDate: '2024-03-01T10:30:00Z',
    dueDate: '2024-03-15',
    priority: 'high',
    category: 'Strategic Initiative',
  },
  {
    id: '2',
    type: 'hiring',
    title: 'CTO Office - Senior Engineers',
    description: 'Hiring approval for 5 senior engineers to support AI platform development',
    department: 'System Administration',
    amount: 1250000,
    requestedBy: 'Mike Johnson',
    requestDate: '2024-03-01T09:15:00Z',
    dueDate: '2024-03-10',
    priority: 'urgent',
    category: 'Talent Acquisition',
  },
  {
    id: '3',
    type: 'initiative',
    title: 'AI Integration Project',
    description: 'Enterprise-wide AI integration for analytics and decision support',
    department: 'Executive',
    amount: 5000000,
    requestedBy: 'CTO Office',
    requestDate: '2024-02-28T14:00:00Z',
    dueDate: '2024-03-20',
    priority: 'urgent',
    category: 'Digital Transformation',
  },
  {
    id: '4',
    type: 'contract',
    title: 'Cloud Infrastructure Agreement',
    description: 'Multi-year contract with AWS for cloud services',
    department: 'IT Operations',
    amount: 3500000,
    requestedBy: 'Infrastructure Team',
    requestDate: '2024-02-28T11:30:00Z',
    dueDate: '2024-03-25',
    priority: 'medium',
    category: 'Infrastructure',
  },
  {
    id: '5',
    type: 'budget',
    title: 'HR Training Program Q2',
    description: 'Leadership development program for middle management across all regions',
    department: 'Human Resources',
    amount: 750000,
    requestedBy: 'Patricia Moore',
    requestDate: '2024-02-27T16:45:00Z',
    dueDate: '2024-03-12',
    priority: 'medium',
    category: 'Training & Development',
  },
]

const approvalHistory = [
  {
    id: '101',
    type: 'budget',
    title: 'Q2 Marketing Campaign',
    department: 'Digital Marketing',
    amount: 1800000,
    requestedBy: 'Sarah Chen',
    action: 'approved',
    actionDate: '2024-02-25T14:30:00Z',
    notes: 'Approved with conditions - focus on ROI tracking',
  },
  {
    id: '102',
    type: 'hiring',
    title: 'Sales Director - Europe',
    department: 'Sales',
    amount: 450000,
    requestedBy: 'James Wilson',
    action: 'approved',
    actionDate: '2024-02-24T10:15:00Z',
    notes: 'Approved - critical role for EU expansion',
  },
  {
    id: '103',
    type: 'initiative',
    title: 'Customer Portal Redesign',
    department: 'Customer Support',
    amount: 1200000,
    requestedBy: 'Lisa Anderson',
    action: 'rejected',
    actionDate: '2024-02-23T16:00:00Z',
    notes: 'Rejected - request resubmission with clearer ROI justification',
  },
]

const delegatedApprovals = [
  {
    id: '201',
    title: 'Regional Office Lease - Nairobi',
    delegatedTo: 'CFO',
    reason: 'Financial expertise required',
    status: 'pending',
  },
  {
    id: '202',
    title: 'Software Licensing Agreement',
    delegatedTo: 'CTO',
    reason: 'Technical evaluation needed',
    status: 'approved',
  },
]

type Approval = typeof pendingApprovals[0]

export default function ApprovalsPage() {
  const [selectedApproval, setSelectedApproval] = useState<Approval | null>(null)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [action, setAction] = useState<'approve' | 'reject' | null>(null)
  const [notes, setNotes] = useState('')
  const [filterType, setFilterType] = useState<string>('all')
  const [filterPriority, setFilterPriority] = useState<string>('all')

  const handleApprove = (approval: Approval) => {
    setSelectedApproval(approval)
    setAction('approve')
    setDialogOpen(true)
  }

  const handleReject = (approval: Approval) => {
    setSelectedApproval(approval)
    setAction('reject')
    setDialogOpen(true)
  }

  const confirmAction = () => {
    // Handle approval/rejection logic here
    console.log(`${action}ing approval ${selectedApproval?.id} with notes: ${notes}`)
    setDialogOpen(false)
    setNotes('')
    setSelectedApproval(null)
    setAction(null)
  }

  const filteredApprovals = pendingApprovals.filter((approval) => {
    if (filterType !== 'all' && approval.type !== filterType) return false
    if (filterPriority !== 'all' && approval.priority !== filterPriority) return false
    return true
  })

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Approvals</h1>
          <p className="page-description">
            Review and approve budget, hiring, and strategic initiatives
          </p>
        </div>
        <Badge variant="destructive" className="text-base px-4 py-1.5">
          {pendingApprovals.length} Pending
        </Badge>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="pending" className="space-y-6">
        <TabsList>
          <TabsTrigger value="pending">
            <Clock className="mr-2 h-4 w-4" />
            Pending ({pendingApprovals.length})
          </TabsTrigger>
          <TabsTrigger value="history">
            <Calendar className="mr-2 h-4 w-4" />
            History
          </TabsTrigger>
          <TabsTrigger value="delegated">
            <CheckCircle2 className="mr-2 h-4 w-4" />
            Delegated
          </TabsTrigger>
        </TabsList>

        {/* Pending Approvals */}
        <TabsContent value="pending" className="space-y-4">
          {/* Filters */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex flex-wrap items-center gap-4">
                <div className="flex items-center gap-2">
                  <Filter className="h-4 w-4 text-muted-foreground" />
                  <span className="text-sm font-medium">Filters:</span>
                </div>
                <Select value={filterType} onValueChange={setFilterType}>
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder="Type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Types</SelectItem>
                    <SelectItem value="budget">Budget</SelectItem>
                    <SelectItem value="hiring">Hiring</SelectItem>
                    <SelectItem value="initiative">Initiative</SelectItem>
                    <SelectItem value="contract">Contract</SelectItem>
                  </SelectContent>
                </Select>
                <Select value={filterPriority} onValueChange={setFilterPriority}>
                  <SelectTrigger className="w-[180px]">
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
              </div>
            </CardContent>
          </Card>

          {/* Pending Approvals List */}
          {filteredApprovals.map((approval) => (
            <Card key={approval.id}>
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <CardTitle className="text-lg">{approval.title}</CardTitle>
                      <Badge
                        variant={
                          approval.priority === 'urgent'
                            ? 'destructive'
                            : approval.priority === 'high'
                            ? 'warning'
                            : 'secondary'
                        }
                      >
                        {approval.priority}
                      </Badge>
                      <Badge variant="outline">{approval.category}</Badge>
                    </div>
                    <CardDescription className="text-base">
                      {approval.description}
                    </CardDescription>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="grid gap-4 md:grid-cols-4">
                  <div>
                    <p className="text-sm text-muted-foreground">Department</p>
                    <p className="font-medium">{approval.department}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Amount</p>
                    <p className="font-medium">{formatCurrency(approval.amount)}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Requested By</p>
                    <p className="font-medium">{approval.requestedBy}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Due Date</p>
                    <p className="font-medium">{approval.dueDate}</p>
                  </div>
                </div>
                <div className="mt-4 flex justify-end gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleReject(approval)}
                  >
                    <XCircle className="mr-2 h-4 w-4" />
                    Reject
                  </Button>
                  <Button
                    size="sm"
                    className="executive-gold-bg text-white"
                    onClick={() => handleApprove(approval)}
                  >
                    <CheckCircle2 className="mr-2 h-4 w-4" />
                    Approve
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        {/* Approval History */}
        <TabsContent value="history">
          <Card>
            <CardHeader>
              <CardTitle>Approval History</CardTitle>
              <CardDescription>Recent approvals and rejections</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Title</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead>Amount</TableHead>
                    <TableHead>Action</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead>Notes</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {approvalHistory.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell className="font-medium">{item.title}</TableCell>
                      <TableCell>{item.department}</TableCell>
                      <TableCell>{formatCurrency(item.amount)}</TableCell>
                      <TableCell>
                        <Badge
                          variant={item.action === 'approved' ? 'success' : 'destructive'}
                        >
                          {item.action}
                        </Badge>
                      </TableCell>
                      <TableCell>{formatDateTime(item.actionDate)}</TableCell>
                      <TableCell className="max-w-xs truncate">{item.notes}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Delegated Approvals */}
        <TabsContent value="delegated">
          <Card>
            <CardHeader>
              <CardTitle>Delegated Approvals</CardTitle>
              <CardDescription>Approvals delegated to other executives</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {delegatedApprovals.map((item) => (
                  <div
                    key={item.id}
                    className="flex items-center justify-between rounded-lg border p-4"
                  >
                    <div>
                      <p className="font-medium">{item.title}</p>
                      <p className="text-sm text-muted-foreground">
                        Delegated to {item.delegatedTo} • {item.reason}
                      </p>
                    </div>
                    <Badge
                      variant={item.status === 'approved' ? 'success' : 'secondary'}
                    >
                      {item.status}
                    </Badge>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Approval Dialog */}
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {action === 'approve' ? 'Approve' : 'Reject'} Request
            </DialogTitle>
            <DialogDescription>
              {selectedApproval?.title}
              {selectedApproval?.amount && ` - ${formatCurrency(selectedApproval.amount)}`}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="notes">Notes (Optional)</Label>
              <Textarea
                id="notes"
                placeholder={
                  action === 'approve'
                    ? 'Add any conditions or notes for this approval...'
                    : 'Please provide a reason for rejection...'
                }
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                rows={4}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDialogOpen(false)}>
              Cancel
            </Button>
            <Button
              variant={action === 'reject' ? 'destructive' : 'default'}
              onClick={confirmAction}
            >
              {action === 'approve' ? 'Approve' : 'Reject'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}
