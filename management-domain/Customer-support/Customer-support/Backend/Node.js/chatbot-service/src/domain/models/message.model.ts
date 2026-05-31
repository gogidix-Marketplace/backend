export interface MessageProps {
  id: string;
  sessionId: string;
  content: string;
  sender: 'user' | 'bot' | 'agent';
  timestamp: Date;
  language: string;
  metadata?: Record<string, unknown>;
  confidence?: number;
}

export class Message {
  private constructor(private readonly props: MessageProps) {}

  static create(props: MessageProps): Message {
    return new Message({
      ...props,
      timestamp: props.timestamp || new Date(),
      language: props.language || 'en',
    });
  }

  get id(): string {
    return this.props.id;
  }

  get sessionId(): string {
    return this.props.sessionId;
  }

  get content(): string {
    return this.props.content;
  }

  get sender(): 'user' | 'bot' | 'agent' {
    return this.props.sender;
  }

  get timestamp(): Date {
    return this.props.timestamp;
  }

  get language(): string {
    return this.props.language;
  }

  get metadata(): Record<string, unknown> | undefined {
    return this.props.metadata;
  }

  get confidence(): number | undefined {
    return this.props.confidence;
  }

  toPlainObject(): MessageProps {
    return { ...this.props };
  }
}
