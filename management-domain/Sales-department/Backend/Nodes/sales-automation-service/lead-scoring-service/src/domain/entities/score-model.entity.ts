import { AggregateRoot } from './aggregate-root.base';
import { ScoreModelUpdatedEvent } from '../events/score-model-updated.event';
import { ScoreModelActivatedEvent } from '../events/score-model-activated.event';
import { ScoreModelStatus, ScoreModelType } from './enums/score-type.enum';
import { GradeConfig, DEFAULT_GRADE_CONFIGS } from './enums/lead-grade.enum';
import { v4 as uuidv4 } from 'uuid';

export interface ModelVariant {
  id: string;
  name: string;
  description: string;
  percentage: number;
  isActive: boolean;
  createdAt: Date;
}

export interface ScoringConfiguration {
  enableScoreDecay: boolean;
  decayRate: number;
  decayPeriodDays: number;
  minimumScoreThreshold: number;
  autoReScoreEnabled: boolean;
  reScoreIntervalDays: number;
  gradeConfigs: GradeConfig[];
}

export class ScoreModel extends AggregateRoot {
  private _id: string;
  private _tenantId: string;
  private _name: string;
  private _description: string;
  private _modelType: ScoreModelType;
  private _status: ScoreModelStatus;
  private _version: number;
  private _scoringConfig: ScoringConfiguration;
  private _ruleIds: string[];
  private _attributeIds: string[];
  private _variants: ModelVariant[];
  private _isDefault: boolean;
  private _isABTestEnabled: boolean;
  private _testStartDate: Date | null;
  private _testEndDate: Date | null;
  private _metadata: Record<string, any>;
  private _createdBy: string;
  private _updatedBy: string;
  private _createdAt: Date;
  private _updatedAt: Date;

  constructor(props: {
    id?: string;
    tenantId: string;
    name: string;
    description: string;
    modelType: ScoreModelType;
    status?: ScoreModelStatus;
    version?: number;
    scoringConfig?: ScoringConfiguration;
    ruleIds?: string[];
    attributeIds?: string[];
    variants?: ModelVariant[];
    isDefault?: boolean;
    isABTestEnabled?: boolean;
    testStartDate?: Date | null;
    testEndDate?: Date | null;
    metadata?: Record<string, any>;
    createdBy: string;
    updatedBy?: string;
  }) {
    super();
    this._id = props.id || uuidv4();
    this._tenantId = props.tenantId;
    this._name = props.name;
    this._description = props.description;
    this._modelType = props.modelType;
    this._status = props.status ?? ScoreModelStatus.DRAFT;
    this._version = props.version ?? 1;
    this._scoringConfig = props.scoringConfig ?? this.getDefaultScoringConfig();
    this._ruleIds = props.ruleIds ?? [];
    this._attributeIds = props.attributeIds ?? [];
    this._variants = props.variants ?? [];
    this._isDefault = props.isDefault ?? false;
    this._isABTestEnabled = props.isABTestEnabled ?? false;
    this._testStartDate = props.testStartDate ?? null;
    this._testEndDate = props.testEndDate ?? null;
    this._metadata = props.metadata ?? {};
    this._createdBy = props.createdBy;
    this._updatedBy = props.updatedBy ?? props.createdBy;
    this._createdAt = new Date();
    this._updatedAt = new Date();
  }

  // Getters
  get id(): string { return this._id; }
  get tenantId(): string { return this._tenantId; }
  get name(): string { return this._name; }
  get description(): string { return this._description; }
  get modelType(): ScoreModelType { return this._modelType; }
  get status(): ScoreModelStatus { return this._status; }
  get version(): number { return this._version; }
  get scoringConfig(): ScoringConfiguration { return this._scoringConfig; }
  get ruleIds(): string[] { return this._ruleIds; }
  get attributeIds(): string[] { return this._attributeIds; }
  get variants(): ModelVariant[] { return this._variants; }
  get isDefault(): boolean { return this._isDefault; }
  get isABTestEnabled(): boolean { return this._isABTestEnabled; }
  get testStartDate(): Date | null { return this._testStartDate; }
  get testEndDate(): Date | null { return this._testEndDate; }
  get metadata(): Record<string, any> { return this._metadata; }
  get createdBy(): string { return this._createdBy; }
  get updatedBy(): string { return this._updatedBy; }
  get createdAt(): Date { return this._createdAt; }
  get updatedAt(): Date { return this._updatedAt; }

