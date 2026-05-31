/**
 * Helper Utility Functions
 */

import { v4 as uuidv4 } from 'uuid';
import { IMessage, IQuickReply, ICard, ResponseType } from '../types';

export const generateSessionId = (): string => {
  return `session_${uuidv4()}`;
};

export const generateMessageId = (): string => {
  return `msg_${uuidv4()}`;
};

export const sanitizeMessage = (message: string): string => {
  return message
    .trim()
    .replace(/<script[^>]*>.*?<\/script>/gi, '')
    .replace(/<[^>]+>/g, '')
    .slice(0, 5000);
};

export const extractEmail = (text: string): string | null => {
  const emailRegex = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/g;
  const matches = text.match(emailRegex);
  return matches ? matches[0] : null;
};

export const extractPhone = (text: string): string | null => {
  const phoneRegex = /(?:\+?1[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}/g;
  const matches = text.match(phoneRegex);
  return matches ? matches[0] : null;
};

export const extractOrderNumber = (text: string): string | null => {
  const orderRegex = /(?:order\s*(?:#|number|num|no)?\s*[:#]?\s*)?([A-Z0-9]{4,15})/gi;
  const match = text.match(orderRegex);
  if (match) {
    const orderMatch = /([A-Z0-9]{4,15})/.exec(match[0]);
    return orderMatch ? orderMatch[1] : null;
  }
  return null;
};

export const detectLanguage = (text: string): string => {
  const patterns: Record<string, RegExp> = {
    es: /\b(hola|gracias|por favor|buenos días|buenas noches|adiós)\b/i,
    fr: /\b(bonjour|merci|s'il vous plaît|au revoir)\b/i,
    de: /\b(guten tag|danke|bitte|auf wiedersehen)\b/i,
    pt: /\b(olá|obrigado|por favor|bom dia|boa noite)\b/i,
    zh: /[\u4e00-\u9fff]/,
    ja: /[\u3040-\u309f\u30a0-\u30ff]/,
    ar: /[\u0600-\u06ff]/,
  };

  for (const [lang, pattern] of Object.entries(patterns)) {
    if (pattern.test(text)) {
      return lang;
    }
  }

  return 'en';
};

export const calculateConfidence = (matches: number, totalWords: number): number => {
  if (totalWords === 0) return 0;
  return Math.min(matches / totalWords, 1);
};

export const formatDuration = (milliseconds: number): string => {
  const seconds = Math.floor(milliseconds / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);

  if (hours > 0) {
    return `${hours}h ${minutes % 60}m`;
  } else if (minutes > 0) {
    return `${minutes}m ${seconds % 60}s`;
  } else {
    return `${seconds}s`;
  }
};

export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength - 3) + '...';
};

export const createQuickReplies = (options: string[]): IQuickReply[] => {
  return options.map(title => ({
    title,
    payload: title.toLowerCase().replace(/\s+/g, '_'),
  }));
};

export const createCard = (
  title: string,
  description?: string,
  imageUrl?: string,
  buttons?: Array<{ title: string; payload: string }>
): ICard => {
  return {
    title,
    description,
    imageUrl,
    buttons: buttons?.map(btn => ({
      title: btn.title,
      payload: btn.payload,
      type: 'postback',
    })),
  };
};

export const shuffleArray = <T>(array: T[]): T[] => {
  const shuffled = [...array];
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }
  return shuffled;
};

export const slugify = (text: string): string => {
  return text
    .toLowerCase()
    .replace(/[^\w\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .trim();
};

export const isValidUrl = (url: string): boolean => {
  try {
    new URL(url);
    return true;
  } catch {
    return false;
  }
};

export const sleep = (ms: number): Promise<void> => {
  return new Promise(resolve => setTimeout(resolve, ms));
};

export const retry = async <T>(
  fn: () => Promise<T>,
  retries: number = 3,
  delay: number = 1000
): Promise<T> => {
  try {
    return await fn();
  } catch (error) {
    if (retries <= 0) throw error;
    await sleep(delay);
    return retry(fn, retries - 1, delay * 2);
  }
};

export const debounce = <T extends (...args: unknown[]) => unknown>(
  fn: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let timeoutId: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => fn(...args), delay);
  };
};

export const throttle = <T extends (...args: unknown[]) => unknown>(
  fn: T,
  limit: number
): ((...args: Parameters<T>) => void) => {
  let inThrottle: boolean;
  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      fn(...args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
};

export const chunkArray = <T>(array: T[], size: number): T[][] => {
  const chunks: T[][] = [];
  for (let i = 0; i < array.length; i += size) {
    chunks.push(array.slice(i, i + size));
  }
  return chunks;
};

export const deepClone = <T>(obj: T): T => {
  return JSON.parse(JSON.stringify(obj));
};

export const omitKeys = <T extends Record<string, unknown>, K extends keyof T>(
  obj: T,
  keys: K[]
): Omit<T, K> => {
  const result = { ...obj };
  keys.forEach(key => delete result[key]);
  return result;
};

export const pickKeys = <T extends Record<string, unknown>, K extends keyof T>(
  obj: T,
  keys: K[]
): Pick<T, K> => {
  const result = {} as Pick<T, K>;
  keys.forEach(key => {
    if (key in obj) {
      result[key] = obj[key];
    }
  });
  return result;
};
