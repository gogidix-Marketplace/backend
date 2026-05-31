// ============================================
// DIGITAL MARKETING - ZUSTAND STORE
// ============================================

import { create } from 'zustand'
import { persist } from 'zustand/middleware'

// Types
export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: 'MARKETING_MANAGER' | 'CONTENT_MANAGER' | 'SOCIAL_MEDIA_MANAGER' | 'SEO_SPECIALIST' | 'DIRECTOR'
  department: string
  country: string
}

export interface Campaign {
  id: string
  name: string
  type: 'email' | 'social' | 'paid' | 'content' | 'seo'
  status: 'draft' | 'active' | 'paused' | 'completed'
  budget: number
  spent: number
  startDate: string
  endDate: string
  clicks: number
  impressions: number
  conversions: number
  ctr: number
  cpc: number
}

export interface ContentItem {
  id: string
  title: string
  type: 'blog' | 'social' | 'email' | 'video' | 'infographic'
  status: 'draft' | 'review' | 'published' | 'scheduled'
  author: string
  publishedAt?: string
  scheduledAt?: string
  views: number
  likes: number
  shares: number
}

export interface SocialPost {
  id: string
  platform: 'twitter' | 'facebook' | 'linkedin' | 'instagram' | 'tiktok'
  content: string
  status: 'draft' | 'scheduled' | 'posted'
  scheduledAt?: string
  postedAt?: string
  likes: number
  comments: number
  shares: number
  reach: number
}

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
}

interface MarketingState {
  campaigns: Campaign[]
  content: ContentItem[]
  socialPosts: SocialPost[]
  loading: boolean
  loadCampaigns: () => Promise<void>
  loadContent: () => Promise<void>
  loadSocialPosts: () => Promise<void>
}

// Mock Marketing Users
const MOCK_MARKETING_USERS: Record<string, User> = {
  'marketing.manager@gogidix.com': {
    id: 'usr-mkt-001',
    email: 'marketing.manager@gogidix.com',
    firstName: 'Amanda',
    lastName: 'Peters',
    displayName: 'Amanda Peters',
    role: 'MARKETING_MANAGER',
    department: 'digital-marketing',
    country: 'GB',
  },
  'content.manager@gogidix.com': {
    id: 'usr-mkt-002',
    email: 'content.manager@gogidix.com',
    firstName: 'Lisa',
    lastName: 'Brown',
    displayName: 'Lisa Brown',
    role: 'CONTENT_MANAGER',
    department: 'digital-marketing',
    country: 'US',
  },
  'social.manager@gogidix.com': {
    id: 'usr-mkt-003',
    email: 'social.manager@gogidix.com',
    firstName: 'Mike',
    lastName: 'Johnson',
    displayName: 'Mike Johnson',
    role: 'SOCIAL_MEDIA_MANAGER',
    department: 'digital-marketing',
    country: 'NG',
  },
}

const MOCK_PASSWORD = 'password123'

// Auth Store
export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,

      login: async (email: string, password: string) => {
        await new Promise((resolve) => setTimeout(resolve, 500))

        const mockUser = MOCK_MARKETING_USERS[email.toLowerCase()]

        if (!mockUser || password !== MOCK_PASSWORD) {
          throw new Error('Invalid credentials')
        }

        set({ user: mockUser, isAuthenticated: true })
      },

      logout: () => {
        set({ user: null, isAuthenticated: false })
      },
    }),
    {
      name: 'gogidix-marketing-auth-storage',
    }
  )
)

// Marketing Store
export const useMarketingStore = create<MarketingState>()((set, get) => ({
  campaigns: [],
  content: [],
  socialPosts: [],
  loading: false,

  loadCampaigns: async () => {
    set({ loading: true })
    await new Promise((resolve) => setTimeout(resolve, 500))
    set({
      campaigns: [
        {
          id: '1',
          name: 'Q1 Brand Awareness',
          type: 'paid',
          status: 'active',
          budget: 50000,
          spent: 32500,
          startDate: '2025-01-01',
          endDate: '2025-03-31',
          clicks: 12500,
          impressions: 250000,
          conversions: 450,
          ctr: 5.0,
          cpc: 2.6,
        },
        {
          id: '2',
          name: 'Product Launch Email',
          type: 'email',
          status: 'active',
          budget: 5000,
          spent: 3200,
          startDate: '2025-02-01',
          endDate: '2025-02-28',
          clicks: 8500,
          impressions: 42000,
          conversions: 680,
          ctr: 20.2,
          cpc: 0.38,
        },
        {
          id: '3',
          name: 'Social Media Content',
          type: 'social',
          status: 'active',
          budget: 15000,
          spent: 8900,
          startDate: '2025-01-01',
          endDate: '2025-03-31',
          clicks: 22000,
          impressions: 450000,
          conversions: 890,
          ctr: 4.9,
          cpc: 0.40,
        },
      ],
      loading: false,
    })
  },

  loadContent: async () => {
    set({ loading: true })
    await new Promise((resolve) => setTimeout(resolve, 500))
    set({
      content: [
        {
          id: '1',
          title: '10 Tips for Business Growth',
          type: 'blog',
          status: 'published',
          author: 'Lisa Brown',
          publishedAt: '2025-03-10',
          views: 1250,
          likes: 89,
          shares: 34,
        },
        {
          id: '2',
          title: 'Product Announcement Video',
          type: 'video',
          status: 'published',
          author: 'Mike Johnson',
          publishedAt: '2025-03-08',
          views: 5600,
          likes: 234,
          shares: 156,
        },
        {
          id: '3',
          title: 'Industry Trends Infographic',
          type: 'infographic',
          status: 'review',
          author: 'Lisa Brown',
          views: 0,
          likes: 0,
          shares: 0,
        },
      ],
      loading: false,
    })
  },

  loadSocialPosts: async () => {
    set({ loading: true })
    await new Promise((resolve) => setTimeout(resolve, 500))
    set({
      socialPosts: [],
      loading: false,
    })
  },
}))
