export interface MessageProps {
  id: string;
  sessionId: string;
  type: import('../enums').MessageType;
  content: string;
  timestamp: Date;
  intent?: import('../enums').Intent;
  confidence?: number;
  metadata?: Record<string, any>;
}

export class Message {
  private constructor(private readonly props: MessageProps) {}
  get id(): string { return this.props.id; }
  get sessionId(): string { return this.props.sessionId; }
  get type(): import('../enums').MessageType { return this.props.type; }
  get content(): string { return this.props.content; }
  get timestamp(): Date { return this.props.timestamp; }
  get intent(): import('../enums').Intent | undefined { return this.props.intent; }
  get confidence(): number | undefined { return this.props.confidence; }
  static create(props: MessageProps): Message { return new Message(props); }
  toPlainObject(): MessageProps { return { ...this.props }; }
}

export interface ConversationContextProps {
  sessionId: string;
  customerId?: string;
  language: string;
  messages: MessageProps[];
  currentIntent: import('../enums').Intent;
  intentHistory: import('../enums').Intent[];
  entities: Record<string, any>;
  handoffRequested: boolean;
  startedAt: Date;
  lastActivityAt: Date;
}

export class ConversationContext {
  private constructor(private readonly props: ConversationContextProps) {}
  get sessionId(): string { return this.props.sessionId; }
  get customerId(): string | undefined { return this.props.customerId; }
  get language(): string { return this.props.language; }
  get messages(): MessageProps[] { return this.props.messages; }
  get currentIntent(): import('../enums').Intent { return this.props.currentIntent; }
  get intentHistory(): import('../enums').Intent[] { return this.props.intentHistory; }
  get entities(): Record<string, any> { return this.props.entities; }
  get handoffRequested(): boolean { return this.props.handoffRequested; }
  get startedAt(): Date { return this.props.startedAt; }
  get lastActivityAt(): Date { return this.props.lastActivityAt; }

  static create(props: ConversationContextProps): ConversationContext { return new ConversationContext(props); }

  addMessage(message: MessageProps): ConversationContext {
    return ConversationContext.create({
      ...this.props,
      messages: [...this.props.messages, message],
      lastActivityAt: new Date(),
    });
  }

  updateEntities(entities: Record<string, any>): ConversationContext {
    return ConversationContext.create({
      ...this.props,
      entities: { ...this.props.entities, ...entities },
    });
  }

  requestHandoff(): ConversationContext {
    return ConversationContext.create({ ...this.props, handoffRequested: true });
  }

  toPlainObject(): ConversationContextProps { return { ...this.props }; }
}

export interface BotResponseProps {
  message: string;
  intent: import('../enums').Intent;
  confidence: number;
  actions?: BotActionProps[];
  suggestedResponses?: string[];
  handoff?: {
    recommended: boolean;
    reason?: import('../enums').HandoffReason;
  };
}

export interface BotActionProps {
  type: 'link' | 'button' | 'form' | 'api_call';
  label: string;
  value?: string;
  url?: string;
}

export class BotResponse {
  private constructor(private readonly props: BotResponseProps) {}
  get message(): string { return this.props.message; }
  get intent(): import('../enums').Intent { return this.props.intent; }
  get confidence(): number { return this.props.confidence; }
  get actions() { return this.props.actions; }
  get suggestedResponses() { return this.props.suggestedResponses; }
  get handoff() { return this.props.handoff; }
  static create(props: BotResponseProps): BotResponse { return new BotResponse(props); }
  toPlainObject(): BotResponseProps { return { ...this.props }; }
}
