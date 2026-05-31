import { useState } from 'react'
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
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@shared/components/ui/dropdown-menu'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import {
  Search,
  Plus,
  MoreHorizontal,
  Calendar,
  Filter,
  TrendingUp,
  AlertCircle,
  CheckCircle2,
  XCircle,
} from 'lucide-react'
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Cell,
} from 'recharts'
import { formatCurrency, formatDate, getStageColor, getPriorityColor, getCountryFlag } from '@shared/utils/cn'
import { mockOpportunities, mockPipelineByStage } from '@shared/data/mockData'

const STAGE_ORDER = ['prospecting', 'qualification', 'proposal', 'negotiation', 'closed_won', 'closed_lost']

export default function OpportunitiesPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [stageFilter, setStageFilter] = useState<string>('all')
  const [viewMode, setViewMode] = useState<'table' | 'kanban'>('table')

  const filteredOpportunities = mockOpportunities.filter(opp => {
    const matchesSearch =
      searchQuery === '' ||
      opp.accountName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      opp.contactName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      opp.dealNumber.toLowerCase().includes(searchQuery.toLowerCase())

    const matchesStage = stageFilter === 'all' || opp.stage === stageFilter

    return matchesSearch && matchesStage
  })

  const getStageLabel = (stage: string) => {
    const labels: Record<string, string> = {
      prospecting: 'Prospecting',
      qualification: 'Qualification',
      proposal: 'Proposal',
      negotiation: 'Negotiation',
      closed_won: 'Closed Won',
      closed_lost: 'Closed Lost',
    }
    return labels[stage] || stage
  }

  const getPriorityLabel = (priority: string) => {
    const labels: Record<string, string> = {
      low: 'Low',
      medium: 'Medium',
      high: 'High',
      urgent: 'Urgent',
    }
    return labels[priority] || priority
  }

  const stageCounts = {
    all: mockOpportunities.length,
    prospecting: mockOpportunities.filter(o => o.stage === 'prospecting').length,
    qualification: mockOpportunities.filter(o => o.stage === 'qualification').length,
    proposal: mockOpportunities.filter(o => o.stage === 'proposal').length,
    negotiation: mockOpportunities.filter(o => o.stage === 'negotiation').length,
    closed_won: mockOpportunities.filter(o => o.stage === 'closed_won').length,
    closed_lost: mockOpportunities.filter(o => o.stage === 'closed_lost').length,
  }

  const totalPipeline = mockOpportunities.reduce((sum, o) => sum + o.value, 0)
  const weightedPipeline = mockOpportunities.reduce((sum, o) => sum + (o.value * o.probability / 100), 0)

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Opportunities Pipeline</h1>
          <p className="text-muted-foreground mt-1">
            Manage and track your sales pipeline
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            variant={viewMode === 'table' ? 'default' : 'outline'}
            size="sm"
            onClick={() => setViewMode('table')}
          >
            Table
          </Button>
          <Button
            variant={viewMode === 'kanban' ? 'default' : 'outline'}
            size="sm"
            onClick={() => setViewMode('kanban')}
          >
            Kanban
          </Button>
          <Button className="gap-2">
            <Plus className="h-4 w-4" />
            Add Opportunity
          </Button>
        </div>
      </div>

      {/* Pipeline Summary */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Pipeline
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(totalPipeline)}</div>
            <p className="text-xs text-muted-foreground mt-1">
              {mockOpportunities.length} active deals
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Weighted Pipeline
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(weightedPipeline)}</div>
            <p className="text-xs text-green-600 mt-1">
              {Math.round((weightedPipeline / totalPipeline) * 100)}% weighted value
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              This Month Closed
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(mockOpportunities.filter(o => o.stage === 'closed_won').reduce((sum, o) => sum + o.value, 0))}
            </div>
            <p className="text-xs text-green-600 mt-1">
              {mockOpportunities.filter(o => o.stage === 'closed_won').length} deals won
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Avg Deal Size
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(totalPipeline / mockOpportunities.length)}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Per opportunity
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Pipeline by Stage Chart */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card className="md:col-span-2">
          <CardHeader>
            <CardTitle>Pipeline Distribution</CardTitle>
            <CardDescription>Value by opportunity stage</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={mockPipelineByStage}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis tickFormatter={(value) => `$${(value / 1000).toFixed(0)}K`} />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Bar dataKey="value" radius={[8, 8, 0, 0]}>
                  {mockPipelineByStage.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Stage Breakdown</CardTitle>
            <CardDescription>Counts and values</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {mockPipelineByStage.map((stage) => (
                <div key={stage.name} className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <div
                      className="h-3 w-3 rounded-full"
                      style={{ backgroundColor: stage.color }}
                    />
                    <span className="text-sm">{stage.name}</span>
                  </div>
                  <div className="text-right">
                    <div className="text-sm font-medium">{formatCurrency(stage.value)}</div>
                    <div className="text-xs text-muted-foreground">{stage.count} deals</div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Stage Tabs and Table */}
      <Tabs defaultValue="all" className="space-y-4">
        <div className="flex items-center justify-between">
          <TabsList>
            <TabsTrigger value="all" onClick={() => setStageFilter('all')}>
              All ({stageCounts.all})
            </TabsTrigger>
            <TabsTrigger value="prospecting" onClick={() => setStageFilter('prospecting')}>
              Prospecting ({stageCounts.prospecting})
            </TabsTrigger>
            <TabsTrigger value="qualification" onClick={() => setStageFilter('qualification')}>
              Qualification ({stageCounts.qualification})
            </TabsTrigger>
            <TabsTrigger value="proposal" onClick={() => setStageFilter('proposal')}>
              Proposal ({stageCounts.proposal})
            </TabsTrigger>
            <TabsTrigger value="negotiation" onClick={() => setStageFilter('negotiation')}>
              Negotiation ({stageCounts.negotiation})
            </TabsTrigger>
            <TabsTrigger value="closed_won" onClick={() => setStageFilter('closed_won')}>
              Won ({stageCounts.closed_won})
            </TabsTrigger>
          </TabsList>

          <div className="flex gap-2">
            <Input
              placeholder="Search opportunities..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-[250px]"
            />
            <Select value={stageFilter} onValueChange={setStageFilter}>
              <SelectTrigger className="w-[150px]">
                <Filter className="h-4 w-4 mr-2" />
                <SelectValue placeholder="Stage" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Stages</SelectItem>
                <SelectItem value="prospecting">Prospecting</SelectItem>
                <SelectItem value="qualification">Qualification</SelectItem>
                <SelectItem value="proposal">Proposal</SelectItem>
                <SelectItem value="negotiation">Negotiation</SelectItem>
                <SelectItem value="closed_won">Closed Won</SelectItem>
                <SelectItem value="closed_lost">Closed Lost</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>

        <TabsContent value={stageFilter} className="space-y-4">
          <Card>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Deal #</TableHead>
                  <TableHead>Account</TableHead>
                  <TableHead>Contact</TableHead>
                  <TableHead>Value</TableHead>
                  <TableHead>Probability</TableHead>
                  <TableHead>Stage</TableHead>
                  <TableHead>Priority</TableHead>
                  <TableHead>Expected Close</TableHead>
                  <TableHead>Owner</TableHead>
                  <TableHead className="w-[50px]"></TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredOpportunities.map((opp) => (
                  <TableRow key={opp.id}>
                    <TableCell className="font-medium">{opp.dealNumber}</TableCell>
                    <TableCell>
                      <div>
                        <div className="font-medium">{opp.accountName}</div>
                        <div className="text-xs text-muted-foreground">{opp.country}</div>
                      </div>
                    </TableCell>
                    <TableCell>{opp.contactName}</TableCell>
                    <TableCell className="font-medium">{formatCurrency(opp.value)}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="w-16 bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-blue-600 h-2 rounded-full"
                            style={{ width: `${opp.probability}%` }}
                          />
                        </div>
                        <span className="text-sm">{opp.probability}%</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant={`stage_${opp.stage}` as any}>
                        {getStageLabel(opp.stage)}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <Badge className={getPriorityColor(opp.priority)}>
                        {getPriorityLabel(opp.priority)}
                      </Badge>
                    </TableCell>
                    <TableCell>{formatDate(opp.expectedCloseDate)}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-6 w-6 rounded-full bg-blue-100 flex items-center justify-center text-xs">
                          {opp.owner.charAt(0)}
                        </div>
                        <span className="text-sm">{opp.owner}</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button variant="ghost" size="icon">
                            <MoreHorizontal className="h-4 w-4" />
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem>
                            <TrendingUp className="h-4 w-4 mr-2" />
                            Advance Stage
                          </DropdownMenuItem>
                          <DropdownMenuItem>
                            <Calendar className="h-4 w-4 mr-2" />
                            Schedule Follow-up
                          </DropdownMenuItem>
                          <DropdownMenuSeparator />
                          <DropdownMenuItem className="text-green-600">
                            <CheckCircle2 className="h-4 w-4 mr-2" />
                            Mark as Won
                          </DropdownMenuItem>
                          <DropdownMenuItem className="text-red-600">
                            <XCircle className="h-4 w-4 mr-2" />
                            Mark as Lost
                          </DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
