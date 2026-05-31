export interface ScoreHistoryEntry {
  score: number;
  reason: string;
  timestamp: Date;
}

export interface Flag {
  type: 'warning' | 'info' | 'critical';
  reason: string;
  createdAt: Date;
}

export interface LeadScoreProps {
  id?: string;
  tenantId: string;
  leadId: string;
  score: number;
  previousScore: number;
  maxScore: number;
  minScore: number;
  categoryScores: Record<string, number>;
  history: ScoreHistoryEntry[];
  flags: Flag[];
  lastEventAt?: Date;
  lastCalculatedAt?: Date;
  expiresAt?: Date;
  metadata?: Record<string, any>;
  createdAt?: Date;
  updatedAt?: Date;
}

export class LeadScore {
  private readonly props: LeadScoreProps;

  constructor(props: LeadScoreProps) {
    this.props = {
      ...props,
      score: props.score ?? 0,
      previousScore: props.previousScore ?? 0,
      maxScore: props.maxScore ?? 100,
      minScore: props.minScore ?? 0,
      categoryScores: props.categoryScores ?? {},
      history: props.history ?? [],
      flags: props.flags ?? [],
      lastCalculatedAt: props.lastCalculatedAt ?? new Date(),
      createdAt: props.createdAt ?? new Date(),
      updatedAt: new Date(),
    };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get leadId(): string { return this.props.leadId; }
  get score(): number { return this.props.score; }
  get previousScore(): number { return this.props.previousScore; }
  get maxScore(): number { return this.props.maxScore; }
  get minScore(): number { return this.props.minScore; }
  get categoryScores(): Record<string, number> { return this.props.categoryScores; }
  get history(): ScoreHistoryEntry[] { return this.props.history; }
  get flags(): Flag[] { return this.props.flags; }
  get lastEventAt(): Date | undefined { return this.props.lastEventAt; }
  get lastCalculatedAt(): Date | undefined { return this.props.lastCalculatedAt; }
  get metadata(): Record<string, any> | undefined { return this.props.metadata; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  updateScore(newScore: number, reason: string): void {
    this.props.previousScore = this.props.score;
    this.props.score = Math.max(this.props.minScore, Math.min(this.props.maxScore, newScore));
    this.props.history.push({ score: this.props.score, reason, timestamp: new Date() });
    this.props.lastCalculatedAt = new Date();
    this.props.updatedAt = new Date();
  }

  updateCategoryScore(category: string, score: number): void {
    this.props.categoryScores[category] = Math.max(this.props.minScore, Math.min(this.props.maxScore, score));
    this.recalculateTotal();
    this.props.updatedAt = new Date();
  }

  private recalculateTotal(): void {
    const scores = Object.values(this.props.categoryScores);
    if (scores.length === 0) return;
    const total = scores.reduce((sum, s) => sum + s, 0) / scores.length;
    this.props.previousScore = this.props.score;
    this.props.score = Math.round(total);
  }

  applyDecay(decayFactor: number): void {
    if (this.props.score > 0) {
      const decayed = this.props.score * decayFactor;
      this.updateScore(Math.round(decayed), `Score decay applied with factor ${decayFactor}`);
    }
  }

  addFlag(type: 'warning' | 'info' | 'critical', reason: string): void {
    this.props.flags.push({ type, reason, createdAt: new Date() });
    this.props.updatedAt = new Date();
  }

  isHot(): boolean { return this.props.score >= 80; }
  isWarm(): boolean { return this.props.score >= 50 && this.props.score < 80; }
  isCold(): boolean { return this.props.score < 50; }

  toPlainObject(): LeadScoreProps { return { ...this.props }; }
}
