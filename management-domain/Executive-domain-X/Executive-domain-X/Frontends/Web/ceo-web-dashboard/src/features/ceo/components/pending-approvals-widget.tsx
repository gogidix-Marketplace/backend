import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import * as React from 'react'
import { useApprovalQueue, useApprovalActions } from '@shared/services/api'

export type ApprovalType = 'budget' | 'hiring' | 'initiative'
export type ApprovalPriority = 'urgent' | 'high' | 'medium' | 'low'
export type ApprovalStatus = 'pending' | 'approved' | 'rejected' | 'delegated'

export interface PendingApproval {
  id: string
  type: ApprovalType
  title: string
  department: string
  amount: number
  priority: ApprovalPriority
  status: ApprovalStatus
}

// Fallback mock data for when API is unavailable
const fallbackApprovals: PendingApproval[] = [
  {
    id: '1',
    type: 'budget',
    title: 'Q3 Marketing Budget Revision',
    department: 'Digital Marketing',
    amount: 2500000,
    priority: 'high',
    status: 'pending',
  },
  {
    id: '2',
    type: 'hiring',
    title: 'CTO Office - Senior Engineers',
    department: 'System Admin',
    amount: 0,
    priority: 'medium',
    status: 'pending',
  },
  {
    id: '3',
    type: 'initiative',
    title: 'AI Integration Project',
    department: 'Executive',
    amount: 5000000,
    priority: 'urgent',
    status: 'pending',
  },
  {
    id: '4',
    type: 'budget',
    title: 'Equipment Upgrade - Sales Team',
    department: 'Sales',
    amount: 450000,
    priority: 'medium',
    status: 'pending',
  },
]

/**
 * Transform API ApprovalQueueItem to PendingApproval format
 */
function transformApiApproval(apiApproval: any): PendingApproval {
  // Map approval type
  const typeMap: Record<string, ApprovalType> = {
    budget_request: 'budget',
    budget: 'budget',
    hiring_request: 'hiring',
    hiring: 'hiring',
    initiative_approval: 'initiative',
    initiative: 'initiative',
    strategic_initiative: 'initiative',
  }

  // Map priority
  const priorityMap: Record<string, ApprovalPriority> = {
    critical: 'urgent',
    urgent: 'urgent',
    high: 'high',
    medium: 'medium',
    low: 'low',
  }

  // Extract amount
  const amount = apiApproval.amount ?? apiApproval.budgetAmount ?? apiApproval.requestedAmount ?? 0

  return {
    id: apiApproval.id || apiApproval.approvalId || '',
    type: typeMap[apiApproval.type?.toLowerCase()] || 'budget',
    title: apiApproval.title || apiApproval.requestTitle || apiApproval.subject || 'Approval Request',
    department: apiApproval.department || apiApproval.requesterDepartment || apiApproval.domain || 'Unknown',
    amount,
    priority: priorityMap[apiApproval.priority?.toLowerCase()] || 'medium',
    status: apiApproval.status?.toLowerCase() === 'pending' ? 'pending' : 'pending',
  }
}

