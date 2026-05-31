import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './presentation/App'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import '@shared/styles/globals.css'

try {
  const old = localStorage.getItem('ceo-auth-storage')
  if (old) {
    const parsed = JSON.parse(old)
    if (parsed?.state?.isAuthenticated) {
      const keys = ['ceo-auth-storage', 'cfo-auth-storage', 'coo-auth-storage', 'cto-auth-storage']
      keys.forEach(k => localStorage.removeItem(k))
    }
  }
} catch {}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5, // 5 minutes
      gcTime: 1000 * 60 * 30, // 30 minutes
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
})

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  </React.StrictMode>,
)
