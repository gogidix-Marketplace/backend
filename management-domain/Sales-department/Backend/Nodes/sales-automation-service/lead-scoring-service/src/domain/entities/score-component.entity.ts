import { v4 as uuidv4 } from 'uuid';
import { ScoreType } from './enums/score-type.enum';

export interface ScoreSnapshot {
  score: number;
  grade: string;
  timestamp: Date;
  breakdown: {
    demographicScore: number;
    behavioralScore: number;
    engagementScore: number;
    firmographicScore: number;
    customScores: Record<string, number>;
  };
  changeReason: string;
  changedBy: string;
}

export class ScoreComponent {
  private _id: string;
  private _tenantId: string;
  private _leadScoreId: string;
  private _name: string;
  private _componentType: ScoreType;
  private _weight: number;
  private _maxScore: number;
  private _currentScore: number;
  private _snapshots: ScoreSnapshot[];
  private _isActive: boolean;
  private _configuration: Record<string, any>;
  private _trendData: Array<{
    date: Date;
    value: number;
  }>;
  private _metadata: Record<string, any>;
  private _createdAt: Date;
  private _updatedAt: Date;

  constructor(props: {
    id?: string;
    tenantId: string;
    leadScoreId: string;
    name: string;
    componentType: ScoreType;
    weight: number;
    maxScore: number;
    currentScore?: number;
    snapshots?: ScoreSnapshot[];
    isActive?: boolean;
    configuration?: Record<string, any>;
    trendData?: Array<{ date: Date; value: number }>;
    metadata?: Record<string, any>;
  }) {
    this._id = props.id || uuidv4();
    this._tenantId = props.tenantId;
    this._leadScoreId = props.leadScoreId;
    this._name = props.name;
    this._componentType = props.componentType;
    this._weight = props.weight;
    this._maxScore = props.maxScore;
    this._currentScore = props.currentScore ?? 0;
    this._snapshots = props.snapshots ?? [];
    this._isActive = props.isActive ?? true;
    this._configuration = props.configuration ?? {};
    this._trendData = props.trendData ?? [];
    this._metadata = props.metadata ?? {};
    this._createdAt = new Date();
    this._updatedAt = new Date();
  }

  // Getters
  get id(): string { return this._id; }
  get tenantId(): string { return this._tenantId; }
  get leadScoreId(): string { return this._leadScoreId; }
  get name(): string { return this._name; }
  get componentType(): ScoreType { return this._componentType; }
  get weight(): number { return this._weight; }
  get maxScore(): number { return this._maxScore; }
  get currentScore(): number { return this._currentScore; }
  get snapshots(): ScoreSnapshot[] { return this._snapshots; }
  get isActive(): boolean { return this._isActive; }
  get configuration(): Record<string, any> { return this._configuration; }
  get trendData(): Array<{ date: Date; value: number }> { return this._trendData; }
  get metadata(): Record<string, any> { return this._metadata; }
  get createdAt(): Date { return this._createdAt; }
  get updatedAt(): Date { return this._updatedAt; }

  // Domain Methods
  updateScore(
    newScore: number,
    reason: string,
    changedBy: string,
    breakdown?: any
  ): void {
    const previousScore = this._currentScore;
    this._currentScore = Math.max(0, Math.min(this._maxScore, newScore));
    this._updatedAt = new Date();

    // Create snapshot
    this.addSnapshot({
      score: this._currentScore,
      grade: this.calculateGrade(),
      timestamp: new Date(),
      breakdown: breakdown ?? this.getDefaultBreakdown(),
      changeReason: reason,
      changedBy,
    });

    // Update trend data
    this._trendData.push({
      date: new Date(),
      value: this._currentScore,
    });

    // Keep only last 90 days of trend data
    const ninetyDaysAgo = new Date();
    ninetyDaysAgo.setDate(ninetyDaysAgo.getDate() - 90);
    this._trendData = this._trendData.filter(t => t.date >= ninetyDaysAgo);
  }

