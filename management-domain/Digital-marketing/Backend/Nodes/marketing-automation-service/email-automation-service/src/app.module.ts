import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { InfrastructureModule } from './infrastructure/infrastructure.module';
import { InterfacesModule } from './interfaces/interfaces.module';
import { SharedModule } from './shared/shared.module';
import { EmailOrchestrationService } from './application/services/email-orchestration.service';
import { SendEmailUseCase } from './application/use-cases/send-email.use-case';
import { SendBatchEmailUseCase } from './application/use-cases/send-batch-email.use-case';
import { ManageTemplatesUseCase } from './application/use-cases/manage-templates.use-case';
import { ProcessEmailQueueUseCase } from './application/use-cases/process-email-queue.use-case';
import { HandleWebhookUseCase } from './application/use-cases/handle-webhook.use-case';

@Module({
  imports: [
    MongooseModule.forRoot(process.env.MONGODB_URI ?? 'mongodb://localhost:27017/email_automation'),
    InfrastructureModule,
    InterfacesModule,
    SharedModule,
  ],
  providers: [EmailOrchestrationService, SendEmailUseCase, SendBatchEmailUseCase, ManageTemplatesUseCase, ProcessEmailQueueUseCase, HandleWebhookUseCase],
})
export class AppModule {}
