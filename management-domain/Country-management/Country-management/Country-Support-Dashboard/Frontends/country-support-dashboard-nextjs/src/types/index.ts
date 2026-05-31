// Domain types matching the backend DTOs

export enum TicketStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  PENDING_CUSTOMER = 'PENDING_CUSTOMER',
  RESOLVED = 'RESOLVED',
  CLOSED = 'CLOSED',
  ESCALATED = 'ESCALATED',
}

export enum TicketPriority {
  CRITICAL = 'CRITICAL',
  HIGH = 'HIGH',
  MEDIUM = 'MEDIUM',
  LOW = 'LOW',
}

export enum TicketCategory {
  TECHNICAL = 'TECHNICAL',
  BILLING = 'BILLING',
  ACCOUNT = 'ACCOUNT',
  PRODUCT = 'PRODUCT',
  SERVICE = 'SERVICE',
  COMPLAINT = 'COMPLAINT',
  COMPLIMENT = 'COMPLIMENT',
  OTHER = 'OTHER',
}

export enum AgentRole {
  AGENT = 'AGENT',
  SENIOR_AGENT = 'SENIOR_AGENT',
  TEAM_LEAD = 'TEAM_LEAD',
  SUPERVISOR = 'SUPERVISOR',
  MANAGER = 'MANAGER',
}

export enum AgentStatus {
  AVAILABLE = 'AVAILABLE',
  BUSY = 'BUSY',
  AWAY = 'AWAY',
  IN_MEETING = 'IN_MEETING',
  OFFLINE = 'OFFLINE',
  ON_BREAK = 'ON_BREAK',
}

export enum SLAStatus {
  COMPLIANT = 'COMPLIANT',
  AT_RISK = 'AT_RISK',
  BREACHED = 'BREACHED',
  PAUSED = 'PAUSED',
  NOT_APPLICABLE = 'N/A',
}

export enum EscalationStatus {
  PENDING = 'Pending',
  APPROVED = 'Approved',
  REJECTED = 'Rejected',
  IN_PROGRESS = 'In Progress',
  RESOLVED = 'Resolved',
  CANCELLED = 'Cancelled',
}

export enum KnowledgeBaseStatus {
  DRAFT = 'DRAFT',
  REVIEW = 'UNDER_REVIEW',
  PUBLISHED = 'PUBLISHED',
  ARCHIVED = 'ARCHIVED',
}

export interface Ticket {
  id: number;
  ticketNumber: string;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  category: TicketCategory;
  customerId: string;
  customerName: string;
  customerEmail: string;
  customerPhone?: string;
  assignedAgentId?: number;
  assignedAgentName?: string;
  teamId?: number;
  escalated: boolean;
  escalationId?: number;
  slaStatus: string;
  slaResponseDeadline?: string;
  slaResolutionDeadline?: string;
  firstResponseAt?: string;
  resolvedAt?: string;
  closedAt?: string;
  resolutionNotes?: string;
  countryCode: string;
  languageCode?: string;
  sourceChannel?: string;
  tags?: string;
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
  minutesToFirstResponse?: number;
  hoursToResolution?: number;
  responseOverdue?: boolean;
  resolutionOverdue?: boolean;
  comments?: TicketComment[];
  history?: TicketHistory[];
}

export interface TicketComment {
  id: number;
  ticketId: number;
  commentText: string;
  isInternal: boolean;
  authorId: string;
  authorName: string;
  authorEmail?: string;
  authorType: string;
  hasAttachments: boolean;
  createdAt: string;
  createdBy?: string;
}

export interface TicketHistory {
  id: number;
  ticketId: number;
  fieldName: string;
  oldValue?: string;
  newValue?: string;
  changeDescription: string;
  changedBy: string;
  changedByName?: string;
  createdAt: string;
}

export interface SupportAgent {
  id: number;
  employeeId: string;
  firstName: string;
  lastName: string;
  fullName: string;
  email: string;
  phoneNumber?: string;
  role: AgentRole;
  roleDisplayName: string;
  status: AgentStatus;
  statusDisplayName: string;
  teamId?: number;
  teamName?: string;
  countryCode: string;
  avatarUrl?: string;
  timezone?: string;
  languageCode?: string;
  dateHired?: string;
  isActive: boolean;
  maxConcurrentTickets: number;
  currentTicketCount: number;
  specialization?: string;
  skillTags?: string;
  createdAt: string;
  updatedAt: string;
  lastLoginAt?: string;
  canTakeMoreTickets: boolean;
}

export interface CustomerFeedback {
  id: number;
  ticketId?: number;
  ticketNumber?: string;
  customerId?: string;
  customerName?: string;
  customerEmail?: string;
  rating: number;
  ratingCategory: string;
  comments?: string;
  sentiment?: string;
  wouldRecommend?: boolean;
  agentId?: number;
  agentName?: string;
  feedbackSource?: string;
  isPublic: boolean;
  createdAt: string;
  createdBy?: string;
}

