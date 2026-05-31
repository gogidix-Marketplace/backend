// Content Store - Zustand
// Manages content state

import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { Content, ContentStatus, ContentType } from '../../domain/types';
import mockContent from '../../shared/mock-data/content.mock';

interface ContentFilters {
  status?: ContentStatus;
  type?: ContentType;
  search?: string;
  country?: string;
  author?: string;
  dateRange?: {
    start: Date;
    end: Date;
  };
}

interface ContentState {
  content: Content[];
  selectedContent: Content | null;
  filters: ContentFilters;
  isLoading: boolean;
  error: string | null;

  // Actions
  setContent: (content: Content[]) => void;
  setSelectedContent: (content: Content | null) => void;
  setFilters: (filters: Partial<ContentFilters>) => void;
  resetFilters: () => void;
  addContent: (content: Content) => void;
  updateContent: (id: string, updates: Partial<Content>) => void;
  deleteContent: (id: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Computed
  getFilteredContent: () => Content[];
  getContentById: (id: string) => Content | undefined;
}

export const useContentStore = create<ContentState>()(
  devtools(
    (set, get) => ({
      content: mockContent,
      selectedContent: null,
      filters: {},
      isLoading: false,
      error: null,

      setContent: (content) => set({ content }),

      setSelectedContent: (content) => set({ selectedContent: content }),

      setFilters: (newFilters) =>
        set((state) => ({
          filters: { ...state.filters, ...newFilters },
        })),

      resetFilters: () =>
        set({
          filters: {},
        }),

      addContent: (content) =>
        set((state) => ({
          content: [...state.content, content],
        })),

      updateContent: (id, updates) =>
        set((state) => ({
          content: state.content.map((c) =>
            c.id === id ? { ...c, ...updates } : c
          ),
          selectedContent:
            state.selectedContent?.id === id
              ? { ...state.selectedContent, ...updates }
              : state.selectedContent,
        })),

      deleteContent: (id) =>
        set((state) => ({
          content: state.content.filter((c) => c.id !== id),
          selectedContent:
            state.selectedContent?.id === id
              ? null
              : state.selectedContent,
        })),

      setLoading: (isLoading) => set({ isLoading }),

      setError: (error) => set({ error }),

      getFilteredContent: () => {
        const { content, filters } = get();
        return content.filter((item) => {
          if (filters.status && item.status !== filters.status) {
            return false;
          }
          if (filters.type && item.type !== filters.type) {
            return false;
          }
          if (
            filters.search &&
            !item.title.toLowerCase().includes(filters.search.toLowerCase()) &&
            !item.excerpt.toLowerCase().includes(filters.search.toLowerCase())
          ) {
            return false;
          }
          if (filters.country && !item.publishing.countries.includes(filters.country)) {
            return false;
          }
          if (filters.author && item.author.id !== filters.author) {
            return false;
          }
          if (filters.dateRange) {
            const publishDate = item.publishing.publishedAt || item.publishing.publishAt;
            if (!publishDate) return false;
            const filterStart = new Date(filters.dateRange.start);
            const filterEnd = new Date(filters.dateRange.end);
            if (publishDate < filterStart || publishDate > filterEnd) {
              return false;
            }
          }
          return true;
        });
      },

      getContentById: (id) => {
        return get().content.find((c) => c.id === id);
      },
    }),
    { name: 'ContentStore' }
  )
);

// Selectors
export const selectAllContent = (state: ContentState) => state.content;
export const selectPublishedContent = (state: ContentState) =>
  state.content.filter((c) => c.status === 'published');
export const selectContentByType = (type: ContentType) => (state: ContentState) =>
  state.content.filter((c) => c.type === type);
