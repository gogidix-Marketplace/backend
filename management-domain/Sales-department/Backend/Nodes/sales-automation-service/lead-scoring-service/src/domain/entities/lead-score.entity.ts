import { AggregateRoot } from './aggregate-root.base';
import { LeadScoredEvent } from '../events/lead-scored.event';
import { ScoreDecayedEvent } from '../events/score-decayed.event';
import { LeadGrade } from './enums/lead-grade.enum';
import { ScoreType } from './enums/score-type.enum';
import { v4 as uuidv4 } from 'uuid';

export interface ScoreBreakdown {
  demographicScore: number;
  behavioralScore: number;
  engagementScore: number;
  firmographicScore: number;
  customScores: Record<string, number>;
}

export interface ScoreAttributeEntry {
  name: string;
  value: any;
  weight: number;
  contribution: number;
}

export class LeadScore extends AggregateRoot {
  private _id: string;
  private _leadId: string;
  private _tenantId: string;
  private _scoreModelId: string;
  private _totalScore: number;
  private _grade: LeadGrade;
  private _scoreType: ScoreType;
  private _breakdown: ScoreBreakdown;
  private _attributes: ScoreAttributeEntry[];
  private _lastScoredAt: Date;
  private _scoreDecayRate: number;
  private _decayApplied: number;
  private _lastDecayAppliedAt: Date | null;
  private _variantId: string | null;
  private _isControlGroup: boolean;
  private _metadata: Record<string, any>;
  private _createdAt: Date;
  private _updatedAt: Date;

  constructor(props: {
    id?: string;
    leadId: string;
    tenantId: string;
    scoreModelId: string;
    totalScore: number;
    grade: LeadGrade;
    scoreType: ScoreType;
    breakdown: ScoreBreakdown;
    attributes: ScoreAttributeEntry[];
    scoreDecayRate?: number;
    variantId?: string | null;
    isControlGroup?: boolean;
    metadata?: Record<string, any>;
  }) {
    super();
    this._id = props.id || uuidv4();
    this._leadId = props.leadId;
    this._tenantId = props.tenantId;
    this._scoreModelId = props.scoreModelId;
    this._totalScore = props.totalScore;
    this._grade = props.grade;
    this._scoreType = props.scoreType;
    this._breakdown = props.breakdown;
    this._attributes = props.attributes;
    this._lastScoredAt = new Date();
    this._scoreDecayRate = props.scoreDecayRate ?? 0.1;
    this._decayApplied = 0;
    this._lastDecayAppliedAt = null;
    this._variantId = props.variantId ?? null;
    this._isControlGroup = props.isControlGroup ?? false;
    this._metadata = props.metadata ?? {};
    this._createdAt = new Date();
    this._updatedAt = new Date();
  }

  // Getters
  get id(): string { return this._id; }
  get leadId(): string { return this._leadId; }
  get tenantId(): string { return this._tenantId; }
  get scoreModelId(): string { return this._scoreModelId; }
  get totalScore(): number { return this._totalScore; }
  get grade(): LeadGrade { return this._grade; }
  get scoreType(): ScoreType { return this._scoreType; }
  get breakdown(): ScoreBreakdown { return this._breakdown; }
  get attributes(): ScoreAttributeEntry[] { return this._attributes; }
  get lastScoredAt(): Date { return this._lastScoredAt; }
  get scoreDecayRate(): number { return this._scoreDecayRate; }
  get decayApplied(): number { return this._decayApplied; }
  get lastDecayAppliedAt(): Date | null { return this._lastDecayAppliedAt; }
  get variantId(): string | null { return this._variantId; }
  get isControlGroup(): boolean { return this._isControlGroup; }
  get metadata(): Record<string, any> { return this._metadata; }
  get createdAt(): Date { return this._createdAt; }
  get updatedAt(): Date { return this._updatedAt; }

  // Domain Methods
  updateScore(
    newScore: number,
    newBreakdown: ScoreBreakdown,
    newAttributes: ScoreAttributeEntry[]
  ): void {
    const previousScore = this._totalScore;
    this._totalScore = Math.max(0, Math.min(100, newScore));
    this._breakdown = newBreakdown;
    this._attributes = newAttributes;
    this._grade = this.calculateGrade(this._totalScore);
    this._lastScoredAt = new Date();
    this._updatedAt = new Date();

    this.addDomainEvent(
      new LeadScoredEvent(
        this._id,
        this._leadId,
        this._tenantId,
        previousScore,
        this._totalScore,
        this._grade,
        this._scoreModelId,
        this._variantId,
        this._isControlGroup
      )
    );
  }

