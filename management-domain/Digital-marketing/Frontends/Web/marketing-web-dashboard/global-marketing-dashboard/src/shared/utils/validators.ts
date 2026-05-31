// Validation utility functions using Zod schemas

import { z } from 'zod';

// Common validation schemas
export const emailSchema = z.string().email('Invalid email address');

export const urlSchema = z.string().url('Invalid URL');

export const phoneSchema = z
  .string()
  .regex(/^\+?[\d\s\-\(\)]+$/, 'Invalid phone number');

export const countryCodeSchema = z
  .string()
  .length(2, 'Country code must be 2 characters')
  .toUpperCase();

export const currencyCodeSchema = z
  .string()
  .length(3, 'Currency code must be 3 characters')
  .toUpperCase();

export const positiveNumberSchema = z.number().positive('Must be a positive number');

export const nonNegativeNumberSchema = z.number().nonnegative('Must be non-negative');

export const percentageSchema = z
  .number()
  .min(0, 'Percentage cannot be negative')
  .max(100, 'Percentage cannot exceed 100');

export const dateRangeSchema = z.object({
  start: z.date(),
  end: z.date(),
}).refine(
  data => data.start <= data.end,
  { message: 'Start date must be before end date' }
);

export const budgetSchema = z.object({
  name: z.string().min(1, 'Budget name is required').max(100, 'Name too long'),
  amount: positiveNumberSchema,
  currency: currencyCodeSchema,
  startDate: z.date(),
  endDate: z.date(),
}).refine(
  data => data.startDate <= data.endDate,
  { message: 'Start date must be before end date' }
);

export const campaignSchema = z.object({
  name: z.string().min(1, 'Campaign name is required').max(100, 'Name too long'),
  description: z.string().max(500, 'Description too long').optional(),
  type: z.enum(['awareness', 'consideration', 'conversion', 'retention']),
  channels: z.array(z.string()).min(1, 'At least one channel is required'),
  budgetTotal: positiveNumberSchema,
  currency: currencyCodeSchema,
  startDate: z.date(),
  endDate: z.date(),
  countries: z.array(z.object({
    countryCode: countryCodeSchema,
    budget: positiveNumberSchema,
    targetAudience: z.string().optional(),
  })).min(1, 'At least one country is required'),
}).refine(
  data => data.startDate <= data.endDate,
  { message: 'Start date must be before end date' }
).refine(
  data => {
    const countryBudgets = data.countries.reduce((sum, c) => sum + c.budget, 0);
    return Math.abs(countryBudgets - data.budgetTotal) < data.budgetTotal * 0.01;
  },
  { message: 'Country budgets must sum to total budget' }
);

export const leadSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  email: emailSchema,
  phone: phoneSchema.optional().or(z.literal('')),
  company: z.string().optional(),
  jobTitle: z.string().optional(),
  country: countryCodeSchema,
  score: z.number().min(0).max(100).optional(),
  estimatedValue: nonNegativeNumberSchema.optional(),
});

export const reportSchema = z.object({
  name: z.string().min(1, 'Report name is required').max(100, 'Name too long'),
  type: z.enum(['executive', 'campaign', 'channel', 'country', 'custom']),
  period: z.object({
    start: z.date(),
    end: z.date(),
  }),
  format: z.enum(['pdf', 'excel', 'csv', 'html']),
});

export const validateEmail = (email: string): boolean => {
  try {
    emailSchema.parse(email);
    return true;
  } catch {
    return false;
  }
};

export const validatePhone = (phone: string): boolean => {
  try {
    phoneSchema.parse(phone);
    return true;
  } catch {
    return false;
  }
};

export const validateUrl = (url: string): boolean => {
  try {
    urlSchema.parse(url);
    return true;
  } catch {
    return false;
  }
};

export const validateDateRange = (start: Date, end: Date): boolean => {
  try {
    dateRangeSchema.parse({ start, end });
    return true;
  } catch {
    return false;
  }
};

export const getValidationErrors = <T>(
  schema: z.ZodSchema<T>,
  data: unknown
): Record<string, string> => {
  try {
    schema.parse(data);
    return {};
  } catch (error) {
    if (error instanceof z.ZodError) {
      return error.errors.reduce((acc, err) => {
        const path = err.path.join('.');
        acc[path] = err.message;
        return acc;
      }, {} as Record<string, string>);
    }
    return { _generic: 'Validation failed' };
  }
};

export const createFormValidator = <T>(schema: z.ZodSchema<T>) => {
  return (data: unknown): { valid: boolean; errors: Record<string, string> } => {
    try {
      schema.parse(data);
      return { valid: true, errors: {} };
    } catch (error) {
      if (error instanceof z.ZodError) {
        const errors = error.errors.reduce((acc, err) => {
          const path = err.path.join('.');
          acc[path] = err.message;
          return acc;
        }, {} as Record<string, string>);
        return { valid: false, errors };
      }
      return { valid: false, errors: { _generic: 'Validation failed' } };
    }
  };
};
