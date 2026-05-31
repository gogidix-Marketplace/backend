import { AgentStatus, AgentRole } from '../../enums';
import { Agent } from '../../models';

export interface AgentManagementInputPort {
  createAgent(data: {
    userId: string;
    name: string;
    email: string;
    role: AgentRole;
    skills: Array<{ name: string; level: number; verified: boolean }>;
    capacity: number;
    teams: string[];
  }): Promise<Agent>;
  getAgent(agentId: string): Promise<Agent | null>;
  updateAgentStatus(agentId: string, status: AgentStatus): Promise<boolean>;
  getAvailableAgents(teamId?: string, requiredSkills?: string[]): Promise<Agent[]>;
  getAllAgents(): Promise<Agent[]>;
  deleteAgent(agentId: string): Promise<boolean>;
  getAgentStats(agentId: string): Promise<Record<string, unknown>>;
}
