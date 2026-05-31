import { PartialType } from '@nestjs/swagger';
import { CreateIntentRequestDto } from './create-intent.request.dto';

export class UpdateIntentRequestDto extends PartialType(CreateIntentRequestDto) {}
