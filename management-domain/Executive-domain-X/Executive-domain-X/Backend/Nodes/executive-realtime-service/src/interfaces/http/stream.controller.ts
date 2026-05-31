import { Controller, Get, Param } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { StreamHandlerService } from '../../application/services/stream-handler.service';
import { ConnectionManagerService } from '../../application/services/connection-manager.service';

@ApiTags('stream')
@ApiBearerAuth()
@Controller('stream')
export class StreamController {
  constructor(
    private readonly streamHandler: StreamHandlerService,
    private readonly connectionManager: ConnectionManagerService,
  ) {}

  @Get('buffer/:roomId')
  getBufferStatus(@Param('roomId') roomId: string) { return this.streamHandler.getBufferStatus(roomId); }

  @Get('buffers')
  getAllBufferStatuses() { return this.streamHandler.getAllBufferStatuses(); }

  @Get('connections')
  getConnectionStats() { return this.connectionManager.getStats(); }
}
