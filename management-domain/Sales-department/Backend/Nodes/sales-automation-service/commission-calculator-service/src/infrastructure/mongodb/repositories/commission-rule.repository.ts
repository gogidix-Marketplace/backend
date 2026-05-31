import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CommissionRule } from '../../../domain/models/commission-rule.entity';
import { CommissionRuleRepositoryPort } from '../../../domain/ports/out/commission-rule.repository.port';
import { CommissionRuleDocument } from '../schemas/commission-rule.schema';

/**
 * MongoDB implementation of CommissionRule repository
 */
@Injectable()
export class CommissionRuleRepository implements CommissionRuleRepositoryPort {
  constructor(
    @InjectModel(CommissionRuleDocument.name)
    private readonly commissionRuleModel: Model<CommissionRuleDocument>,
  ) {}

  async save(rule: CommissionRule): Promise<CommissionRule> {
    const document = this.toDocument(rule);
    const savedDocument = await this.commissionRuleModel.findOneAndUpdate(
      { _id: document._id },
      document,
      { upsert: true, new: true },
    );

    return this.toEntity(savedDocument);
  }

  async findById(ruleId: string): Promise<CommissionRule | null> {
    const document = await this.commissionRuleModel.findById(ruleId);
    return document ? this.toEntity(document) : null;
  }

  async findByTenantId(tenantId: string, organizationId?: string): Promise<CommissionRule[]> {
    const query: any = { tenantId };
    if (organizationId) {
      query.organizationId = organizationId;
    }

    const documents = await this.commissionRuleModel.find(query);
    return documents.map(doc => this.toEntity(doc));
  }

  async findActive(tenantId: string, organizationId?: string): Promise<CommissionRule[]> {
    const now = new Date();
    const query: any = {
      tenantId,
      isActive: true,
      effectiveDate: { $lte: now },
      $or: [
        { expirationDate: { $exists: false } },
        { expirationDate: { $gt: now } },
      ],
    };

    if (organizationId) {
      query.organizationId = organizationId;
    }

    const documents = await this.commissionRuleModel.find(query);
    return documents.map(doc => this.toEntity(doc));
  }

  async findByName(name: string, tenantId: string): Promise<CommissionRule[]> {
    const documents = await this.commissionRuleModel.find({ name, tenantId });
    return documents.map(doc => this.toEntity(doc));
  }

  async delete(ruleId: string): Promise<void> {
    await this.commissionRuleModel.findByIdAndDelete(ruleId);
  }

  async exists(ruleId: string): Promise<boolean> {
    const count = await this.commissionRuleModel.countDocuments({ _id: ruleId });
    return count > 0;
  }

  private toDocument(entity: CommissionRule): any {
    const obj = entity.toObject();
    return {
      _id: obj._id,
      ...obj,
      scopeFilters: obj.scopeFilters || {},
      productRates: obj.productRates || [],
      customerRates: obj.customerRates || [],
    };
  }

  private toEntity(document: any): CommissionRule {
    return CommissionRule.fromObject({
      id: document._id?.toString(),
      name: document.name,
      description: document.description,
      calculationType: document.calculationType,
      baseRate: document.baseRate,
      applicationScope: document.applicationScope,
      scopeFilters: document.scopeFilters || {},
      acceleratorType: document.acceleratorType,
      acceleratorThreshold: document.acceleratorThreshold,
      acceleratorMultiplier: document.acceleratorMultiplier,
      capType: document.capType,
      capValue: document.capValue,
      isActive: document.isActive,
      effectiveDate: document.effectiveDate,
      expirationDate: document.expirationDate,
      tenantId: document.tenantId,
      organizationId: document.organizationId,
      tiers: document.tiers || [],
      productRates: document.productRates || [],
      customerRates: document.customerRates || [],
      createdAt: document.createdAt,
      updatedAt: document.updatedAt,
      version: document.version,
    });
  }
}
