import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { FulfillmentService } from './fulfillment.service';
import { FulfillmentController } from '../interfaces/rest/fulfillment.controller';
import { Fulfillment, FulfillmentSchema } from '../domain/entities/fulfillment.entity';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Fulfillment.name, schema: FulfillmentSchema },
    ]),
  ],
  controllers: [FulfillmentController],
  providers: [FulfillmentService],
  exports: [FulfillmentService],
})
export class FulfillmentModule {}
