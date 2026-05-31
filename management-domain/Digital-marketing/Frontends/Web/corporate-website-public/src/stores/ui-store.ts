import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UIState {
  // Sidebar state
  sidebarOpen: boolean;
  setSidebarOpen: (open: boolean) => void;
  toggleSidebar: () => void;

  // Search state
  searchOpen: boolean;
  setSearchOpen: (open: boolean) => void;
  toggleSearch: () => void;

  // Mobile menu state
  mobileMenuOpen: boolean;
  setMobileMenuOpen: (open: boolean) => void;
  toggleMobileMenu: () => void;

  // Cookie consent
  cookieConsent: 'accepted' | 'declined' | null;
  setCookieConsent: (consent: 'accepted' | 'declined') => void;

  // Language
  locale: string;
  setLocale: (locale: string) => void;

  // Theme
  theme: 'light' | 'dark' | 'system';
  setTheme: (theme: 'light' | 'dark' | 'system') => void;

  // Region
  region: string;
  setRegion: (region: string) => void;
}

export const useUIStore = create<UIState>()(
  persist(
    (set) => ({
      // Sidebar
      sidebarOpen: false,
      setSidebarOpen: (open) => set({ sidebarOpen: open }),
      toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),

      // Search
      searchOpen: false,
      setSearchOpen: (open) => set({ searchOpen: open }),
      toggleSearch: () => set((state) => ({ searchOpen: !state.searchOpen })),

      // Mobile menu
      mobileMenuOpen: false,
      setMobileMenuOpen: (open) => set({ mobileMenuOpen: open }),
      toggleMobileMenu: () => set((state) => ({ mobileMenuOpen: !state.mobileMenuOpen })),

      // Cookie consent
      cookieConsent: null,
      setCookieConsent: (consent) => set({ cookieConsent: consent }),

      // Language
      locale: 'en',
      setLocale: (locale) => set({ locale }),

      // Theme
      theme: 'system',
      setTheme: (theme) => set({ theme }),

      // Region
      region: 'na',
      setRegion: (region) => set({ region }),
    }),
    {
      name: 'gogidix-ui-storage',
      partialize: (state) => ({
        cookieConsent: state.cookieConsent,
        locale: state.locale,
        theme: state.theme,
        region: state.region,
      }),
    }
  )
);