  addSnapshot(snapshot: ScoreSnapshot): void {
    this._snapshots.push(snapshot);

    // Keep only last 100 snapshots
    if (this._snapshots.length > 100) {
      this._snapshots = this._snapshots.slice(-100);
    }
    this._updatedAt = new Date();
  }

  setWeight(weight: number): void {
    if (weight < 0 || weight > 1) {
      throw new Error('Weight must be between 0 and 1');
    }
    this._weight = weight;
    this._updatedAt = new Date();
  }

  setMaxScore(maxScore: number): void {
    if (maxScore < 0) {
      throw new Error('Max score cannot be negative');
    }
    this._maxScore = maxScore;
    this._currentScore = Math.min(this._currentScore, this._maxScore);
    this._updatedAt = new Date();
  }

  activate(): void {
    this._isActive = true;
    this._updatedAt = new Date();
  }

  deactivate(): void {
    this._isActive = false;
    this._updatedAt = new Date();
  }

  updateConfiguration(config: Record<string, any>): void {
    this._configuration = { ...this._configuration, ...config };
    this._updatedAt = new Date();
  }

  getTrend(days: number = 30): Array<{ date: Date; value: number }> {
    const cutoffDate = new Date();
    cutoffDate.setDate(cutoffDate.getDate() - days);

    return this._trendData
      .filter(t => t.date >= cutoffDate)
      .sort((a, b) => a.date.getTime() - b.date.getTime());
  }

  getAverageScore(days: number = 30): number {
    const trend = this.getTrend(days);
    if (trend.length === 0) return 0;

    const sum = trend.reduce((acc, t) => acc + t.value, 0);
    return sum / trend.length;
  }

  getScoreChange(days: number = 7): number {
    const trend = this.getTrend(days);
    if (trend.length < 2) return 0;

    const oldest = trend[0].value;
    const newest = trend[trend.length - 1].value;

    return newest - oldest;
  }

  isImproving(days: number = 7): boolean {
    return this.getScoreChange(days) > 0;
  }

  calculateGrade(): string {
    const percentage = (this._currentScore / this._maxScore) * 100;
    if (percentage >= 80) return 'A';
    if (percentage >= 60) return 'B';
    if (percentage >= 40) return 'C';
    return 'D';
  }

  private getDefaultBreakdown() {
    return {
      demographicScore: 0,
      behavioralScore: 0,
      engagementScore: 0,
      firmographicScore: 0,
      customScores: {},
    };
  }

  toPrimitives(): Record<string, any> {
    return {
      id: this._id,
      tenantId: this._tenantId,
      leadScoreId: this._leadScoreId,
      name: this._name,
      componentType: this._componentType,
      weight: this._weight,
      maxScore: this._maxScore,
      currentScore: this._currentScore,
      snapshots: this._snapshots,
      isActive: this._isActive,
      configuration: this._configuration,
      trendData: this._trendData,
      metadata: this._metadata,
      createdAt: this._createdAt,
      updatedAt: this._updatedAt,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreComponent {
    const component = new ScoreComponent({
      id: data.id,
      tenantId: data.tenantId,
      leadScoreId: data.leadScoreId,
      name: data.name,
      componentType: data.componentType,
      weight: data.weight,
      maxScore: data.maxScore,
      currentScore: data.currentScore,
      snapshots: data.snapshots?.map((s: any) => ({
        ...s,
        timestamp: new Date(s.timestamp),
      })) ?? [],
      isActive: data.isActive,
      configuration: data.configuration,
      trendData: data.trendData?.map((t: any) => ({
        ...t,
        date: new Date(t.date),
      })) ?? [],
      metadata: data.metadata,
    });

    component._createdAt = new Date(data.createdAt);
    component._updatedAt = new Date(data.updatedAt);

    return component;
  }
}
