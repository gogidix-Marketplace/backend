import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PackingController } from '../interfaces/rest/packing.controller';
import { PackingService } from './packing.service';
import { PackOrderEntity } from '../domain/entities/packing.entity';

@Module({
  imports: [TypeOrmModule.forFeature([PackOrderEntity])],
  controllers: [PackingController],
  providers: [PackingService],
  exports: [PackingService],
})
export class PackingModule {}
