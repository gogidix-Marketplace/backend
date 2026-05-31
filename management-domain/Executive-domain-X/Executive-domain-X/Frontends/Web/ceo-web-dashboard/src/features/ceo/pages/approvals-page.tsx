import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@shared/components/ui/dialog'
import { Textarea } from '@shared/components/ui/textarea'
import { Check, X, Eye, Clock, CheckCircle2, XCircle, ArrowUpDown, Filter } from 'lucide-react'
import { cn, formatCurrency } from '@shared/utils/cn'

/**
 * CEO Approvals Page
 *
 * Components:
 * - ApprovalQueue - Tabbed view: Pending, Approved, Rejected, Delegated
 * - ApprovalDetailModal - Full request details with comments
 * - ApprovalWorkflow - Multi-stage approval visualization
 */

type ApprovalStatus = 'pending' | 'approved' | 'rejected' | 'delegated'
type ApprovalType = 'budget' | 'hiring' | 'initiative'
type Priority = 'urgent' | 'high' | 'medium' | 'low'

interface Approval {
  id: string
  type: ApprovalType
  title: string
  department: string
  amount?: number
  requestedBy: string
  requestDate: string
  priority: Priority
  status: ApprovalStatus
  description: string
  workflow?: WorkflowStage[]
  comments?: Comment[]
}

interface WorkflowStage {
  name: string
  role: string
  status: 'pending' | 'approved' | 'rejected'
  date?: string
  approver?: string
}

interface Comment {
  id: string
  author: string
  text: string
  timestamp: string
}

const mockApprovals: Approval[] = [
  {
    id: '1',
    type: 'budget',
    title: 'Q3 Marketing Budget Revision',
    department: 'Digital Marketing',
    amount: 2500000,
    requestedBy: 'Sarah Chen',
    requestDate: '2024-03-01',
    priority: 'high',
    status: 'pending',
    description: 'Additional budget required for Q3 campaigns in European markets. Market conditions have changed since initial planning.',
    workflow: [
      { name: 'Department Head', role: 'VP Marketing', status: 'approved', date: '2024-03-01', approver: 'Mike Johnson' },
      { name: 'Budget Review', role: 'CFO', status: 'approved', date: '2024-03-02', approver: 'CFO Office' },
      { name: 'Final Approval', role: 'CEO', status: 'pending' },
    ],
  },
  {
    id: '2',
    type: 'hiring',
    title: 'CTO Office - Senior Engineers',
    department: 'System Admin',
    requestedBy: 'Mike Johnson',
    requestDate: '2024-03-01',
    priority: 'medium',
    status: 'pending',
    description: 'Request to hire 3 senior engineers for infrastructure team. Critical for upcoming platform improvements.',
    workflow: [
      { name: 'Department Head', role: 'CTO', status: 'approved', date: '2024-03-01', approver: 'CTO Office' },
      { name: 'HR Review', role: 'CHRO', status: 'approved', date: '2024-03-02', approver: 'HR Department' },
      { name: 'Final Approval', role: 'CEO', status: 'pending' },
    ],
  },
  {
    id: '3',
    type: 'initiative',
    title: 'AI Integration Project',
    department: 'Executive',
    amount: 5000000,
    requestedBy: 'CTO Office',
    requestDate: '2024-02-28',
    priority: 'urgent',
    status: 'pending',
    description: 'Strategic initiative to integrate AI across all product lines. This includes ML models for predictions, NLP for customer support, and automated decision systems.',
    workflow: [
      { name: 'Technical Review', role: 'CTO', status: 'approved', date: '2024-02-28', approver: 'CTO Office' },
      { name: 'Financial Review', role: 'CFO', status: 'approved', date: '2024-03-01', approver: 'CFO Office' },
      { name: 'Strategic Review', role: 'COO', status: 'approved', date: '2024-03-02', approver: 'COO Office' },
      { name: 'Final Approval', role: 'CEO', status: 'pending' },
    ],
  },
]

