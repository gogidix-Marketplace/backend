/**
 * Helper Functions
 * Common utility functions for the email automation service
 */

const crypto = require('crypto');

/**
 * Generate a unique ID
 */
const generateId = () => {
  return crypto.randomUUID();
};

/**
 * Hash a string using SHA256
 */
const hashString = (str) => {
  return crypto.createHash('sha256').update(str).digest('hex');
};

/**
 * Normalize email address
 */
const normalizeEmail = (email) => {
  if (typeof email === 'string') {
    return email.toLowerCase().trim();
  }
  if (Array.isArray(email)) {
    return email.map(e => e.toLowerCase().trim());
  }
  return email;
};

/**
 * Calculate exponential backoff delay
 */
const calculateBackoff = (attempt, baseDelay = 2000) => {
  return Math.min(baseDelay * Math.pow(2, attempt), 60000); // Max 1 minute
};

/**
 * Retry with exponential backoff
 */
const retryWithBackoff = async (fn, maxAttempts = 3, baseDelay = 2000) => {
  for (let attempt = 0; attempt < maxAttempts; attempt++) {
    try {
      return await fn();
    } catch (error) {
      if (attempt === maxAttempts - 1) throw error;

      const delay = calculateBackoff(attempt, baseDelay);
      await new Promise(resolve => setTimeout(resolve, delay));
    }
  }
};

/**
 * Parse email address to extract name and email
 */
const parseEmailAddress = (emailString) => {
  const match = emailString.match(/(?:"?([^"]*)"?\s)?(?:<)?([^>]+@[^>]+)(?:>)?/);
  if (match) {
    return {
      name: match[1]?.trim() || null,
      email: match[2].trim().toLowerCase()
    };
  }
  return { name: null, email: emailString.trim().toLowerCase() };
};

/**
 * Format email address
 */
const formatEmailAddress = (email, name = null) => {
  if (name) {
    return `"${name}" <${email}>`;
  }
  return email;
};

/**
 * Validate email format
 */
const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

/**
 * Sanitize HTML content
 */
const sanitizeHtml = (html) => {
  // Basic sanitization - in production, use a library like sanitize-html
  return html
    .replace(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '')
    .replace(/javascript:/gi, '')
    .replace(/on\w+\s*=/gi, '');
};

/**
 * Extract domain from email
 */
const extractDomain = (email) => {
  const parts = email.split('@');
  return parts.length === 2 ? parts[1].toLowerCase() : null;
};

/**
 * Sleep utility
 */
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

/**
 * Chunk array into smaller arrays
 */
const chunkArray = (array, chunkSize) => {
  const chunks = [];
  for (let i = 0; i < array.length; i += chunkSize) {
    chunks.push(array.slice(i, i + chunkSize));
  }
  return chunks;
};

/**
 * Build pagination metadata
 */
const buildPaginationMeta = (page, limit, total) => {
  const totalPages = Math.ceil(total / limit);
  return {
    page,
    limit,
    total,
    totalPages,
    hasNextPage: page < totalPages,
    hasPrevPage: page > 1
  };
};

/**
 * Filter object by allowed keys
 */
const filterObject = (obj, allowedKeys) => {
  return Object.keys(obj)
    .filter(key => allowedKeys.includes(key))
    .reduce((acc, key) => {
      acc[key] = obj[key];
      return acc;
    }, {});
};

/**
 * Parse user agent
 */
const parseUserAgent = (userAgent) => {
  if (!userAgent) return { browser: 'unknown', os: 'unknown' };

  const ua = userAgent.toLowerCase();

  let browser = 'unknown';
  if (ua.includes('chrome')) browser = 'chrome';
  else if (ua.includes('firefox')) browser = 'firefox';
  else if (ua.includes('safari')) browser = 'safari';
  else if (ua.includes('edge')) browser = 'edge';

  let os = 'unknown';
  if (ua.includes('windows')) os = 'windows';
  else if (ua.includes('mac')) os = 'macos';
  else if (ua.includes('linux')) os = 'linux';
  else if (ua.includes('android')) os = 'android';
  else if (ua.includes('iphone') || ua.includes('ipad')) os = 'ios';

  return { browser, os };
};

module.exports = {
  generateId,
  hashString,
  normalizeEmail,
  calculateBackoff,
  retryWithBackoff,
  parseEmailAddress,
  formatEmailAddress,
  isValidEmail,
  sanitizeHtml,
  extractDomain,
  sleep,
  chunkArray,
  buildPaginationMeta,
  filterObject,
  parseUserAgent
};
