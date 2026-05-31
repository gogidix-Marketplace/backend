import { Module } from '@nestjs/common';
import { RestModule } from './rest/interfaces.module';
@Module({ imports: [RestModule] })
export class InterfacesModule {}
