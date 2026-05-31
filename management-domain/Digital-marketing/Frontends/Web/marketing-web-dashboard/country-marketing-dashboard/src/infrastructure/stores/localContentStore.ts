// Local Content Store - Zustand
import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { LocalContent, LocalContentStatus } from '../../domain/types';

const mockLocalContent: LocalContent[] = [
  {
    id: 'local-content-001',
    title: 'Spring Collection Launch Blog Post',
    type: 'blog_post',
    status: 'published',
    content: 'Full blog post content...',
    excerpt: 'Introducing our new spring collection with exclusive offers.',
    author: {
      id: 'user-local-001',
      name: 'Jane Doe',
      email: 'jane.doe@gogidix.com',
    },
    country: {
      code: 'US',
      name: 'United States',
    },
    publishing: {
      publishedAt: new Date('2026-02-15T10:00:00'),
      channels: ['blog', 'social'],
    },
    metrics: {
      views: 4500,
      clicks: 890,
      shares: 125,
      likes: 340,
      leads: 156,
    },
    approval: {
      status: 'approved',
      approvedBy: 'manager-001',
      approvedAt: new Date('2026-02-14T14:00:00'),
    },
    createdAt: new Date('2026-02-10T09:00:00'),
    updatedAt: new Date('2026-02-15T10:00:00'),
  },
  {
    id: 'local-content-002',
    title: 'Instagram Post - Spring Sale',
    type: 'social_post',
    status: 'published',
    content: 'Instagram post content...',
    excerpt: 'Check out our amazing spring sale!',
    author: {
      id: 'user-local-002',
      name: 'Mike Johnson',
      email: 'mike.johnson@gogidix.com',
    },
    country: {
      code: 'US',
      name: 'United States',
    },
    publishing: {
      publishedAt: new Date('2026-02-18T14:00:00'),
      channels: ['instagram', 'facebook'],
    },
    metrics: {
      views: 12500,
      clicks: 2500,
      shares: 380,
      likes: 2100,
      leads: 425,
    },
    approval: {
      status: 'approved',
      approvedBy: 'manager-001',
      approvedAt: new Date('2026-02-17T10:00:00'),
    },
    createdAt: new Date('2026-02-15T09:00:00'),
    updatedAt: new Date('2026-02-18T14:00:00'),
  },
  {
    id: 'local-content-003',
    title: 'March Event Email Invitation',
    type: 'email',
    status: 'scheduled',
    content: 'Email content...',
    excerpt: 'You\'re invited to our special event!',
    author: {
      id: 'user-local-001',
      name: 'Jane Doe',
      email: 'jane.doe@gogidix.com',
    },
    country: {
      code: 'US',
      name: 'United States',
    },
    publishing: {
      scheduledFor: new Date('2026-03-01T10:00:00'),
      channels: ['email'],
    },
    metrics: {
      views: 0,
      clicks: 0,
      shares: 0,
      likes: 0,
      leads: 0,
    },
    approval: {
      status: 'approved',
      approvedBy: 'manager-001',
      approvedAt: new Date('2026-02-18T16:00:00'),
    },
    createdAt: new Date('2026-02-18T09:00:00'),
    updatedAt: new Date('2026-02-18T16:00:00'),
  },
];

interface LocalContentState {
  content: LocalContent[];
  selectedContent: LocalContent | null;
  filters: {
    status?: LocalContentStatus[];
    search?: string;
    type?: string[];
  };
  isLoading: boolean;
  error: string | null;

  setContent: (content: LocalContent[]) => void;
  setSelectedContent: (content: LocalContent | null) => void;
  setFilters: (filters: Partial<LocalContentState['filters']>) => void;
  addContent: (content: LocalContent) => void;
  updateContent: (id: string, updates: Partial<LocalContent>) => void;
  deleteContent: (id: string) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  getFilteredContent: () => LocalContent[];
}

export const useLocalContentStore = create<LocalContentState>()(
  devtools(
    (set, get) => ({
      content: mockLocalContent,
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
            state.selectedContent?.id === id ? null : state.selectedContent,
        })),
      setLoading: (loading) => set({ isLoading: loading }),
      setError: (error) => set({ error }),

      getFilteredContent: () => get().content,
    }),
    { name: 'LocalContentStore' }
  )
);