const priorityConfig = {
  urgent: { label: 'Urgent', color: 'bg-red-100 text-red-700 dark:bg-red-900/20 dark:text-red-400 border-red-200 dark:border-red-800' },
  high: { label: 'High', color: 'bg-orange-100 text-orange-700 dark:bg-orange-900/20 dark:text-orange-400 border-orange-200 dark:border-orange-800' },
  medium: { label: 'Medium', color: 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/20 dark:text-yellow-400 border-yellow-200 dark:border-yellow-800' },
  low: { label: 'Low', color: 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400 border-green-200 dark:border-green-800' },
}

const typeIcons = {
  budget: '💰',
  hiring: '👥',
  initiative: '🚀',
}

export interface PendingApprovalsWidgetProps {
  onApprove?: (id: string) => void
  onReject?: (id: string) => void
  onReview?: (id: string) => void
  filter?: ApprovalType | 'all'
  maxVisible?: number
  className?: string
  /**
   * Force use of mock data (for testing/demos)
   * @default false - will try API first, fall back to mock on error
   */
  useMockData?: boolean
}

export function PendingApprovalsWidget({
  onApprove,
  onReject,
  onReview,
  filter = 'all',
  maxVisible = 5,
  className,
  useMockData = false,
}: PendingApprovalsWidgetProps) {
  const [selectedFilter, setSelectedFilter] = React.useState<ApprovalType | 'all'>(filter)
  const [expandedId, setExpandedId] = React.useState<string | null>(null)
  const [processingId, setProcessingId] = React.useState<string | null>(null)

  // Fetch pending approvals from API
  const { items, loading } = useApprovalQueue(
    { status: 'PENDING' as const },
    { page: 0, size: 20 },
    useMockData ? undefined : 60000
  )

  // Use approval actions for quick approve/reject
  const { approve, reject, loading: actionLoading } = useApprovalActions()

  // Transform API data or use fallback
  let approvals: PendingApproval[] = fallbackApprovals

  if (!useMockData && items.length > 0) {
    approvals = items.map(transformApiApproval)
  }

  const filteredApprovals =
    selectedFilter === 'all'
      ? approvals
      : approvals.filter((a) => a.type === selectedFilter)

  const visibleApprovals = filteredApprovals.slice(0, maxVisible)
  const pendingCount = approvals.filter((a) => a.status === 'pending').length

  const formatCurrency = (amount: number) => {
    if (amount === 0) return '-'
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount)
  }

  // Handle approve with API call
  const handleApprove = async (id: string) => {
    if (useMockData) {
      onApprove?.(id)
      return
    }

    setProcessingId(id)
    try {
      await approve(id, { comments: 'Approved from dashboard' })
      onApprove?.(id)
    } catch (error) {
      console.error('Failed to approve:', error)
    } finally {
      setProcessingId(null)
    }
  }

  // Handle reject with API call
  const handleReject = async (id: string) => {
    if (useMockData) {
      onReject?.(id)
      return
    }

    setProcessingId(id)
    try {
      await reject(id, { reason: 'Rejected from dashboard' })
      onReject?.(id)
    } catch (error) {
      console.error('Failed to reject:', error)
    } finally {
      setProcessingId(null)
    }
  }

  return (
    <Card className={className}>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <div>
            <CardTitle className="text-lg">Pending Approvals</CardTitle>
            <p className="text-sm text-muted-foreground">
              {loading && !useMockData ? 'Loading...' : 'Items requiring attention'}
            </p>
          </div>
          <Badge variant="destructive" className="text-xs">{pendingCount}</Badge>
        </div>

        {/* Filter Tabs */}
        <div className="flex gap-1 mt-2 overflow-x-auto">
          {(['all', 'budget', 'hiring', 'initiative'] as const).map((type) => (
            <button
              key={type}
              onClick={() => setSelectedFilter(type)}
              className={cn(
                'px-2.5 py-1 text-xs font-medium rounded-md transition-colors whitespace-nowrap',
                selectedFilter === type
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-muted text-muted-foreground hover:bg-muted/80'
              )}
            >
              {type === 'all' ? 'All' : type.charAt(0).toUpperCase() + type.slice(1)}
            </button>
          ))}
        </div>
      </CardHeader>

      <CardContent className="p-0">
        {loading && !useMockData ? (
          <div className="text-center py-8 text-muted-foreground">
            <div className="animate-pulse">Loading approvals...</div>
          </div>
        ) : visibleApprovals.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">
            <p className="text-sm">No pending approvals</p>
          </div>
        ) : (
          <div className="divide-y">
            {visibleApprovals.map((approval) => {
              const priorityStyle = priorityConfig[approval.priority]
              const isProcessing = processingId === approval.id || actionLoading

              return (
                <React.Fragment key={approval.id}>
                  <div
                    className={cn('p-3 hover:bg-muted/30 cursor-pointer transition-colors', expandedId === approval.id && 'bg-muted/30')}
                    onClick={() => setExpandedId(expandedId === approval.id ? null : approval.id)}
                  >
                    <div className="flex items-start gap-3">
                      {/* Type Icon */}
                      <span className="text-xl">{typeIcons[approval.type]}</span>

                      {/* Content */}
                      <div className="flex-1 min-w-0">
                        <div className="flex items-start justify-between gap-2 mb-1">
                          <p className="font-medium text-sm truncate">{approval.title}</p>
                          <Badge className={cn('text-xs', priorityStyle.color)}>
                            {priorityStyle.label}
                          </Badge>
                        </div>
                        <p className="text-xs text-muted-foreground">{approval.department}</p>
                        <div className="flex items-center justify-between mt-2">
                          <span className="text-sm font-medium">{formatCurrency(approval.amount)}</span>
                          <div className="flex gap-1" onClick={(e) => e.stopPropagation()}>
                            <Button
                              variant="ghost"
                              size="sm"
                              className="h-7 w-7 p-0 text-emerald-600 hover:text-emerald-700 hover:bg-emerald-50"
                              onClick={() => handleApprove(approval.id)}
                              disabled={isProcessing}
                            >
                              ✓
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              className="h-7 w-7 p-0 text-red-600 hover:text-red-700 hover:bg-red-50"
                              onClick={() => handleReject(approval.id)}
                              disabled={isProcessing}
                            >
                              ✕
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              className="h-7 w-7 p-0"
                              onClick={() => onReview?.(approval.id)}
                            >
                              …
                            </Button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </React.Fragment>
              )
            })}
          </div>
        )}
      </CardContent>
    </Card>
  )
}
