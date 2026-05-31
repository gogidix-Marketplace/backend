import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Schema } from 'mongoose';
import { AssignmentRepository } from '@domain/ports/output';
import { Assignment } from '@domain/models';

const AssignmentSchema = new Schema({
  assignmentId: { type: String, required: true, unique: true, index: true },
  ticketId: { type: String, required: true, index: true },
  agentId: { type: String, required: true, index: true },
  assignedAt: { type: Date, required: true, default: Date.now },
  assignedBy: { type: String, required: true, default: 'system' },
  routingStrategy: { type: String, required: true },
  confidence: { type: Number, required: true, min: 0, max: 1 },
  metadata: { type: Schema.Types.Mixed, default: {} },
}, { timestamps: true, collection: 'assignments' });

AssignmentSchema.index({ ticketId: 1, agentId: 1 });
AssignmentSchema.index({ assignedAt: -1 });

@Injectable()
export class MongoAssignmentRepository implements AssignmentRepository {
  private readonly logger = new Logger(MongoAssignmentRepository.name);

  constructor(
    @InjectModel('Assignment') private readonly model: Model<any>,
  ) {}

  async save(assignment: Assignment): Promise<Assignment> {
    const props = assignment.toPlainObject();
    const doc = new this.model(props);
    await doc.save();
    return assignment;
  }

  async findByTicket(ticketId: string): Promise<Assignment[]> {
    const docs = await this.model.find({ ticketId }).sort({ assignedAt: -1 });
    return docs.map(d => this.toDomain(d));
  }

  async findByAgent(agentId: string, startDate?: Date, endDate?: Date): Promise<Assignment[]> {
    const query: any = { agentId };
    if (startDate || endDate) {
      query.assignedAt = {};
      if (startDate) query.assignedAt.$gte = startDate;
      if (endDate) query.assignedAt.$lte = endDate;
    }
    const docs = await this.model.find(query).sort({ assignedAt: -1 });
    return docs.map(d => this.toDomain(d));
  }

  async getCurrentAssignments(agentId: string): Promise<Assignment[]> {
    const docs = await this.model.find({
      agentId,
      assignedAt: { $gte: new Date(Date.now() - 24 * 60 * 60 * 1000) },
    });
    return docs.map(d => this.toDomain(d));
  }

  async getAgentWorkload(agentId: string): Promise<number> {
    const result = await this.model.aggregate([
      { $match: { agentId, assignedAt: { $gte: new Date(Date.now() - 60 * 60 * 1000) } } },
      { $group: { _id: null, activeAssignments: { $sum: 1 } } },
    ]);
    return result[0]?.activeAssignments || 0;
  }

  async getAssignmentStats(agentId: string): Promise<Record<string, unknown>> {
    const stats = await this.model.aggregate([
      { $match: { agentId } },
      { $group: { _id: '$agentId', totalAssignments: { $sum: 1 }, avgConfidence: { $avg: '$confidence' } } },
    ]);
    return stats[0] || { agentId, totalAssignments: 0, avgConfidence: 0 };
  }

  private toDomain(doc: any): Assignment {
    return Assignment.create({
      id: doc._id.toString(),
      assignmentId: doc.assignmentId,
      ticketId: doc.ticketId,
      agentId: doc.agentId,
      assignedAt: doc.assignedAt,
      assignedBy: doc.assignedBy,
      routingStrategy: doc.routingStrategy,
      confidence: doc.confidence,
      metadata: doc.metadata || {},
      createdAt: doc.createdAt,
      updatedAt: doc.updatedAt,
    });
  }
}

export { AssignmentSchema };
