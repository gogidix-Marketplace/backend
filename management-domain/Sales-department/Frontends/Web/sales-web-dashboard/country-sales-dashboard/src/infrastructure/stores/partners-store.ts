// Partners Store - Zustand
// Manages partners state

import { create } from 'zustand';
import type { CountryPartner, PartnerApplication, PartnerFilters, PartnerCommission } from '@domain/types';
import { partnersRepository } from '../api';

interface PartnersState {
  // Data
  partners: CountryPartner[];
  applications: PartnerApplication[];
  selectedPartner: CountryPartner | null;
  selectedApplication: PartnerApplication | null;
  commissions: PartnerCommission[];

  // Pagination
  pagination: {
    page: number;
    pageSize: number;
    totalItems: number;
    totalPages: number;
  };

  // UI State
  isLoading: boolean;
  error: string | null;

  // Actions
  loadPartners: (filters?: PartnerFilters) => Promise<void>;
  loadPartnerById: (partnerId: string) => Promise<void>;
  loadApplications: (filters?: PartnerFilters) => Promise<void>;
  loadApplicationById: (applicationId: string) => Promise<void>;
  approveApplication: (applicationId: string, partnerId?: string) => Promise<void>;
  rejectApplication: (applicationId: string, reason: string) => Promise<void>;
  loadCommissions: (period?: string) => Promise<void>;
  setSelectedPartner: (partner: CountryPartner | null) => void;
  setSelectedApplication: (application: PartnerApplication | null) => void;
  clearError: () => void;
}

export const usePartnersStore = create<PartnersState>()((set, get) => ({
  // Initial state
  partners: [],
  applications: [],
  selectedPartner: null,
  selectedApplication: null,
  commissions: [],
  pagination: {
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 0,
  },
  isLoading: false,
  error: null,

  // Load partners
  loadPartners: async (filters?: PartnerFilters) => {
    set({ isLoading: true, error: null });

    try {
      const response = await partnersRepository.getPartners(filters);

      set({
        partners: response.data,
        pagination: response.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load partners',
        isLoading: false,
      });
    }
  },

  // Load partner by ID
  loadPartnerById: async (partnerId: string) => {
    set({ isLoading: true, error: null });

    try {
      const partner = await partnersRepository.getPartnerById(partnerId);

      set({
        selectedPartner: partner,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load partner',
        isLoading: false,
      });
    }
  },

  // Load applications
  loadApplications: async (filters?: PartnerFilters) => {
    set({ isLoading: true, error: null });

    try {
      const response = await partnersRepository.getApplications(filters);

      set({
        applications: response.data,
        pagination: response.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load applications',
        isLoading: false,
      });
    }
  },

  // Load application by ID
  loadApplicationById: async (applicationId: string) => {
    set({ isLoading: true, error: null });

    try {
      const application = await partnersRepository.getApplicationById(applicationId);

      set({
        selectedApplication: application,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load application',
        isLoading: false,
      });
    }
  },

  // Approve application
  approveApplication: async (applicationId: string, partnerId?: string) => {
    set({ isLoading: true, error: null });

    try {
      await partnersRepository.approveApplication(applicationId, partnerId);

      set((state) => ({
        applications: state.applications.map((app) =>
          app.id === applicationId
            ? { ...app, status: 'APPROVED' as const }
            : app
        ),
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to approve application',
        isLoading: false,
      });
      throw error;
    }
  },

  // Reject application
  rejectApplication: async (applicationId: string, reason: string) => {
    set({ isLoading: true, error: null });

    try {
      await partnersRepository.rejectApplication(applicationId, reason);

      set((state) => ({
        applications: state.applications.map((app) =>
          app.id === applicationId
            ? { ...app, status: 'REJECTED' as const, rejectionReason: reason }
            : app
        ),
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to reject application',
        isLoading: false,
      });
      throw error;
    }
  },

  // Load commissions
  loadCommissions: async (period?: string) => {
    set({ isLoading: true, error: null });

    try {
      const commissions = await partnersRepository.getCommissions(period);

      set({
        commissions,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load commissions',
        isLoading: false,
      });
    }
  },

  // Set selected partner
  setSelectedPartner: (partner: CountryPartner | null) => {
    set({ selectedPartner: partner });
  },

  // Set selected application
  setSelectedApplication: (application: PartnerApplication | null) => {
    set({ selectedApplication: application });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
