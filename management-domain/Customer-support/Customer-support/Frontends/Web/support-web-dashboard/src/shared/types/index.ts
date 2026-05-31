// User & Authentication Types
export type SupportUserRole =
  | 'SUPPORT_LEAD'
  | 'SUPPORT_AGENT'
  | 'SPECIALIST_AGENT'
  | 'CUSTOMER'
  | 'ADMIN'

export type Department =
  | 'customer-support'
  | 'sales'
  | 'technical'
  | 'billing'
  | 'executive'

export interface SupportUser {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: SupportUserRole
  department?: Department
  avatar?: string
  permissions: string[]
  country?: string
  isActive: boolean
  createdAt: string
  updatedAt: string
  agentId?: string
  customerId?: string
}

// Ticket Types
export type TicketStatus =
  | 'open'
  | 'pending'
  | 'in_progress'
  | 'customer_reply'
  | 'resolved'
  | 'closed'
  | 'escalated'

export type TicketPriority = 'low' | 'medium' | 'high' | 'urgent'

export type TicketChannel = 'email' | 'chat' | 'phone' | 'web' | 'api' | 'social'

export type TicketCategory =
  | 'technical'
  | 'billing'
  | 'account'
  | 'feature_request'
  | 'bug_report'
  | 'general_inquiry'
  | 'complaint'
  | 'other'

export interface Ticket {
  id: string
  ticketNumber: string
  subject: string
  description: string
  status: TicketStatus
  priority: TicketPriority
  channel: TicketChannel
  category: TicketCategory
  customerId: string
  customerName: string
  customerEmail: string
  assignedTo?: string
  assignedAgentName?: string
  assignedTeam?: string
  country?: string
  language: string
  tags: string[]
  createdDate: string
  updatedDate: string
  dueDate?: string
  resolvedDate?: string
  closedDate?: string
  slaBreached: boolean
  slaWarning: boolean
  firstResponseTime?: number // in minutes
  resolutionTime?: number // in minutes
  satisfactionRating?: number // 1-5
  sourceCountry?: string
  isEscalated: boolean
  escalationLevel?: number
  parentTicketId?: string
  relatedTicketIds?: string[]
  attachments?: TicketAttachment[]
}

export interface TicketAttachment {
  id: string
  name: string
  url: string
  size: number
  type: string
  uploadedAt: string
  uploadedBy: string
}

// Customer Types
export interface Customer {
  id: string
  firstName: string
  lastName: string
  email: string
  phone?: string
  avatar?: string
  country: string
  language: string
  timezone: string
  tier: 'free' | 'basic' | 'premium' | 'enterprise'
  status: 'active' | 'inactive' | 'suspended'
  createdAt: string
  lastLogin?: string
  totalTickets: number
  openTickets: number
  averageSatisfaction: number
  lifetimeValue: number
  company?: {
    name: string
    industry: string
    size: string
  }
  customFields?: Record<string, string | number | boolean>
}

// Agent/Team Types
export interface SupportAgent {
  id: string
  userId: string
  firstName: string
  lastName: string
  email: string
  avatar?: string
  role: SupportUserRole
  department?: Department
  teams: string[]
  skills: string[]
  languages: string[]
  status: 'online' | 'away' | 'offline' | 'busy' | 'in_call' | 'in_chat'
  currentStatus?: string
  country?: string
  isActive: boolean
  capacity: number
  currentLoad: number
  metrics: AgentMetrics
  createdAt: string
  lastActive: string
}

export interface AgentMetrics {
  totalTicketsAssigned: number
  totalTicketsResolved: number
  averageFirstResponseTime: number // in minutes
  averageResolutionTime: number // in minutes
  customerSatisfactionScore: number // 1-5
  slaComplianceRate: number // percentage
  ticketsToday: number
  ticketsThisWeek: number
  ticketsThisMonth: number
}

