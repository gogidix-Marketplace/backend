import { apiClient, queryKeys } from './api';
import { Page, BlogPost, PressRelease, Resource, ListParams, ApiResponse } from '@/types';

// Pages
export const getPages = async (params?: ListParams): Promise<ApiResponse<Page[]>> => {
  return apiClient.get('/content/pages', { params });
};

export const getPage = async (id: string): Promise<{ page: Page }> => {
  return apiClient.get(`/content/pages/${id}`);
};

export const createPage = async (data: Partial<Page>): Promise<{ page: Page }> => {
  return apiClient.post('/content/pages', data);
};

export const updatePage = async (id: string, data: Partial<Page>): Promise<{ page: Page }> => {
  return apiClient.put(`/content/pages/${id}`, data);
};

export const deletePage = async (id: string): Promise<void> => {
  return apiClient.delete(`/content/pages/${id}`);
};

export const duplicatePage = async (id: string): Promise<{ page: Page }> => {
  return apiClient.post(`/content/pages/${id}/duplicate`);
};

// Blog Posts
export const getBlogPosts = async (params?: ListParams): Promise<ApiResponse<BlogPost[]>> => {
  return apiClient.get('/content/blog', { params });
};

export const getBlogPost = async (id: string): Promise<{ blogPost: BlogPost }> => {
  return apiClient.get(`/content/blog/${id}`);
};

export const createBlogPost = async (data: Partial<BlogPost>): Promise<{ blogPost: BlogPost }> => {
  return apiClient.post('/content/blog', data);
};

export const updateBlogPost = async (id: string, data: Partial<BlogPost>): Promise<{ blogPost: BlogPost }> => {
  return apiClient.put(`/content/blog/${id}`, data);
};

export const deleteBlogPost = async (id: string): Promise<void> => {
  return apiClient.delete(`/content/blog/${id}`);
};

// Press Releases
export const getPressReleases = async (params?: ListParams): Promise<ApiResponse<PressRelease[]>> => {
  return apiClient.get('/content/press', { params });
};

export const getPressRelease = async (id: string): Promise<{ pressRelease: PressRelease }> => {
  return apiClient.get(`/content/press/${id}`);
};

export const createPressRelease = async (data: Partial<PressRelease>): Promise<{ pressRelease: PressRelease }> => {
  return apiClient.post('/content/press', data);
};

export const updatePressRelease = async (id: string, data: Partial<PressRelease>): Promise<{ pressRelease: PressRelease }> => {
  return apiClient.put(`/content/press/${id}`, data);
};

export const deletePressRelease = async (id: string): Promise<void> => {
  return apiClient.delete(`/content/press/${id}`);
};

// Resources
export const getResources = async (params?: ListParams): Promise<ApiResponse<Resource[]>> => {
  return apiClient.get('/content/resources', { params });
};

export const getResource = async (id: string): Promise<{ resource: Resource }> => {
  return apiClient.get(`/content/resources/${id}`);
};

export const createResource = async (data: Partial<Resource>): Promise<{ resource: Resource }> => {
  return apiClient.post('/content/resources', data);
};

export const updateResource = async (id: string, data: Partial<Resource>): Promise<{ resource: Resource }> => {
  return apiClient.put(`/content/resources/${id}`, data);
};

export const deleteResource = async (id: string): Promise<void> => {
  return apiClient.delete(`/content/resources/${id}`);
};
