// Teams Store - Zustand
// Manages sales teams state

import { create } from 'zustand';
import type { SalesTeam, TeamPerformance, TeamFilters } from '@domain/types';
import { teamsRepository } from '../api';

interface TeamsState {
  // Data
  teams: SalesTeam[];
  teamPerformance: TeamPerformance | null;
  selectedTeam: SalesTeam | null;

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
  loadTeams: (filters?: TeamFilters) => Promise<void>;
  loadTeamById: (teamId: string) => Promise<void>;
  loadTeamPerformance: (teamId: string, period?: string) => Promise<void>;
  createTeam: (team: Partial<SalesTeam>) => Promise<SalesTeam>;
  updateTeam: (teamId: string, updates: Partial<SalesTeam>) => Promise<void>;
  deleteTeam: (teamId: string) => Promise<void>;
  setSelectedTeam: (team: SalesTeam | null) => void;
  clearError: () => void;
}

export const useTeamsStore = create<TeamsState>()((set, get) => ({
  // Initial state
  teams: [],
  teamPerformance: null,
  selectedTeam: null,
  pagination: {
    page: 1,
    pageSize: 20,
    totalItems: 0,
    totalPages: 0,
  },
  isLoading: false,
  error: null,

  // Load teams
  loadTeams: async (filters?: TeamFilters) => {
    set({ isLoading: true, error: null });

    try {
      const response = await teamsRepository.getTeams(filters);

      set({
        teams: response.data,
        pagination: response.pagination,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load teams',
        isLoading: false,
      });
    }
  },

  // Load team by ID
  loadTeamById: async (teamId: string) => {
    set({ isLoading: true, error: null });

    try {
      const team = await teamsRepository.getTeamById(teamId);

      set({
        selectedTeam: team,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load team',
        isLoading: false,
      });
    }
  },

  // Load team performance
  loadTeamPerformance: async (teamId: string, period?: string) => {
    set({ isLoading: true, error: null });

    try {
      const performance = await teamsRepository.getTeamPerformance(teamId, period);

      set({
        teamPerformance: performance,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load team performance',
        isLoading: false,
      });
    }
  },

  // Create team
  createTeam: async (team: Partial<SalesTeam>) => {
    set({ isLoading: true, error: null });

    try {
      const newTeam = await teamsRepository.createTeam(team);

      set((state) => ({
        teams: [...state.teams, newTeam],
        isLoading: false,
      }));

      return newTeam;
    } catch (error: any) {
      set({
        error: error.message || 'Failed to create team',
        isLoading: false,
      });
      throw error;
    }
  },

  // Update team
  updateTeam: async (teamId: string, updates: Partial<SalesTeam>) => {
    set({ isLoading: true, error: null });

    try {
      const updatedTeam = await teamsRepository.updateTeam(teamId, updates);

      set((state) => ({
        teams: state.teams.map((team) =>
          team.id === teamId ? updatedTeam : team
        ),
        selectedTeam: state.selectedTeam?.id === teamId ? updatedTeam : state.selectedTeam,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to update team',
        isLoading: false,
      });
      throw error;
    }
  },

  // Delete team
  deleteTeam: async (teamId: string) => {
    set({ isLoading: true, error: null });

    try {
      await teamsRepository.deleteTeam(teamId);

      set((state) => ({
        teams: state.teams.filter((team) => team.id !== teamId),
        selectedTeam: state.selectedTeam?.id === teamId ? null : state.selectedTeam,
        isLoading: false,
      }));
    } catch (error: any) {
      set({
        error: error.message || 'Failed to delete team',
        isLoading: false,
      });
      throw error;
    }
  },

  // Set selected team
  setSelectedTeam: (team: SalesTeam | null) => {
    set({ selectedTeam: team });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
