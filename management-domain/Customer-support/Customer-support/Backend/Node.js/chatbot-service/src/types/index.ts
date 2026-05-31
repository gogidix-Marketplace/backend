/**
 * Type definitions for Chatbot Service
 */

import { Document, Types } from 'mongoose';

// ============= Core Types =============

export interface ServiceConfig {
  port: number;
  host: string;
  nodeEnv: string;
  mongoUri: string;
  mongoDbName: string;
  redis: {
    host: string;
    port: number;
    password?: string;
    db: number;
  };
  jwt: {
    secret: string;
    expiresIn: string;
    refreshSecret: string;
    refreshExpiresIn: string;
  };
  openai: {
    apiKey: string;
    model: string;
    maxTokens: number;
    temperature: number;
  };
  services: {
    knowledgeBase: string;
    ticket: string;
    agent: string;
  };
  rateLimit: {
    windowMs: number;
    maxRequests: number;
  };
  logging: {
    level: string;
    filePath: string;
  };
  cors: {
    origin: string;
  };
  session: {
    timeoutMs: number;
    maxContextTurns: number;
  };
  handoff: {
    threshold: number;
    maxAutomatedTurns: number;
    escalationTopics: string[];
  };
  i18n: {
    defaultLanguage: string;
    supportedLanguages: string[];
  };
}

export interface JwtPayload {
  sub: string;
  userId: string;
  sessionId: string;
  role: string;
  iat?: number;
  exp?: number;
}

export interface AuthenticatedRequest extends Request {
  user?: JwtPayload;
  sessionId?: string;
}

// ============= Chat Session Types =============

export enum SessionStatus {
  ACTIVE = 'active',
  WAITING_FOR_AGENT = 'waiting_for_agent',
  WITH_AGENT = 'with_agent',
  CLOSED = 'closed',
  TIMEOUT = 'timeout',
}

export interface IMessage {
  id: string;
  sessionId: string;
  content: string;
  sender: 'user' | 'bot' | 'agent';
  timestamp: Date;
  language: string;
  metadata?: Record<string, unknown>;
  confidence?: number;
}

export interface ISessionContext {
  userId?: string;
  customerId?: string;
  locale: string;
  timezone?: string;
  metadata: Record<string, unknown>;
  accumulatedContext: Record<string, unknown>;
  turnCount: number;
  lastActivity: Date;
}

export interface IHandoffRequest {
  sessionId: string;
  reason: string;
  priority: 'low' | 'medium' | 'high' | 'urgent';
  requiredSkills: string[];
  summary: string;
  conversationHistory: IMessage[];
  customerInfo?: Record<string, unknown>;
}

export interface IChatSession extends Document {
  _id: Types.ObjectId;
  sessionId: string;
  userId?: string;
  customerId?: string;
  status: SessionStatus;
  language: string;
  startedAt: Date;
  endedAt?: Date;
  lastActivityAt: Date;
  context: ISessionContext;
  messages: Types.DocumentArray<IMessage>;
  assignedAgentId?: string;
  handoffRequest?: IHandoffRequest;
  sentiment?: {
    score: number;
    label: 'positive' | 'neutral' | 'negative';
  };
  tags: string[];
  metadata: Record<string, unknown>;
}

// ============= Intent Types =============

export enum IntentCategory {
  GREETING = 'greeting',
  FAQ = 'faq',
  ORDER_STATUS = 'order_status',
  PRODUCT_INFO = 'product_info',
  SUPPORT = 'support',
  COMPLAINT = 'complaint',
  REFUND = 'refund',
  BILLING = 'billing',
  TECHNICAL = 'technical',
  SHIPPING = 'shipping',
  RETURNS = 'returns',
  ACCOUNT = 'account',
  PAYMENT = 'payment',
  GENERAL = 'general',
  ESCALATION = 'escalation',
}

export enum IntentConfidence {
  HIGH = 'high',
  MEDIUM = 'medium',
  LOW = 'low',
}

export interface IIntent {
  name: string;
  category: IntentCategory;
  description: string;
  trainingPhrases: string[];
  responses: string[];
  parameters?: IParameter[];
  requiredSkills?: string[];
  requiresHandoff: boolean;
  priority: number;
  language: string;
  isActive: boolean;
}

export interface IParameter {
  name: string;
  type: string;
  required: boolean;
  entityType: string;
  prompts: string[];
}

export interface IIntentDetection {
  intent: string;
  category: IntentCategory;
  confidence: number;
  confidenceLevel: IntentConfidence;
  entities: IEntity[];
  sentiment: {
    score: number;
    label: 'positive' | 'neutral' | 'negative';
  };
  language: string;
  requiresHandoff: boolean;
  suggestedResponses: string[];
}

export interface IEntity {
  type: string;
  value: string;
  confidence: number;
  start: number;
  end: number;
  resolved?: boolean;
}

