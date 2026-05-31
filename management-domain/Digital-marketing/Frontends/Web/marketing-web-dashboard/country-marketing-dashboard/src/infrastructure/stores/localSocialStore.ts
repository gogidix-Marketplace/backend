// Local Social Store - Zustand
import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { LocalSocialPost } from '../../domain/types';

const mockLocalSocialPosts: LocalSocialPost[] = [
  {
    id: 'local-social-001',
    content: 'Join us for our Spring Festival Sale! Up to 50% off selected items in-store and online. #SpringSale #ShopLocal',
    platform: 'instagram',
    status: 'published',
    media: [
      {
        type: 'image',
        url: 'https://example.com/images/spring-sale.jpg',
      },
    ],
    publishedAt: new Date('2026-02-18T14:00:00'),
    metrics: {
      impressions: 8750,
      engagement: 2450,
      likes: 1980,
      comments: 85,
      shares: 125,
      clicks: 3500,
    },
    country: 'US',
    createdBy: 'user-local-002',
    createdAt: new Date('2026-02-17T10:00:00'),
    updatedAt: new Date('2026-02-18T14:00:00'),
  },
  {
    id: 'local-social-002',
    content: 'Big announcement coming soon! Stay tuned for exciting news. #ComingSoon #Teaser',
    platform: 'facebook',
    status: 'scheduled',
    media: [],
    scheduledFor: new Date('2026-02-25T10:00:00'),
    metrics: {
      impressions: 0,
      engagement: 0,
      likes: 0,
      comments: 0,
      shares: 0,
      clicks: 0,
    },
    country: 'US',
    createdBy: 'user-local-002',
    createdAt: new Date('2026-02-18T09:00:00'),
    updatedAt: new Date('2026-02-18T09:00:00'),
  },
  {
    id: 'local-social-003',
    content: 'Thank you to everyone who visited our store this week! See you soon! #Community #Appreciation',
    platform: 'instagram',
    status: 'published',
    media: [
      {
        type: 'image',
        url: 'https://example.com/images/store-team.jpg',
      },
      {
        type: 'image',
        url: 'https://example.com/images/store-customers.jpg',
      },
    ],
    publishedAt: new Date('2026-02-16T16:00:00'),
    metrics: {
      impressions: 5200,
      engagement: 1120,
      likes: 890,
      comments: 45,
      shares: 32,
      clicks: 1800,
    },
    country: 'US',
    createdBy: 'user-local-001',
    createdAt: new Date('2026-02-15T14:00:00'),
    updatedAt: new Date('2026-02-16T16:00:00'),
  },
];

interface LocalSocialState {
  posts: LocalSocialPost[];
  selectedPost: LocalSocialPost | null;
  filters: {
    platform?: string;
    status?: string;
  };
  isLoading: boolean;
  error: string | null;

  setPosts: (posts: LocalSocialPost[]) => void;
  setSelectedPost: (post: LocalSocialPost | null) => void;
  setFilters: (filters: Partial<LocalSocialState['filters']>) => void;
  addPost: (post: LocalSocialPost) => void;
  updatePost: (id: string, updates: Partial<LocalSocialPost>) => void;
  deletePost: (id: string) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  getFilteredPosts: () => LocalSocialPost[];
}

export const useLocalSocialStore = create<LocalSocialState>()(
  devtools(
    (set, get) => ({
      posts: mockLocalSocialPosts,
      selectedPost: null,
      filters: {},
      isLoading: false,
      error: null,

      setPosts: (posts) => set({ posts }),
      setSelectedPost: (post) => set({ selectedPost: post }),
      setFilters: (newFilters) =>
        set((state) => ({
          filters: { ...state.filters, ...newFilters },
        })),
      addPost: (post) =>
        set((state) => ({
          posts: [...state.posts, post],
        })),
      updatePost: (id, updates) =>
        set((state) => ({
          posts: state.posts.map((p) =>
            p.id === id ? { ...p, ...updates } : p
          ),
          selectedPost:
            state.selectedPost?.id === id
              ? { ...state.selectedPost, ...updates }
              : state.selectedPost,
        })),
      deletePost: (id) =>
        set((state) => ({
          posts: state.posts.filter((p) => p.id !== id),
          selectedPost:
            state.selectedPost?.id === id ? null : state.selectedPost,
        })),
      setLoading: (loading) => set({ isLoading: loading }),
      setError: (error) => set({ error }),

      getFilteredPosts: () => get().posts,
    }),
    { name: 'LocalSocialStore' }
  )
);
