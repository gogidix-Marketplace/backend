// Leads Store - Zustand
// Manages leads state

import { create } from 'zustand';
import type { Lead, LeadFilters, LeadAssignment } from '@domain/types';
import { leadsRepository } from '../api';

interface LeadsState {
  // Data
  leads: Lead[];
  selectedLead: Lead | null;
  assignments: LeadAssignment[];

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
  loadLeads: (filters?: LeadFilters) => Promise<void>;
  loadLeadById: (leadId: string) => Promise<void>;
  createLead: (lead: Partial<Lead>) => Promise<Lead>;
  updateLead: (leadId: string, updates: Partial<Lead>) => Promise<void>;
  deleteLead: (leadId: string) => Promise<void>;
  assignLead: (leadId: string, assignedTo: string, note?: string) => Promise<void>;
  convertLeadToDeal: (leadId: string) => Promise<void>;
  setSelectedLead: (lead: Lead | null) => void;
  clearError: () => void;
}

export const useLeadsStore = create<LeadsState>()((set, get) => ({
  // Initial state
  leads: [],
  selectedLead: null,
  assignments: [],
  pagination: {
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 0,
  },
  isLoading: false,
  error: null,

  // Load leads
  loadLeads: async (filters?: LeadFilters) => {
    set({ isLoading: true, error: null });

    try {
      const response = await leadsRepository.getLeads(filters);

      set({
        leads: response.data,
        pagination: response.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load leads',
        isLoading: false,
      });
    }
  },

  // Load lead by ID
  loadLeadById: async (leadId: string) => {
    set({ isLoading: true, error: null });

    try {
      const lead = await leadsRepository.getLeadById(leadId);

      set({
        selectedLead: lead,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load lead',
        isLoading: false,
      });
    }
  },

  // Create lead
  createLead: async (lead: Partial<Lead>) => {
    set({ isLoading: true, error: null });

    try {
      const newLead = await leadsRepository.createLead(lead);

      set((state) => ({
        leads: [...state.leads, newLead],
        isLoading: false,
      }));

      return newLead;
    } catch (error: any) {
      set({
        error: error.message || 'Failed to create lead',
        isLoading: false,
      });
      throw error;
    }
  },

  // Update lead
  updateLead: async (leadId: string, updates: Partial<Lead>) => {
    set({ isLoading: true, error: null });

    try {
      const updatedLead = await leadsRepository.updateLead(leadId, updates);

      set((state) => ({
        leads: state.leads.map((lead) =>
          lead.id === leadId ? updatedLead : lead
        ),
        selectedLead: state.selectedLead?.id === leadId ? updatedLead : state.selectedLead,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to update lead',
        isLoading: false,
      });
      throw error;
    }
  },

  // Delete lead
  deleteLead: async (leadId: string) => {
    set({ isLoading: true, error: null });

    try {
      await leadsRepository.deleteLead(leadId);

      set((state) => ({
        leads: state.leads.filter((lead) => lead.id !== leadId),
        selectedLead: state.selectedLead?.id === leadId ? null : state.selectedLead,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to delete lead',
        isLoading: false,
      });
      throw error;
    }
  },

  // Assign lead
  assignLead: async (leadId: string, assignedTo: string, note?: string) => {
    set({ isLoading: true, error: null });

    try {
      await leadsRepository.assignLead(leadId, assignedTo, note);

      set((state) => ({
        leads: state.leads.map((lead) =>
          lead.id === leadId
            ? { ...lead, assignedTo, assignedToName: state.assignedToName }
            : lead
        ),
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to assign lead',
        isLoading: false,
      });
      throw error;
    }
  },

  // Convert lead to deal
  convertLeadToDeal: async (leadId: string) => {
    set({ isLoading: true, error: null });

    try {
      await leadsRepository.convertLeadToDeal(leadId);

      set((state) => ({
        leads: state.leads.map((lead) =>
          lead.id === leadId
            ? { ...lead, status: 'CONVERTED' as const, convertedToDeal: true }
            : lead
        ),
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to convert lead',
        isLoading: false,
      });
      throw error;
    }
  },

  // Set selected lead
  setSelectedLead: (lead: Lead | null) => {
    set({ selectedLead: lead });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
