/**
 * KPI Domain Model
 */

const { ObjectId } = require('mongodb');

/**
 * KPI (Key Performance Indicator) Model
 * Represents a high-level metric tracked by executives
 */
class KPI {
  constructor(data) {
    this._id = data._id || new ObjectId();
    this.tenantId = data.tenantId;
    this.name = data.name;
    this.category = data.category; // FINANCIAL, OPERATIONAL, CUSTOMER, EMPLOYEE
    this.executiveLevel = data.executiveLevel || 'ALL'; // CEO, CFO, CTO, COO, ALL
    this.value = data.value;
    this.unit = data.unit; // $, %, count, score
    this.period = data.period; // 2024-01, Q1-2024, YTD-2024
    this.target = data.target;
    this.previousValue = data.previousValue;
    this.percentChange = data.percentChange;
    this.status = data.status || 'ON_TRACK'; // ON_TRACK, AT_RISK, BEHIND, AHEAD
    this.trend = data.trend; // UP, DOWN, STABLE
    this.dataSources = data.dataSources || [];
    this.metadata = data.metadata || {};
    this.visible = data.visible !== false;
    this.isCalculated = data.isCalculated !== false;
    this.lastCalculatedAt = data.lastCalculatedAt || new Date();
    this.createdAt = data.createdAt || new Date();
    this.updatedAt = data.updatedAt || new Date();
  }

  /**
   * Convert to MongoDB document
   */
  toDocument() {
    return {
      _id: this._id,
      tenantId: this.tenantId,
      name: this.name,
      category: this.category,
      executiveLevel: this.executiveLevel,
      value: this.value,
      unit: this.unit,
      period: this.period,
      target: this.target,
      previousValue: this.previousValue,
      percentChange: this.percentChange,
      status: this.status,
      trend: this.trend,
      dataSources: this.dataSources,
      metadata: this.metadata,
      visible: this.visible,
      isCalculated: this.isCalculated,
      lastCalculatedAt: this.lastCalculatedAt,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt,
    };
  }

  /**
   * Create KPI from MongoDB document
   */
  static fromDocument(doc) {
    if (!doc) return null;
    return new KPI({
      _id: doc._id,
      tenantId: doc.tenantId,
      name: doc.name,
      category: doc.category,
      executiveLevel: doc.executiveLevel,
      value: doc.value,
      unit: doc.unit,
      period: doc.period,
      target: doc.target,
      previousValue: doc.previousValue,
      percentChange: doc.percentChange,
      status: doc.status,
      trend: doc.trend,
      dataSources: doc.dataSources || [],
      metadata: doc.metadata || {},
      visible: doc.visible,
      isCalculated: doc.isCalculated,
      lastCalculatedAt: doc.lastCalculatedAt,
      createdAt: doc.createdAt,
      updatedAt: doc.updatedAt,
    });
  }

  /**
   * Calculate percent change from previous value
   */
  calculatePercentChange() {
    if (this.previousValue && this.previousValue !== 0) {
      const change = this.value - this.previousValue;
      this.percentChange = (change / this.previousValue) * 100;
    } else {
      this.percentChange = null;
    }
    return this.percentChange;
  }

  /**
   * Update status based on value vs target
   */
  updateStatus() {
    if (this.target && this.target !== 0) {
      const ratio = this.value / this.target;

      if (ratio >= 1.1) {
        this.status = 'AHEAD';
        this.trend = 'UP';
      } else if (ratio >= 0.9) {
        this.status = 'ON_TRACK';
        this.trend = 'STABLE';
      } else if (ratio >= 0.8) {
        this.status = 'AT_RISK';
        this.trend = 'DOWN';
      } else {
        this.status = 'BEHIND';
        this.trend = 'DOWN';
      }
    }
    return this.status;
  }

  /**
   * Check if KPI is on track
   */
  isOnTrack() {
    return this.status === 'ON_TRACK' || this.status === 'AHEAD';
  }

  /**
   * Check if KPI needs attention
   */
  needsAttention() {
    return this.status === 'AT_RISK' || this.status === 'BEHIND';
  }

  /**
   * Add metadata
   */
  addMetadata(key, value) {
    this.metadata[key] = value;
    return this;
  }

  /**
   * Get collection name
   */
  static getCollectionName() {
    return 'kpi_metrics';
  }

  /**
   * Get index specifications
   */
  static getIndexes() {
    return [
      { key: { tenantId: 1, category: 1, period: -1 } },
      { key: { tenantId: 1, executiveLevel: 1, category: 1, period: -1 } },
      { key: { tenantId: 1, period: -1 } },
      { key: { tenantId: 1, name: 1 } },
    ];
  }
}

module.exports = KPI;