  applyDecay(daysSinceLastActivity: number): void {
    if (daysSinceLastActivity <= 0) return;

    const decayFactor = Math.pow(1 - this._scoreDecayRate, daysSinceLastActivity / 30);
    const previousScore = this._totalScore;
    const decayedScore = this._totalScore * decayFactor;

    this._totalScore = Math.max(0, decayedScore);
    this._decayApplied += previousScore - this._totalScore;
    this._lastDecayAppliedAt = new Date();
    this._grade = this.calculateGrade(this._totalScore);
    this._updatedAt = new Date();

    this.addDomainEvent(
      new ScoreDecayedEvent(
        this._id,
        this._leadId,
        this._tenantId,
        previousScore,
        this._totalScore,
        this._grade,
        daysSinceLastActivity,
        this._decayApplied
      )
    );
  }

  resetDecay(): void {
    this._decayApplied = 0;
    this._lastDecayAppliedAt = null;
    this._updatedAt = new Date();
  }

  updateGrade(newGrade: LeadGrade): void {
    this._grade = newGrade;
    this._updatedAt = new Date();
  }

  setVariant(variantId: string, isControlGroup: boolean): void {
    this._variantId = variantId;
    this._isControlGroup = isControlGroup;
    this._updatedAt = new Date();
  }

  addMetadata(key: string, value: any): void {
    this._metadata[key] = value;
    this._updatedAt = new Date();
  }

  removeMetadata(key: string): void {
    delete this._metadata[key];
    this._updatedAt = new Date();
  }

  private calculateGrade(score: number): LeadGrade {
    if (score >= 80) return LeadGrade.A;
    if (score >= 60) return LeadGrade.B;
    if (score >= 40) return LeadGrade.C;
    return LeadGrade.D;
  }

  isQualified(qualifyingScore: number = 50): boolean {
    return this._totalScore >= qualifyingScore;
  }

  isHotLead(): boolean {
    return this._grade === LeadGrade.A;
  }

  needsRescoring(thresholdDays: number = 7): boolean {
    const daysSinceScoring = this.daysSinceLastScored();
    return daysSinceScoring >= thresholdDays;
  }

  daysSinceLastScored(): number {
    const now = new Date();
    const diffInMs = now.getTime() - this._lastScoredAt.getTime();
    return Math.floor(diffInMs / (1000 * 60 * 60 * 24));
  }

  toPrimitives(): Record<string, any> {
    return {
      id: this._id,
      leadId: this._leadId,
      tenantId: this._tenantId,
      scoreModelId: this._scoreModelId,
      totalScore: this._totalScore,
      grade: this._grade,
      scoreType: this._scoreType,
      breakdown: this._breakdown,
      attributes: this._attributes,
      lastScoredAt: this._lastScoredAt,
      scoreDecayRate: this._scoreDecayRate,
      decayApplied: this._decayApplied,
      lastDecayAppliedAt: this._lastDecayAppliedAt,
      variantId: this._variantId,
      isControlGroup: this._isControlGroup,
      metadata: this._metadata,
      createdAt: this._createdAt,
      updatedAt: this._updatedAt,
    };
  }

  static fromPrimitives(data: Record<string, any>): LeadScore {
    const entity = new LeadScore({
      id: data.id,
      leadId: data.leadId,
      tenantId: data.tenantId,
      scoreModelId: data.scoreModelId,
      totalScore: data.totalScore,
      grade: data.grade,
      scoreType: data.scoreType,
      breakdown: data.breakdown,
      attributes: data.attributes,
      scoreDecayRate: data.scoreDecayRate,
      variantId: data.variantId,
      isControlGroup: data.isControlGroup,
      metadata: data.metadata,
    });

    entity._lastScoredAt = new Date(data.lastScoredAt);
    entity._decayApplied = data.decayApplied;
    entity._lastDecayAppliedAt = data.lastDecayAppliedAt ? new Date(data.lastDecayAppliedAt) : null;
    entity._createdAt = new Date(data.createdAt);
    entity._updatedAt = new Date(data.updatedAt);

    return entity;
  }
}
