import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ILogAlertRuleRepository } from '@domain/repositories/log-alert-rule-repository.interface';
import { LogAlertRule } from '@domain/models/log-alert-rule.entity';

@Injectable()
export class LogAlertRuleRepositoryImpl implements ILogAlertRuleRepository {
  private readonly logger = new Logger(LogAlertRuleRepositoryImpl.name);

  constructor(@InjectModel('LogAlertRule') private readonly model: Model<any>) {}

  async save(rule: LogAlertRule): Promise<LogAlertRule> {
    const doc = new this.model(rule);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async findById(id: string): Promise<LogAlertRule | null> {
    const doc = await this.model.findById(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findEnabled(): Promise<LogAlertRule[]> {
    const docs = await this.model.find({ enabled: true }).lean();
    return docs.map(d => this.toEntity(d));
  }

  async findByIdAndUpdate(id: string, update: any): Promise<LogAlertRule | null> {
    const doc = await this.model.findByIdAndUpdate(id, update, { new: true, runValidators: true }).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findByIdAndDelete(id: string): Promise<LogAlertRule | null> {
    const doc = await this.model.findByIdAndDelete(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  private toEntity(doc: any): LogAlertRule {
    return new LogAlertRule(
      doc.name, doc.description, doc.enabled, doc.conditions, doc.actions,
      doc.cooldown, doc.lastTriggeredAt, doc.triggerCount,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
