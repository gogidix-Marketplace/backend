import { useState } from 'react'
import {
  TrendingUp,
  Star,
  Target,
  Calendar,
  Clock,
  CheckCircle,
  BarChart3,
  Award,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { cn } from '@shared/utils/cn'
import { performanceReviews } from '@shared/data/mockData'

const ratingStars = (rating: number) => {
  return Array.from({ length: 5 }, (_, i) => (
    <Star
      key={i}
      className={cn('h-4 w-4', i < rating ? 'text-yellow-500 fill-yellow-500' : 'text-slate-300')}
    />
  ))
}

const goalStatusConfig: Record<string, { label: string; variant: 'default' | 'success' | 'warning' | 'destructive' | 'secondary' | 'info' }> = {
  NOT_STARTED: { label: 'Not Started', variant: 'secondary' },
  IN_PROGRESS: { label: 'In Progress', variant: 'info' },
  COMPLETED: { label: 'Completed', variant: 'success' },
  OVERDUE: { label: 'Overdue', variant: 'destructive' },
}

const reviewStatusConfig: Record<string, { label: string; variant: 'default' | 'success' | 'warning' | 'secondary' }> = {
  DRAFT: { label: 'Draft', variant: 'secondary' },
  SUBMITTED: { label: 'Submitted', variant: 'warning' },
  REVIEWED: { label: 'Reviewed', variant: 'success' },
}

export default function PerformancePage() {
  const avgRating = performanceReviews.reduce((sum, r) => sum + r.overallRating, 0) / performanceReviews.length
  const completedReviews = performanceReviews.filter(r => r.status === 'REVIEWED').length
  const pendingReviews = performanceReviews.filter(r => r.status === 'SUBMITTED').length
  const totalGoals = performanceReviews.reduce((sum, r) => sum + r.goals.length, 0)
  const completedGoals = performanceReviews.reduce((sum, r) => sum + r.goals.filter(g => g.status === 'COMPLETED').length, 0)

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Performance</h1>
          <p className="page-description">Employee performance reviews and goal tracking</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <BarChart3 className="mr-2 h-4 w-4" />
            Reports
          </Button>
          <Button variant="hr" size="sm">
            <Calendar className="mr-2 h-4 w-4" />
            New Review Cycle
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-yellow-100 p-2 dark:bg-yellow-900/20">
                <Star className="h-4 w-4 text-yellow-600 dark:text-yellow-400" />
              </div>
              <div>
                <div className="flex items-center gap-1">
                  <p className="text-2xl font-bold">{avgRating.toFixed(1)}</p>
                  <span className="text-sm text-muted-foreground">/5</span>
                </div>
                <p className="text-xs text-muted-foreground">Average Rating</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-green-100 p-2 dark:bg-green-900/20">
                <CheckCircle className="h-4 w-4 text-green-600 dark:text-green-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{completedReviews}</p>
                <p className="text-xs text-muted-foreground">Reviews Completed</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <Clock className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{pendingReviews}</p>
                <p className="text-xs text-muted-foreground">Pending Reviews</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 p-2 dark:bg-purple-900/20">
                <Target className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{completedGoals}/{totalGoals}</p>
                <p className="text-xs text-muted-foreground">Goals Completed</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="reviews" className="space-y-4">
        <TabsList>
          <TabsTrigger value="reviews">Performance Reviews</TabsTrigger>
          <TabsTrigger value="goals">Goals</TabsTrigger>
          <TabsTrigger value="calibration">Calibration</TabsTrigger>
        </TabsList>

        <TabsContent value="reviews" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Performance Reviews</CardTitle>
              <CardDescription>Current review cycle performance assessments</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Reviewer</TableHead>
                    <TableHead>Period</TableHead>
                    <TableHead>Rating</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Strengths</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {performanceReviews.map((review) => (
                    <TableRow key={review.id}>
                      <TableCell className="font-medium">{review.employeeName}</TableCell>
                      <TableCell className="text-sm text-muted-foreground">{review.reviewerName}</TableCell>
                      <TableCell>{review.period}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1">{ratingStars(review.overallRating)}</div>
                      </TableCell>
                      <TableCell>
                        <Badge variant={reviewStatusConfig[review.status]?.variant || 'secondary'}>
                          {reviewStatusConfig[review.status]?.label || review.status}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex flex-wrap gap-1">
                          {review.strengths.slice(0, 2).map((s, i) => (
                            <Badge key={i} variant="outline" className="text-xs">{s}</Badge>
                          ))}
                          {review.strengths.length > 2 && (
                            <Badge variant="outline" className="text-xs">+{review.strengths.length - 2}</Badge>
                          )}
                        </div>
                      </TableCell>
                      <TableCell className="text-right">
                        <Button size="sm" variant="outline">View</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="goals" className="space-y-4">
          {performanceReviews.map((review) => (
            <Card key={review.id}>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div>
                    <CardTitle>{review.employeeName} — Goals</CardTitle>
                    <CardDescription>{review.period} Review Period</CardDescription>
                  </div>
                  <div className="flex items-center gap-1">{ratingStars(review.overallRating)}</div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {review.goals.map((goal) => (
                    <div key={goal.id} className="rounded-lg border p-4">
                      <div className="flex items-center justify-between mb-2">
                        <div>
                          <p className="font-medium">{goal.title}</p>
                          <p className="text-sm text-muted-foreground">{goal.description}</p>
                        </div>
                        <Badge variant={goalStatusConfig[goal.status]?.variant || 'secondary'}>
                          {goalStatusConfig[goal.status]?.label || goal.status}
                        </Badge>
                      </div>
                      <div className="flex items-center gap-3 mt-3">
                        <div className="h-2 flex-1 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                          <div
                            className={cn(
                              'h-full rounded-full',
                              goal.progress === 100 ? 'bg-green-500' : goal.progress >= 50 ? 'bg-blue-500' : 'bg-yellow-500'
                            )}
                            style={{ width: `${goal.progress}%` }}
                          />
                        </div>
                        <span className="text-sm text-muted-foreground w-12">{goal.progress}%</span>
                        <span className="text-xs text-muted-foreground">Due: {goal.targetDate}</span>
                      </div>
                      <div className="flex items-center gap-2 mt-2">
                        <Badge variant="outline" className="text-xs">{goal.category}</Badge>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="calibration" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Performance Calibration</CardTitle>
              <CardDescription>Distribution of performance ratings across the organization</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {[
                  { rating: 5, label: 'Exceptional', count: 45, percentage: 8, color: 'bg-green-500' },
                  { rating: 4, label: 'Exceeds Expectations', count: 180, percentage: 32, color: 'bg-blue-500' },
                  { rating: 3, label: 'Meets Expectations', count: 250, percentage: 44, color: 'bg-yellow-500' },
                  { rating: 2, label: 'Needs Improvement', count: 65, percentage: 12, color: 'bg-orange-500' },
                  { rating: 1, label: 'Below Expectations', count: 22, percentage: 4, color: 'bg-red-500' },
                ].map((row) => (
                  <div key={row.rating} className="space-y-2">
                    <div className="flex items-center justify-between text-sm">
                      <div className="flex items-center gap-2">
                        <div className="flex">{ratingStars(row.rating)}</div>
                        <span className="font-medium">{row.label}</span>
                      </div>
                      <div className="flex items-center gap-4">
                        <span className="text-muted-foreground">{row.count} employees</span>
                        <span className="font-medium w-12 text-right">{row.percentage}%</span>
                      </div>
                    </div>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                      <div className={cn('h-full rounded-full', row.color)} style={{ width: `${row.percentage}%` }} />
                    </div>
                  </div>
                ))}
              </div>
              <div className="mt-6 flex items-center gap-2 text-sm text-muted-foreground">
                <Award className="h-4 w-4" />
                <span>Total: 562 reviews across all departments</span>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