  // Domain Methods
  activate(userId: string): void {
    if (this._status === ScoreModelStatus.ACTIVE) {
      throw new Error('Model is already active');
    }

    this._status = ScoreModelStatus.ACTIVE;
    this._updatedBy = userId;
    this._updatedAt = new Date();

    this.addDomainEvent(
      new ScoreModelActivatedEvent(
        this._id,
        this._tenantId,
        this._name,
        this._version,
        userId
      )
    );
  }

  deactivate(userId: string): void {
    if (this._status !== ScoreModelStatus.ACTIVE) {
      throw new Error('Only active models can be deactivated');
    }

    this._status = ScoreModelStatus.PAUSED;
    this._updatedBy = userId;
    this._updatedAt = new Date();
  }

  archive(userId: string): void {
    if (this._status === ScoreModelStatus.ACTIVE) {
      throw new Error('Active models cannot be archived');
    }

    this._status = ScoreModelStatus.ARCHIVED;
    this._updatedBy = userId;
    this._updatedAt = new Date();
  }

  update(
    props: {
      name?: string;
      description?: string;
      scoringConfig?: Partial<ScoringConfiguration>;
    },
    userId: string
  ): void {
    if (this._status === ScoreModelStatus.ACTIVE) {
      throw new Error('Cannot update active model. Create a new version instead.');
    }

    if (props.name) this._name = props.name;
    if (props.description) this._description = props.description;
    if (props.scoringConfig) {
      this._scoringConfig = { ...this._scoringConfig, ...props.scoringConfig };
    }

    this._updatedBy = userId;
    this._updatedAt = new Date();

    this.addDomainEvent(
      new ScoreModelUpdatedEvent(
        this._id,
        this._tenantId,
        this._name,
        this._version,
        userId,
        props
      )
    );
  }

  addRule(ruleId: string): void {
    if (!this._ruleIds.includes(ruleId)) {
      this._ruleIds.push(ruleId);
      this._updatedAt = new Date();
    }
  }

  removeRule(ruleId: string): void {
    this._ruleIds = this._ruleIds.filter(id => id !== ruleId);
    this._updatedAt = new Date();
  }

  addAttribute(attributeId: string): void {
    if (!this._attributeIds.includes(attributeId)) {
      this._attributeIds.push(attributeId);
      this._updatedAt = new Date();
    }
  }

  removeAttribute(attributeId: string): void {
    this._attributeIds = this._attributeIds.filter(id => id !== attributeId);
    this._updatedAt = new Date();
  }

  addVariant(variant: Omit<ModelVariant, 'id' | 'createdAt'>): void {
    const newVariant: ModelVariant = {
      id: uuidv4(),
      createdAt: new Date(),
      ...variant,
    };

    // Validate total percentage doesn't exceed 100
    const totalPercentage = this._variants.reduce((sum, v) => sum + v.percentage, 0) + variant.percentage;
    if (totalPercentage > 100) {
      throw new Error('Total variant percentage cannot exceed 100%');
    }

    this._variants.push(newVariant);
    this._updatedAt = new Date();
  }

  removeVariant(variantId: string): void {
    this._variants = this._variants.filter(v => v.id !== variantId);
    this._updatedAt = new Date();
  }

  enableABTesting(startDate: Date, endDate: Date): void {
    if (this._variants.length < 2) {
      throw new Error('At least 2 variants required for A/B testing');
    }

    if (endDate <= startDate) {
      throw new Error('End date must be after start date');
    }

    this._isABTestEnabled = true;
    this._testStartDate = startDate;
    this._testEndDate = endDate;
    this._updatedAt = new Date();
  }

  disableABTesting(): void {
    this._isABTestEnabled = false;
    this._testStartDate = null;
    this._testEndDate = null;
    this._updatedAt = new Date();
  }

  setAsDefault(): void {
    this._isDefault = true;
    this._updatedAt = new Date();
  }

  unsetAsDefault(): void {
    this._isDefault = false;
    this._updatedAt = new Date();
  }

