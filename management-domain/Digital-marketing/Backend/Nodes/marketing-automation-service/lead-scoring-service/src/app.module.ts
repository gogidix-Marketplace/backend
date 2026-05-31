import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { InfrastructureModule } from './infrastructure/infrastructure.module';
import { InterfacesModule } from './interfaces/interfaces.module';
import { SharedModule } from './shared/shared.module';
import { ScoringOrchestrationService } from './application/services/scoring-orchestration.service';
import { CalculateScoreUseCase } from './application/use-cases/calculate-score.use-case';
import { ManageRulesUseCase } from './application/use-cases/manage-rules.use-case';
import { ManageSegmentsUseCase } from './application/use-cases/manage-segments.use-case';
import { ProcessEventUseCase } from './application/use-cases/process-event.use-case';

@Module({
  imports: [
    MongooseModule.forRoot(process.env.MONGODB_URI ?? 'mongodb://localhost:27017/lead_scoring'),
    InfrastructureModule, InterfacesModule, SharedModule,
  ],
  providers: [ScoringOrchestrationService, CalculateScoreUseCase, ManageRulesUseCase, ManageSegmentsUseCase, ProcessEventUseCase],
})
export class AppModule {}
