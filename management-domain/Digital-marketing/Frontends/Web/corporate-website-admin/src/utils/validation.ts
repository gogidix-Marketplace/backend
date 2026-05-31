import { z } from 'zod';

// Auth schemas
export const loginSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(8, 'Password must be at least 8 characters'),
  rememberMe: z.boolean().optional(),
});

export const registerSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(8, 'Password must be at least 8 characters'),
  firstName: z.string().min(2, 'First name must be at least 2 characters'),
  lastName: z.string().min(2, 'Last name must be at least 2 characters'),
});

// Content schemas
export const pageSchema = z.object({
  title: z.string().min(1, 'Title is required'),
  slug: z.string().min(1, 'Slug is required'),
  content: z.string().min(1, 'Content is required'),
  status: z.enum(['draft', 'pending_review', 'scheduled', 'published', 'archived']),
  template: z.string().default('default'),
});

export const blogPostSchema = z.object({
  title: z.string().min(1, 'Title is required'),
  slug: z.string().min(1, 'Slug is required'),
  content: z.string().min(1, 'Content is required'),
  excerpt: z.string().min(1, 'Excerpt is required'),
  status: z.enum(['draft', 'pending_review', 'scheduled', 'published', 'archived']),
  categoryId: z.string().optional(),
  tags: z.array(z.string()).default([]),
});

// Product schemas
export const productSchema = z.object({
  name: z.string().min(1, 'Product name is required'),
  slug: z.string().min(1, 'Slug is required'),
  description: z.string().min(1, 'Description is required'),
  status: z.enum(['draft', 'pending_review', 'scheduled', 'published', 'archived']),
  categoryId: z.string().optional(),
  featured: z.boolean().default(false),
});

export const pricingPlanSchema = z.object({
  name: z.string().min(1, 'Plan name is required'),
  price: z.number().min(0, 'Price must be positive'),
  currency: z.string().default('USD'),
  billingCycle: z.enum(['monthly', 'quarterly', 'annual', 'custom']),
  features: z.array(z.string()).default([]),
  highlighted: z.boolean().default(false),
});

// Career schemas
export const jobSchema = z.object({
  title: z.string().min(1, 'Job title is required'),
  description: z.string().min(1, 'Description is required'),
  department: z.string().min(1, 'Department is required'),
  type: z.enum(['full_time', 'part_time', 'contract', 'internship']),
  experience: z.enum(['entry', 'mid', 'senior', 'lead', 'executive']).optional(),
  location: z.string(),
  status: z.enum(['draft', 'open', 'closed', 'on_hold', 'archived']),
  featured: z.boolean().default(false),
});

// Lead schemas
export const demoRequestSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  email: z.string().email('Invalid email address'),
  company: z.string().optional(),
  companySize: z.string().optional(),
  interests: z.array(z.string()).default([]),
});

export const supportTicketSchema = z.object({
  customerName: z.string().min(1, 'Name is required'),
  customerEmail: z.string().email('Invalid email address'),
  category: z.enum(['technical', 'billing', 'feature_request', 'bug_report', 'other']),
  subject: z.string().min(1, 'Subject is required'),
  description: z.string().min(1, 'Description is required'),
  priority: z.enum(['low', 'normal', 'high', 'urgent']),
});

// Settings schemas
export const generalSettingsSchema = z.object({
  siteName: z.string().min(1, 'Site name is required'),
  siteDescription: z.string().optional(),
  siteUrl: z.string().url('Invalid URL'),
  email: z.string().email('Invalid email address').optional(),
  phone: z.string().optional(),
});

export type LoginFormValues = z.infer<typeof loginSchema>;
export type RegisterFormValues = z.infer<typeof registerSchema>;
export type PageFormValues = z.infer<typeof pageSchema>;
export type BlogPostFormValues = z.infer<typeof blogPostSchema>;
export type ProductFormValues = z.infer<typeof productSchema>;
export type PricingPlanFormValues = z.infer<typeof pricingPlanSchema>;
export type JobFormValues = z.infer<typeof jobSchema>;
export type DemoRequestFormValues = z.infer<typeof demoRequestSchema>;
export type SupportTicketFormValues = z.infer<typeof supportTicketSchema>;
export type GeneralSettingsFormValues = z.infer<typeof generalSettingsSchema>;
