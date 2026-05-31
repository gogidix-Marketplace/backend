import { Module } from '@nestjs/common';
import { EMAIL_SENDER_PORT } from '../../../domain/ports/services/email-sender.port';
import { SendGridAdapter } from './sendgrid.adapter';
import { SesAdapter } from './ses.adapter';

@Module({
  providers: [
    { provide: 'EMAIL_SENDER_PORT' useClass: process.env.EMAIL_PROVIDER === 'ses' ? SesAdapter : SendGridAdapter },
  ],
  exports: [EMAIL_SENDER_PORT],
})
export class EmailProvidersModule {}
