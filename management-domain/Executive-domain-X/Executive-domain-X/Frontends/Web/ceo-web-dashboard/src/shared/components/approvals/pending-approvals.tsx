import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import { Check, X, Eye, ArrowUpRight, FileText, Users, Briefcase } from 'lucide-react'
import { formatCurrency } from '@shared/utils/cn'
import { useState } from 'react'

/**
 * PendingApprovalsWidget Component
 *
 * List of items requiring approval
 * Quick actions: Approve, Reject, Review
 * Badge count of pending items
 * Filter by type: Budget, Hiring, Initiative
 */

export type ApprovalType = 'budget' | 'hiring' | 'initiative'
export type ApprovalPriority = 'urgent' | 'high' | 'medium' | 'low'

export interface PendingApproval {
  id: string
  type: ApprovalType
  title: string
  department: string
  amount?: number
  requestedBy: string
  requestDate: string
  priority: ApprovalPriority
  description?: string
}

export interface PendingApprovalsProps {
  approvals?: PendingApproval[]
  onAction?: (id: string, action: 'approve' | 'reject' | 'review') => void
  onViewAll?: () => void
  loading?: boolean
}

const defaultApprovals: PendingApproval[] = [
  {
    id: '1',
    type: 'budget',
    title: 'Q3 Marketing Budget Revision',
    department: 'Digital Marketing',
    amount: 2500000,
    requestedBy: 'Sarah Chen',
    requestDate: '2024-03-01',
    priority: 'high',
    description: 'Additional budget for Q3 campaigns in European markets.',
  },
  {
    id: '2',
    type: 'hiring',
    title: 'CTO Office - Senior Engineers',
    department: 'System Admin',
    requestedBy: 'Mike Johnson',
    requestDate: '2024-03-01',
    priority: 'medium',
    description: 'Request to hire 3 senior engineers for infrastructure team.',
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
    description: 'Strategic initiative to integrate AI across all product lines.',
  },
  {
    id: '4',
    type: 'budget',
    title: 'Infrastructure Upgrade',
    department: 'Operations',
    amount: 1200000,
    requestedBy: 'IT Department',
    requestDate: '2024-02-27',
    priority: 'high',
  },
]

const typeConfig = {
  budget: {
    icon: Briefcase,
    label: 'Budget',
    color: 'text-blue-600',
    bgColor: 'bg-blue-100 dark:bg-blue-900/20',
  },
  hiring: {
    icon: Users,
    label: 'Hiring',
    color: 'text-green-600',
    bgColor: 'bg-green-100 dark:bg-green-900/20',
  },
  initiative: {
    icon: FileText,
    label: 'Initiative',
    color: 'text-purple-600',
    bgColor: 'bg-purple-100 dark:bg-purple-900/20',
  },
}

const priorityConfig = {
  urgent: { label: 'Urgent', variant: 'destructive' as const },
  high: { label: 'High', variant: 'default' as const },
  medium: { label: 'Medium', variant: 'secondary' as const },
  low: { label: 'Low', variant: 'outline' as const },
}

