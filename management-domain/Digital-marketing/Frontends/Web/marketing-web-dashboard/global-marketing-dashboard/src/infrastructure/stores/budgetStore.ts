// Budget Store - Zustand
// Manages budget state

import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { Budget, BudgetStatus } from '../../domain/entities/Budget.entity';
import mockBudgets from '../../shared/mock-data/budgets.mock';

interface BudgetFilters {
  status?: BudgetStatus;
  fiscalYear?: number;
  search?: string;
  country?: string;
}

interface BudgetState {
  budgets: Budget[];
  selectedBudget: Budget | null;
  filters: BudgetFilters;
  isLoading: boolean;
  error: string | null;

  // Actions
  setBudgets: (budgets: Budget[]) => void;
  setSelectedBudget: (budget: Budget | null) => void;
  setFilters: (filters: Partial<BudgetFilters>) => void;
  resetFilters: () => void;
  addBudget: (budget: Budget) => void;
  updateBudget: (id: string, updates: Partial<Budget>) => void;
  deleteBudget: (id: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Computed
  getFilteredBudgets: () => Budget[];
  getBudgetById: (id: string) => Budget | undefined;
  getTotalBudget: () => number;
  getTotalSpent: () => number;
  getRemainingBudget: () => number;
}

export const useBudgetStore = create<BudgetState>()(
  devtools(
    persist(
      (set, get) => ({
        budgets: mockBudgets,
        selectedBudget: null,
        filters: { fiscalYear: new Date().getFullYear() },
        isLoading: false,
        error: null,

        setBudgets: (budgets) => set({ budgets }),

        setSelectedBudget: (budget) => set({ selectedBudget: budget }),

        setFilters: (newFilters) =>
          set((state) => ({
            filters: { ...state.filters, ...newFilters },
          })),

        resetFilters: () =>
          set({
            filters: { fiscalYear: new Date().getFullYear() },
          }),

        addBudget: (budget) =>
          set((state) => ({
            budgets: [...state.budgets, budget],
          })),

        updateBudget: (id, updates) =>
          set((state) => ({
            budgets: state.budgets.map((b) =>
              b.id === id ? { ...b, ...updates } : b
            ),
            selectedBudget:
              state.selectedBudget?.id === id
                ? { ...state.selectedBudget, ...updates }
                : state.selectedBudget,
          })),

        deleteBudget: (id) =>
          set((state) => ({
            budgets: state.budgets.filter((b) => b.id !== id),
            selectedBudget:
              state.selectedBudget?.id === id
                ? null
                : state.selectedBudget,
          })),

        setLoading: (isLoading) => set({ isLoading }),

        setError: (error) => set({ error }),

        getFilteredBudgets: () => {
          const { budgets, filters } = get();
          return budgets.filter((budget) => {
            if (filters.status && budget.status !== filters.status) {
              return false;
            }
            if (filters.fiscalYear && budget.fiscalYear !== filters.fiscalYear) {
              return false;
            }
            if (
              filters.search &&
              !budget.name.toLowerCase().includes(filters.search.toLowerCase())
            ) {
              return false;
            }
            if (
              filters.country &&
              !budget.countries.some((c) => c.countryCode === filters.country)
            ) {
              return false;
            }
            return true;
          });
        },

        getBudgetById: (id) => {
          return get().budgets.find((b) => b.id === id);
        },

        getTotalBudget: () => {
          return get().budgets.reduce((sum, b) => sum + b.totalAmount, 0);
        },

        getTotalSpent: () => {
          return get().budgets.reduce((sum, b) => sum + b.spentAmount, 0);
        },

        getRemainingBudget: () => {
          return get().budgets.reduce((sum, b) => sum + b.remainingAmount, 0);
        },
      }),
      {
        name: 'budget-storage',
        partialize: (state) => ({
          filters: state.filters,
        }),
      }
    ),
    { name: 'BudgetStore' }
  )
);

// Selectors
export const selectAllBudgets = (state: BudgetState) => state.budgets;
export const selectActiveBudgets = (state: BudgetState) =>
  state.budgets.filter((b) => b.status === BudgetStatus.ACTIVE);
export const selectBudgetById = (id: string) => (state: BudgetState) =>
  state.budgets.find((b) => b.id === id);
