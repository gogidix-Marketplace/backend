export interface SentimentResultProps {
  text: string; sentiment: import('../enums').SentimentType; score: number;
  confidence: number; keywords: string[]; language?: string;
}

export class SentimentResult {
  private constructor(private readonly props: SentimentResultProps) {}
  get text(): string { return this.props.text; }
  get sentiment(): import('../enums').SentimentType { return this.props.sentiment; }
  get score(): number { return this.props.score; }
  get confidence(): number { return this.props.confidence; }
  get keywords(): string[] { return this.props.keywords; }
  static create(props: SentimentResultProps): SentimentResult { return new SentimentResult(props); }
  toPlainObject(): SentimentResultProps { return { ...this.props }; }
}

export interface EmotionResultProps {
  text: string; primaryEmotion: import('../enums').Emotion;
  emotions: Record<import('../enums').Emotion, number>;
  confidence: number; keywords: Record<import('../enums').Emotion, string[]>;
}

export class EmotionResult {
  private constructor(private readonly props: EmotionResultProps) {}
  get text(): string { return this.props.text; }
  get primaryEmotion(): import('../enums').Emotion { return this.props.primaryEmotion; }
  get emotions() { return this.props.emotions; }
  get confidence(): number { return this.props.confidence; }
  static create(props: EmotionResultProps): EmotionResult { return new EmotionResult(props); }
  toPlainObject(): EmotionResultProps { return { ...this.props }; }
}

export interface AnalysisResultProps {
  id: string; text: string; sentiment: SentimentResultProps;
  emotions?: EmotionResultProps; timestamp: Date; createdAt: Date;
}

export class AnalysisResult {
  private constructor(private readonly props: AnalysisResultProps) {}
  get id(): string { return this.props.id; }
  get text(): string { return this.props.text; }
  get sentiment() { return this.props.sentiment; }
  get emotions() { return this.props.emotions; }
  get timestamp(): Date { return this.props.timestamp; }
  static create(props: AnalysisResultProps): AnalysisResult { return new AnalysisResult(props); }
  toPlainObject(): AnalysisResultProps { return { ...this.props }; }
}
