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
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@shared/components/ui/dialog'
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
  Phone,
  Mail,
  Calendar,
  TrendingUp,
  Filter,
  Star,
  MapPin,
  Building,
  UserPlus,
  CheckCircle,
  XCircle,
  Clock,
} from 'lucide-react'
import { formatCurrency, formatDate, getLeadStatusColor, getCountryFlag } from '@shared/utils/cn'
import { mockLeads } from '@shared/data/mockData'

export default function LeadsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [sourceFilter, setSourceFilter] = useState<string>('all')
  const [selectedLead, setSelectedLead] = useState<typeof mockLeads[0] | null>(null)

  const filteredLeads = mockLeads.filter(lead => {
    const matchesSearch =
      searchQuery === '' ||
      lead.company.toLowerCase().includes(searchQuery.toLowerCase()) ||
      lead.contactName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      lead.email.toLowerCase().includes(searchQuery.toLowerCase())

    const matchesStatus = statusFilter === 'all' || lead.status === statusFilter
    const matchesSource = sourceFilter === 'all' || lead.source === sourceFilter

    return matchesSearch && matchesStatus && matchesSource
  })

  const getScoreColor = (score: number) => {
    if (score >= 80) return 'bg-green-500'
    if (score >= 60) return 'bg-blue-500'
    if (score >= 40) return 'bg-yellow-500'
    return 'bg-gray-300'
  }

  const statusCounts = {
    all: mockLeads.length,
    new: mockLeads.filter(l => l.status === 'new').length,
    contacted: mockLeads.filter(l => l.status === 'contacted').length,
    qualified: mockLeads.filter(l => l.status === 'qualified').length,
    converted: mockLeads.filter(l => l.status === 'converted').length,
    lost: mockLeads.filter(l => l.status === 'unqualified').length,
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Leads Management</h1>
          <p className="text-muted-foreground mt-1">
            Manage and track your sales leads
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          Add Lead
        </Button>
      </div>

      {/* Status Tabs */}
      <Tabs defaultValue="all" className="space-y-4">
        <TabsList>
          <TabsTrigger value="all" onClick={() => setStatusFilter('all')}>
            All ({statusCounts.all})
          </TabsTrigger>
          <TabsTrigger value="new" onClick={() => setStatusFilter('new')}>
            New ({statusCounts.new})
          </TabsTrigger>
          <TabsTrigger value="contacted" onClick={() => setStatusFilter('contacted')}>
            Contacted ({statusCounts.contacted})
          </TabsTrigger>
          <TabsTrigger value="qualified" onClick={() => setStatusFilter('qualified')}>
            Qualified ({statusCounts.qualified})
          </TabsTrigger>
          <TabsTrigger value="converted" onClick={() => setStatusFilter('converted')}>
            Converted ({statusCounts.converted})
          </TabsTrigger>
        </TabsList>

        <TabsContent value={statusFilter} className="space-y-4">
          {/* Filters and Stats */}
          <div className="grid gap-4 md:grid-cols-4">
            <Card>
              <CardHeader className="pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  Total Leads
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{mockLeads.length}</div>
                <p className="text-xs text-muted-foreground mt-1">
                  +12% from last month
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  Qualified Leads
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {mockLeads.filter(l => l.status === 'qualified').length}
                </div>
                <p className="text-xs text-green-600 mt-1">
                  {Math.round((mockLeads.filter(l => l.status === 'qualified').length / mockLeads.length) * 100)}% conversion rate
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  Pipeline Value
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {formatCurrency(mockLeads.reduce((sum, l) => sum + l.estimatedValue, 0))}
                </div>
                <p className="text-xs text-muted-foreground mt-1">
                  From all active leads
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  Avg Lead Score
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {Math.round(mockLeads.reduce((sum, l) => sum + l.score, 0) / mockLeads.length)}
                </div>
                <p className="text-xs text-green-600 mt-1">
                  +5 points improvement
                </p>
              </CardContent>
            </Card>
          </div>

          {/* Search and Filter */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex flex-col sm:flex-row gap-4">
                <div className="relative flex-1">
                  <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    placeholder="Search leads by company, contact, or email..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="pl-10"
                  />
                </div>
                <Select value={sourceFilter} onValueChange={setSourceFilter}>
                  <SelectTrigger className="w-[180px]">
                    <Filter className="h-4 w-4 mr-2" />
                    <SelectValue placeholder="Source" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Sources</SelectItem>
                    <SelectItem value="website">Website</SelectItem>
                    <SelectItem value="referral">Referral</SelectItem>
                    <SelectItem value="trade_show">Trade Show</SelectItem>
                    <SelectItem value="cold_call">Cold Call</SelectItem>
                    <SelectItem value="partner">Partner</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardContent>
          </Card>

          {/* Leads Table */}
          <Card>
            <CardHeader>
              <CardTitle>Lead List</CardTitle>
              <CardDescription>
                Showing {filteredLeads.length} of {mockLeads.length} leads
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-[50px]">Score</TableHead>
                    <TableHead>Company</TableHead>
                    <TableHead>Contact</TableHead>
                    <TableHead>Source</TableHead>
                    <TableHead>Value</TableHead>
                    <TableHead>Country</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Assigned To</TableHead>
                    <TableHead>Last Activity</TableHead>
                    <TableHead className="w-[50px]"></TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredLeads.map((lead) => (
                    <TableRow
                      key={lead.id}
                      className="cursor-pointer hover:bg-muted/50"
                      onClick={() => setSelectedLead(lead)}
                    >
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <div
                            className={`h-8 w-8 rounded-full flex items-center justify-center text-white text-sm font-medium ${getScoreColor(lead.score)}`}
                          >
                            {lead.score}
                          </div>
                          {lead.score >= 80 && <Star className="h-3 w-3 text-yellow-500 fill-yellow-500" />}
                        </div>
                      </TableCell>
                      <TableCell>
                        <div>
                          <div className="font-medium">{lead.company}</div>
                          <div className="text-sm text-muted-foreground">{lead.title}</div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div>
                          <div className="font-medium">{lead.contactName}</div>
                          <div className="text-sm text-muted-foreground">{lead.email}</div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline" className="capitalize">
                          {lead.source.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell className="font-medium">
                        {formatCurrency(lead.estimatedValue)}
                      </TableCell>
                      <TableCell>
                        <span className="mr-1">{getCountryFlag(lead.country)}</span>
                        {lead.country}
                      </TableCell>
                      <TableCell>
                        <Badge className={getLeadStatusColor(lead.status)}>
                          {lead.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <div className="h-6 w-6 rounded-full bg-blue-100 flex items-center justify-center text-xs">
                            {lead.assignedTo ? lead.assignedTo.slice(-2) : '—'}
                          </div>
                          <span className="text-sm">John D.</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="text-sm text-muted-foreground">
                          {lead.lastActivity ? formatDate(lead.lastActivity) : '—'}
                        </div>
                      </TableCell>
                      <TableCell>
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild onClick={(e) => e.stopPropagation()}>
                            <Button variant="ghost" size="icon">
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem onClick={(e) => e.stopPropagation()}>
                              <Phone className="h-4 w-4 mr-2" />
                              Call
                            </DropdownMenuItem>
                            <DropdownMenuItem onClick={(e) => e.stopPropagation()}>
                              <Mail className="h-4 w-4 mr-2" />
                              Send Email
                            </DropdownMenuItem>
                            <DropdownMenuItem onClick={(e) => e.stopPropagation()}>
                              <Calendar className="h-4 w-4 mr-2" />
                              Schedule Meeting
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem onClick={(e) => e.stopPropagation()}>
                              <CheckCircle className="h-4 w-4 mr-2" />
                              Mark as Qualified
                            </DropdownMenuItem>
                            <DropdownMenuItem onClick={(e) => e.stopPropagation()}>
                              <UserPlus className="h-4 w-4 mr-2" />
                              Convert to Opportunity
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Lead Detail Dialog */}
      <Dialog open={!!selectedLead} onOpenChange={() => setSelectedLead(null)}>
        <DialogContent className="max-w-2xl">
          {selectedLead && (
            <>
              <DialogHeader>
                <div className="flex items-start justify-between">
                  <div>
                    <DialogTitle className="text-xl">{selectedLead.company}</DialogTitle>
                    <DialogDescription className="mt-1">
                      {selectedLead.contactName} • {selectedLead.title}
                    </DialogDescription>
                  </div>
                  <Badge className={getLeadStatusColor(selectedLead.status)}>
                    {selectedLead.status.replace('_', ' ')}
                  </Badge>
                </div>
              </DialogHeader>

              <div className="space-y-6">
                {/* Lead Score */}
                <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div>
                    <div className="text-sm text-muted-foreground">Lead Score</div>
                    <div className="text-2xl font-bold mt-1">{selectedLead.score}/100</div>
                  </div>
                  <div className={`h-16 w-16 rounded-full flex items-center justify-center text-white text-xl font-bold ${getScoreColor(selectedLead.score)}`}>
                    {selectedLead.score}
                  </div>
                </div>

                {/* Contact Information */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-3">
                    <h4 className="font-medium">Contact Information</h4>
                    <div className="space-y-2 text-sm">
                      <div className="flex items-center gap-2">
                        <Mail className="h-4 w-4 text-muted-foreground" />
                        <span>{selectedLead.email}</span>
                      </div>
                      {selectedLead.phone && (
                        <div className="flex items-center gap-2">
                          <Phone className="h-4 w-4 text-muted-foreground" />
                          <span>{selectedLead.phone}</span>
                        </div>
                      )}
                      <div className="flex items-center gap-2">
                        <Building className="h-4 w-4 text-muted-foreground" />
                        <span>
                          {getCountryFlag(selectedLead.country)} {selectedLead.country}
                        </span>
                      </div>
                      {selectedLead.territory && (
                        <div className="flex items-center gap-2">
                          <MapPin className="h-4 w-4 text-muted-foreground" />
                          <span>{selectedLead.territory}</span>
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="space-y-3">
                    <h4 className="font-medium">Lead Details</h4>
                    <div className="space-y-2 text-sm">
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Source</span>
                        <span className="capitalize">{selectedLead.source.replace('_', ' ')}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Est. Value</span>
                        <span className="font-medium">{formatCurrency(selectedLead.estimatedValue)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Assigned To</span>
                        <span>John Doe</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Created</span>
                        <span>{formatDate(selectedLead.createdAt)}</span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Tags */}
                <div>
                  <h4 className="font-medium mb-2">Tags</h4>
                  <div className="flex flex-wrap gap-2">
                    {selectedLead.tags.map((tag) => (
                      <Badge key={tag} variant="outline">
                        {tag}
                      </Badge>
                    ))}
                  </div>
                </div>
              </div>

              <DialogFooter className="gap-2">
                <Button variant="outline">
                  <XCircle className="h-4 w-4 mr-2" />
                  Disqualify
                </Button>
                <Button variant="outline">
                  <Clock className="h-4 w-4 mr-2" />
                  Follow Up Later
                </Button>
                <Button>
                  <CheckCircle className="h-4 w-4 mr-2" />
                  Convert to Opportunity
                </Button>
              </DialogFooter>
            </>
          )}
        </DialogContent>
      </Dialog>
    </div>
  )
}