export default function ApprovalsPage() {
  const [activeTab, setActiveTab] = useState<ApprovalStatus>('pending')
  const [selectedApproval, setSelectedApproval] = useState<Approval | null>(null)
  const [detailModalOpen, setDetailModalOpen] = useState(false)
  const [commentText, setCommentText] = useState('')
  const [processing, setProcessing] = useState<string | null>(null)

  const handleAction = async (id: string, action: 'approve' | 'reject') => {
    setProcessing(id)
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000))
    setProcessing(null)
    // In real app, update the approval status
  }

  const handleViewDetails = (approval: Approval) => {
    setSelectedApproval(approval)
    setDetailModalOpen(true)
  }

  const filteredApprovals = mockApprovals.filter(a => a.status === activeTab)

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Approvals</h1>
        <p className="page-description">
          Review and approve requests across the organization
        </p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-4 gap-4">
        <Card className={cn('border-l-4', activeTab === 'pending' && 'border-l-amber-500')}>
          <CardContent className="pt-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Pending</p>
                <p className="text-2xl font-bold">{mockApprovals.filter(a => a.status === 'pending').length}</p>
              </div>
              <Clock className="h-8 w-8 text-amber-500" />
            </div>
          </CardContent>
        </Card>
        <Card className="border-l-4 border-l-green-500">
          <CardContent className="pt-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Approved</p>
                <p className="text-2xl font-bold">{mockApprovals.filter(a => a.status === 'approved').length}</p>
              </div>
              <CheckCircle2 className="h-8 w-8 text-green-500" />
            </div>
          </CardContent>
        </Card>
        <Card className="border-l-4 border-l-red-500">
          <CardContent className="pt-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Rejected</p>
                <p className="text-2xl font-bold">{mockApprovals.filter(a => a.status === 'rejected').length}</p>
              </div>
              <XCircle className="h-8 w-8 text-red-500" />
            </div>
          </CardContent>
        </Card>
        <Card className="border-l-4 border-l-blue-500">
          <CardContent className="pt-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Delegated</p>
                <p className="text-2xl font-bold">{mockApprovals.filter(a => a.status === 'delegated').length}</p>
              </div>
              <ArrowUpDown className="h-8 w-8 text-blue-500" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Approval Queue */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Approval Queue</CardTitle>
            <div className="flex items-center gap-2">
              <Button variant="outline" size="sm">
                <Filter className="h-4 w-4 mr-1" />
                Filter
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <Tabs value={activeTab} onValueChange={(v) => setActiveTab(v as ApprovalStatus)}>
            <TabsList>
              <TabsTrigger value="pending">Pending</TabsTrigger>
              <TabsTrigger value="approved">Approved</TabsTrigger>
              <TabsTrigger value="rejected">Rejected</TabsTrigger>
              <TabsTrigger value="delegated">Delegated</TabsTrigger>
            </TabsList>

            <TabsContent value={activeTab} className="mt-4">
              {filteredApprovals.length === 0 ? (
                <div className="text-center py-12 text-muted-foreground">
                  <CheckCircle2 className="h-12 w-12 mx-auto mb-3 opacity-50" />
                  <p>No {activeTab} approvals</p>
                </div>
              ) : (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Type</TableHead>
                      <TableHead>Title</TableHead>
                      <TableHead>Department</TableHead>
                      <TableHead>Amount</TableHead>
                      <TableHead>Requested By</TableHead>
                      <TableHead>Priority</TableHead>
                      <TableHead>Date</TableHead>
                      <TableHead className="text-right">Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredApprovals.map((approval) => (
                      <TableRow key={approval.id}>
                        <TableCell>
                          <Badge variant="outline">{approval.type}</Badge>
                        </TableCell>
                        <TableCell>
                          <button
                            onClick={() => handleViewDetails(approval)}
                            className="font-medium hover:underline text-left"
                          >
                            {approval.title}
                          </button>
                        </TableCell>
                        <TableCell>{approval.department}</TableCell>
                        <TableCell>
                          {approval.amount ? formatCurrency(approval.amount) : '-'}
                        </TableCell>
                        <TableCell>{approval.requestedBy}</TableCell>
                        <TableCell>
                          <Badge
                            variant={
                              approval.priority === 'urgent' ? 'destructive' :
                              approval.priority === 'high' ? 'default' : 'secondary'
                            }
                          >
                            {approval.priority}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          {new Date(approval.requestDate).toLocaleDateString()}
                        </TableCell>
                        <TableCell className="text-right">
                          <div className="flex items-center justify-end gap-1">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleViewDetails(approval)}
                            >
                              <Eye className="h-4 w-4" />
                            </Button>
                            {approval.status === 'pending' && (
                              <>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="text-green-600 hover:text-green-700"
                                  onClick={() => handleAction(approval.id, 'approve')}
                                  disabled={processing === approval.id}
                                >
                                  <Check className="h-4 w-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="text-red-600 hover:text-red-700"
                                  onClick={() => handleAction(approval.id, 'reject')}
                                  disabled={processing === approval.id}
                                >
                                  <X className="h-4 w-4" />
                                </Button>
                              </>
                            )}
                          </div>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              )}
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>

      {/* Detail Modal */}
      <Dialog open={detailModalOpen} onOpenChange={setDetailModalOpen}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          {selectedApproval && (
            <>
              <DialogHeader>
                <div className="flex items-start justify-between">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <DialogTitle className="text-xl">{selectedApproval.title}</DialogTitle>
                      <Badge variant="outline">{selectedApproval.type}</Badge>
                      <Badge
                        variant={
                          selectedApproval.priority === 'urgent' ? 'destructive' :
                          selectedApproval.priority === 'high' ? 'default' : 'secondary'
                        }
                      >
                        {selectedApproval.priority}
                      </Badge>
                    </div>
                    <DialogDescription>
                      {selectedApproval.department} • Requested by {selectedApproval.requestedBy}
                    </DialogDescription>
                  </div>
                </div>
              </DialogHeader>

              <div className="space-y-6">
                {/* Description */}
                <div>
                  <h4 className="text-sm font-medium mb-2">Description</h4>
                  <p className="text-sm text-muted-foreground">{selectedApproval.description}</p>
                </div>

                {selectedApproval.amount && (
                  <div>
                    <h4 className="text-sm font-medium mb-2">Amount</h4>
                    <p className="text-2xl font-bold">{formatCurrency(selectedApproval.amount)}</p>
                  </div>
                )}

                {/* Workflow */}
                {selectedApproval.workflow && (
                  <div>
                    <h4 className="text-sm font-medium mb-4">Approval Workflow</h4>
                    <div className="space-y-4">
                      {selectedApproval.workflow.map((stage, index) => (
                        <div key={index} className="flex items-start gap-4">
                          <div className={cn(
                            'flex h-8 w-8 items-center justify-center rounded-full',
                            stage.status === 'approved' ? 'bg-green-100 text-green-600' :
                            stage.status === 'rejected' ? 'bg-red-100 text-red-600' :
                            'bg-slate-100 text-slate-600'
                          )}>
                            {stage.status === 'approved' ? <Check className="h-4 w-4" /> :
                             stage.status === 'rejected' ? <X className="h-4 w-4" /> :
                             <Clock className="h-4 w-4" />}
                          </div>
                          <div className="flex-1 min-w-0">
                            <p className="font-medium">{stage.name}</p>
                            <p className="text-sm text-muted-foreground">{stage.role}</p>
                            {stage.approver && (
                              <p className="text-xs text-muted-foreground">
                                Approved by {stage.approver}
                                {stage.date && ` on ${new Date(stage.date).toLocaleDateString()}`}
                              </p>
                            )}
                          </div>
                          {index < selectedApproval.workflow!.length - 1 && (
                            <div className="absolute left-4 mt-8 w-0.5 h-8 bg-slate-200" />
                          )}
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {/* Comments */}
                <div>
                  <h4 className="text-sm font-medium mb-2">Add Comment</h4>
                  <Textarea
                    placeholder="Enter your comments..."
                    value={commentText}
                    onChange={(e) => setCommentText(e.target.value)}
                    rows={3}
                  />
                </div>

                {/* Actions */}
                {selectedApproval.status === 'pending' && (
                  <div className="flex justify-end gap-2 pt-4 border-t">
                    <Button variant="outline" onClick={() => setDetailModalOpen(false)}>
                      Cancel
                    </Button>
                    <Button
                      variant="destructive"
                      onClick={() => {
                        handleAction(selectedApproval.id, 'reject')
                        setDetailModalOpen(false)
                      }}
                      disabled={processing === selectedApproval.id}
                    >
                      Reject
                    </Button>
                    <Button
                      onClick={() => {
                        handleAction(selectedApproval.id, 'approve')
                        setDetailModalOpen(false)
                      }}
                      disabled={processing === selectedApproval.id}
                    >
                      Approve
                    </Button>
                  </div>
                )}
              </div>
            </>
          )}
        </DialogContent>
      </Dialog>
    </div>
  )
}
