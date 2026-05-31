import { Module } from '@nestjs/common';
import { DatabaseModule } from './persistence/database.module';
import { EmailProvidersModule } from './email-providers/email-providers.module';
import { QueueModule } from './queue/queue.module';
import { CacheModule } from './cache/cache.module';
import { EmailJobRepositoryImpl } from './persistence/mongoose/email-job.repository.impl';
import { EmailTemplateRepositoryImpl } from './persistence/mongoose/email-template.repository.impl';
import { BounceRecordRepositoryImpl } from './persistence/mongoose/bounce-record.repository.impl';
import { ComplaintRecordRepositoryImpl } from './persistence/mongoose/complaint-record.repository.impl';
import { EMAIL_JOB_REPOSITORY } from '../domain/ports/repositories/email-job.repository';
import { EMAIL_TEMPLATE_REPOSITORY } from '../domain/ports/repositories/email-template.repository';
import { BOUNCE_RECORD_REPOSITORY } from '../domain/ports/repositories/bounce-record.repository';
import { COMPLAINT_RECORD_REPOSITORY } from '../domain/ports/repositories/complaint-record.repository';

@Module({
  imports: [DatabaseModule, EmailProvidersModule, QueueModule, CacheModule],
  providers: [
    { provide: 'EMAIL_JOB_REPOSITORY' useClass: EmailJobRepositoryImpl },
    { provide: 'EMAIL_TEMPLATE_REPOSITORY' useClass: EmailTemplateRepositoryImpl },
    { provide: 'BOUNCE_RECORD_REPOSITORY' useClass: BounceRecordRepositoryImpl },
    { provide: 'COMPLAINT_RECORD_REPOSITORY' useClass: ComplaintRecordRepositoryImpl },
  ],
  exports: [EMAIL_JOB_REPOSITORY, EMAIL_TEMPLATE_REPOSITORY, BOUNCE_RECORD_REPOSITORY, COMPLAINT_RECORD_REPOSITORY, EmailProvidersModule, QueueModule, CacheModule],
})
export class InfrastructureModule {}