export interface SupportTeam {
  id: string
  name: string
  description: string
  department: Department
  country?: string
  leadId: string
  leadName: string
  memberIds: string[]
  members: SupportAgent[]
  specialization: string[]
  capacity: number
  currentLoad: number
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// SLA Types
export interface SLA {
  id: string
  name: string
  description: string
  isActive: boolean
  priorities: SLAPriority[]
  businessHours: BusinessHours
  holidays: Holiday[]
  createdAt: string
  updatedAt: string
}

export interface SLAPriority {
  priority: TicketPriority
  firstResponseTarget: number // in minutes
  resolutionTarget: number // in minutes
  updateInterval: number // in minutes
}

export interface BusinessHours {
  timezone: string
  schedule: {
    dayOfWeek: number // 0-6, Sunday-Saturday
    isOpen: boolean
    openTime?: string // HH:mm
    closeTime?: string // HH:mm
  }[]
}

export interface Holiday {
  id: string
  name: string
  date: string
  isRecurring: boolean
}

export interface SLAViolation {
  id: string
  ticketId: string
  ticketNumber: string
  slaType: 'first_response' | 'resolution' | 'update'
  priority: TicketPriority
  dueDate: string
  breachedDate: string
  overdueBy: number // in minutes
  assignedTo?: string
  isNotified: boolean
  status: 'open' | 'acknowledged' | 'escalated' | 'resolved'
}

// Knowledge Base Types
export interface KBArticle {
  id: string
  title: string
  slug: string
  content: string
  excerpt: string
  category: string
  tags: string[]
  language: string
  country?: string
  status: 'draft' | 'published' | 'archived'
  authorId: string
  authorName: string
  views: number
  helpfulCount: number
  notHelpfulCount: number
  relatedArticles: string[]
  attachments?: KBAttachment[]
  createdAt: string
  updatedAt: string
  publishedAt?: string
}

export interface KBAttachment {
  id: string
  name: string
  url: string
  type: string
  size: number
}

export interface KBCategory {
  id: string
  name: string
  slug: string
  description: string
  icon?: string
  parentId?: string
  order: number
  articleCount: number
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// Chat Types
export interface ChatSession {
  id: string
  ticketId?: string
  customerName: string
  customerId: string
  agentId?: string
  agentName?: string
  status: 'waiting' | 'active' | 'ended' | 'transferred'
  channel: 'web' | 'mobile' | 'widget'
  country: string
  language: string
  startedAt: string
  endedAt?: string
  messages: ChatMessage[]
  satisfactionRating?: number
  tags: string[]
}

export interface ChatMessage {
  id: string
  sessionId: string
  senderId: string
  senderName: string
  senderType: 'customer' | 'agent' | 'system'
  content: string
  type: 'text' | 'file' | 'image' | 'system'
  attachments?: ChatAttachment[]
  sentAt: string
  isRead: boolean
  readAt?: string
}

export interface ChatAttachment {
  id: string
  name: string
  url: string
  type: string
  size: number
}

// Phone Support Types
export interface PhoneCall {
  id: string
  ticketId?: string
  customerId: string
  customerName: string
  customerPhone: string
  agentId?: string
  agentName?: string
  direction: 'inbound' | 'outbound'
  status: 'queued' | 'ringing' | 'in_progress' | 'completed' | 'failed' | 'missed' | 'voicemail'
  queueName: string
  country: string
  language: string
  startedAt: string
  endedAt?: string
  duration?: number // in seconds
  recordingUrl?: string
  transcription?: string
  disposition?: string
  notes?: string
  satisfactionRating?: number
}

export interface CallQueue {
  id: string
  name: string
  description: string
  department: Department
  country?: string
  phoneNumbers: string[]
  agentIds: string[]
  maxWaitTime: number // in seconds
  maxQueueSize: number
  currentQueueSize: number
  averageWaitTime: number // in seconds
  serviceLevel: number // percentage target
  actualServiceLevel: number // percentage
  isActive: boolean
  businessHours: BusinessHours
}

export interface IVRMenu {
  id: string
  name: string
  tree: IVRNode[]
  defaultAction: string
  language: string
  country?: string
  isActive: boolean
}

export interface IVRNode {
  id: string
  prompt: string
  audioUrl?: string
  options: IVROption[]
  defaultAction?: string
  order: number
}

export interface IVROption {
  key: string // DTMF key or speech command
  label: string
  action: string
  targetNodeId?: string
}

// Quality Management Types
export interface QualityReview {
  id: string
  ticketId: string
  ticketNumber: string
  agentId: string
  agentName: string
  reviewerId: string
  reviewerName: string
  type: 'ticket' | 'chat' | 'call'
  interactionId: string
  status: 'pending' | 'completed' | 'disputed'
  overallScore: number // 0-100
  criteria: ReviewCriterion[]
  feedback: string
  strengths: string[]
  areasForImprovement: string[]
  reviewedAt: string
  createdAt: string
}

export interface ReviewCriterion {
  id: string
  name: string
  description: string
  score: number // 0-100
  weight: number // 0-1
  maxScore: number
  comments?: string
}

export interface QAForm {
  id: string
  name: string
  description: string
  type: 'ticket' | 'chat' | 'call'
  criteria: FormCriterion[]
  department?: Department
  country?: string
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export interface FormCriterion {
  id: string
  name: string
  description: string
  weight: number
  maxScore: number
  order: number
  required: boolean
}

// Analytics & Metrics Types
export interface SupportMetrics {
  totalTickets: number
  openTickets: number
  resolvedTickets: number
  closedTickets: number
  escalatedTickets: number
  slaBreaches: number
  averageFirstResponseTime: number
  averageResolutionTime: number
  customerSatisfactionScore: number
  netPromoterScore: number
  firstContactResolution: number
  totalInteractions: number
  channelBreakdown: ChannelMetrics[]
  agentPerformance: AgentPerformanceMetric[]
}

export interface ChannelMetrics {
  channel: TicketChannel
  count: number
  percentage: number
  averageResponseTime: number
  averageSatisfaction: number
}

export interface AgentPerformanceMetric {
  agentId: string
  agentName: string
  ticketsAssigned: number
  ticketsResolved: number
  averageResponseTime: number
  averageResolutionTime: number
  satisfactionScore: number
  slaCompliance: number
  status: string
}

export interface TrendData {
  date: string
  value: number
  tickets?: number
  resolved?: number
  slaBreached?: number
}

// Country/Region Types
export type CountryCode =
  | 'US'
  | 'CA'
  | 'GB'
  | 'NG'
  | 'KE'
  | 'ZA'
  | 'GH'
  | 'EG'
  | 'GLOBAL'

export interface CountryMetrics {
  country: CountryCode
  countryName: string
  totalTickets: number
  openTickets: number
  resolvedTickets: number
  averageResponseTime: number
  averageResolutionTime: number
  satisfactionScore: number
  slaCompliance: number
  activeAgents: number
  teams: SupportTeam[]
}

// Filter & Sort Types
export interface TicketFilters {
  status?: TicketStatus[]
  priority?: TicketPriority[]
  channel?: TicketChannel[]
  category?: TicketCategory[]
  assignedTo?: string[]
  country?: CountryCode[]
  dateRange?: {
    from: string
    to: string
  }
  search?: string
}

export interface TicketSort {
  field: 'createdDate' | 'updatedDate' | 'priority' | 'status' | 'dueDate'
  order: 'asc' | 'desc'
}

// Notification Types
export interface Notification {
  id: string
  userId: string
  type: 'ticket_assigned' | 'ticket_updated' | 'sla_warning' | 'sla_breach' | 'chat_message' | 'call_incoming'
  title: string
  message: string
  data?: Record<string, unknown>
  isRead: boolean
  createdAt: string
}
