export class EmailDomainException extends Error { constructor(message: string) { super(message); this.name = 'EmailDomainException'; } }
export class EmailNotFoundException extends EmailDomainException { constructor(id: string) { super(`Email job ${id} not found`); this.name = 'EmailNotFoundException'; } }
export class TemplateNotFoundException extends EmailDomainException { constructor(id: string) { super(`Email template ${id} not found`); this.name = 'TemplateNotFoundException'; } }
export class InvalidEmailException extends EmailDomainException { constructor(email: string) { super(`Invalid email address: ${email}`); this.name = 'InvalidEmailException'; } }
export class EmailSendFailedException extends EmailDomainException { constructor(reason: string) { super(`Email send failed: ${reason}`); this.name = 'EmailSendFailedException'; } }
