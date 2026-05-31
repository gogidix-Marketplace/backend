import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PickingService } from './picking.service';
import { PickOrder, PickItem, PickBatch, ZoneOptimization } from '../domain/entities/picking.entity';
import { PickingController } from '../interfaces/rest/picking.controller';

@Module({
  imports: [TypeOrmModule.forFeature([PickOrder, PickItem, PickBatch, ZoneOptimization])],
  controllers: [PickingController],
  providers: [PickingService],
  exports: [PickingService],
})
export class PickingModule {}
