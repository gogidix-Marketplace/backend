import { KpiCategory } from '../enums/kpi-category.enum';
import { KpiStatus } from '../enums/kpi-status.enum';
import { ExecutiveLevel } from '../enums/executive-level.enum';
import { KpiTrend } from '../enums/kpi-trend.enum';

export interface KpiProps {
  id?: string;
  tenantId: string;
  name: string;
  category: KpiCategory;
  executiveLevel: ExecutiveLevel;
  value: number;
  unit: string;
  period: string;
  target?: number;
  previousValue?: number;
  percentChange?: number;
  status: KpiStatus;
  trend?: KpiTrend;
  dataSources: string[];
  metadata: Record<string, unknown>;
  visible: boolean;
  isCalculated: boolean;
  lastCalculatedAt: Date;
  createdAt: Date;
  updatedAt: Date;
}

export class Kpi {
  readonly props: KpiProps;

  constructor(props: KpiProps) {
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      executiveLevel: props.executiveLevel || ExecutiveLevel.ALL,
      status: props.status || KpiStatus.ON_TRACK,
      dataSources: props.dataSources || [],
      metadata: props.metadata || {},
      visible: props.visible !== false,
      isCalculated: props.isCalculated !== false,
      lastCalculatedAt: props.lastCalculatedAt || new Date(),
      createdAt: props.createdAt || new Date(),
      updatedAt: props.updatedAt || new Date(),
    };
  }

  get id(): string {
    return this.props.id!;
  }

  get tenantId(): string {
    return this.props.tenantId;
  }

  get name(): string {
    return this.props.name;
  }

  get status(): KpiStatus {
    return this.props.status;
  }

  calculatePercentChange(): number | null {
    if (this.props.previousValue && this.props.previousValue !== 0) {
      const change = this.props.value - this.props.previousValue;
      this.props.percentChange = (change / this.props.previousValue) * 100;
    } else {
      this.props.percentChange = null;
    }
    return this.props.percentChange;
  }

  updateStatus(): KpiStatus {
    if (this.props.target && this.props.target !== 0) {
      const ratio = this.props.value / this.props.target;

      if (ratio >= 1.1) {
        this.props.status = KpiStatus.AHEAD;
        this.props.trend = KpiTrend.UP;
      } else if (ratio >= 0.9) {
        this.props.status = KpiStatus.ON_TRACK;
        this.props.trend = KpiTrend.STABLE;
      } else if (ratio >= 0.8) {
        this.props.status = KpiStatus.AT_RISK;
        this.props.trend = KpiTrend.DOWN;
      } else {
        this.props.status = KpiStatus.BEHIND;
        this.props.trend = KpiTrend.DOWN;
      }
    }
    return this.props.status;
  }

  isOnTrack(): boolean {
    return this.props.status === KpiStatus.ON_TRACK || this.props.status === KpiStatus.AHEAD;
  }

  needsAttention(): boolean {
    return this.props.status === KpiStatus.AT_RISK || this.props.status === KpiStatus.BEHIND;
  }

  addMetadata(key: string, value: unknown): this {
    this.props.metadata[key] = value;
    return this;
  }

  toJSON() {
    return {
      id: this.props.id,
      tenantId: this.props.tenantId,
      name: this.props.name,
      category: this.props.category,
      executiveLevel: this.props.executiveLevel,
      value: this.props.value,
      unit: this.props.unit,
      period: this.props.period,
      target: this.props.target,
      previousValue: this.props.previousValue,
      percentChange: this.props.percentChange,
      status: this.props.status,
      trend: this.props.trend,
      dataSources: this.props.dataSources,
      metadata: this.props.metadata,
      visible: this.props.visible,
      isCalculated: this.props.isCalculated,
      lastCalculatedAt: this.props.lastCalculatedAt,
      createdAt: this.props.createdAt,
      updatedAt: this.props.updatedAt,
    };
  }
}
