import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { StorageSpace } from '../domain/entities/storage.entity';
import { StorageService } from './storage.service';
import { StorageController } from '../interfaces/rest/storage.controller';

@Module({
  imports: [TypeOrmModule.forFeature([StorageSpace])],
  controllers: [StorageController],
  providers: [StorageService],
  exports: [StorageService],
})
export class StorageModule {}
