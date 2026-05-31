import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import envConfig from './infrastructure/config/env.config';
import { LoggerModule } from './infrastructure/config/logger.module';
import { MongooseConfigModule } from './infrastructure/persistence/mongodb/mongoose/mongoose.module';
import { ChatSessionRepository } from './infrastructure/persistence/mongodb/repositories/chat-session.repository';
import { IntentRepository } from './infrastructure/persistence/mongodb/repositories/intent.repository';
import { SessionAnalyticsRepository } from './infrastructure/persistence/mongodb/repositories/session-analytics.repository';
import { DailyAnalyticsRepository } from './infrastructure/persistence/mongodb/repositories/daily-analytics.repository';
import { CacheAdapter } from './infrastructure/persistence/redis/cache.adapter';
import { OpenAIAdapter } from './infrastructure/external/openai.adapter';
import { KnowledgeBaseAdapter } from './infrastructure/external/knowledge-base.adapter';
import { AgentServiceAdapter } from './infrastructure/external/agent-service.adapter';
import { KafkaModule } from './infrastructure/messaging/kafka/kafka.module';
import { ChatApplicationService } from './application/services/chat.service';
import { IntentApplicationService } from './application/services/intent.service';
import { AnalyticsApplicationService } from './application/services/analytics.service';
import { HandoffApplicationService } from './application/services/handoff.service';
import { TranslationApplicationService } from './application/services/translation.service';
import { HealthApplicationService } from './application/services/health.service';
import { ChatController } from './interfaces/http/chat.controller';
import { IntentController } from './interfaces/http/intent.controller';
import { AnalyticsController } from './interfaces/http/analytics.controller';
import { HealthController } from './interfaces/http/health.controller';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      load: [envConfig],
    }),
    LoggerModule,
    MongooseConfigModule,
    KafkaModule,
  ],
  controllers: [ChatController, IntentController, AnalyticsController, HealthController],
  providers: [
    ChatSessionRepository,
    IntentRepository,
    SessionAnalyticsRepository,
    DailyAnalyticsRepository,
    CacheAdapter,
    OpenAIAdapter,
    KnowledgeBaseAdapter,
    AgentServiceAdapter,
    ChatApplicationService,
    IntentApplicationService,
    AnalyticsApplicationService,
    HandoffApplicationService,
    TranslationApplicationService,
    HealthApplicationService,
    { provide: 'IChatSessionRepository', useExisting: ChatSessionRepository },
    { provide: 'IIntentRepository', useExisting: IntentRepository },
    { provide: 'ISessionAnalyticsRepository', useExisting: SessionAnalyticsRepository },
    { provide: 'IDailyAnalyticsRepository', useExisting: DailyAnalyticsRepository },
    { provide: 'ICachePort', useExisting: CacheAdapter },
    { provide: 'IOpenAIPort', useExisting: OpenAIAdapter },
    { provide: 'IKnowledgeBasePort', useExisting: KnowledgeBaseAdapter },
    { provide: 'IAgentServicePort', useExisting: AgentServiceAdapter },
    { provide: 'IIntentQueryPort', useExisting: IntentApplicationService },
    { provide: 'IHandoffCommandPort', useExisting: HandoffApplicationService },
  ],
})
export class AppModule {}
