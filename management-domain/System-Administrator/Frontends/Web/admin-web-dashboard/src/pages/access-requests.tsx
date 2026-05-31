import { useState } from 'react'
import { Search, Filter, Check, X, FileText, Clock } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Textarea } from '@shared/components/ui/textarea'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
import { Label } from '@shared/components/ui/label'
import { cn, formatDateTime, formatRelativeTime } from '@shared/utils/cn'
import { mockAccessRequests } from '@shared/data/mockData'

interface ReviewDialogProps {
  request: typeof mockAccessRequests[0] | null
  open: boolean
  onClose: () => void
  onApprove: (comment: string) => void
  onReject: (comment: string) => void
}

function ReviewDialog({ request, open, onClose, onApprove, onReject }: ReviewDialogProps) {
  const [comment, setComment] = useState('')
  const [action, setAction] = useState<'approve' | 'reject' | null>(null)

  if (!request) return null

  const handleAction = () => {
    if (action === 'approve') {
      onApprove(comment)
    } else if (action === 'reject') {
      onReject(comment)
    }
    setComment('')
    setAction(null)
    onClose()
  }

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Review Access Request</DialogTitle>
          <DialogDescription>
            Review and respond to {request.userName}&apos;s access request
          </DialogDescription>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label>User</Label>
            <p className="text-sm font-medium">{request.userName}</p>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>Resource</Label>
              <p className="text-sm">{request.resource}</p>
            </div>
            <div className="space-y-2">
              <Label>Access Type</Label>
              <Badge variant="outline">{request.accessType}</Badge>
            </div>
          </div>
          <div className="space-y-2">
            <Label>Reason</Label>
            <p className="text-sm text-muted-foreground">{request.reason}</p>
          </div>
          <div className="space-y-2">
            <Label htmlFor="comment">Comment (Optional)</Label>
            <Textarea
              id="comment"
              placeholder="Add a comment for the requester..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
          </div>
        </div>
        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={onClose}>Cancel</Button>
          <Button
            variant="destructive"
            onClick={() => setAction('reject')}
            disabled={action === 'approve'}
          >
            <X className="mr-2 h-4 w-4" />
            Reject
          </Button>
          <Button
            variant={action === 'reject' ? 'outline' : 'default'}
            onClick={() => setAction('approve')}
            className={action === 'approve' ? 'bg-green-600 hover:bg-green-700' : ''}
          >
            <Check className="mr-2 h-4 w-4" />
            Approve
          </Button>
          {action && (
            <Button onClick={handleAction}>
              Confirm {action}
            </Button>
          )}
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

export default function AccessRequestsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedTab, setSelectedTab] = useState('pending')
  const [selectedRequest, setSelectedRequest] = useState<typeof mockAccessRequests[0] | null>(null)
  const [dialogOpen, setDialogOpen] = useState(false)

  const pendingRequests = mockAccessRequests.filter(r => r.status === 'pending')
  const approvedRequests = mockAccessRequests.filter(r => r.status === 'approved')
  const rejectedRequests = mockAccessRequests.filter(r => r.status === 'rejected')

  const handleReview = (request: typeof mockAccessRequests[0]) => {
    setSelectedRequest(request)
    setDialogOpen(true)
  }

  const handleApprove = (comment: string) => {
    console.log('Approved:', selectedRequest?.id, 'Comment:', comment)
    // Handle approval logic
  }

  const handleReject = (comment: string) => {
    console.log('Rejected:', selectedRequest?.id, 'Comment:', comment)
    // Handle rejection logic
  }

  const RequestTable = ({ requests }: { requests: typeof mockAccessRequests }) => (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Requester</TableHead>
          <TableHead>Resource</TableHead>
          <TableHead>Access Type</TableHead>
          <TableHead>Requested</TableHead>
          <TableHead>Status</TableHead>
          <TableHead className="text-right">Actions</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {requests.map((request) => (
          <TableRow key={request.id}>
            <TableCell>
              <div>
                <p className="font-medium">{request.userName}</p>
                <p className="text-xs text-muted-foreground">{request.resourceType}</p>
              </div>
            </TableCell>
            <TableCell>
              <span className="text-sm">{request.resource}</span>
            </TableCell>
            <TableCell>
              <Badge variant="outline" className="capitalize">{request.accessType}</Badge>
            </TableCell>
            <TableCell>
              <span className="text-sm text-muted-foreground">{formatRelativeTime(request.requestedAt)}</span>
            </TableCell>
            <TableCell>
              <Badge
                variant={
                  request.status === 'approved' ? 'success' :
                  request.status === 'rejected' ? 'destructive' : 'warning'
                }
                className="capitalize"
              >
                {request.status}
              </Badge>
            </TableCell>
            <TableCell className="text-right">
              {request.status === 'pending' && (
                <div className="flex justify-end gap-2">
                  <Button variant="ghost" size="sm" onClick={() => handleReview(request)}>
                    Review
                  </Button>
                </div>
              )}
              {request.status !== 'pending' && (
                <Button variant="ghost" size="sm">
                  <FileText className="h-4 w-4" />
                </Button>
              )}
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Access Requests</h1>
          <p className="page-description">
            Review and manage user access requests across the system
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline">
            <Filter className="mr-2 h-4 w-4" />
            Filter
          </Button>
          <Button variant="admin">
            Request Access
          </Button>
        </div>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Pending Requests</CardDescription>
            <CardTitle className="text-2xl">{pendingRequests.length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Approved Today</CardDescription>
            <CardTitle className="text-2xl text-green-600">3</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Rejected Today</CardDescription>
            <CardTitle className="text-2xl text-red-600">1</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Avg. Response Time</CardDescription>
            <CardTitle className="text-2xl">2.4h</CardTitle>
          </CardHeader>
        </Card>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search by user, resource, or request ID..."
          className="pl-9"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="pending">
            Pending ({pendingRequests.length})
          </TabsTrigger>
          <TabsTrigger value="approved">
            Approved ({approvedRequests.length})
          </TabsTrigger>
          <TabsTrigger value="rejected">
            Rejected ({rejectedRequests.length})
          </TabsTrigger>
          <TabsTrigger value="all">All Requests</TabsTrigger>
        </TabsList>

        <TabsContent value="pending">
          <Card>
            <CardHeader>
              <CardTitle>Pending Access Requests</CardTitle>
              <CardDescription>Requests awaiting your review</CardDescription>
            </CardHeader>
            <CardContent>
              <RequestTable requests={pendingRequests} />
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="approved">
          <Card>
            <CardHeader>
              <CardTitle>Approved Requests</CardTitle>
              <CardDescription>Previously approved access requests</CardDescription>
            </CardHeader>
            <CardContent>
              <RequestTable requests={approvedRequests} />
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="rejected">
          <Card>
            <CardHeader>
              <CardTitle>Rejected Requests</CardTitle>
              <CardDescription>Requests that were denied access</CardDescription>
            </CardHeader>
            <CardContent>
              <RequestTable requests={rejectedRequests} />
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="all">
          <Card>
            <CardHeader>
              <CardTitle>All Access Requests</CardTitle>
              <CardDescription>Complete history of access requests</CardDescription>
            </CardHeader>
            <CardContent>
              <RequestTable requests={mockAccessRequests} />
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Review Dialog */}
      <ReviewDialog
        request={selectedRequest}
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        onApprove={handleApprove}
        onReject={handleReject}
      />
    </div>
  )
}
