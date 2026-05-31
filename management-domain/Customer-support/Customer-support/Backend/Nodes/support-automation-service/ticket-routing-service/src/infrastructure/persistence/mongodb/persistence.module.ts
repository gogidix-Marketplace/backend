import { Module } from '@nestjs/common';
import { AgentRepository, TicketRepository, AssignmentRepository } from '@domain/ports/output';
import { RedisAgentRepository, RedisTicketRepository, RedisAssignmentRepository } from './repositories.impl';

@Module({
  providers: [
    { provide: 'AgentRepository', useClass: RedisAgentRepository },
    { provide: 'TicketRepository', useClass: RedisTicketRepository },
    { provide: 'AssignmentRepository', useClass: RedisAssignmentRepository },
  ],
  exports: ['AgentRepository', 'TicketRepository', 'AssignmentRepository'],
})
export class PersistenceModule {}