export interface SLACompliance {
  id: number;
  complianceDate: string;
  countryCode: string;
  totalTickets: number;
  slaCompliantCount: number;
  slaBreachedCount: number;
  slaAtRiskCount: number;
  responseSlaRate?: number;
  resolutionSlaRate?: number;
  avgResponseTimeMinutes?: number;
  avgResolutionTimeHours?: number;
  escalationRate?: number;
  customerSatisfactionScore?: number;
  targetResponseSla: number;
  targetResolutionSla: number;
  targetCsat: number;
  isCompliant: boolean;
  overallComplianceRate?: number;
  createdAt: string;
  updatedAt: string;
}

export interface Escalation {
  id: number;
  escalationNumber: string;
  ticketId: number;
  ticketNumber: string;
  ticketSubject?: string;
  status: EscalationStatus;
  statusDisplayName: string;
  escalationLevel?: TicketPriority;
  escalationReason: string;
  businessImpact?: string;
  stepsTaken?: string;
  requestedBy: string;
  requestedByName?: string;
  requestedAt: string;
  countryCode: string;
  hqTicketNumber?: string;
  hqAssignedTo?: string;
  hqResponse?: string;
  hqRespondedAt?: string;
  hqRespondedBy?: string;
  resolutionDetails?: string;
  resolvedAt?: string;
  resolutionSummary?: string;
  requiresFollowUp?: boolean;
  followUpDate?: string;
  createdAt: string;
  closedAt?: string;
  hoursSinceRequest?: number;
  overdue?: boolean;
}

export interface KnowledgeBaseArticle {
  id: number;
  articleNumber: string;
  title: string;
  summary?: string;
  content: string;
  status: KnowledgeBaseStatus;
  statusDisplayName: string;
  category?: string;
  tags?: string;
  countryCode?: string;
  languageCode?: string;
  isHqApproved: boolean;
  hqArticleId?: number;
  authorId?: string;
  authorName?: string;
  viewCount: number;
  helpfulCount: number;
  notHelpfulCount: number;
  helpfulnessPercentage?: number;
  lastReviewedAt?: string;
  lastReviewedBy?: string;
  publishedAt?: string;
  expiryDate?: string;
  relatedTicketsCount: number;
  createdAt: string;
  updatedAt: string;
  attachments?: KnowledgeBaseAttachment[];
}

export interface KnowledgeBaseAttachment {
  id: number;
  fileName: string;
  filePath: string;
  fileSize?: number;
  fileType?: string;
  contentType?: string;
  createdAt: string;
  createdBy?: string;
}

export interface DashboardMetrics {
  totalTickets: number;
  openTickets: number;
  inProgressTickets: number;
  resolvedTickets: number;
  closedTickets: number;
  escalatedTickets: number;
  slaResponseRate?: number;
  slaResolutionRate?: number;
  slaBreachedCount: number;
  slaAtRiskCount: number;
  avgResponseTimeMinutes?: number;
  avgResolutionTimeHours?: number;
  totalAgents: number;
  availableAgents: number;
  busyAgents: number;
  offlineAgents: number;
  averageRating?: number;
  totalFeedback: number;
  positiveFeedback: number;
  neutralFeedback: number;
  negativeFeedback: number;
  activeEscalations: number;
  pendingEscalations: number;
  resolvedEscalations: number;
  escalationRate?: number;
  totalArticles: number;
  publishedArticles: number;
  totalViews: number;
  ticketTrend?: Record<string, number>;
  csatTrend?: Record<string, number>;
  slaTrend?: Record<string, number>;
}

export interface TeamPerformance {
  agentId: number;
  agentName: string;
  role: string;
  ticketsResolved: number;
  avgResponseTime: number;
  customerSatisfaction: number;
  slaComplianceRate: number;
}

export interface CreateTicketRequest {
  subject: string;
  description: string;
  priority?: TicketPriority;
  category: TicketCategory;
  customerId: string;
  customerName: string;
  customerEmail: string;
  customerPhone?: string;
  assignedAgentId?: number;
  languageCode?: string;
  sourceChannel?: string;
  tags?: string;
}

export interface UpdateTicketRequest {
  status?: TicketStatus;
  priority?: TicketPriority;
  assignedAgentId?: number;
  resolutionNotes?: string;
  tags?: string;
}

export interface CreateCommentRequest {
  commentText: string;
  isInternal?: boolean;
}

export interface EscalateTicketRequest {
  reason: string;
  businessImpact?: string;
  stepsTaken?: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path?: string;
  validationErrors?: Record<string, string>;
}
