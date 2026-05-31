import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { EmailJobDocument, EmailJobSchema } from './mongoose/schemas/email-job.schema';
import { EmailTemplateDocument, EmailTemplateSchema } from './mongoose/schemas/email-template.schema';
import { BounceRecordDocument, BounceRecordSchema } from './mongoose/schemas/bounce-record.schema';
import { ComplaintRecordDocument, ComplaintRecordSchema } from './mongoose/schemas/complaint-record.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: EmailJobDocument.name, schema: EmailJobSchema },
      { name: EmailTemplateDocument.name, schema: EmailTemplateSchema },
      { name: BounceRecordDocument.name, schema: BounceRecordSchema },
      { name: ComplaintRecordDocument.name, schema: ComplaintRecordSchema },
    ]),
  ],
  exports: [MongooseModule],
})
export class DatabaseModule {}
