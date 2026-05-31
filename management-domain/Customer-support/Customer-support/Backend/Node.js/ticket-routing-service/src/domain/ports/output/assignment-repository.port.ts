import { Assignment } from '../../models';

export interface AssignmentRepository {
  save(assignment: Assignment): Promise<Assignment>;
  findByTicket(ticketId: string): Promise<Assignment[]>;
  findByAgent(agentId: string, startDate?: Date, endDate?: Date): Promise<Assignment[]>;
  getCurrentAssignments(agentId: string): Promise<Assignment[]>;
  getAgentWorkload(agentId: string): Promise<number>;
  getAssignmentStats(agentId: string): Promise<Record<string, unknown>>;
}
