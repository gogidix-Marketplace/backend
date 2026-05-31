// Deals Store - Zustand
// Manages deals state

import { create } from 'zustand';
import type { Deal, DealFilters, CountryPipeline } from '@domain/types';
import { dealsRepository } from '../api';

interface DealsState {
  // Data
  deals: Deal[];
  selectedDeal: Deal | null;
  pipeline: CountryPipeline | null;

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
  loadDeals: (filters?: DealFilters) => Promise<void>;
  loadDealById: (dealId: string) => Promise<void>;
  loadPipeline: () => Promise<void>;
  createDeal: (deal: Partial<Deal>) => Promise<Deal>;
  updateDeal: (dealId: string, updates: Partial<Deal>) => Promise<void>;
  deleteDeal: (dealId: string) => Promise<void>;
  updateDealStage: (dealId: string, stage: string) => Promise<void>;
  setSelectedDeal: (deal: Deal | null) => void;
  clearError: () => void;
}

export const useDealsStore = create<DealsState>()((set, get) => ({
  // Initial state
  deals: [],
  selectedDeal: null,
  pipeline: null,
  pagination: {
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 0,
  },
  isLoading: false,
  error: null,

  // Load deals
  loadDeals: async (filters?: DealFilters) => {
    set({ isLoading: true, error: null });

    try {
      const response = await dealsRepository.getDeals(filters);

      set({
        deals: response.data,
        pagination: response.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load deals',
        isLoading: false,
      });
    }
  },

  // Load deal by ID
  loadDealById: async (dealId: string) => {
    set({ isLoading: true, error: null });

    try {
      const deal = await dealsRepository.getDealById(dealId);

      set({
        selectedDeal: deal,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load deal',
        isLoading: false,
      });
    }
  },

  // Load pipeline
  loadPipeline: async () => {
    set({ isLoading: true, error: null });

    try {
      const pipeline = await dealsRepository.getPipeline();

      set({
        pipeline,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load pipeline',
        isLoading: false,
      });
    }
  },

  // Create deal
  createDeal: async (deal: Partial<Deal>) => {
    set({ isLoading: true, error: null });

    try {
      const newDeal = await dealsRepository.createDeal(deal);

      set((state) => ({
        deals: [...state.deals, newDeal],
        isLoading: false,
      }));

      return newDeal;
    } catch (error: any) {
      set({
        error: error.message || 'Failed to create deal',
        isLoading: false,
      });
      throw error;
    }
  },

  // Update deal
  updateDeal: async (dealId: string, updates: Partial<Deal>) => {
    set({ isLoading: true, error: null });

    try {
      const updatedDeal = await dealsRepository.updateDeal(dealId, updates);

      set((state) => ({
        deals: state.deals.map((deal) =>
          deal.id === dealId ? updatedDeal : deal
        ),
        selectedDeal: state.selectedDeal?.id === dealId ? updatedDeal : state.selectedDeal,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to update deal',
        isLoading: false,
      });
      throw error;
    }
  },

  // Delete deal
  deleteDeal: async (dealId: string) => {
    set({ isLoading: true, error: null });

    try {
      await dealsRepository.deleteDeal(dealId);

      set((state) => ({
        deals: state.deals.filter((deal) => deal.id !== dealId),
        selectedDeal: state.selectedDeal?.id === dealId ? null : state.selectedDeal,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to delete deal',
        isLoading: false,
      });
      throw error;
    }
  },

  // Update deal stage
  updateDealStage: async (dealId: string, stage: string) => {
    set({ isLoading: true, error: null });

    try {
      const updatedDeal = await dealsRepository.updateDealStage(dealId, stage);

      set((state) => ({
        deals: state.deals.map((deal) =>
          deal.id === dealId ? updatedDeal : deal
        ),
        selectedDeal: state.selectedDeal?.id === dealId ? updatedDeal : state.selectedDeal,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to update deal stage',
        isLoading: false,
      });
      throw error;
    }
  },

  // Set selected deal
  setSelectedDeal: (deal: Deal | null) => {
    set({ selectedDeal: deal });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
