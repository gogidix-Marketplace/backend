export class SocialDomainException extends Error { constructor(message: string) { super(message); this.name = 'SocialDomainException'; } }
export class PostNotFoundException extends SocialDomainException { constructor(id: string) { super(`Post ${id} not found`); this.name = 'PostNotFoundException'; } }
export class AccountNotFoundException extends SocialDomainException { constructor(id: string) { super(`Social account ${id} not found`); this.name = 'AccountNotFoundException'; } }
export class PublishingFailedException extends SocialDomainException { constructor(reason: string) { super(`Publishing failed: ${reason}`); this.name = 'PublishingFailedException'; } }
export class TokenExpiredException extends SocialDomainException { constructor(platform: string) { super(`Token expired for ${platform}`); this.name = 'TokenExpiredException'; } }