  selectVariant(): ModelVariant | null {
    if (!this._isABTestEnabled || this._variants.length === 0) {
      return null;
    }

    // Select variant based on percentage allocation
    const random = Math.random() * 100;
    let cumulative = 0;

    for (const variant of this._variants) {
      if (!variant.isActive) continue;
      cumulative += variant.percentage;
      if (random <= cumulative) {
        return variant;
      }
    }

    return this._variants.find(v => v.isActive) ?? null;
  }

  isTestActive(): boolean {
    if (!this._isABTestEnabled || !this._testStartDate || !this._testEndDate) {
      return false;
    }

    const now = new Date();
    return now >= this._testStartDate && now <= this._testEndDate;
  }

  getGradeForScore(score: number): string {
    const gradeConfig = this._scoringConfig.gradeConfigs.find(
      config => score >= config.minScore && score <= config.maxScore
    );
    return gradeConfig?.grade ?? 'D';
  }

  getGradeConfig(grade: string): GradeConfig | undefined {
    return this._scoringConfig.gradeConfigs.find(config => config.grade === grade);
  }

  updateGradeConfig(grade: string, config: Partial<GradeConfig>): void {
    const index = this._scoringConfig.gradeConfigs.findIndex(c => c.grade === grade);
    if (index >= 0) {
      this._scoringConfig.gradeConfigs[index] = {
        ...this._scoringConfig.gradeConfigs[index],
        ...config,
      };
      this._updatedAt = new Date();
    }
  }

  createNewVersion(userId: string): ScoreModel {
    const newModel = new ScoreModel({
      tenantId: this._tenantId,
      name: this._name,
      description: this._description,
      modelType: this._modelType,
      version: this._version + 1,
      scoringConfig: { ...this._scoringConfig },
      ruleIds: [...this._ruleIds],
      attributeIds: [...this._attributeIds],
      variants: [],
      isDefault: false,
      isABTestEnabled: false,
      metadata: { ...this._metadata, parentModelId: this._id },
      createdBy: userId,
    });

    return newModel;
  }

  private getDefaultScoringConfig(): ScoringConfiguration {
    return {
      enableScoreDecay: true,
      decayRate: 0.1,
      decayPeriodDays: 30,
      minimumScoreThreshold: 50,
      autoReScoreEnabled: true,
      reScoreIntervalDays: 7,
      gradeConfigs: DEFAULT_GRADE_CONFIGS,
    };
  }

  toPrimitives(): Record<string, any> {
    return {
      id: this._id,
      tenantId: this._tenantId,
      name: this._name,
      description: this._description,
      modelType: this._modelType,
      status: this._status,
      version: this._version,
      scoringConfig: this._scoringConfig,
      ruleIds: this._ruleIds,
      attributeIds: this._attributeIds,
      variants: this._variants,
      isDefault: this._isDefault,
      isABTestEnabled: this._isABTestEnabled,
      testStartDate: this._testStartDate,
      testEndDate: this._testEndDate,
      metadata: this._metadata,
      createdBy: this._createdBy,
      updatedBy: this._updatedBy,
      createdAt: this._createdAt,
      updatedAt: this._updatedAt,
    };
  }

  static fromPrimitives(data: Record<string, any>): ScoreModel {
    const model = new ScoreModel({
      id: data.id,
      tenantId: data.tenantId,
      name: data.name,
      description: data.description,
      modelType: data.modelType,
      status: data.status,
      version: data.version,
      scoringConfig: data.scoringConfig,
      ruleIds: data.ruleIds,
      attributeIds: data.attributeIds,
      variants: data.variants?.map((v: any) => ({
        ...v,
        createdAt: new Date(v.createdAt),
      })) ?? [],
      isDefault: data.isDefault,
      isABTestEnabled: data.isABTestEnabled,
      testStartDate: data.testStartDate ? new Date(data.testStartDate) : null,
      testEndDate: data.testEndDate ? new Date(data.testEndDate) : null,
      metadata: data.metadata,
      createdBy: data.createdBy,
      updatedBy: data.updatedBy,
    });

    model._createdAt = new Date(data.createdAt);
    model._updatedAt = new Date(data.updatedAt);

    return model;
  }
}
