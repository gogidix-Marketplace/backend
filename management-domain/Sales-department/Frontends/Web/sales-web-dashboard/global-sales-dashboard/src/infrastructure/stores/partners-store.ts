// Partners Store - Zustand
// Manages sales team partners state

import { create } from 'zustand';
import type {
  GlobalPartnersSummary,
  SalesTeamPartner,
  PartnerApplication,
  ApplicationStatus,
  PartnerType,
  PartnerStatus,
  PartnerTier,
  PartnerFilters,
  PaginationInfo,
} from '@domain/types';
import { partnersRepository } from '../api';

interface PartnersState {
  // Data
  summary: GlobalPartnersSummary | null;
  partners: SalesTeamPartner[];
  applications: PartnerApplication[];
  selectedApplication: PartnerApplication | null;

  // UI State
  filters: PartnerFilters;
  pagination: PaginationInfo;
  isLoading: boolean;
  isSubmitting: boolean;
  error: string | null;

  // Actions
  loadPartnersSummary: (period?: string) => Promise<void>;
  loadPartners: (filters?: PartnerFilters) => Promise<void>;
  loadPartnerApplications: (filters?: { status?: ApplicationStatus[] }) => Promise<void>;
  reviewApplication: (id: string, action: 'APPROVE' | 'REJECT', data?: any) => Promise<void>;
  setSelectedApplication: (application: PartnerApplication | null) => void;
  setFilters: (filters: Partial<PartnerFilters>) => void;
  clearError: () => void;
}

export const usePartnersStore = create<PartnersState>()((set, get) => ({
  // Initial state
  summary: null,
  partners: [],
  applications: [],
  selectedApplication: null,
  filters: {
    page: 1,
    pageSize: 20,
    sortBy: 'createdAt',
    sortOrder: 'desc',
  },
  pagination: {
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 0,
    hasNext: false,
    hasPrevious: false,
  },
  isLoading: false,
  isSubmitting: false,
  error: null,

  // Load partners summary
  loadPartnersSummary: async (period?: string) => {
    set({ isLoading: true, error: null });

    try {
      const summary = await partnersRepository.getPartnersSummary(period);
      set({ summary, isLoading: false });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load partners summary', isLoading: false });
    }
  },

  // Load partners
  loadPartners: async (filters?: PartnerFilters) => {
    set({ isLoading: true, error: null });

    try {
      const mergedFilters = { ...get().filters, ...filters };
      const result = await partnersRepository.getPartners(mergedFilters);

      set({
        partners: result.items,
        pagination: result.pagination,
        filters: mergedFilters,
        isLoading: false,
      });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load partners', isLoading: false });
    }
  },

  // Load partner applications
  loadPartnerApplications: async (filters?: { status?: ApplicationStatus[] }) => {
    set({ isLoading: true, error: null });

    try {
      const result = await partnersRepository.getPartnerApplications({
        ...filters,
        page: get().filters.page,
        pageSize: get().filters.pageSize,
      });

      set({
        applications: result.items,
        pagination: result.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load applications', isLoading: false });
    }
  },

  // Review application
  reviewApplication: async (id: string, action: 'APPROVE' | 'REJECT', data?: any) => {
    set({ isSubmitting: true, error: null });

    try {
      const application = await partnersRepository.reviewApplication(id, action, data);

      // Update the applications list
      const applications = get().applications.map(app =>
        app.id === id ? application : app
      );

      set({
        applications,
        selectedApplication: null,
        isSubmitting: false,
      });

      // Reload applications to get fresh data
      await get().loadPartnerApplications();
    } catch (error: any) {
      set({ error: error.message || 'Failed to review application', isSubmitting: false });
      throw error;
    }
  },

  // Set selected application
  setSelectedApplication: (application: PartnerApplication | null) => {
    set({ selectedApplication: application });
  },

  // Set filters
  setFilters: (filters: Partial<PartnerFilters>) => {
    set({ filters: { ...get().filters, ...filters } });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
