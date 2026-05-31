import { Module } from '@nestjs/common';
import ConfigModule from './configuration';

@Module({
  imports: [ConfigModule],
  exports: [ConfigModule],
})
export class ConfigInfrastructureModule {}
