export enum TicketPriority { CRITICAL = 'critical', HIGH = 'high', MEDIUM = 'medium', LOW = 'low' }
export enum TicketStatus { PENDING = 'pending', ASSIGNED = 'assigned', IN_PROGRESS = 'in_progress', RESOLVED = 'resolved', CLOSED = 'closed' }
export enum AgentStatus { AVAILABLE = 'available', BUSY = 'busy', OFFLINE = 'offline', AWAY = 'away' }
export enum RoutingStrategy { ROUND_ROBIN = 'round_robin', LEAST_BUSY = 'least_busy', SKILLS_BASED = 'skills_based', PRIORITY_BASED = 'priority_based' }
