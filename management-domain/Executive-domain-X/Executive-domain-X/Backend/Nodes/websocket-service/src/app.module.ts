import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { DashboardSchema } from './infrastructure/persistence/mongodb/mongoose/dashboard.schema';
import { DashboardRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/dashboard.repository.impl';
import { RoomRepositoryImpl } from './infrastructure/persistence/memory/room.repository.impl';
import { BroadcastService } from './application/services/broadcast.service';
import { RoomService } from './application/services/room.service';
import { PresenceService } from './application/services/presence.service';
import { DashboardService } from './application/services/dashboard.service';
import { DashboardController } from './interfaces/http/dashboard.controller';
import { LoggerModule } from './infrastructure/config/logger.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    MongooseModule.forRootAsync({ imports: [ConfigModule], useFactory: (cs: ConfigService) => ({ uri: cs.get('MONGODB_URI') || 'mongodb://localhost:27017/websocket-service' }), inject: [ConfigService] }),
    MongooseModule.forFeature([{ name: 'Dashboard', schema: DashboardSchema }]),
    LoggerModule,
  ],
  controllers: [DashboardController],
  providers: [
    BroadcastService, RoomService, PresenceService, DashboardService,
    { provide: 'IDashboardRepository', useClass: DashboardRepositoryImpl },
    { provide: 'IRoomRepository', useClass: RoomRepositoryImpl },
    DashboardRepositoryImpl, RoomRepositoryImpl,
  ],
  exports: [BroadcastService, RoomService, PresenceService, DashboardService],
})
export class AppModule {}
