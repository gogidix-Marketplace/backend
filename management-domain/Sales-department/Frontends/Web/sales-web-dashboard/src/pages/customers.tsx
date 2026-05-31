import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@shared/components/ui/table'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@shared/components/ui/dropdown-menu'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
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
  Mail,
  Phone,
  MapPin,
  Building2,
  TrendingUp,
  Users,
} from 'lucide-react'
import { formatCurrency, formatDate, getCountryFlag } from '@shared/utils/cn'
import { mockCustomers } from '@shared/data/mockData'

export default function CustomersPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedCustomer, setSelectedCustomer] = useState<typeof mockCustomers[0] | null>(null)

  const filteredCustomers = mockCustomers.filter(customer => {
    return (
      searchQuery === '' ||
      customer.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      customer.industry.toLowerCase().includes(searchQuery.toLowerCase())
    )
  })

  const getTierColor = (tier: string) => {
    const colors: Record<string, string> = {
      enterprise: 'bg-purple-100 text-purple-800',
      mid_market: 'bg-blue-100 text-blue-800',
      small_business: 'bg-green-100 text-green-800',
      micro: 'bg-gray-100 text-gray-800',
    }
    return colors[tier] || 'bg-gray-100 text-gray-800'
  }

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      active: 'bg-green-100 text-green-800',
      inactive: 'bg-gray-100 text-gray-800',
      prospect: 'bg-blue-100 text-blue-800',
      churned: 'bg-red-100 text-red-800',
    }
    return colors[status] || 'bg-gray-100 text-gray-800'
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Customers</h1>
          <p className="text-muted-foreground mt-1">
            Manage your customer accounts and relationships
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          Add Customer
        </Button>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Customers
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockCustomers.length}</div>
            <p className="text-xs text-green-600 mt-1">
              +3 new this month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Active Customers
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {mockCustomers.filter(c => c.status === 'active').length}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              95% retention rate
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Value
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(mockCustomers.reduce((sum, c) => sum + c.totalValue, 0))}
            </div>
            <p className="text-xs text-green-600 mt-1">
              +18% from last quarter
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Enterprise Accounts
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {mockCustomers.filter(c => c.tier === 'enterprise').length}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              67% of total revenue
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Search and Filter */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                placeholder="Search customers by name or industry..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Customers Table */}
      <Card>
        <CardHeader>
          <CardTitle>Customer Accounts</CardTitle>
          <CardDescription>
            Showing {filteredCustomers.length} of {mockCustomers.length} customers
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Account</TableHead>
                <TableHead>Industry</TableHead>
                <TableHead>Tier</TableHead>
                <TableHead>Contacts</TableHead>
                <TableHead>Country</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Total Value</TableHead>
                <TableHead>Acquisitions</TableHead>
                <TableHead className="w-[50px]"></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredCustomers.map((customer) => (
                <TableRow
                  key={customer.id}
                  className="cursor-pointer hover:bg-muted/50"
                  onClick={() => setSelectedCustomer(customer)}
                >
                  <TableCell>
                    <div>
                      <div className="font-medium">{customer.name}</div>
                      <div className="text-sm text-muted-foreground">{customer.accountNumber}</div>
                    </div>
                  </TableCell>
                  <TableCell>{customer.industry}</TableCell>
                  <TableCell>
                    <Badge className={getTierColor(customer.tier)} className="capitalize">
                      {customer.tier.replace('_', ' ')}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <div className="flex items-center gap-1">
                      <Users className="h-3 w-3 text-muted-foreground" />
                      {customer.contacts.length}
                    </div>
                  </TableCell>
                  <TableCell>
                    <span className="mr-1">{getCountryFlag(customer.country)}</span>
                    {customer.country}
                  </TableCell>
                  <TableCell>
                    <Badge className={getStatusColor(customer.status)} className="capitalize">
                      {customer.status}
                    </Badge>
                  </TableCell>
                  <TableCell className="font-medium">
                    {formatCurrency(customer.totalValue)}
                  </TableCell>
                  <TableCell>{customer.acquisitions}</TableCell>
                  <TableCell>
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild onClick={(e) => e.stopPropagation()}>
                        <Button variant="ghost" size="icon">
                          <MoreHorizontal className="h-4 w-4" />
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem>
                          <Mail className="h-4 w-4 mr-2" />
                          Send Email
                        </DropdownMenuItem>
                        <DropdownMenuItem>
                          <Phone className="h-4 w-4 mr-2" />
                          Log Call
                        </DropdownMenuItem>
                        <DropdownMenuItem>
                          <Building2 className="h-4 w-4 mr-2" />
                          View Details
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

      {/* Customer Detail Dialog */}
      <Dialog open={!!selectedCustomer} onOpenChange={() => setSelectedCustomer(null)}>
        <DialogContent className="max-w-3xl max-h-[80vh] overflow-y-auto">
          {selectedCustomer && (
            <>
              <DialogHeader>
                <div className="flex items-start justify-between">
                  <div>
                    <DialogTitle className="text-xl">{selectedCustomer.name}</DialogTitle>
                    <DialogDescription className="mt-1">
                      {selectedCustomer.accountNumber} • {selectedCustomer.industry}
                    </DialogDescription>
                  </div>
                  <div className="flex gap-2">
                    <Badge className={getTierColor(selectedCustomer.tier)} className="capitalize">
                      {selectedCustomer.tier.replace('_', ' ')}
                    </Badge>
                    <Badge className={getStatusColor(selectedCustomer.status)} className="capitalize">
                      {selectedCustomer.status}
                    </Badge>
                  </div>
                </div>
              </DialogHeader>

              <Tabs defaultValue="overview" className="mt-4">
                <TabsList>
                  <TabsTrigger value="overview">Overview</TabsTrigger>
                  <TabsTrigger value="contacts">Contacts</TabsTrigger>
                  <TabsTrigger value="history">History</TabsTrigger>
                </TabsList>

                <TabsContent value="overview" className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <Card>
                      <CardHeader className="pb-3">
                        <CardTitle className="text-sm">Account Information</CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-3 text-sm">
                        <div className="flex items-center gap-2">
                          <Building2 className="h-4 w-4 text-muted-foreground" />
                          <span>Employees: {selectedCustomer.employeeCount?.toLocaleString()}</span>
                        </div>
                        <div className="flex items-center gap-2">
                          <TrendingUp className="h-4 w-4 text-muted-foreground" />
                          <span>Annual Revenue: {formatCurrency(selectedCustomer.annualRevenue)}</span>
                        </div>
                        <div className="flex items-center gap-2">
                          <MapPin className="h-4 w-4 text-muted-foreground" />
                          <span>
                            {selectedCustomer.billingAddress?.city}, {selectedCustomer.billingAddress?.country}
                          </span>
                        </div>
                      </CardContent>
                    </Card>

                    <Card>
                      <CardHeader className="pb-3">
                        <CardTitle className="text-sm">Sales Summary</CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-3 text-sm">
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Total Acquisitions</span>
                          <span className="font-medium">{selectedCustomer.acquisitions}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Total Value</span>
                          <span className="font-medium">{formatCurrency(selectedCustomer.totalValue)}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-muted-foreground">Avg Deal Size</span>
                          <span className="font-medium">
                            {formatCurrency(selectedCustomer.totalValue / selectedCustomer.acquisitions)}
                          </span>
                        </div>
                      </CardContent>
                    </Card>
                  </div>

                  <Card>
                    <CardHeader className="pb-3">
                      <CardTitle className="text-sm">Address</CardTitle>
                    </CardHeader>
                    <CardContent className="text-sm">
                      {selectedCustomer.billingAddress && (
                        <div>
                          <p>{selectedCustomer.billingAddress.street}</p>
                          <p>
                            {selectedCustomer.billingAddress.city}, {selectedCustomer.billingAddress.state} {selectedCustomer.billingAddress.postalCode}
                          </p>
                          <p>{selectedCustomer.billingAddress.country}</p>
                        </div>
                      )}
                    </CardContent>
                  </Card>

                  {selectedCustomer.notes && (
                    <Card>
                      <CardHeader className="pb-3">
                        <CardTitle className="text-sm">Notes</CardTitle>
                      </CardHeader>
                      <CardContent className="text-sm">
                        {selectedCustomer.notes}
                      </CardContent>
                    </Card>
                  )}
                </TabsContent>

                <TabsContent value="contacts" className="space-y-4">
                  <div className="space-y-3">
                    {selectedCustomer.contacts.map((contact) => (
                      <Card key={contact.id}>
                        <CardContent className="pt-4">
                          <div className="flex items-start justify-between">
                            <div className="flex items-center gap-3">
                              <div className="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center">
                                <span className="text-sm font-medium text-blue-600">
                                  {contact.firstName.charAt(0)}{contact.lastName.charAt(0)}
                                </span>
                              </div>
                              <div>
                                <div className="font-medium">
                                  {contact.firstName} {contact.lastName}
                                  {contact.isPrimary && (
                                    <Badge variant="outline" className="ml-2">Primary</Badge>
                                  )}
                                </div>
                                <div className="text-sm text-muted-foreground">{contact.title}</div>
                              </div>
                            </div>
                            <div className="flex gap-2">
                              <Button size="sm" variant="outline">
                                <Mail className="h-3 w-3 mr-1" />
                                Email
                              </Button>
                              {contact.phone && (
                                <Button size="sm" variant="outline">
                                  <Phone className="h-3 w-3 mr-1" />
                                  Call
                                </Button>
                              )}
                            </div>
                          </div>
                          <div className="mt-3 text-sm text-muted-foreground">
                            <div>{contact.email}</div>
                            {contact.phone && <div>{contact.phone}</div>}
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </TabsContent>

                <TabsContent value="history">
                  <Card>
                    <CardContent className="pt-6">
                      <p className="text-sm text-muted-foreground text-center py-8">
                        Activity history will be displayed here
                      </p>
                    </CardContent>
                  </Card>
                </TabsContent>
              </Tabs>
            </>
          )}
        </DialogContent>
      </Dialog>
    </div>
  )
}
