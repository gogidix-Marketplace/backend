import { create } from 'zustand';

interface UiState {
  sidebarOpen: boolean;
  mobileSidebarOpen: boolean;
  selectedMediaFiles: string[];
  selectedContentType: string;
  setSidebarOpen: (open: boolean) => void;
  setMobileSidebarOpen: (open: boolean) => void;
  toggleSidebar: () => void;
  toggleMobileSidebar: () => void;
  setSelectedMediaFiles: (files: string[]) => void;
  setSelectedContentType: (type: string) => void;
}

export const useUiStore = create<UiState>((set) => ({
  sidebarOpen: true,
  mobileSidebarOpen: false,
  selectedMediaFiles: [],
  selectedContentType: 'all',
  setSidebarOpen: (open) => set({ sidebarOpen: open }),
  setMobileSidebarOpen: (open) => set({ mobileSidebarOpen: open }),
  toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),
  toggleMobileSidebar: () => set((state) => ({ mobileSidebarOpen: !state.mobileSidebarOpen })),
  setSelectedMediaFiles: (files) => set({ selectedMediaFiles: files }),
  setSelectedContentType: (type) => set({ selectedContentType: type }),
}));
