// Social Store - Zustand
// Manages social media posts state

import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { SocialPost, SocialPlatform, SocialPostStatus } from '../../domain/types';
import mockSocialPosts from '../../shared/mock-data/social.mock';

interface SocialFilters {
  platform?: SocialPlatform;
  status?: SocialPostStatus;
  search?: string;
  country?: string;
  campaignId?: string;
  dateRange?: {
    start: Date;
    end: Date;
  };
}

interface SocialState {
  posts: SocialPost[];
  selectedPost: SocialPost | null;
  filters: SocialFilters;
  isLoading: boolean;
  error: string | null;

  // Actions
  setPosts: (posts: SocialPost[]) => void;
  setSelectedPost: (post: SocialPost | null) => void;
  setFilters: (filters: Partial<SocialFilters>) => void;
  resetFilters: () => void;
  addPost: (post: SocialPost) => void;
  updatePost: (id: string, updates: Partial<SocialPost>) => void;
  deletePost: (id: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Computed
  getFilteredPosts: () => SocialPost[];
  getPostById: (id: string) => SocialPost | undefined;
  getMetrics: () => {
    totalPosts: number;
    published: number;
    scheduled: number;
    drafts: number;
    totalEngagement: number;
    totalImpressions: number;
    avgEngagementRate: number;
  };
}

export const useSocialStore = create<SocialState>()(
  devtools(
    (set, get) => ({
      posts: mockSocialPosts,
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

      resetFilters: () =>
        set({
          filters: {},
        }),

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
            state.selectedPost?.id === id
              ? null
              : state.selectedPost,
        })),

      setLoading: (isLoading) => set({ isLoading }),

      setError: (error) => set({ error }),

      getFilteredPosts: () => {
        const { posts, filters } = get();
        return posts.filter((post) => {
          if (filters.platform && post.platform !== filters.platform) {
            return false;
          }
          if (filters.status && post.status !== filters.status) {
            return false;
          }
          if (
            filters.search &&
            !post.content.toLowerCase().includes(filters.search.toLowerCase())
          ) {
            return false;
          }
          if (filters.country && post.country !== filters.country) {
            return false;
          }
          if (filters.campaignId && post.campaignId !== filters.campaignId) {
            return false;
          }
          if (filters.dateRange) {
            const postDate = post.publishedAt || post.scheduledFor || post.createdAt;
            const filterStart = new Date(filters.dateRange.start);
            const filterEnd = new Date(filters.dateRange.end);
            if (postDate < filterStart || postDate > filterEnd) {
              return false;
            }
          }
          return true;
        });
      },

      getPostById: (id) => {
        return get().posts.find((p) => p.id === id);
      },

      getMetrics: () => {
        const posts = get().posts;
        const published = posts.filter(p => p.status === 'published');
        const scheduled = posts.filter(p => p.status === 'scheduled');
        const drafts = posts.filter(p => p.status === 'draft');

        const totalEngagement = published.reduce((sum, p) => sum + p.metrics.engagement, 0);
        const totalImpressions = published.reduce((sum, p) => sum + p.metrics.impressions, 0);
        const avgEngagementRate = totalImpressions > 0
          ? (totalEngagement / totalImpressions) * 100
          : 0;

        return {
          totalPosts: posts.length,
          published: published.length,
          scheduled: scheduled.length,
          drafts: drafts.length,
          totalEngagement,
          totalImpressions,
          avgEngagementRate,
        };
      },
    }),
    { name: 'SocialStore' }
  )
);

// Selectors
export const selectAllPosts = (state: SocialState) => state.posts;
export const selectPublishedPosts = (state: SocialState) =>
  state.posts.filter((p) => p.status === 'published');
export const selectPostsByPlatform = (platform: SocialPlatform) => (state: SocialState) =>
  state.posts.filter((p) => p.platform === platform);
