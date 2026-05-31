import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatCurrency, getStatusColor } from '@shared/utils/cn'
import { currenciesData } from '@shared/mock-data'
import { DollarSign, TrendingUp, TrendingDown, RefreshCw, Calculator, History, Plus } from 'lucide-react'

export default function CurrencyPage() {
  const [baseCurrency, setBaseCurrency] = useState('USD')
  const [amount, setAmount] = useState(1000)
  const [fromCurrency, setFromCurrency] = useState('USD')
  const [toCurrency, setToCurrency] = useState('NGN')

  const fromRate = currenciesData.find(c => c.code === fromCurrency)?.exchangeRate || 1
  const toRate = currenciesData.find(c => c.code === toCurrency)?.exchangeRate || 1
  const convertedAmount = (amount / fromRate) * toRate

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header flex items-center justify-between">
        <div>
          <h1 className="page-title">Currency Management</h1>
          <p className="page-description">
            Manage exchange rates and currency conversions
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          Add Currency
        </Button>
      </div>

      {/* Currency Converter */}
      <Card className="gbm-gradient text-white">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-white">
            <Calculator className="h-5 w-5" />
            Currency Converter
          </CardTitle>
          <CardDescription className="text-white/80">
            Real-time currency conversion based on current exchange rates
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-6 md:grid-cols-2">
            <div className="space-y-4">
              <div>
                <Label className="text-white">Amount</Label>
                <Input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(Number(e.target.value))}
                  className="bg-white/20 text-white border-white/30 placeholder:text-white/60"
                />
              </div>
              <div>
                <Label className="text-white">From</Label>
                <select
                  value={fromCurrency}
                  onChange={(e) => setFromCurrency(e.target.value)}
                  className="w-full h-10 rounded-md border border-white/30 bg-white/20 px-3 text-white"
                >
                  {currenciesData.map(currency => (
                    <option key={currency.code} value={currency.code} className="text-black">
                      {currency.flag} {currency.name} ({currency.code})
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="space-y-4">
              <div>
                <Label className="text-white">To</Label>
                <select
                  value={toCurrency}
                  onChange={(e) => setToCurrency(e.target.value)}
                  className="w-full h-10 rounded-md border border-white/30 bg-white/20 px-3 text-white"
                >
                  {currenciesData.map(currency => (
                    <option key={currency.code} value={currency.code} className="text-black">
                      {currency.flag} {currency.name} ({currency.code})
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <Label className="text-white">Result</Label>
                <div className="h-10 rounded-md bg-white/20 flex items-center px-3 text-white font-semibold">
                  {convertedAmount.toLocaleString('en-US', { maximumFractionDigits: 2 })} {toCurrency}
                </div>
              </div>
            </div>
          </div>
          <div className="mt-4 text-sm text-white/80">
            Exchange Rate: 1 {fromCurrency} = {(toRate / fromRate).toFixed(6)} {toCurrency}
          </div>
        </CardContent>
      </Card>

      {/* Exchange Rate Summary */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Base Currency</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">USD</div>
            <p className="text-xs text-muted-foreground">US Dollar</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Active Currencies</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{currenciesData.filter(c => c.isActive).length}</div>
            <p className="text-xs text-muted-foreground">configured markets</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Last Update</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">Today</div>
            <p className="text-xs text-muted-foreground">06:00 AM UTC</p>
          </CardContent>
        </Card>
      </div>

      {/* Exchange Rates Table */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Exchange Rates</CardTitle>
              <CardDescription>Current rates relative to USD</CardDescription>
            </div>
            <Button variant="outline" size="sm" className="gap-2">
              <RefreshCw className="h-4 w-4" />
              Refresh Rates
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Currency</TableHead>
                <TableHead>Code</TableHead>
                <TableHead>Exchange Rate</TableHead>
                <TableHead>1 USD =</TableHead>
                <TableHead>24h Change</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {currenciesData.map((currency) => (
                <TableRow key={currency.code}>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <span className="text-xl">{currency.flag}</span>
                      <span className="font-medium">{currency.name}</span>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline">{currency.code}</Badge>
                  </TableCell>
                  <TableCell>
                    <span className="font-mono">{currency.exchangeRate.toFixed(6)}</span>
                  </TableCell>
                  <TableCell>
                    <span className="font-mono">{(1 / currency.exchangeRate).toFixed(4)}</span>
                  </TableCell>
                  <TableCell>
                    <div className={cn(
                      'flex items-center gap-1 text-sm',
                      Math.random() > 0.5 ? 'text-green-600' : 'text-red-600'
                    )}>
                      {Math.random() > 0.5 ? (
                        <TrendingUp className="h-4 w-4" />
                      ) : (
                        <TrendingDown className="h-4 w-4" />
                      )}
                      {(Math.random() * 2 - 1).toFixed(2)}%
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge className={cn(
                      currency.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                    )}>
                      {currency.isActive ? 'Active' : 'Inactive'}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <Button variant="ghost" size="icon">
                        <History className="h-4 w-4" />
                      </Button>
                      <Button variant="ghost" size="icon">
                        <RefreshCw className="h-4 w-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Rate History Placeholder */}
      <Card>
        <CardHeader>
          <CardTitle>Exchange Rate History</CardTitle>
          <CardDescription>Historical rate trends for selected currencies</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="h-64 flex items-center justify-center text-muted-foreground">
            <div className="text-center">
              <History className="h-12 w-12 mx-auto mb-4 opacity-50" />
              <p>Select a currency to view historical exchange rate trends</p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