export function PendingApprovalsWidget({
  approvals = defaultApprovals,
  onAction,
  onViewAll,
  loading = false,
}: PendingApprovalsProps) {
  const [filter, setFilter] = useState<ApprovalType | 'all'>('all')
  const [processing, setProcessing] = useState<Set<string>>(new Set())

  const filteredApprovals =
    filter === 'all' ? approvals : approvals.filter((a) => a.type === filter)

  const handleAction = async (id: string, action: 'approve' | 'reject' | 'review') => {
    setProcessing((prev) => new Set([...prev, id]))
    // Simulate async action
    await new Promise((resolve) => setTimeout(resolve, 500))
    setProcessing((prev) => {
      const next = new Set(prev)
      next.delete(id)
      return next
    })
    onAction?.(id, action)
  }

  const typeCounts = {
    budget: approvals.filter((a) => a.type === 'budget').length,
    hiring: approvals.filter((a) => a.type === 'hiring').length,
    initiative: approvals.filter((a) => a.type === 'initiative').length,
  }

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <CardTitle>Pending Approvals</CardTitle>
            <Badge variant="destructive">{approvals.length}</Badge>
          </div>
          <Button variant="outline" size="sm" onClick={onViewAll}>
            View All
            <ArrowUpRight className="h-3 w-3 ml-1" />
          </Button>
        </div>

        {/* Filter Tabs */}
        <div className="flex gap-1 mt-2">
          <Button
            variant={filter === 'all' ? 'default' : 'ghost'}
            size="sm"
            className="h-7 text-xs"
            onClick={() => setFilter('all')}
          >
            All ({approvals.length})
          </Button>
          <Button
            variant={filter === 'budget' ? 'default' : 'ghost'}
            size="sm"
            className="h-7 text-xs"
            onClick={() => setFilter('budget')}
          >
            Budget ({typeCounts.budget})
          </Button>
          <Button
            variant={filter === 'hiring' ? 'default' : 'ghost'}
            size="sm"
            className="h-7 text-xs"
            onClick={() => setFilter('hiring')}
          >
            Hiring ({typeCounts.hiring})
          </Button>
          <Button
            variant={filter === 'initiative' ? 'default' : 'ghost'}
            size="sm"
            className="h-7 text-xs"
            onClick={() => setFilter('initiative')}
          >
            Initiative ({typeCounts.initiative})
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        {loading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-20 bg-slate-100 dark:bg-slate-800 rounded-lg animate-pulse" />
            ))}
          </div>
        ) : filteredApprovals.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">
            <Check className="h-8 w-8 mx-auto mb-2 opacity-50" />
            <p>No pending {filter !== 'all' ? filter : ''} approvals</p>
          </div>
        ) : (
          <div className="space-y-3">
            {filteredApprovals.slice(0, 5).map((approval) => {
              const config = typeConfig[approval.type]
              const Icon = config.icon
              const isProcessing = processing.has(approval.id)

              return (
                <div
                  key={approval.id}
                  className="p-3 rounded-lg border hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors"
                >
                  <div className="flex items-start justify-between gap-3">
                    <div className="flex items-start gap-3 flex-1 min-w-0">
                      {/* Type Icon */}
                      <div className={cn('flex h-8 w-8 items-center justify-center rounded-full', config.bgColor)}>
                        <Icon className={cn('h-4 w-4', config.color)} />
                      </div>

                      {/* Content */}
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center gap-2 mb-1">
                          <h4 className="font-medium text-sm truncate">{approval.title}</h4>
                          <Badge
                            variant={priorityConfig[approval.priority].variant}
                            className="text-xs h-5"
                          >
                            {priorityConfig[approval.priority].label}
                          </Badge>
                        </div>
                        <p className="text-xs text-muted-foreground">{approval.department}</p>
                        <div className="flex items-center gap-2 mt-1 text-xs text-muted-foreground">
                          <span>{approval.requestedBy}</span>
                          <span>•</span>
                          <span>{new Date(approval.requestDate).toLocaleDateString()}</span>
                          {approval.amount !== undefined && (
                            <>
                              <span>•</span>
                              <span className="font-medium">{formatCurrency(approval.amount)}</span>
                            </>
                          )}
                        </div>
                      </div>
                    </div>

                    {/* Actions */}
                    <div className="flex items-center gap-1">
                      <Button
                        variant="ghost"
                        size="sm"
                        className="h-7 w-7 p-0 text-muted-foreground hover:text-blue-600"
                        onClick={() => handleAction(approval.id, 'review')}
                        disabled={isProcessing}
                      >
                        <Eye className="h-3 w-3" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        className="h-7 w-7 p-0 text-muted-foreground hover:text-red-600"
                        onClick={() => handleAction(approval.id, 'reject')}
                        disabled={isProcessing}
                      >
                        <X className="h-3 w-3" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        className={cn(
                          'h-7 w-7 p-0 text-muted-foreground hover:text-green-600',
                          isProcessing && 'animate-pulse'
                        )}
                        onClick={() => handleAction(approval.id, 'approve')}
                        disabled={isProcessing}
                      >
                        <Check className="h-3 w-3" />
                      </Button>
                    </div>
                  </div>
                </div>
              )
            })}
          </div>
        )}
      </CardContent>
    </Card>
  )
}
