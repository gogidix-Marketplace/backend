import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { IDailyAnalyticsRepository } from '@domain/ports/output';

@Injectable()
export class DailyAnalyticsRepository implements IDailyAnalyticsRepository {
  constructor(
    @InjectModel('DailyAnalytics') private readonly model: any,
  ) {}

  async findByDateRange(startDate: Date, endDate: Date): Promise<any[]> {
    return this.model.findByDateRange(startDate, endDate);
  }

  async getTodayAnalytics(): Promise<any> {
    return this.model.getTodayAnalytics();
  }

  async save(data: any): Promise<any> {
    const today = new Date(data.date);
    today.setHours(0, 0, 0, 0);
    const existing = await this.model.findOne({ date: today });
    if (existing) {
      await this.model.updateOne({ date: today }, { $set: data });
      return this.model.findOne({ date: today }).lean();
    }
    const created = await this.model.create(data);
    return created.toObject();
  }
}
