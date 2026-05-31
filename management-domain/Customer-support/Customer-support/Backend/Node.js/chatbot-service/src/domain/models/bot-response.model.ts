import { ResponseType } from '../enums';

export interface QuickReplyProps {
  title: string;
  payload: string;
  image?: string;
}

export interface CardProps {
  title: string;
  description?: string;
  imageUrl?: string;
  buttons?: ButtonProps[];
}

export interface ButtonProps {
  title: string;
  payload: string;
  url?: string;
  type: 'postback' | 'web_url' | 'phone_number';
}

export interface BotResponseProps {
  type: ResponseType;
  text?: string;
  quickReplies?: QuickReplyProps[];
  cards?: CardProps[];
  elements?: CardProps[];
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

export class BotResponse {
  private constructor(private props: BotResponseProps) {}

  static create(props: BotResponseProps): BotResponse {
    return new BotResponse(props);
  }

  static error(text: string, language: string): BotResponse {
    return new BotResponse({
      type: ResponseType.ERROR,
      text,
      confidence: 0,
      language,
    });
  }

  get type(): ResponseType {
    return this.props.type;
  }

  get text(): string | undefined {
    return this.props.text;
  }

  get quickReplies(): QuickReplyProps[] | undefined {
    return this.props.quickReplies;
  }

  get cards(): CardProps[] | undefined {
    return this.props.cards;
  }

  get confidence(): number {
    return this.props.confidence;
  }

  get intent(): string | undefined {
    return this.props.intent;
  }

  get language(): string {
    return this.props.language;
  }

  get metadata(): Record<string, unknown> | undefined {
    return this.props.metadata;
  }

  get handoff(): { agentId?: string; reason: string; queuePosition?: number } | undefined {
    return this.props.handoff;
  }

  toPlainObject(): BotResponseProps {
    return { ...this.props };
  }
}
