import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './hooks/useAuth';
import LoginPage from './pages/auth/LoginPage';
import ProtectedRoute from './components/auth/ProtectedRoute';
import MainLayout from './layouts/MainLayout';
import DashboardPage from './pages/dashboard/DashboardPage';
import ContentPages from './pages/content/ContentPages';
import ContentBlog from './pages/content/ContentBlog';
import ContentPressReleases from './pages/content/ContentPressReleases';
import ContentResources from './pages/content/ContentResources';
import ProductsList from './pages/products/ProductsList';
import ProductsCategories from './pages/products/ProductsCategories';
import ProductsFeatures from './pages/products/ProductsFeatures';
import ProductsPricing from './pages/products/ProductsPricing';
import ProductsIntegrations from './pages/products/ProductsIntegrations';
import DeveloperApiDocs from './pages/developer/DeveloperApiDocs';
import DeveloperSdks from './pages/developer/DeveloperSdks';
import DeveloperCodeExamples from './pages/developer/DeveloperCodeExamples';
import CareersJobs from './pages/careers/CareersJobs';
import CareersApplications from './pages/careers/CareersApplications';
import CareersPipeline from './pages/careers/CareersPipeline';
import PartnersPrograms from './pages/partners/PartnersPrograms';
import PartnersApplications from './pages/partners/PartnersApplications';
import PartnersPortal from './pages/partners/PartnersPortal';
import LeadsDemoRequests from './pages/leads/LeadsDemoRequests';
import LeadsSalesInquiries from './pages/leads/LeadsSalesInquiries';
import LeadsSupportTickets from './pages/leads/LeadsSupportTickets';
import AnalyticsSite from './pages/analytics/AnalyticsSite';
import AnalyticsBehavior from './pages/analytics/AnalyticsBehavior';
import AnalyticsFunnels from './pages/analytics/AnalyticsFunnels';
import AnalyticsSeo from './pages/analytics/AnalyticsSeo';
import SettingsGeneral from './pages/settings/SettingsGeneral';
import SettingsUsers from './pages/settings/SettingsUsers';
import SettingsWorkflows from './pages/settings/SettingsWorkflows';
import SettingsIntegrations from './pages/settings/SettingsIntegrations';
import NotFoundPage from './pages/NotFoundPage';

function App() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return null; // or a loading spinner
  }

  return (
    <Routes>
      <Route
        path="/login"
        element={isAuthenticated ? <Navigate to="/" replace /> : <LoginPage />}
      />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<DashboardPage />} />

        {/* Content Management */}
        <Route path="content">
          <Route path="pages" element={<ContentPages />} />
          <Route path="blog" element={<ContentBlog />} />
          <Route path="press" element={<ContentPressReleases />} />
          <Route path="resources" element={<ContentResources />} />
        </Route>

        {/* Product Catalog */}
        <Route path="products">
          <Route path="list" element={<ProductsList />} />
          <Route path="categories" element={<ProductsCategories />} />
          <Route path="features" element={<ProductsFeatures />} />
          <Route path="pricing" element={<ProductsPricing />} />
          <Route path="integrations" element={<ProductsIntegrations />} />
        </Route>

        {/* Developer Resources */}
        <Route path="developer">
          <Route path="api-docs" element={<DeveloperApiDocs />} />
          <Route path="sdks" element={<DeveloperSdks />} />
          <Route path="examples" element={<DeveloperCodeExamples />} />
        </Route>

        {/* Careers */}
        <Route path="careers">
          <Route path="jobs" element={<CareersJobs />} />
          <Route path="applications" element={<CareersApplications />} />
          <Route path="pipeline" element={<CareersPipeline />} />
        </Route>

        {/* Partners */}
        <Route path="partners">
          <Route path="programs" element={<PartnersPrograms />} />
          <Route path="applications" element={<PartnersApplications />} />
          <Route path="portal" element={<PartnersPortal />} />
        </Route>

        {/* Leads */}
        <Route path="leads">
          <Route path="demo-requests" element={<LeadsDemoRequests />} />
          <Route path="sales-inquiries" element={<LeadsSalesInquiries />} />
          <Route path="support-tickets" element={<LeadsSupportTickets />} />
        </Route>

        {/* Analytics */}
        <Route path="analytics">
          <Route path="site" element={<AnalyticsSite />} />
          <Route path="behavior" element={<AnalyticsBehavior />} />
          <Route path="funnels" element={<AnalyticsFunnels />} />
          <Route path="seo" element={<AnalyticsSeo />} />
        </Route>

        {/* Settings */}
        <Route path="settings">
          <Route path="general" element={<SettingsGeneral />} />
          <Route path="users" element={<SettingsUsers />} />
          <Route path="workflows" element={<SettingsWorkflows />} />
          <Route path="integrations" element={<SettingsIntegrations />} />
        </Route>
      </Route>
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}

export default App;
