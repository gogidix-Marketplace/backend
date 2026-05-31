// Lead Store - Zustand
// Manages lead state

import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { Lead, LeadStatus, LeadQuality, LeadMetrics } from '../../domain/entities/Lead.entity';
import mockLeads, { mockLeadMetrics } from '../../shared/mock-data/leads.mock';

interface LeadFilters {
  status?: LeadStatus;
  quality?: LeadQuality;
  search?: string;
  country?: string;
  source?: string;
  dateRange?: {
    start: Date;
    end: Date;
  };
}

interface LeadState {
  leads: Lead[];
  selectedLead: Lead | null;
  metrics: LeadMetrics;
  filters: LeadFilters;
  isLoading: boolean;
  error: string | null;

  // Actions
  setLeads: (leads: Lead[]) => void;
  setSelectedLead: (lead: Lead | null) => void;
  setMetrics: (metrics: LeadMetrics) => void;
  setFilters: (filters: Partial<LeadFilters>) => void;
  resetFilters: () => void;
  addLead: (lead: Lead) => void;
  updateLead: (id: string, updates: Partial<Lead>) => void;
  deleteLead: (id: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Computed
  getFilteredLeads: () => Lead[];
  getLeadById: (id: string) => Lead | undefined;
  getLeadsByStatus: (status: LeadStatus) => Lead[];
}

export const useLeadStore = create<LeadState>()(
  devtools(
    persist(
      (set, get) => ({
        leads: mockLeads,
        selectedLead: null,
        metrics: mockLeadMetrics,
        filters: {},
        isLoading: false,
        error: null,

        setLeads: (leads) => set({ leads }),

        setSelectedLead: (lead) => set({ selectedLead: lead }),

        setMetrics: (metrics) => set({ metrics }),

        setFilters: (newFilters) =>
          set((state) => ({
            filters: { ...state.filters, ...newFilters },
          })),

        resetFilters: () =>
          set({
            filters: {},
          }),

        addLead: (lead) =>
          set((state) => ({
            leads: [...state.leads, lead],
          })),

        updateLead: (id, updates) =>
          set((state) => ({
            leads: state.leads.map((l) =>
              l.id === id ? { ...l, ...updates } : l
            ),
            selectedLead:
              state.selectedLead?.id === id
                ? { ...state.selectedLead, ...updates }
                : state.selectedLead,
          })),

        deleteLead: (id) =>
          set((state) => ({
            leads: state.leads.filter((l) => l.id !== id),
            selectedLead:
              state.selectedLead?.id === id
                ? null
                : state.selectedLead,
          })),

        setLoading: (isLoading) => set({ isLoading }),

        setError: (error) => set({ error }),

        getFilteredLeads: () => {
          const { leads, filters } = get();
          return leads.filter((lead) => {
            if (filters.status && lead.status !== filters.status) {
              return false;
            }
            if (filters.quality && lead.quality !== filters.quality) {
              return false;
            }
            if (
              filters.search &&
              !lead.firstName.toLowerCase().includes(filters.search.toLowerCase()) &&
              !lead.lastName.toLowerCase().includes(filters.search.toLowerCase()) &&
              !lead.email.toLowerCase().includes(filters.search.toLowerCase()) &&
              !lead.company?.toLowerCase().includes(filters.search.toLowerCase())
            ) {
              return false;
            }
            if (filters.country && lead.country.code !== filters.country) {
              return false;
            }
            if (filters.source && lead.source !== filters.source) {
              return false;
            }
            if (filters.dateRange) {
              const leadDate = new Date(lead.createdAt);
              const filterStart = new Date(filters.dateRange.start);
              const filterEnd = new Date(filters.dateRange.end);
              if (leadDate < filterStart || leadDate > filterEnd) {
                return false;
              }
            }
            return true;
          });
        },

        getLeadById: (id) => {
          return get().leads.find((l) => l.id === id);
        },

        getLeadsByStatus: (status) => {
          return get().leads.filter((l) => l.status === status);
        },
      }),
      {
        name: 'lead-storage',
        partialize: (state) => ({
          filters: state.filters,
        }),
      }
    ),
    { name: 'LeadStore' }
  )
);

// Selectors
export const selectAllLeads = (state: LeadState) => state.leads;
export const selectNewLeads = (state: LeadState) =>
  state.leads.filter((l) => l.status === LeadStatus.NEW);
export const selectQualifiedLeads = (state: LeadState) =>
  state.leads.filter((l) => l.status === LeadStatus.QUALIFIED);
export const selectHotLeads = (state: LeadState) =>
  state.leads.filter((l) => l.quality === LeadQuality.HOT);
export const selectLeadById = (id: string) => (state: LeadState) =>
  state.leads.find((l) => l.id === id);
