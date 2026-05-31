import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatNumber } from '@shared/utils/cn'
import { aggregationBatchesData, realTimeDataStreamsData } from '@shared/mock-data'
import { Database, Play, Clock, CheckCircle, XCircle, AlertCircle, RefreshCw, Plus, Stream } from 'lucide-react'

export default function AggregationPage() {
  const [activeTab, setActiveTab] = useState<'batches' | 'streams' | 'schedule'>('batches')

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header flex items-center justify-between">
        <div>
          <h1 className="page-title">Data Aggregation</h1>
          <p className="page-description">
            Manage batch aggregation and real-time data streams
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          New Aggregation
        </Button>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium flex items-center gap-2">
              <Database className="h-4 w-4" />
              Total Batches
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{aggregationBatchesData.length}</div>
            <p className="text-xs text-muted-foreground">this month</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium flex items-center gap-2">
              <Stream className="h-4 w-4 text-green-500" />
              Active Streams
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{realTimeDataStreamsData.filter(s => s.isActive).length}</div>
            <p className="text-xs text-muted-foreground">real-time data</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium flex items-center gap-2">
              <CheckCircle className="h-4 w-4 text-green-500" />
              Completed
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{aggregationBatchesData.filter(b => b.status === 'completed').length}</div>
            <p className="text-xs text-muted-foreground">successful batches</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium flex items-center gap-2">
              <AlertCircle className="h-4 w-4 text-yellow-500" />
              Data Points
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatNumber(aggregationBatchesData.reduce((sum, b) => sum + b.dataPoints, 0))}</div>
            <p className="text-xs text-muted-foreground">aggregated records</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <div className="flex gap-2 border-b">
        <Button
          variant={activeTab === 'batches' ? 'gbm' : 'ghost'}
          onClick={() => setActiveTab('batches')}
        >
          Batch Aggregation
        </Button>
        <Button
          variant={activeTab === 'streams' ? 'gbm' : 'ghost'}
          onClick={() => setActiveTab('streams')}
        >
          Real-time Streams
        </Button>
        <Button
          variant={activeTab === 'schedule' ? 'gbm' : 'ghost'}
          onClick={() => setActiveTab('schedule')}
        >
          Schedule
        </Button>
      </div>

      {activeTab === 'batches' && (
        <>
          {/* Aggregation Batches */}
          <Card>
            <CardHeader>
              <CardTitle>Aggregation Batches</CardTitle>
              <CardDescription>Recent and scheduled batch aggregations</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Name</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Sources</TableHead>
                    <TableHead>Data Points</TableHead>
                    <TableHead>Started</TableHead>
                    <TableHead>Duration</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {aggregationBatchesData.map((batch) => {
                    const duration = batch.completedAt
                      ? new Date(batch.completedAt).getTime() - new Date(batch.startedAt).getTime()
                      : null
                    return (
                      <TableRow key={batch.id}>
                        <TableCell className="font-medium">{batch.name}</TableCell>
                        <TableCell>
                          <Badge variant="outline">{batch.type}</Badge>
                        </TableCell>
                        <TableCell>
                          {batch.sourceRegions.length > 0
                            ? `${batch.sourceRegions.length} regions`
                            : `${batch.sourceCountries.length} countries`}
                        </TableCell>
                        <TableCell>{formatNumber(batch.dataPoints)}</TableCell>
                        <TableCell>
                          {new Date(batch.startedAt).toLocaleString()}
                        </TableCell>
                        <TableCell>
                          {duration ? `${Math.round(duration / 60000)} min` : '-'}
                        </TableCell>
                        <TableCell>
                          <Badge className={cn(
                            batch.status === 'completed' && 'bg-green-100 text-green-800',
                            batch.status === 'running' && 'bg-blue-100 text-blue-800',
                            batch.status === 'pending' && 'bg-yellow-100 text-yellow-800',
                            batch.status === 'failed' && 'bg-red-100 text-red-800'
                          )}>
                            {batch.status === 'completed' && <CheckCircle className="inline h-3 w-3 mr-1" />}
                            {batch.status === 'running' && <RefreshCw className="inline h-3 w-3 mr-1 animate-spin" />}
                            {batch.status === 'pending' && <Clock className="inline h-3 w-3 mr-1" />}
                            {batch.status === 'failed' && <XCircle className="inline h-3 w-3 mr-1" />}
                            {batch.status}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center gap-2">
                            {batch.status === 'pending' && (
                              <Button variant="ghost" size="icon">
                                <Play className="h-4 w-4" />
                              </Button>
                            )}
                            <Button variant="ghost" size="icon">
                              <RefreshCw className="h-4 w-4" />
                            </Button>
                          </div>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </>
      )}

      {activeTab === 'streams' && (
        <>
          {/* Real-time Data Streams */}
          <Card>
            <CardHeader>
              <CardTitle>Real-time Data Streams</CardTitle>
              <CardDescription>Active and configured real-time data streams</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {realTimeDataStreamsData.map((stream) => (
                  <div key={stream.id} className="flex items-center justify-between p-4 border rounded-lg">
                    <div className="flex items-center gap-4">
                      <div className={cn(
                        'h-10 w-10 rounded-full flex items-center justify-center',
                        stream.isActive ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-400'
                      )}>
                        <Stream className="h-5 w-5" />
                      </div>
                      <div>
                        <p className="font-medium">{stream.name}</p>
                        <p className="text-sm text-muted-foreground">
                          {stream.metrics.length} metrics • every {stream.updateFrequency}s
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center gap-4">
                      <Badge variant={stream.isActive ? 'success' : 'secondary'}>
                        {stream.isActive ? 'Active' : 'Inactive'}
                      </Badge>
                      <span className="text-sm text-muted-foreground">
                        Last: {new Date(stream.lastUpdate).toLocaleTimeString()}
                      </span>
                      <Button variant="outline" size="sm">
                        Configure
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </>
      )}

      {activeTab === 'schedule' && (
        <>
          {/* Scheduled Aggregations */}
          <Card>
            <CardHeader>
              <CardTitle>Scheduled Aggregations</CardTitle>
              <CardDescription>Configure automatic data aggregation schedules</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {[
                  { name: 'Daily Global Sync', frequency: 'Daily at 02:00 UTC', nextRun: 'Tomorrow 02:00 UTC', active: true },
                  { name: 'Weekly Regional Summary', frequency: 'Every Monday 06:00 UTC', nextRun: 'Monday 06:00 UTC', active: true },
                  { name: 'Monthly Country Deep Dive', frequency: '1st of month 08:00 UTC', nextRun: 'Apr 1 08:00 UTC', active: true },
                  { name: 'Quarterly Executive Report', frequency: 'Quarter start 00:00 UTC', nextRun: 'Apr 1 00:00 UTC', active: false },
                ].map((schedule, index) => (
                  <div key={index} className="flex items-center justify-between p-4 border rounded-lg">
                    <div>
                      <p className="font-medium">{schedule.name}</p>
                      <p className="text-sm text-muted-foreground">{schedule.frequency}</p>
                    </div>
                    <div className="flex items-center gap-4">
                      <span className="text-sm text-muted-foreground">{schedule.nextRun}</span>
                      <Badge variant={schedule.active ? 'success' : 'secondary'}>
                        {schedule.active ? 'Active' : 'Inactive'}
                      </Badge>
                      <Button variant="ghost" size="icon">
                        <RefreshCw className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  )
}
