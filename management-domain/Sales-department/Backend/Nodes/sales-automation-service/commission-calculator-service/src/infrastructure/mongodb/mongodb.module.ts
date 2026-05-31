import { Module, Global } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigService } from '@nestjs/config';
import {
  CommissionSchema,
  CommissionRuleSchema,
  CommissionPeriodSchema,
  CommissionPayoutSchema,
} from './schemas';
import {
  CommissionRepository,
  CommissionRuleRepository,
  CommissionPeriodRepository,
} from './repositories';

/**
 * MongoDB Module for database connectivity
 */
@Global()
@Module({
  imports: [
    MongooseModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        uri: configService.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/commission-calculator',
        retryWrites: true,
        w: 'majority',
      }),
    }),
    MongooseModule.forFeature([
      { name: 'Commission', schema: CommissionSchema },
      { name: 'CommissionRule', schema: CommissionRuleSchema },
      { name: 'CommissionPeriod', schema: CommissionPeriodSchema },
      { name: 'CommissionPayout', schema: CommissionPayoutSchema },
    ]),
  ],
  providers: [
    CommissionRepository,
    CommissionRuleRepository,
    CommissionPeriodRepository,
  ],
  exports: [
    MongooseModule,
    CommissionRepository,
    CommissionRuleRepository,
    CommissionPeriodRepository,
  ],
})
export class MongoModule {}
