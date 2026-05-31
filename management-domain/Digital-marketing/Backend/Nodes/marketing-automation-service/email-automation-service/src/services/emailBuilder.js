/**
 * Email Builder Service
 * Builds raw email messages for sending with attachments
 */

const logger = require('../utils/logger');

/**
 * Build raw email message
 */
const buildRawEmail = (emailData) => {
  const {
    to,
    cc,
    bcc,
    subject,
    html,
    text,
    from,
    replyTo,
    attachments
  } = emailData;

  const boundary = `boundary_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;

  // Build headers
  let raw = '';

  // From
  const fromEmail = from?.email || process.env.SES_FROM_EMAIL || 'noreply@gogidix.com';
  if (from?.name) {
    raw += `From: "${from.name}" <${fromEmail}>\r\n`;
  } else {
    raw += `From: ${fromEmail}\r\n`;
  }

  // To
  const toAddresses = Array.isArray(to)
    ? to.map(addr => typeof addr === 'string' ? addr : `"${addr.name}" <${addr.email}>`).join(', ')
    : (typeof to === 'string' ? to : `"${to.name}" <${to.email}>`);
  raw += `To: ${toAddresses}\r\n`;

  // CC
  if (cc && cc.length > 0) {
    const ccAddresses = Array.isArray(cc)
      ? cc.map(addr => typeof addr === 'string' ? addr : `"${addr.name}" <${addr.email}>`).join(', ')
      : cc;
    raw += `Cc: ${ccAddresses}\r\n`;
  }

  // Reply-To
  if (replyTo) {
    raw += `Reply-To: ${replyTo}\r\n`;
  }

  // Subject
  raw += `Subject: ${subject}\r\n`;
  raw += `MIME-Version: 1.0\r\n`;

  // Check if we have attachments
  if (attachments && attachments.length > 0) {
    raw += `Content-Type: multipart/mixed; boundary="${boundary}"\r\n\r\n`;
  } else if (html && text) {
    raw += `Content-Type: multipart/alternative; boundary="${boundary}"\r\n\r\n`;
  } else if (html) {
    raw += `Content-Type: text/html; charset=UTF-8\r\n\r\n`;
    raw += `${html}\r\n`;
    return Buffer.from(raw);
  } else {
    raw += `Content-Type: text/plain; charset=UTF-8\r\n\r\n`;
    raw += `${text || ''}\r\n`;
    return Buffer.from(raw);
  }

  // Build body
  if (attachments && attachments.length > 0) {
    // Multipart with attachments
    if (html && text) {
      // Alternative part first
      const altBoundary = `alt_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;

      raw += `--${boundary}\r\n`;
      raw += `Content-Type: multipart/alternative; boundary="${altBoundary}"\r\n\r\n`;

      // Text part
      raw += `--${altBoundary}\r\n`;
      raw += `Content-Type: text/plain; charset=UTF-8\r\n\r\n`;
      raw += `${text}\r\n\r\n`;

      // HTML part
      raw += `--${altBoundary}\r\n`;
      raw += `Content-Type: text/html; charset=UTF-8\r\n\r\n`;
      raw += `${html}\r\n\r\n`;

      raw += `--${altBoundary}--\r\n\r\n`;
    } else if (html) {
      raw += `--${boundary}\r\n`;
      raw += `Content-Type: text/html; charset=UTF-8\r\n\r\n`;
      raw += `${html}\r\n\r\n`;
    } else if (text) {
      raw += `--${boundary}\r\n`;
      raw += `Content-Type: text/plain; charset=UTF-8\r\n\r\n`;
      raw += `${text}\r\n\r\n`;
    }

    // Add attachments
    attachments.forEach(att => {
      raw += `--${boundary}\r\n`;
      raw += `Content-Type: ${att.contentType || 'application/octet-stream'}\r\n`;
      raw += `Content-Disposition: ${att.disposition || 'attachment'}; filename="${att.filename}"\r\n`;

      if (att.contentId) {
        raw += `Content-ID: <${att.contentId}>\r\n`;
      }

      if (att.encoding === 'utf8') {
        raw += `Content-Transfer-Encoding: 8bit\r\n\r\n`;
        raw += `${att.content}\r\n\r\n`;
      } else {
        raw += `Content-Transfer-Encoding: base64\r\n\r\n`;
        raw += `${att.content}\r\n\r\n`;
      }
    });

    raw += `--${boundary}--\r\n`;
  } else {
    // Multipart alternative without attachments
    raw += `--${boundary}\r\n`;
    raw += `Content-Type: text/plain; charset=UTF-8\r\n\r\n`;
    raw += `${text}\r\n\r\n`;

    raw += `--${boundary}\r\n`;
    raw += `Content-Type: text/html; charset=UTF-8\r\n\r\n`;
    raw += `${html}\r\n\r\n`;

    raw += `--${boundary}--\r\n`;
  }

  return Buffer.from(raw, 'utf-8');
};

/**
 * Parse email address
 */
const parseEmailAddress = (emailString) => {
  // Handle "Name <email>" format
  const match = emailString.match(/^(?:")?([^"]+)"?\s*<([^>]+)>$/);
  if (match) {
    return {
      name: match[1].trim(),
      email: match[2].trim().toLowerCase()
    };
  }

  // Handle <email> format
  const simpleMatch = emailString.match(/^<([^>]+)>$/);
  if (simpleMatch) {
    return {
      name: null,
      email: simpleMatch[1].trim().toLowerCase()
    };
  }

  // Plain email
  if (emailString.includes('@')) {
    return {
      name: null,
      email: emailString.trim().toLowerCase()
    };
  }

  return null;
};

/**
 * Format email address
 */
const formatEmailAddress = (email, name = null) => {
  if (name) {
    return `"${name.replace(/"/g, '\\"')}" <${email}>`;
  }
  return email;
};

/**
 * Base64 encode with line wrapping
 */
const base64Encode = (data) => {
  const base64 = Buffer.from(data).toString('base64');
  // Wrap lines at 76 characters per RFC 2045
  return base64.replace(/(.{76})/g, '$1\r\n');
};

module.exports = {
  buildRawEmail,
  parseEmailAddress,
  formatEmailAddress,
  base64Encode
};
