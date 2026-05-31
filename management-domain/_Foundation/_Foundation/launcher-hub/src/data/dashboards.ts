export interface Dashboard {
  id: string
  name: string
  description: string
  category: 'executive' | 'department' | 'operations'
  port: number
  path: string
  command: string
  icon: string
  features: string[]
  status: 'running' | 'stopped' | 'pending'
  dependencies?: string[]
}

export const dashboards: Dashboard[] = [
  // Executive Domain
  {
    id: 'ceo-dashboard',
    name: 'CEO Dashboard',
    description: 'Strategic overview with executive insights, KPIs, and decision support',
    category: 'executive',
    port: 3000,
    path: 'Executive-domain/Frontends/Web/ceo-web-dashboard',
    command: 'npm run dev',
    icon: '👔',
    features: ['Strategic Health', 'KPI Monitoring', 'Approvals', 'Analytics', 'Reports'],
    status: 'stopped',
  },
  {
    id: 'executive-portal',
    name: 'Executive Portal',
    description: 'Unified portal for C-Suite executives across all domains',
    category: 'executive',
    port: 3001,
    path: 'Executive-domain/Frontends/Web/executive-web-portal',
    command: 'npm run dev',
    icon: '🏛️',
    features: ['Cross-Domain View', 'Executive Briefings', 'Strategy Alignment'],
    status: 'stopped',
  },
  {
    id: 'cfo-dashboard',
    name: 'CFO Dashboard',
    description: 'Financial oversight, budget management, and fiscal analytics',
    category: 'executive',
    port: 3010,
    path: 'Finance-department/Frontends/Web/finance-web-dashboard/cfo-dashboard',
    command: 'npm run dev',
    icon: '💰',
    features: ['Financial Health', 'Budget Oversight', 'Fiscal Planning'],
    status: 'stopped',
  },

  // Departments
  {
    id: 'finance-dashboard',
    name: 'Finance Department',
    description: 'Complete financial management system with AP, AR, GL, and reporting',
    category: 'department',
    port: 3002,
    path: 'Finance-department/Frontends/Web/finance-web-dashboard',
    command: 'npm run dev',
    icon: '💵',
    features: ['Accounts Payable', 'Accounts Receivable', 'General Ledger', 'Budgeting', 'Tax', 'Reports'],
    status: 'stopped',
  },
  {
    id: 'finance-portal',
    name: 'Finance Portal',
    description: 'Self-service portal for finance team and vendors',
    category: 'department',
    port: 3011,
    path: 'Finance-department/Frontends/Web/finance-web-portal',
    command: 'npm run dev',
    icon: '🔐',
    features: ['Invoice Submission', 'Payment Status', 'Document Access'],
    status: 'stopped',
  },
  {
    id: 'hr-dashboard',
    name: 'HR Department',
    description: 'Human resources management with employee lifecycle, payroll, and leave',
    category: 'department',
    port: 3003,
    path: 'Human-resource/Frontends/Web/hr-web-dashboard',
    command: 'npm run dev',
    icon: '👥',
    features: ['Employee Management', 'Leave Tracking', 'Payroll', 'Onboarding', 'Performance'],
    status: 'stopped',
  },
  {
    id: 'hr-portal',
    name: 'HR Portal',
    description: 'Employee self-service portal for HR activities',
    category: 'department',
    port: 3012,
    path: 'Human-resource/Frontends/Web/hr-web-portal',
    command: 'npm run dev',
    icon: '👤',
    features: ['Leave Requests', 'Payslips', 'Profile Management', 'Benefits'],
    status: 'stopped',
  },
  {
    id: 'sales-dashboard',
    name: 'Sales Department',
    description: 'Sales pipeline, customer management, forecasting and commission tracking',
    category: 'department',
    port: 3004,
    path: 'Sales-department/Frontends/Web/sales-web-dashboard',
    command: 'npm run dev',
    icon: '📈',
    features: ['Pipeline', 'Leads', 'Opportunities', 'Forecasting', 'Commission', 'Analytics'],
    status: 'stopped',
  },
  {
    id: 'sales-portal',
    name: 'Sales Portal',
    description: 'Sales team collaboration and customer relationship portal',
    category: 'department',
    port: 3013,
    path: 'Sales-department/Frontends/Web/sales-web-portal',
    command: 'npm run dev',
    icon: '🤝',
    features: ['Customer Portal', 'Collaboration', 'Document Sharing'],
    status: 'stopped',
  },
  {
    id: 'support-dashboard',
    name: 'Customer Support',
    description: 'Support ticketing, knowledge base, and customer communication',
    category: 'department',
    port: 3005,
    path: 'Customer-support/Frontends/Web/support-web-dashboard',
    command: 'npm run dev',
    icon: '🎧',
    features: ['Ticket Management', 'Knowledge Base', 'Live Chat', 'Phone', 'SLA', 'Analytics'],
    status: 'stopped',
  },
  {
    id: 'support-portal',
    name: 'Support Portal',
    description: 'Customer self-service portal for tickets and knowledge base',
    category: 'department',
    port: 3014,
    path: 'Customer-support/Frontends/Web/support-web-portal',
    command: 'npm run dev',
    icon: '❓',
    features: ['Submit Tickets', 'Track Status', 'Knowledge Base Search', 'Community'],
    status: 'stopped',
  },

  // Operations
  {
    id: 'admin-dashboard',
    name: 'System Administrator',
    description: 'System administration, access control, and infrastructure management',
    category: 'operations',
    port: 3006,
    path: 'System-administrator/Frontends/Web/admin-web-dashboard',
    command: 'npm run dev',
    icon: '⚙️',
    features: ['Access Control', 'User Management', 'Infrastructure', 'Security', 'Deployments', 'Monitoring'],
    status: 'stopped',
  },
  {
    id: 'admin-portal',
    name: 'Admin Portal',
    description: 'IT service desk and admin operations portal',
    category: 'operations',
    port: 3015,
    path: 'System-administrator/Frontends/Web/admin-web-portal',
    command: 'npm run dev',
    icon: '🔧',
    features: ['Service Requests', 'Asset Management', 'Password Reset'],
    status: 'stopped',
  },
  {
    id: 'gbm-portal',
    name: 'Global Business Management',
    description: 'Multi-country business operations and regional analytics',
    category: 'operations',
    port: 3007,
    path: 'Global-business-management/Frontends/Web/gbm-web-portal',
    command: 'npm run dev',
    icon: '🌍',
    features: ['Country Management', 'Currency', 'Regional Analytics', 'Compliance', 'Reports'],
    status: 'stopped',
  },
  {
    id: 'monitoring-dashboard',
    name: 'Foundation Services Monitoring',
    description: 'Real-time monitoring of all foundation services and infrastructure',
    category: 'operations',
    port: 3008,
    path: 'Foundation-Services-Monitoring/Frontends/Web/monitoring-web-dashboard',
    command: 'npm run dev',
    icon: '📊',
    features: ['Service Health', 'Dependencies', 'Performance Metrics', 'Alerts', 'Incidents'],
    status: 'stopped',
  },
  {
    id: 'ai-monitoring',
    name: 'AI Orchestration Monitor',
    description: 'AI services and orchestration layer monitoring',
    category: 'operations',
    port: 3009,
    path: 'Foundation-Services-Monitoring/Frontends/Web/ai-orchestration-monitoring-dashboard',
    command: 'npm run dev',
    icon: '🤖',
    features: ['AI Service Health', 'Model Performance', 'Orchestration Flow', 'Resource Usage'],
    status: 'stopped',
  },
]

export const categories = [
  { id: 'all', name: 'All Dashboards', icon: '🎯' },
  { id: 'executive', name: 'Executive', icon: '👔' },
  { id: 'department', name: 'Departments', icon: '🏢' },
  { id: 'operations', name: 'Operations', icon: '⚙️' },
]

export const getDashboardsByCategory = (category: string) => {
  if (category === 'all') return dashboards
  return dashboards.filter(d => d.category === category)
}

export const getDashboardById = (id: string) => {
  return dashboards.find(d => d.id === id)
}
