// Customers Store - Zustand
// Manages customers state

import { create } from 'zustand';
import type { Customer, CustomerFilters, CustomerAnalytics } from '@domain/types';
import { customersRepository } from '../api';

interface CustomersState {
  // Data
  customers: Customer[];
  selectedCustomer: Customer | null;
  analytics: CustomerAnalytics | null;

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
  loadCustomers: (filters?: CustomerFilters) => Promise<void>;
  loadCustomerById: (customerId: string) => Promise<void>;
  loadAnalytics: () => Promise<void>;
  createCustomer: (customer: Partial<Customer>) => Promise<Customer>;
  updateCustomer: (customerId: string, updates: Partial<Customer>) => Promise<void>;
  deleteCustomer: (customerId: string) => Promise<void>;
  setSelectedCustomer: (customer: Customer | null) => void;
  clearError: () => void;
}

export const useCustomersStore = create<CustomersState>()((set, get) => ({
  // Initial state
  customers: [],
  selectedCustomer: null,
  analytics: null,
  pagination: {
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 0,
  },
  isLoading: false,
  error: null,

  // Load customers
  loadCustomers: async (filters?: CustomerFilters) => {
    set({ isLoading: true, error: null });

    try {
      const response = await customersRepository.getCustomers(filters);

      set({
        customers: response.data,
        pagination: response.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load customers',
        isLoading: false,
      });
    }
  },

  // Load customer by ID
  loadCustomerById: async (customerId: string) => {
    set({ isLoading: true, error: null });

    try {
      const customer = await customersRepository.getCustomerById(customerId);

      set({
        selectedCustomer: customer,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load customer',
        isLoading: false,
      });
    }
  },

  // Load analytics
  loadAnalytics: async () => {
    set({ isLoading: true, error: null });

    try {
      const analytics = await customersRepository.getAnalytics();

      set({
        analytics,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load analytics',
        isLoading: false,
      });
    }
  },

  // Create customer
  createCustomer: async (customer: Partial<Customer>) => {
    set({ isLoading: true, error: null });

    try {
      const newCustomer = await customersRepository.createCustomer(customer);

      set((state) => ({
        customers: [...state.customers, newCustomer],
        isLoading: false,
      }));

      return newCustomer;
    } catch (error: any) {
      set({
        error: error.message || 'Failed to create customer',
        isLoading: false,
      });
      throw error;
    }
  },

  // Update customer
  updateCustomer: async (customerId: string, updates: Partial<Customer>) => {
    set({ isLoading: true, error: null });

    try {
      const updatedCustomer = await customersRepository.updateCustomer(customerId, updates);

      set((state) => ({
        customers: state.customers.map((customer) =>
          customer.id === customerId ? updatedCustomer : customer
        ),
        selectedCustomer: state.selectedCustomer?.id === customerId ? updatedCustomer : state.selectedCustomer,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to update customer',
        isLoading: false,
      });
      throw error;
    }
  },

  // Delete customer
  deleteCustomer: async (customerId: string) => {
    set({ isLoading: true, error: null });

    try {
      await customersRepository.deleteCustomer(customerId);

      set((state) => ({
        customers: state.customers.filter((customer) => customer.id !== customerId),
        selectedCustomer: state.selectedCustomer?.id === customerId ? null : state.selectedCustomer,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to delete customer',
        isLoading: false,
      });
      throw error;
    }
  },

  // Set selected customer
  setSelectedCustomer: (customer: Customer | null) => {
    set({ selectedCustomer: customer });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
