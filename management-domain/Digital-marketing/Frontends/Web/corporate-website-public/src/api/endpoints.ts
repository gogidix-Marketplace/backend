import { apiClient } from './client';

// Types
export interface Product {
  id: string;
  name: string;
  slug: string;
  description: string;
  icon: string;
  features: string[];
}

export interface ContactForm {
  name: string;
  email: string;
  company?: string;
  subject: string;
  message: string;
}

export interface BlogPost {
  id: string;
  title: string;
  excerpt: string;
  content: string;
  author: string;
  date: string;
  category: string;
  readTime: string;
  slug: string;
}

export interface Webinar {
  id: string;
  title: string;
  description: string;
  date: string;
  time?: string;
  duration: string;
  speakers: string[];
  isUpcoming: boolean;
}

// API Functions
export const productsApi = {
  getAll: () => apiClient.get<Product[]>('/v1/products'),
  getBySlug: (slug: string) => apiClient.get<Product>(`/v1/products/${slug}`),
};

export const contactApi = {
  submit: (data: ContactForm) => apiClient.post('/v1/contact', data),
};

export const blogApi = {
  getAll: (params?: { category?: string; limit?: number }) =>
    apiClient.get<BlogPost[]>('/v1/blog', { params }),
  getBySlug: (slug: string) => apiClient.get<BlogPost>(`/v1/blog/${slug}`),
};

export const webinarsApi = {
  getUpcoming: () => apiClient.get<Webinar[]>('/v1/webinars/upcoming'),
  getOnDemand: () => apiClient.get<Webinar[]>('/v1/webinars/on-demand'),
  getById: (id: string) => apiClient.get<Webinar>(`/v1/webinars/${id}`),
};

export const newsletterApi = {
  subscribe: (email: string) => apiClient.post('/v1/newsletter/subscribe', { email }),
};
