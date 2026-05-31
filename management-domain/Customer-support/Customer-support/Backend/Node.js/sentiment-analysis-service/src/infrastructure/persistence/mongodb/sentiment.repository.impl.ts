import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Schema } from 'mongoose';
import { SentimentAnalysisRepository, SentimentAlertRepository } from '@domain/ports/output';
import { SentimentAnalysis, SentimentAlert } from '@domain/models';

const SentimentAnalysisSchema = new Schema({
  ticketId: { type: String, index: true },
  text: String,
  sentiment: { score: Number, normalized: Number, label: String, confidence: Number },
  emotions: { joy: Number, sadness: Number, anger: Number, fear: Number, disgust: Number, surprise: Number },
  keywords: [{ word: String, sentiment: String, score: Number }],
  language: { type: String, default: 'en' },
  timestamp: Date,
}, { timestamps: true, collection: 'sentiment_analyses' });

const SentimentAlertSchema = new Schema({
  alertId: { type: String, required: true, unique: true, index: true },
  ticketId: { type: String, required: true, index: true },
  customerId: String,
  sentimentScore: Number,
  sentimentLabel: String,
  severity: String,
  triggeredBy: String,
  message: String,
  acknowledged: { type: Boolean, default: false },
  acknowledgedBy: String,
  acknowledgedAt: Date,
  resolvedAt: Date,
}, { timestamps: true, collection: 'sentiment_alerts' });

@Injectable()
export class MongoSentimentAnalysisRepository implements SentimentAnalysisRepository {
  constructor(@InjectModel('SentimentAnalysis') private readonly model: Model<any>) {}

  async save(analysis: SentimentAnalysis): Promise<SentimentAnalysis> {
    const doc = new this.model(analysis.toPlainObject());
    await doc.save();
    return analysis;
  }

  async findByTicketId(ticketId: string): Promise<SentimentAnalysis[]> {
    const docs = await this.model.find({ ticketId }).sort({ timestamp: -1 });
    return docs.map(d => SentimentAnalysis.create(d.toObject()));
  }

  async findRecent(limit: number): Promise<SentimentAnalysis[]> {
    const docs = await this.model.find().sort({ timestamp: -1 }).limit(limit);
    return docs.map(d => SentimentAnalysis.create(d.toObject()));
  }
}

@Injectable()
export class MongoSentimentAlertRepository implements SentimentAlertRepository {
  constructor(@InjectModel('SentimentAlert') private readonly model: Model<any>) {}

  async save(alert: SentimentAlert): Promise<SentimentAlert> {
    const doc = new this.model(alert.toPlainObject());
    await doc.save();
    return alert;
  }

  async findActive(): Promise<SentimentAlert[]> {
    const docs = await this.model.find({ resolvedAt: { $exists: false } }).sort({ createdAt: -1 });
    return docs.map(d => SentimentAlert.create(d.toObject()));
  }

  async findById(alertId: string): Promise<SentimentAlert | null> {
    const doc = await this.model.findOne({ alertId });
    return doc ? SentimentAlert.create(doc.toObject()) : null;
  }

  async update(alert: SentimentAlert): Promise<SentimentAlert> {
    await this.model.updateOne({ alertId: alert.alertId }, { $set: alert.toPlainObject() });
    return alert;
  }
}

export { SentimentAnalysisSchema, SentimentAlertSchema };
