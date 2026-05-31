import { MongooseModule } from '@nestjs/mongoose';
import { ConfigService } from '@nestjs/config';

export const getMongoConfig = (configService: ConfigService) => ({
  uri: configService.get<string>('MONGODB_URI', 'mongodb://localhost:27017/payment-automation'),
  options: {
    directConnection: configService.get<boolean>('MONGODB_DIRECT_CONNECTION', false),
  },
});

export const mongoProviders = [
  MongooseModule.forRootAsync({
    useFactory: getMongoConfig,
    inject: [ConfigService],
  }),
];
