import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { ISessionAnalyticsRepository } from '@domain/ports/output';

@Injectable()
export class SessionAnalyticsRepository implements ISessionAnalyticsRepository {
  constructor(
    @InjectModel('SessionAnalytics') private readonly model: any,
  ) {}

  async findOne(filter: any): Promise<any> {
    return this.model.findOne(filter).lean();
  }

  async find(filter: any): Promise<any[]> {
    return this.model.find(filter).lean();
  }

  async findByDateRange(startDate: Date, endDate: Date): Promise<any[]> {
    return this.model.find({ createdAt: { $gte: startDate, $lte: endDate } }).lean();
  }

  async findByResolutionStatus(status: 'resolved' | 'escalated' | 'abandoned'): Promise<any[]> {
    return this.model.find({ resolutionStatus: status }).lean();
  }

  async getAverageSatisfaction(): Promise<{ averageSatisfaction: number; totalRatings: number }> {
    return this.model.getAverageSatisfaction();
  }

  async getResolutionStats(): Promise<any[]> {
    return this.model.getResolutionStats();
  }

  async countDocuments(filter: any): Promise<number> {
    return this.model.countDocuments(filter);
  }

  async save(data: any): Promise<any> {
    const existing = await this.model.findOne({ sessionId: data.sessionId });
    if (existing) {
      await this.model.updateOne({ sessionId: data.sessionId }, { $set: data });
      return this.model.findOne({ sessionId: data.sessionId }).lean();
    }
    const created = await this.model.create(data);
    return created.toObject();
  }
}
