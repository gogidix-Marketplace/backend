import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Schema } from 'mongoose';
import { AgentRepository } from '@domain/ports/output';
import { Agent } from '@domain/models';
import { AgentStatus } from '@domain/enums';

const AgentSchema = new Schema({
  agentId: { type: String, required: true, unique: true, index: true },
  userId: { type: String, required: true, index: true },
  name: { type: String, required: true },
  email: { type: String, required: true },
  role: { type: String, required: true },
  status: { type: String, required: true, index: true },
  skills: [{
    name: String,
    level: Number,
    verified: Boolean,
    lastUsed: Date,
  }],
  capacity: { type: Number, required: true, default: 5 },
  currentTickets: { type: Number, default: 0 },
  teams: [{ type: String }],
  assignedQueues: [{ type: String }],
  performance: {
    avgResolutionTime: Number,
    avgResponseTime: Number,
    customerSatisfaction: Number,
    ticketsResolved: Number,
    ticketsEscalated: Number,
  },
  lastActivity: { type: Date, default: Date.now },
  isActive: { type: Boolean, default: true },
}, { timestamps: true, collection: 'agents' });

@Injectable()
export class MongoAgentRepository implements AgentRepository {
  private readonly logger = new Logger(MongoAgentRepository.name);

  constructor(
    @InjectModel('Agent') private readonly agentModel: Model<any>,
  ) {}

  async findById(id: string): Promise<Agent | null> {
    const doc = await this.agentModel.findById(id);
    return doc ? this.toDomain(doc) : null;
  }

  async findByAgentId(agentId: string): Promise<Agent | null> {
    const doc = await this.agentModel.findOne({ agentId });
    return doc ? this.toDomain(doc) : null;
  }

  async findAvailable(teamId?: string, requiredSkills?: string[]): Promise<Agent[]> {
    const query: any = { status: AgentStatus.AVAILABLE, isActive: true };
    if (teamId) query.teams = teamId;
    if (requiredSkills && requiredSkills.length > 0) {
      query['skills.name'] = { $in: requiredSkills };
      query['skills.verified'] = true;
    }
    const docs = await this.agentModel.find(query);
    return docs.map(d => this.toDomain(d)).filter(a => a.canAcceptTicket());
  }

  async findAll(): Promise<Agent[]> {
    const docs = await this.agentModel.find({ isActive: true });
    return docs.map(d => this.toDomain(d));
  }

  async findByTeam(teamId: string): Promise<Agent[]> {
    const docs = await this.agentModel.find({ teams: teamId, isActive: true });
    return docs.map(d => this.toDomain(d));
  }

  async findByStatus(status: string): Promise<Agent[]> {
    const docs = await this.agentModel.find({ status, isActive: true });
    return docs.map(d => this.toDomain(d));
  }

  async save(agent: Agent): Promise<Agent> {
    const props = agent.toPlainObject();
    const doc = new this.agentModel(props);
    await doc.save();
    return agent;
  }

  async update(agent: Agent): Promise<Agent> {
    const props = agent.toPlainObject();
    await this.agentModel.updateOne({ agentId: props.agentId }, { $set: props });
    return agent;
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.agentModel.findByIdAndUpdate(id, { isActive: false });
    return !!result;
  }

  async updateWorkload(agentId: string, delta: number): Promise<void> {
    await this.agentModel.updateOne({ agentId }, { $inc: { currentTickets: delta } });
  }

  private toDomain(doc: any): Agent {
    return Agent.create({
      id: doc._id.toString(),
      agentId: doc.agentId,
      userId: doc.userId,
      name: doc.name,
      email: doc.email,
      role: doc.role,
      status: doc.status,
      skills: doc.skills || [],
      capacity: doc.capacity,
      currentTickets: doc.currentTickets,
      teams: doc.teams || [],
      assignedQueues: doc.assignedQueues || [],
      performance: doc.performance || {
        avgResolutionTime: 0, avgResponseTime: 0,
        customerSatisfaction: 0, ticketsResolved: 0, ticketsEscalated: 0,
      },
      lastActivity: doc.lastActivity,
      isActive: doc.isActive,
      createdAt: doc.createdAt,
      updatedAt: doc.updatedAt,
    });
  }
}

export { AgentSchema };
