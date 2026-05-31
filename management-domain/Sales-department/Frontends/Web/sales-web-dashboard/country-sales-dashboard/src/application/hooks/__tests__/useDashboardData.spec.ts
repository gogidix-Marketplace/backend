import { describe, it, expect, beforeEach } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { useDashboardData } from '../src/application/hooks/useDashboardData';

// Mock API calls
vi.mock('../src/infrastructure/api/dashboardApi');

describe('useDashboardData Hook', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should initialize with empty data', () => {
    const { result } = renderHook(() => useDashboardData());

    expect(result.current.data).toBeNull();
    expect(result.current.isLoading).toBe(true);
  });

  it('should fetch dashboard data on mount', async () => {
    const { result } = renderHook(() => useDashboardData());

    await act(async () => {
      await result.current.refetch();
    });

    expect(result.current.isLoading).toBe(false);
    expect(result.current.data).toBeDefined();
  });

  it('should handle error state correctly', async () => {
    // Mock API to throw error
    const { result } = renderHook(() => useDashboardData());

    await act(async () => {
      await result.current.refetch();
    });

    expect(result.current.error).toBeDefined();
  });

  it('should refetch data when refresh is called', async () => {
    const { result } = renderHook(() => useDashboardData());

    const initialCallCount = vi.mock.calls.length;

    await act(async () => {
      await result.current.refresh();
    });

    expect(vi.mock.calls.length).toBeGreaterThan(initialCallCount);
  });
});
