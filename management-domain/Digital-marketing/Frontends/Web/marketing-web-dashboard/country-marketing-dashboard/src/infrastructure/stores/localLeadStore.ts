// Local Lead Store - Zustand
import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { LocalLead } from '../../domain/types';

const mockLocalLeads: LocalLead[] = [
  {
    id: 'local-lead-001',
    firstName: 'Alice',
    lastName: 'Johnson',
    email: 'alice.johnson@example.com',
    phone: '+1-555-0123-4567',
    company: 'Johnson Enterprises',
    jobTitle: 'Marketing Director',
    status: 'new',
    source: 'Spring Festival Campaign',
    score: 85,
    value: {
      estimated: 15000,
      currency: 'USD',
    },
    activities: [],
    country: 'US',
    assignedTo: {
      id: 'sales-001',
      name: 'Bob Wilson',
    },
    handoffToSales: {
      salesUserId: 'sales-001',
      salesUserName: 'Bob Wilson',
      handedOffAt: new Date('2026-02-18T10:00:00'),
      status: 'accepted',
    },
    createdAt: new Date('2026-02-18T10:00:00'),
    updatedAt: new Date('2026-02-18T10:00:00'),
  },
  {
    id: 'local-lead-002',
    firstName: 'Bob',
    lastName: 'Martinez',
    email: 'b.martinez@example.com',
    phone: '+1-555-987-6543',
    company: 'Martinez Inc',
    jobTitle: 'CEO',
    status: 'contacted',
    source: 'Website',
    score: 92,
    value: {
      estimated: 75000,
      currency: 'USD',
    },
    activities: [],
    country: 'US',
    createdAt: new Date('2026-02-17T14:00:00'),
    updatedAt: new Date('2026-02-18T09:00:00'),
  },
];

interface LocalLeadState {
  leads: LocalLead[];
  selectedLead: LocalLead | null;
  filters: {
    status?: string[];
    score?: string[];
    source?: string[];
  };
  isLoading: boolean;
  error: string | null;

  setLeads: (leads: LocalLead[]) => void;
  setSelectedLead: (lead: LocalLead | null) => void;
  setFilters: (filters: Partial<LocalLeadState['filters']>) => void;
  addLead: (lead: LocalLead) => void;
  updateLead: (id: string, updates: Partial<LocalLead>) => void;
  deleteLead: (id: string) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  handoffToSales: (leadId: string, salesUserId: string) => void;
}

export const useLocalLeadStore = create<LocalLeadState>()(
  devtools(
    (set, get) => ({
      leads: mockLocalLeads,
      selectedLead: null,
      filters: {},
      isLoading: false,
      error: null,

      setLeads: (leads) => set({ leads }),
      setSelectedLead: (lead) => set({ selectedLead: lead }),
      setFilters: (newFilters) =>
        set((state) => ({
          filters: { ...state.filters, ...newFilters },
        })),
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
            state.selectedLead?.id === id ? null : state.selectedLead,
        })),
      setLoading: (loading) => set({ isLoading: loading }),
      setError: (error) => set({ error }),
      handoffToSales: (leadId, salesUserId) =>
        set((state) => ({
          leads: state.leads.map((l) =>
            l.id === leadId
              ? {
                  ...l,
                  handoffToSales: {
                    salesUserId,
                    salesUserName: 'Sales User',
                    handedOffAt: new Date(),
                    status: 'pending',
                  },
                }
              : l
          ),
        })),
    }),
    { name: 'LocalLeadStore' }
  )
);
