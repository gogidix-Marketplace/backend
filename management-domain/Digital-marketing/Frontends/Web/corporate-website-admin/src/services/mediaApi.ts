import { apiClient } from './api';
import { MediaFile, ListParams } from '@/types';

export const getMediaFiles = async (params?: ListParams): Promise<{ files: MediaFile[]; pagination?: any }> => {
  return apiClient.get('/media', { params });
};

export const uploadMediaFile = async (
  file: File,
  onProgress?: (progress: number) => void
): Promise<{ file: MediaFile }> => {
  return apiClient.upload('/media/upload', file, onProgress);
};

export const uploadMultipleFiles = async (
  files: File[],
  onProgress?: (progress: number) => void
): Promise<{ files: MediaFile[] }> => {
  const formData = new FormData();
  files.forEach((file) => formData.append('files', file));

  return apiClient.post('/media/upload-multiple', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    onUploadProgress: (progressEvent) => {
      if (onProgress && progressEvent.total) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
        onProgress(progress);
      }
    },
  });
};

export const deleteMediaFile = async (id: string): Promise<void> => {
  return apiClient.delete(`/media/${id}`);
};

export const updateMediaFile = async (id: string, data: Partial<MediaFile>): Promise<{ file: MediaFile }> => {
  return apiClient.put(`/media/${id}`, data);
};

export const createFolder = async (name: string, parentPath?: string): Promise<{ folder: any }> => {
  return apiClient.post('/media/folders', { name, parentPath });
};

export const deleteFolder = async (folderId: string): Promise<void> => {
  return apiClient.delete(`/media/folders/${folderId}`);
};

export const getSignedUploadUrl = async (filename: string, fileType: string): Promise<{ uploadUrl: string; fileUrl: string }> => {
  return apiClient.post('/media/signed-url', { filename, fileType });
};