// ============= Knowledge Base Types =============

export interface IKnowledgeArticle {
  articleId: string;
  title: string;
  content: string;
  category: string;
  tags: string[];
  language: string;
  relevanceScore: number;
}

export interface ISearchResult {
  articles: IKnowledgeArticle[];
  totalResults: number;
  searchTime: number;
}

// ============= Bot Response Types =============

export enum ResponseType {
  TEXT = 'text',
  QUICK_REPLIES = 'quick_replies',
  CARDS = 'cards',
  LIST = 'list',
  IMAGE = 'image',
  HANDOFF = 'handoff',
  ERROR = 'error',
}

export interface IQuickReply {
  title: string;
  payload: string;
  image?: string;
}

export interface ICard {
  title: string;
  description?: string;
  imageUrl?: string;
  buttons?: IButton[];
}

export interface IButton {
  title: string;
  payload: string;
  url?: string;
  type: 'postback' | 'web_url' | 'phone_number';
}

export interface IBotResponse {
  type: ResponseType;
  text?: string;
  quickReplies?: IQuickReply[];
  cards?: ICard[];
  elements?: ICard[];
  imageUrl?: string;
  handoff?: {
    agentId?: string;
    reason: string;
    queuePosition?: number;
  };
  metadata?: Record<string, unknown>;
  confidence: number;
  intent?: string;
  language: string;
}

// ============= Analytics Types =============

export interface ISessionAnalytics {
  sessionId: string;
  userId?: string;
  duration: number;
  messageCount: number;
  turnCount: number;
  resolutionStatus: 'resolved' | 'escalated' | 'abandoned';
  intents: string[];
  sentiment: {
    average: number;
    trend: 'improving' | 'declining' | 'stable';
  };
  handoffRequired: boolean;
  agentId?: string;
  customerSatisfaction?: number;
}

export interface IDailyAnalytics {
  date: Date;
  totalSessions: number;
  activeSessions: number;
  resolvedByBot: number;
  escalatedToAgent: number;
  averageResolutionTime: number;
  averageSessionDuration: number;
  topIntents: Array<{ intent: string; count: number }>;
  sentimentDistribution: {
    positive: number;
    neutral: number;
    negative: number;
  };
  languageDistribution: Record<string, number>;
}

// ============= API Request/Response Types =============

export interface SendMessageRequest {
  sessionId?: string;
  message: string;
  language?: string;
  metadata?: Record<string, unknown>;
  customerId?: string;
}

export interface SendMessageResponse {
  sessionId: string;
  response: IBotResponse;
  timestamp: Date;
}

export interface CreateSessionRequest {
  customerId?: string;
  language?: string;
  metadata?: Record<string, unknown>;
}

export interface HandoffRequest {
  sessionId: string;
  reason: string;
  priority?: 'low' | 'medium' | 'high' | 'urgent';
}

export interface FeedbackRequest {
  sessionId: string;
  rating: number;
  comment?: string;
  resolved: boolean;
}

// ============= Translation Types =============

export interface ITranslationRequest {
  text: string;
  targetLanguage: string;
  sourceLanguage?: string;
}

export interface ITranslationResponse {
  translatedText: string;
  sourceLanguage: string;
  targetLanguage: string;
  confidence: number;
}

// ============= WebSocket Types =============

export interface ISocketMessage {
  type: 'message' | 'typing' | 'handoff' | 'session_update' | 'error';
  sessionId: string;
  data: unknown;
  timestamp: Date;
}

export interface ITypingIndicator {
  isTyping: boolean;
  userId?: string;
  agentId?: string;
}

// ============= Error Types =============

export class ChatbotError extends Error {
  constructor(
    message: string,
    public code: string,
    public statusCode: number = 500,
    public details?: unknown
  ) {
    super(message);
    this.name = 'ChatbotError';
    Error.captureStackTrace(this, this.constructor);
  }
}

export class IntentDetectionError extends ChatbotError {
  constructor(message: string, details?: unknown) {
    super(message, 'INTENT_DETECTION_ERROR', 500, details);
    this.name = 'IntentDetectionError';
  }
}

export class SessionNotFoundError extends ChatbotError {
  constructor(sessionId: string) {
    super(`Session ${sessionId} not found`, 'SESSION_NOT_FOUND', 404);
    this.name = 'SessionNotFoundError';
  }
}

export class HandoffError extends ChatbotError {
  constructor(message: string, details?: unknown) {
    super(message, 'HANDOFF_ERROR', 500, details);
    this.name = 'HandoffError';
  }
}

export class TranslationError extends ChatbotError {
  constructor(message: string, details?: unknown) {
    super(message, 'TRANSLATION_ERROR', 500, details);
    this.name = 'TranslationError';
  }
}

// ============= Utility Types =============

export type DeepPartial<T> = {
  [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P];
};

export type PaginatedResult<T> = {
  data: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
};
