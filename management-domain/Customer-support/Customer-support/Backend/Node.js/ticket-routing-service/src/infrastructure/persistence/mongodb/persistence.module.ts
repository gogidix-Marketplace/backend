import { MongooseModule } from '@nestjs/mongoose';
import { Module } from '@nestjs/common';
import { AgentRepository, AssignmentRepository, QueueRepository } from '@domain/ports/output';
import { MongoAgentRepository, AgentSchema } from './agent.repository.impl';
import { MongoAssignmentRepository, AssignmentSchema } from './assignment.repository.impl';
import { MongoQueueRepository, QueueSchema } from './queue.repository.impl';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: 'Agent', schema: AgentSchema },
      { name: 'Assignment', schema: AssignmentSchema },
      { name: 'Queue', schema: QueueSchema },
    ]),
  ],
  providers: [
    { provide: 'AgentRepository', useClass: MongoAgentRepository },
    { provide: 'AssignmentRepository', useClass: MongoAssignmentRepository },
    { provide: 'QueueRepository', useClass: MongoQueueRepository },
  ],
  exports: ['AgentRepository', 'AssignmentRepository', 'QueueRepository'],
})
export class PersistenceModule {}
