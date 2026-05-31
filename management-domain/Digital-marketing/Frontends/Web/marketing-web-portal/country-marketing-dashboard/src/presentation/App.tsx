import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

// Components
import { CountryDashboardLayout } from './components/layouts/CountryDashboardLayout'
import { CountryOverviewPage } from './pages/CountryOverviewPage/CountryOverviewPage'
import { LocalCampaignsPage } from './pages/LocalCampaignsPage/LocalCampaignsPage'
import { LocalBudgetPage } from './pages/LocalBudgetPage/LocalBudgetPage'
import { SocialMediaPage } from './pages/SocialMediaPage/SocialMediaPage'
import { EmailMarketingPage } from './pages/EmailMarketingPage/EmailMarketingPage'
import { ContentPage } from './pages/ContentPage/ContentPage'
import { SEOPage } from './pages/SEOPage/SEOPage'
import { LeadsPage } from './pages/LeadsPage/LeadsPage'
import { ReportsPage } from './pages/ReportsPage/ReportsPage'
import { SettingsPage } from './pages/SettingsPage/SettingsPage'

import './styles/global.css'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000,
    },
  },
})

const routes = [
  { path: '/', component: CountryOverviewPage, label: 'Overview' },
  { path: '/campaigns', component: LocalCampaignsPage, label: 'Campaigns' },
  { path: '/budget', component: LocalBudgetPage, label: 'Budget' },
  { path: '/social', component: SocialMediaPage, label: 'Social Media' },
  { path: '/email', component: EmailMarketingPage, label: 'Email Marketing' },
  { path: '/content', component: ContentPage, label: 'Content' },
  { path: '/seo', component: SEOPage, label: 'SEO' },
  { path: '/leads', component: LeadsPage, label: 'Leads' },
  { path: '/reports', component: ReportsPage, label: 'Reports' },
  { path: '/settings', component: SettingsPage, label: 'Settings' },
]

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<CountryDashboardLayout routes={routes} />}>
            <Route index element={<CountryOverviewPage />} />
            <Route path="campaigns" element={<LocalCampaignsPage />} />
            <Route path="budget" element={<LocalBudgetPage />} />
            <Route path="social" element={<SocialMediaPage />} />
            <Route path="email" element={<EmailMarketingPage />} />
            <Route path="content" element={<ContentPage />} />
            <Route path="seo" element={<SEOPage />} />
            <Route path="leads" element={<LeadsPage />} />
            <Route path="reports" element={<ReportsPage />} />
            <Route path="settings" element={<SettingsPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
      {import.meta.env.DEV && <ReactQueryDevtools initialIsOpen={false} position="bottom-right" />}
    </QueryClientProvider>
  )
}

export default App
