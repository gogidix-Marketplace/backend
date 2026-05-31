import { Agent } from '../../models';

export interface AgentRepository {
  findById(id: string): Promise<Agent | null>;
  findByAgentId(agentId: string): Promise<Agent | null>;
  findAvailable(teamId?: string, requiredSkills?: string[]): Promise<Agent[]>;
  findAll(): Promise<Agent[]>;
  findByTeam(teamId: string): Promise<Agent[]>;
  findByStatus(status: string): Promise<Agent[]>;
  save(agent: Agent): Promise<Agent>;
  update(agent: Agent): Promise<Agent>;
  delete(id: string): Promise<boolean>;
  updateWorkload(agentId: string, delta: number): Promise<void>;
}
