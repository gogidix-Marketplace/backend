import { Module, NestModule, MiddlewareConsumer } from '@nestjs/common';
import { APP_FILTER, APP_INTERCEPTOR } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { CommissionModule } from './commission.module';
import { AllExceptionsFilter } from './interface/interceptors/exception-filter';
import { TransformInterceptor } from './interface/interceptors/transform.interceptor';
import { LoggingInterceptor } from './interface/interceptors/logging.interceptor';

/**
 * Root Application Module
 */
@Module({
  imports: [
    CommissionModule,
  ],
  providers: [
    {
      provide: APP_FILTER,
      useClass: AllExceptionsFilter,
    },
    {
      provide: APP_INTERCEPTOR,
      useClass: LoggingInterceptor,
    },
    {
      provide: APP_INTERCEPTOR,
      useClass: TransformInterceptor,
    },
  ],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    // Additional middleware configuration if needed
  }
}
