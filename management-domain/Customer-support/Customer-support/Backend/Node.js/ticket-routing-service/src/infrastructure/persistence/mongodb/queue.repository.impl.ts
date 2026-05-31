import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Schema } from 'mongoose';
import { QueueRepository } from '@domain/ports/output';
import { Queue } from '@domain/models';
import { QueueType, RoutingStrategy } from '@domain/enums';

const EscalationRuleSchema = new Schema({
  id: String,
  name: String,
  condition: { field: String, operator: String, value: Schema.Types.Mixed },
  action: { type: String, target: String, priority: String },
  isActive: Boolean,
}, { _id: false });

const SLASchema = new Schema({
  firstResponseTime: { type: Number, default: 300 },
  resolutionTime: { type: Number, default: 86400 },
  businessHoursOnly: { type: Boolean, default: false },
}, { _id: false });

const QueueSchema = new Schema({
  queueId: { type: String, required: true, unique: true, index: true },
  name: { type: String, required: true },
  description: String,
  type: { type: String, required: true, default: QueueType.GENERAL },
  priority: { type: Number, default: 0 },
  requiredSkills: [{ type: String }],
  assignedTeams: [{ type: String }],
  assignedAgents: [{ type: String }],
  routingStrategy: { type: String, default: RoutingStrategy.LEAST_BUSY },
  escalationRules: [EscalationRuleSchema],
  isActive: { type: Boolean, default: true },
  sla: SLASchema,
}, { timestamps: true, collection: 'queues' });

@Injectable()
export class MongoQueueRepository implements QueueRepository {
  private readonly logger = new Logger(MongoQueueRepository.name);

  constructor(
    @InjectModel('Queue') private readonly model: Model<any>,
  ) {}

  async findById(id: string): Promise<Queue | null> {
    const doc = await this.model.findById(id);
    return doc ? this.toDomain(doc) : null;
  }

  async findByQueueId(queueId: string): Promise<Queue | null> {
    const doc = await this.model.findOne({ queueId, isActive: true });
    return doc ? this.toDomain(doc) : null;
  }

  async findActive(): Promise<Queue[]> {
    const docs = await this.model.find({ isActive: true }).sort({ priority: -1 });
    return docs.map(d => this.toDomain(d));
  }

  async findByType(type: string): Promise<Queue[]> {
    const docs = await this.model.find({ type, isActive: true });
    return docs.map(d => this.toDomain(d));
  }

  async findByTeam(teamId: string): Promise<Queue[]> {
    const docs = await this.model.find({ assignedTeams: teamId, isActive: true });
    return docs.map(d => this.toDomain(d));
  }

  async save(queue: Queue): Promise<Queue> {
    const props = queue.toPlainObject();
    const doc = new this.model(props);
    await doc.save();
    return queue;
  }

  async getDefaultQueue(): Promise<Queue | null> {
    const doc = await this.model.findOne({ type: QueueType.GENERAL, isActive: true }).sort({ priority: -1 });
    return doc ? this.toDomain(doc) : null;
  }

  private toDomain(doc: any): Queue {
    return Queue.create({
      id: doc._id.toString(),
      queueId: doc.queueId,
      name: doc.name,
      description: doc.description || '',
      type: doc.type,
      priority: doc.priority,
      requiredSkills: doc.requiredSkills || [],
      assignedTeams: doc.assignedTeams || [],
      assignedAgents: doc.assignedAgents || [],
      routingStrategy: doc.routingStrategy,
      escalationRules: doc.escalationRules || [],
      isActive: doc.isActive,
      sla: doc.sla || { firstResponseTime: 300, resolutionTime: 86400, businessHoursOnly: false },
      createdAt: doc.createdAt,
      updatedAt: doc.updatedAt,
    });
  }
}

export { QueueSchema };
