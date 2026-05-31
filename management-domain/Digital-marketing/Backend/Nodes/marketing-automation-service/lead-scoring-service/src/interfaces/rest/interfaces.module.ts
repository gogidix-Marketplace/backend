import { Module } from '@nestjs/common';
import { ScoringController } from './controllers/scoring.controller';
import { EventController } from './controllers/event.controller';
import { HealthController } from './controllers/health.controller';

@Module({ controllers: [ScoringController, EventController, HealthController] })
export class RestModule {}
