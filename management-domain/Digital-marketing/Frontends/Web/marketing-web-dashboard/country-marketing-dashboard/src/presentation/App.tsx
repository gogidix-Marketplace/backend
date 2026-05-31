import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

// Layouts
import { DashboardLayout } from './components/layouts/DashboardLayout'
import { Sidebar } from './components/layouts/Sidebar'
import { Header } from './components/layouts/Header'

// Pages
import CountryOverviewPage from './pages/CountryOverviewPage/CountryOverviewPage'
import LocalCampaignsPage from './pages/LocalCampaignsPage/LocalCampaignsPage'
import LocalBudgetPage from './pages/LocalBudgetPage/LocalBudgetPage'
import SocialMediaPage from './pages/SocialMediaPage/SocialMediaPage'
import EmailMarketingPage from './pages/EmailMarketingPage/EmailMarketingPage'
import LocalContentPage from './pages/LocalContentPage/LocalContentPage'
import LocalSEOPage from './pages/LocalSEOPage/LocalSEOPage'
import LocalLeadsPage from './pages/LocalLeadsPage/LocalLeadsPage'
import CountryReportsPage from './pages/CountryReportsPage/CountryReportsPage'
import SettingsPage from './pages/SettingsPage/SettingsPage'

function App() {
  const routes = [
    { path: '/', component: CountryOverviewPage, label: 'Overview' },
    { path: '/campaigns', component: LocalCampaignsPage, label: 'Campaigns' },
    { path: '/budget', component: LocalBudgetPage, label: 'Budget' },
    { path: '/social', component: SocialMediaPage, label: 'Social Media' },
    { path: '/email', component: EmailMarketingPage, label: 'Email' },
    { path: '/content', component: LocalContentPage, label: 'Content' },
    { path: '/seo', component: LocalSEOPage, label: 'SEO' },
    { path: '/leads', component: LocalLeadsPage, label: 'Leads' },
    { path: '/reports', component: CountryReportsPage, label: 'Reports' },
    { path: '/settings', component: SettingsPage, label: 'Settings' },
  ]

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={
          <DashboardLayout
            sidebar={<Sidebar routes={routes} />}
            header={<Header title="Country Marketing Dashboard" />}
          />
        }>
          <Route index element={<Navigate to="/overview" replace />} />
          <Route path="overview" element={<CountryOverviewPage />} />
          <Route path="campaigns" element={<LocalCampaignsPage />} />
          <Route path="budget" element={<LocalBudgetPage />} />
          <Route path="social" element={<SocialMediaPage />} />
          <Route path="email" element={<EmailMarketingPage />} />
          <Route path="content" element={<LocalContentPage />} />
          <Route path="seo" element={<LocalSEOPage />} />
          <Route path="leads" element={<LocalLeadsPage />} />
          <Route path="reports" element={<CountryReportsPage />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
