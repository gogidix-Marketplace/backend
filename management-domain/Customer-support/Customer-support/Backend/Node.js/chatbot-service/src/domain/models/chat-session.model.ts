import { AggregateRoot } from '@shared/base';
import { SessionStatus } from '../enums';
import { Message, MessageProps } from './message.model';
import { SessionContext, SessionContextProps } from './session-context.model';

export interface SentimentProps {
  score: number;
  label: 'positive' | 'neutral' | 'negative';
}

export interface HandoffRequestProps {
  sessionId: string;
  reason: string;
  priority: 'low' | 'medium' | 'high' | 'urgent';
  requiredSkills: string[];
  summary?: string;
  conversationHistory?: MessageProps[];
  customerInfo?: Record<string, unknown>;
}

export interface ChatSessionProps {
  sessionId: string;
  userId?: string;
  customerId?: string;
  status: SessionStatus;
  language: string;
  startedAt: Date;
  endedAt?: Date;
  lastActivityAt: Date;
  context: SessionContextProps;
  messages: MessageProps[];
  assignedAgentId?: string;
  handoffRequest?: HandoffRequestProps;
  sentiment?: SentimentProps;
  tags: string[];
  metadata: Record<string, unknown>;
}

export class ChatSession extends AggregateRoot {
  private props: ChatSessionProps;

  constructor(id: string, props: ChatSessionProps) {
    super(id, props.startedAt, props.lastActivityAt);
    this.props = props;
  }

  static create(props: Partial<ChatSessionProps> & { sessionId: string }): ChatSession {
    const now = new Date();
    return new ChatSession(props.sessionId, {
      sessionId: props.sessionId,
      status: props.status || SessionStatus.ACTIVE,
      language: props.language || 'en',
      startedAt: props.startedAt || now,
      lastActivityAt: props.lastActivityAt || now,
      context: props.context || SessionContext.create({}).toPlainObject(),
      messages: props.messages || [],
      tags: props.tags || [],
      metadata: props.metadata || {},
      userId: props.userId,
      customerId: props.customerId,
      endedAt: props.endedAt,
      assignedAgentId: props.assignedAgentId,
      handoffRequest: props.handoffRequest,
      sentiment: props.sentiment,
    });
  }

  get sessionId(): string {
    return this.props.sessionId;
  }

  get userId(): string | undefined {
    return this.props.userId;
  }

  get customerId(): string | undefined {
    return this.props.customerId;
  }

  get status(): SessionStatus {
    return this.props.status;
  }

  get language(): string {
    return this.props.language;
  }

  get startedAt(): Date {
    return this.props.startedAt;
  }

  get endedAt(): Date | undefined {
    return this.props.endedAt;
  }

  get lastActivityAt(): Date {
    return this.props.lastActivityAt;
  }

  get context(): SessionContextProps {
    return this.props.context;
  }

  get messages(): MessageProps[] {
    return this.props.messages;
  }

  get assignedAgentId(): string | undefined {
    return this.props.assignedAgentId;
  }

  get handoffRequest(): HandoffRequestProps | undefined {
    return this.props.handoffRequest;
  }

  get sentiment(): SentimentProps | undefined {
    return this.props.sentiment;
  }

  get tags(): string[] {
    return this.props.tags;
  }

  get metadata(): Record<string, unknown> {
    return this.props.metadata;
  }

  get duration(): number {
    const end = this.props.endedAt || new Date();
    return end.getTime() - this.props.startedAt.getTime();
  }

  get isExpired(): boolean {
    return (
      this.props.status === SessionStatus.ACTIVE &&
      Date.now() - this.props.lastActivityAt.getTime() > 1800000
    );
  }

  addMessage(message: Message): void {
    this.props.messages.push(message.toPlainObject());
    this.props.context.turnCount = (this.props.context.turnCount || 0) + 1;
    this.props.lastActivityAt = new Date();
    this.props.context.lastActivity = new Date();
    this.touch();
  }

  updateStatus(status: SessionStatus): void {
    this.props.status = status;
    if (status === SessionStatus.CLOSED || status === SessionStatus.TIMEOUT) {
      this.props.endedAt = new Date();
    }
    this.props.lastActivityAt = new Date();
    this.touch();
  }

  updateSentiment(score: number, label: 'positive' | 'neutral' | 'negative'): void {
    this.props.sentiment = { score, label };
    this.touch();
  }

  addTag(tag: string): void {
    if (!this.props.tags.includes(tag)) {
      this.props.tags.push(tag);
      this.touch();
    }
  }

  setMetadata(key: string, value: unknown): void {
    if (!this.props.metadata) {
      this.props.metadata = {};
    }
    this.props.metadata[key] = value;
    this.touch();
  }

  toPlainObject(): ChatSessionProps {
    return { ...this.props };
  }
}
