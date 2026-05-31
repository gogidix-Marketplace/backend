import { SentimentLabel } from '../enums';

export interface SentimentScoreProps {
  score: number;
  normalized: number;
  label: SentimentLabel;
  confidence: number;
}

export class SentimentScore {
  private constructor(private readonly props: SentimentScoreProps) {}

  get score(): number { return this.props.score; }
  get normalized(): number { return this.props.normalized; }
  get label(): SentimentLabel { return this.props.label; }
  get confidence(): number { return this.props.confidence; }

  static create(props: SentimentScoreProps): SentimentScore {
    return new SentimentScore(props);
  }

  toPlainObject(): SentimentScoreProps { return { ...this.props }; }
}

export interface SentimentAnalysisProps {
  id: string;
  ticketId: string;
  text: string;
  sentiment: SentimentScoreProps;
  emotions: {
    joy: number;
    sadness: number;
    anger: number;
    fear: number;
    disgust: number;
    surprise: number;
  };
  keywords: Array<{ word: string; sentiment: string; score: number }>;
  language: string;
  timestamp: Date;
  createdAt: Date;
  updatedAt: Date;
}

export class SentimentAnalysis {
  private constructor(private readonly props: SentimentAnalysisProps) {}

  get id(): string { return this.props.id; }
  get ticketId(): string { return this.props.ticketId; }
  get text(): string { return this.props.text; }
  get sentiment(): SentimentScoreProps { return this.props.sentiment; }
  get emotions() { return this.props.emotions; }
  get keywords() { return this.props.keywords; }
  get language(): string { return this.props.language; }
  get timestamp(): Date { return this.props.timestamp; }
  get createdAt(): Date { return this.props.createdAt; }

  static create(props: SentimentAnalysisProps): SentimentAnalysis {
    return new SentimentAnalysis(props);
  }

  isNegative(): boolean {
    return this.props.sentiment.normalized < 30;
  }

  isCritical(): boolean {
    return this.props.sentiment.label === SentimentLabel.ANGRY || this.props.sentiment.normalized < 15;
  }

  toPlainObject(): SentimentAnalysisProps {
    return { ...this.props };
  }
}

export interface SentimentAlertProps {
  id: string;
  alertId: string;
  ticketId: string;
  customerId?: string;
  sentimentScore: number;
  sentimentLabel: SentimentLabel;
  severity: 'low' | 'medium' | 'high' | 'critical';
  triggeredBy: string;
  message: string;
  acknowledged: boolean;
  acknowledgedBy?: string;
  acknowledgedAt?: Date;
  resolvedAt?: Date;
  createdAt: Date;
}

export class SentimentAlert {
  private constructor(private readonly props: SentimentAlertProps) {}

  get id(): string { return this.props.id; }
  get alertId(): string { return this.props.alertId; }
  get ticketId(): string { return this.props.ticketId; }
  get customerId(): string | undefined { return this.props.customerId; }
  get sentimentScore(): number { return this.props.sentimentScore; }
  get sentimentLabel(): SentimentLabel { return this.props.sentimentLabel; }
  get severity(): string { return this.props.severity; }
  get message(): string { return this.props.message; }
  get acknowledged(): boolean { return this.props.acknowledged; }
  get createdAt(): Date { return this.props.createdAt; }

  static create(props: SentimentAlertProps): SentimentAlert {
    return new SentimentAlert(props);
  }

  acknowledge(userId: string): SentimentAlert {
    return SentimentAlert.create({
      ...this.props,
      acknowledged: true,
      acknowledgedBy: userId,
      acknowledgedAt: new Date(),
    });
  }

  resolve(): SentimentAlert {
    return SentimentAlert.create({
      ...this.props,
      resolvedAt: new Date(),
    });
  }

  isActive(): boolean {
    return !this.props.resolvedAt;
  }

  toPlainObject(): SentimentAlertProps {
    return { ...this.props };
  }
}
