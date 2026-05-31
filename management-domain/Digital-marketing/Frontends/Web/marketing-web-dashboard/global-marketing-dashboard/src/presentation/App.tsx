import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

// Pages
import OverviewPage from './pages/OverviewPage/OverviewPage'
import CampaignsPage from './pages/CampaignsPage/CampaignsPage'
import BudgetsPage from './pages/BudgetsPage/BudgetsPage'
import AnalyticsPage from './pages/AnalyticsPage/AnalyticsPage'
import LeadsPage from './pages/LeadsPage/LeadsPage'
import BrandsPage from './pages/BrandsPage/BrandsPage'
import ContentPage from './pages/ContentPage/ContentPage'
import SocialPage from './pages/SocialPage/SocialPage'
import EmailPage from './pages/EmailPage/EmailPage'
import SEOPage from './pages/SEOPage/SEOPage'
import ReportsPage from './pages/ReportsPage/ReportsPage'
import SettingsPage from './pages/SettingsPage/SettingsPage'

// Components
import { Sidebar } from './components/layouts/Sidebar'
import { Header } from './components/layouts/Header'
import { DashboardLayout } from './components/layouts/DashboardLayout'

function App() {
  const routes = [
    { path: '/', component: OverviewPage, label: 'Overview' },
    { path: '/campaigns', component: CampaignsPage, label: 'Campaigns' },
    { path: '/budgets', component: BudgetsPage, label: 'Budgets' },
    { path: '/analytics', component: AnalyticsPage, label: 'Analytics' },
    { path: '/leads', component: LeadsPage, label: 'Leads' },
    { path: '/brands', component: BrandsPage, label: 'Brands' },
    { path: '/content', component: ContentPage, label: 'Content' },
    { path: '/social', component: SocialPage, label: 'Social' },
    { path: '/email', component: EmailPage, label: 'Email' },
    { path: '/seo', component: SEOPage, label: 'SEO' },
    { path: '/reports', component: ReportsPage, label: 'Reports' },
    { path: '/settings', component: SettingsPage, label: 'Settings' },
  ]

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<DashboardLayout sidebar={<Sidebar routes={routes} />} header={<Header />} />}>
          <Route index element={<Navigate to="/overview" replace />} />
          <Route path="overview" element={<OverviewPage />} />
          <Route path="campaigns" element={<CampaignsPage />} />
          <Route path="budgets" element={<BudgetsPage />} />
          <Route path="analytics" element={<AnalyticsPage />} />
          <Route path="leads" element={<LeadsPage />} />
          <Route path="brands" element={<BrandsPage />} />
          <Route path="content" element={<ContentPage />} />
          <Route path="social" element={<SocialPage />} />
          <Route path="email" element={<EmailPage />} />
          <Route path="seo" element={<SEOPage />} />
          <Route path="reports" element={<ReportsPage />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
