import { useQuery, useMutation, UseQueryOptions } from '@tanstack/react-query';
import { productsApi, contactApi, blogApi, webinarsApi, newsletterApi } from './endpoints';
import type { Product, ContactForm, BlogPost, Webinar } from './endpoints';

// Products hooks
export function useProducts(options?: Omit<UseQueryOptions<Product[]>, 'queryKey' | 'queryFn'>) {
  return useQuery({
    queryKey: ['products'],
    queryFn: productsApi.getAll,
    staleTime: 1000 * 60 * 60, // 1 hour
    ...options,
  });
}

export function useProduct(slug: string, options?: Omit<UseQueryOptions<Product>, 'queryKey' | 'queryFn'>) {
  return useQuery({
    queryKey: ['products', slug],
    queryFn: () => productsApi.getBySlug(slug),
    enabled: !!slug,
    ...options,
  });
}

// Contact hooks
export function useContactSubmit() {
  return useMutation({
    mutationFn: (data: ContactForm) => contactApi.submit(data),
  });
}

// Blog hooks
export function useBlogPosts(params?: { category?: string; limit?: number }) {
  return useQuery({
    queryKey: ['blog', params],
    queryFn: () => blogApi.getAll(params),
    staleTime: 1000 * 60 * 5, // 5 minutes
  });
}

export function useBlogPost(slug: string) {
  return useQuery({
    queryKey: ['blog', slug],
    queryFn: () => blogApi.getBySlug(slug),
    enabled: !!slug,
  });
}

// Webinars hooks
export function useUpcomingWebinars() {
  return useQuery({
    queryKey: ['webinars', 'upcoming'],
    queryFn: webinarsApi.getUpcoming,
    staleTime: 1000 * 60 * 15, // 15 minutes
  });
}

export function useOnDemandWebinars() {
  return useQuery({
    queryKey: ['webinars', 'on-demand'],
    queryFn: webinarsApi.getOnDemand,
    staleTime: 1000 * 60 * 60, // 1 hour
  });
}

// Newsletter hooks
export function useNewsletterSubscribe() {
  return useMutation({
    mutationFn: (email: string) => newsletterApi.subscribe(email),
  });
}
