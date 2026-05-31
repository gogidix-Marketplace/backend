import * as React from 'react'
import { Users, Calendar, Mail, Phone, MapPin } from 'lucide-react'
import { cn } from '@lib/utils'
import { Card } from '@/components/ui/card'
import { Avatar } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'

export interface EmployeeCardProps {
  id: string
  name: string
  position: string
  department: string
  avatar?: string
  email?: string
  phone?: string
  location?: string
  status: 'active' | 'inactive' | 'on_leave'
  joinDate: string
  className?: string
  onClick?: () => void
}

export function EmployeeCard({
  name,
  position,
  department,
  avatar,
  email,
  phone,
  location,
  status,
  joinDate,
  className,
  onClick,
}: EmployeeCardProps) {
  const getStatusColor = () => {
    switch (status) {
      case 'active':
        return 'bg-green-100 text-green-700'
      case 'inactive':
        return 'bg-gray-100 text-gray-700'
      case 'on_leave':
        return 'bg-yellow-100 text-yellow-700'
    }
  }

  const initials = name
    .split(' ')
    .map((n) => n[0])
    .join('')
    .toUpperCase()
    .slice(0, 2)

  return (
    <Card
      className={cn('p-4 cursor-pointer hover:shadow-md transition-shadow', className)}
      onClick={onClick}
    >
      <div className="flex items-start gap-4">
        {/* Avatar */}
        <Avatar className="h-12 w-12">
          {avatar ? (
            <img src={avatar} alt={name} />
          ) : (
            <div className="flex h-full w-full items-center justify-center bg-primary-600 text-white rounded-full">
              {initials}
            </div>
          )}
        </Avatar>

        {/* Info */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 mb-1">
            <h3 className="font-semibold text-gray-900 truncate">{name}</h3>
            <span className={cn('px-2 py-0.5 rounded-full text-xs font-medium', getStatusColor())}>
              {status.replace('_', ' ')}
            </span>
          </div>
          <p className="text-sm text-gray-600">{position}</p>
          <p className="text-xs text-gray-500">{department}</p>
        </div>
      </div>

      {/* Contact Info */}
      <div className="mt-4 space-y-1 text-sm text-gray-600">
        {email && (
          <div className="flex items-center gap-2">
            <Mail className="h-3 w-3" />
            <span className="truncate">{email}</span>
          </div>
        )}
        {phone && (
          <div className="flex items-center gap-2">
            <Phone className="h-3 w-3" />
            <span>{phone}</span>
          </div>
        )}
        {location && (
          <div className="flex items-center gap-2">
            <MapPin className="h-3 w-3" />
            <span className="truncate">{location}</span>
          </div>
        )}
      </div>

      {/* Footer */}
      <div className="mt-4 pt-4 border-t border-gray-100 flex items-center justify-between text-xs text-gray-500">
        <span>Joined {new Date(joinDate).toLocaleDateString()}</span>
      </div>
    </Card>
  )
}

// Leave Balance Card
export interface LeaveBalanceProps {
  employeeId: string
  annual: { total: number; used: number; remaining: number }
  sick: { total: number; used: number; remaining: number }
  className?: string
}

export function LeaveBalanceCard({ annual, sick, className }: LeaveBalanceProps) {
  const leaveTypes = [
    { name: 'Annual Leave', ...annual, color: 'bg-blue-500' },
    { name: 'Sick Leave', ...sick, color: 'bg-green-500' },
  ] as const

  return (
    <Card className={cn('p-4', className)}>
      <div className="flex items-center gap-2 mb-4">
        <Calendar className="h-5 w-5 text-primary-600" />
        <h3 className="font-semibold text-gray-900">Leave Balance</h3>
      </div>

      <div className="space-y-4">
        {leaveTypes.map((leave) => {
          const percentage = Math.round((leave.used / leave.total) * 100)

          return (
            <div key={leave.name}>
              <div className="flex items-center justify-between mb-1">
                <span className="text-sm font-medium text-gray-700">{leave.name}</span>
                <span className="text-sm text-gray-500">
                  {leave.used} / {leave.total} days
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2 mb-1">
                <div
                  className={cn('h-2 rounded-full', leave.color)}
                  style={{ width: `${percentage}%` }}
                />
              </div>
              <p className="text-xs text-gray-500">{leave.remaining} days remaining</p>
            </div>
          )
        })}
      </div>
    </Card>
  )
}

// Payroll Summary Widget
export interface PayrollSummaryProps {
  totalPayroll: number
  currency: string
  period: string
  employeeCount: number
  change?: number
  className?: string
}

export function PayrollSummary({
  totalPayroll,
  currency,
  period,
  employeeCount,
  change,
  className,
}: PayrollSummaryProps) {
  return (
    <Card className={cn('p-4', className)}>
      <div className="flex items-center justify-between mb-4">
        <h3 className="font-semibold text-gray-900">Payroll Summary</h3>
        <span className="text-sm text-gray-500">{period}</span>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <p className="text-xs text-gray-500">Total Payroll</p>
          <p className="text-2xl font-bold text-gray-900">
            {currency}
            {totalPayroll.toLocaleString()}
          </p>
          {change !== undefined && (
            <p className={cn(
              'text-xs flex items-center gap-1',
              change >= 0 ? 'text-green-600' : 'text-red-600'
            )}>
              {change >= 0 ? '↑' : '↓'} {Math.abs(change)}% from last period
            </p>
          )}
        </div>

        <div>
          <p className="text-xs text-gray-500">Employees</p>
          <div className="flex items-center gap-2">
            <Users className="h-5 w-5 text-gray-400" />
            <p className="text-2xl font-bold text-gray-900">{employeeCount}</p>
          </div>
        </div>
      </div>
    </Card>
  )
}
