import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
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
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import {
  DollarSign,
  Calculator,
  TrendingUp,
  Download,
  Calendar,
  CheckCircle,
  Clock,
  XCircle,
} from 'lucide-react'
import { formatCurrency, formatNumber } from '@shared/utils/cn'
import { mockCommissions } from '@shared/data/mockData'

export default function CommissionPage() {
  const [period, setPeriod] = useState('2024-02')
  const [showCalculator, setShowCalculator] = useState(false)
  const [calculatorData, setCalculatorData] = useState({
    salesAmount: 0,
    commissionRate: 10,
    dealsCount: 0,
  })

  const calculatedCommission = (calculatorData.salesAmount * calculatorData.calculatorRate) / 100
  const quotaAttainment = calculatorData.salesAmount / 500000

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      pending: 'bg-yellow-100 text-yellow-800',
      approved: 'bg-blue-100 text-blue-800',
      paid: 'bg-green-100 text-green-800',
      declined: 'bg-red-100 text-red-800',
    }
    return colors[status] || 'bg-gray-100 text-gray-800'
  }

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'paid':
        return <CheckCircle className="h-4 w-4 text-green-600" />
      case 'approved':
        return <Clock className="h-4 w-4 text-blue-600" />
      case 'pending':
        return <Clock className="h-4 w-4 text-yellow-600" />
      case 'declined':
        return <XCircle className="h-4 w-4 text-red-600" />
      default:
        return null
    }
  }

  const getTypeLabel = (type: string) => {
    const labels: Record<string, string> = {
      direct_sales: 'Direct Sales',
      override: 'Manager Override',
      bonus: 'Performance Bonus',
      partner_referral: 'Partner Referral',
    }
    return labels[type] || type
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Commission Tracker</h1>
          <p className="text-muted-foreground mt-1">
            Track and calculate sales commissions
          </p>
        </div>
        <div className="flex gap-2">
          <Select value={period} onValueChange={setPeriod}>
            <SelectTrigger className="w-[180px]">
              <Calendar className="h-4 w-4 mr-2" />
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="2024-02">February 2024</SelectItem>
              <SelectItem value="2024-01">January 2024</SelectItem>
              <SelectItem value="2023-12">December 2023</SelectItem>
            </SelectContent>
          </Select>
          <Button variant="outline" className="gap-2">
            <Download className="h-4 w-4" />
            Export
          </Button>
          <Button className="gap-2" onClick={() => setShowCalculator(true)}>
            <Calculator className="h-4 w-4" />
            Calculator
          </Button>
        </div>
      </div>

      {/* Summary Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Commission
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(mockCommissions.reduce((sum, c) => sum + c.commissionAmount, 0))}
            </div>
            <p className="text-xs text-green-600 mt-1">
              +12% from last month
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Pending
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(mockCommissions.filter(c => c.status === 'pending').reduce((sum, c) => sum + c.commissionAmount, 0))}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Awaiting approval
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Paid This Month
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(mockCommissions.filter(c => c.status === 'paid').reduce((sum, c) => sum + c.commissionAmount, 0))}
            </div>
            <p className="text-xs text-green-600 mt-1">
              Successfully processed
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Avg Commission
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatCurrency(mockCommissions.reduce((sum, c) => sum + c.commissionAmount, 0) / mockCommissions.length)}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Per representative
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Commission Calculator Dialog */}
      <Dialog open={showCalculator} onOpenChange={setShowCalculator}>
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>Commission Calculator</DialogTitle>
            <DialogDescription>
              Calculate estimated commission based on sales performance
            </DialogDescription>
          </DialogHeader>

          <Tabs defaultValue="simple" className="mt-4">
            <TabsList className="grid w-full grid-cols-2">
              <TabsTrigger value="simple">Simple</TabsTrigger>
              <TabsTrigger value="advanced">Advanced</TabsTrigger>
            </TabsList>

            <TabsContent value="simple" className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="sales-amount">Total Sales Amount</Label>
                <div className="relative">
                  <DollarSign className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    id="sales-amount"
                    type="number"
                    value={calculatorData.salesAmount}
                    onChange={(e) => setCalculatorData({ ...calculatorData, salesAmount: Number(e.target.value) })}
                    className="pl-10"
                    placeholder="100000"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="commission-rate">Commission Rate: {calculatorData.commissionRate}%</Label>
                <input
                  type="range"
                  id="commission-rate"
                  min="1"
                  max="20"
                  value={calculatorData.commissionRate}
                  onChange={(e) => setCalculatorData({ ...calculatorData, commissionRate: Number(e.target.value) })}
                  className="w-full"
                />
                <div className="flex justify-between text-xs text-muted-foreground">
                  <span>1%</span>
                  <span>10%</span>
                  <span>20%</span>
                </div>
              </div>

              <Card className="bg-blue-50 border-blue-200">
                <CardContent className="pt-6">
                  <div className="text-center">
                    <div className="text-sm text-muted-foreground mb-1">Estimated Commission</div>
                    <div className="text-3xl font-bold text-blue-600">
                      {formatCurrency((calculatorData.salesAmount * calculatorData.commissionRate) / 100)}
                    </div>
                  </div>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="advanced" className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>Annual Quota</Label>
                  <Input type="number" defaultValue="500000" disabled />
                </div>
                <div className="space-y-2">
                  <Label>Quota Attainment</Label>
                  <Input
                    value={`${quotaAttainment >= 1 ? '>100' : Math.round(quotaAttainment * 100)}%`}
                    disabled
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label>Accelerators</Label>
                <div className="space-y-2">
                  <div className="flex items-center justify-between p-2 border rounded">
                    <span className="text-sm">100-110% of quota</span>
                    <Badge variant="info">+2% bonus</Badge>
                  </div>
                  <div className="flex items-center justify-between p-2 border rounded">
                    <span className="text-sm">110-125% of quota</span>
                    <Badge variant="success">+5% bonus</Badge>
                  </div>
                  <div className="flex items-center justify-between p-2 border rounded">
                    <span className="text-sm">125%+ of quota</span>
                    <Badge variant="success">+10% bonus</Badge>
                  </div>
                </div>
              </div>
            </TabsContent>
          </Tabs>

          <DialogFooter>
            <Button variant="outline" onClick={() => setShowCalculator(false)}>
              Cancel
            </Button>
            <Button onClick={() => setShowCalculator(false)}>
              Save Calculation
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Commission Table */}
      <Card>
        <CardHeader>
          <CardTitle>Commission Records</CardTitle>
          <CardDescription>
            Commission statements for {period}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Representative</TableHead>
                <TableHead>Type</TableHead>
                <TableHead>Sales Amount</TableHead>
                <TableHead>Rate</TableHead>
                <TableHead>Deals</TableHead>
                <TableHead>Commission</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Paid Date</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {mockCommissions.map((commission) => (
                <TableRow key={commission.id}>
                  <TableCell>
                    <div className="font-medium">{commission.salesRepName}</div>
                    <div className="text-xs text-muted-foreground">{commission.salesRepId}</div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline" className="capitalize">
                      {getTypeLabel(commission.type)}
                    </Badge>
                  </TableCell>
                  <TableCell className="font-medium">
                    {formatCurrency(commission.salesAmount)}
                  </TableCell>
                  <TableCell>{(commission.commissionRate * 100).toFixed(1)}%</TableCell>
                  <TableCell>{commission.deals}</TableCell>
                  <TableCell className="font-bold">
                    {formatCurrency(commission.commissionAmount)}
                  </TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      {getStatusIcon(commission.status)}
                      <Badge className={getStatusColor(commission.status)}>
                        {commission.status}
                      </Badge>
                    </div>
                  </TableCell>
                  <TableCell>
                    {commission.paidDate ? (
                      <span className="text-sm">{commission.paidDate}</span>
                    ) : (
                      <span className="text-sm text-muted-foreground">—</span>
                    )}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Commission Structure Info */}
      <Card>
        <CardHeader>
          <CardTitle>Commission Structure</CardTitle>
          <CardDescription>Current commission rates and tiers</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-3">
            <div className="p-4 border rounded-lg">
              <div className="font-medium mb-2">Sales Representative</div>
              <div className="text-2xl font-bold">10%</div>
              <div className="text-sm text-muted-foreground">Base commission rate</div>
            </div>
            <div className="p-4 border rounded-lg">
              <div className="font-medium mb-2">Sales Manager</div>
              <div className="text-2xl font-bold">3%</div>
              <div className="text-sm text-muted-foreground">Override on team sales</div>
            </div>
            <div className="p-4 border rounded-lg">
              <div className="font-medium mb-2">Country Director</div>
              <div className="text-2xl font-bold">1.5%</div>
              <div className="text-sm text-muted-foreground">Override on country sales</div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
