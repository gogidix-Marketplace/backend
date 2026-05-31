import { Module } from '@nestjs/common';
import { EVENT_BUS_PORT } from '../../../domain/ports/services/event-bus.port';
import { RedisEventBusAdapter } from './redis-event-bus.adapter';

@Module({
  providers: [{ provide: 'EVENT_BUS_PORT' useClass: RedisEventBusAdapter }],
  exports: [EVENT_BUS_PORT],
})
export class EventsModule {}
