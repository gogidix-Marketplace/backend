import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter } from 'react-router-dom';
import Dashboard from '../pages/Dashboard';
import * as api from '../api/devTools';

// Mock the API
vi.mock('../api/devTools', () => ({
  getTools: vi.fn(),
  getTestStatistics: vi.fn(),
  getQueryStatistics: vi.fn(),
  getDeploymentStatistics: vi.fn(),
}));

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false },
  },
});

function renderWithProviders(component) {
  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>{component}</BrowserRouter>
    </QueryClientProvider>
  );
}

describe('Dashboard', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders dashboard title', () => {
    api.devToolsApi.getTools.mockResolvedValue({
      data: { tools: [] }
    });

    renderWithProviders(<Dashboard />);

    expect(screen.getByText('Developer Portal Dashboard')).toBeInTheDocument();
  });

  it('displays available tools', async () => {
    api.devToolsApi.getTools.mockResolvedValue({
      data: {
        tools: [
          {
            name: 'API Testing',
            description: 'Test and validate REST APIs',
            endpoint: '/api-testing',
            enabled: true
          },
          {
            name: 'Database Query',
            description: 'Execute and analyze database queries',
            endpoint: '/database',
            enabled: true
          }
        ]
      }
    });

    renderWithProviders(<Dashboard />);

    await waitFor(() => {
      expect(screen.getByText('API Testing')).toBeInTheDocument();
      expect(screen.getByText('Database Query')).toBeInTheDocument();
    });
  });

  it('shows loading state while fetching data', () => {
    api.devToolsApi.getTools.mockImplementation(() => new Promise(() => {}));

    renderWithProviders(<Dashboard />);

    // Dashboard should render even while loading
    expect(screen.getByText('Developer Portal Dashboard')).toBeInTheDocument();
  });
});
