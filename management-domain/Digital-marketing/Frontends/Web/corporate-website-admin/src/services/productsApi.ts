import { apiClient } from './api';
import { Product, ProductCategory, ProductFeature, PricingPlan, Integration, ListParams, ApiResponse } from '@/types';

// Products
export const getProducts = async (params?: ListParams): Promise<ApiResponse<Product[]>> => {
  return apiClient.get('/products', { params });
};

export const getProduct = async (id: string): Promise<{ product: Product }> => {
  return apiClient.get(`/products/${id}`);
};

export const createProduct = async (data: Partial<Product>): Promise<{ product: Product }> => {
  return apiClient.post('/products', data);
};

export const updateProduct = async (id: string, data: Partial<Product>): Promise<{ product: Product }> => {
  return apiClient.put(`/products/${id}`, data);
};

export const deleteProduct = async (id: string): Promise<void> => {
  return apiClient.delete(`/products/${id}`);
};

// Categories
export const getCategories = async (params?: ListParams): Promise<ApiResponse<ProductCategory[]>> => {
  return apiClient.get('/products/categories', { params });
};

export const getCategory = async (id: string): Promise<{ category: ProductCategory }> => {
  return apiClient.get(`/products/categories/${id}`);
};

export const createCategory = async (data: Partial<ProductCategory>): Promise<{ category: ProductCategory }> => {
  return apiClient.post('/products/categories', data);
};

export const updateCategory = async (id: string, data: Partial<ProductCategory>): Promise<{ category: ProductCategory }> => {
  return apiClient.put(`/products/categories/${id}`, data);
};

export const deleteCategory = async (id: string): Promise<void> => {
  return apiClient.delete(`/products/categories/${id}`);
};

// Features
export const getFeatures = async (params?: ListParams): Promise<ApiResponse<ProductFeature[]>> => {
  return apiClient.get('/products/features', { params });
};

export const createFeature = async (data: Partial<ProductFeature>): Promise<{ feature: ProductFeature }> => {
  return apiClient.post('/products/features', data);
};

export const updateFeature = async (id: string, data: Partial<ProductFeature>): Promise<{ feature: ProductFeature }> => {
  return apiClient.put(`/products/features/${id}`, data);
};

export const deleteFeature = async (id: string): Promise<void> => {
  return apiClient.delete(`/products/features/${id}`);
};

// Pricing
export const getPricingPlans = async (params?: ListParams): Promise<ApiResponse<PricingPlan[]>> => {
  return apiClient.get('/products/pricing', { params });
};

export const createPricingPlan = async (data: Partial<PricingPlan>): Promise<{ plan: PricingPlan }> => {
  return apiClient.post('/products/pricing', data);
};

export const updatePricingPlan = async (id: string, data: Partial<PricingPlan>): Promise<{ plan: PricingPlan }> => {
  return apiClient.put(`/products/pricing/${id}`, data);
};

export const deletePricingPlan = async (id: string): Promise<void> => {
  return apiClient.delete(`/products/pricing/${id}`);
};

// Integrations
export const getIntegrations = async (params?: ListParams): Promise<ApiResponse<Integration[]>> => {
  return apiClient.get('/products/integrations', { params });
};

export const getIntegration = async (id: string): Promise<{ integration: Integration }> => {
  return apiClient.get(`/products/integrations/${id}`);
};

export const createIntegration = async (data: Partial<Integration>): Promise<{ integration: Integration }> => {
  return apiClient.post('/products/integrations', data);
};

export const updateIntegration = async (id: string, data: Partial<Integration>): Promise<{ integration: Integration }> => {
  return apiClient.put(`/products/integrations/${id}`, data);
};

export const deleteIntegration = async (id: string): Promise<void> => {
  return apiClient.delete(`/products/integrations/${id}`);
};
