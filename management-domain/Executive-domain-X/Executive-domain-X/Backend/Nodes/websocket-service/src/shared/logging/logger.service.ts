import { LoggerService as N, Injectable } from '@nestjs/common';
@Injectable()
export class LoggerService implements N { constructor(private l: N) {} log(m: string, c?: string) { this.l.log?.(m, c); } error(m: string, t?: string, c?: string) { this.l.error?.(m, t, c); } warn(m: string, c?: string) { this.l.warn?.(m, c); } debug(m: string, c?: string) { this.l.debug?.(m, c); } verbose(m: string, c?: string) { this.l.verbose?.(m, c); } setContext(c: string) { if ((this.l as any).setContext) (this.l as any).setContext(c); } }
