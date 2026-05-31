// Route Constants

export const ROUTES = {
  HOME: '/',
  OVERVIEW: '/overview',
  CAMPAIGNS: '/campaigns',
  CAMPAIGN_DETAIL: '/campaigns/:id',
  CAMPAIGN_CREATE: '/campaigns/new',
  BUDGETS: '/budgets',
  BUDGET_DETAIL: '/budgets/:id',
  ANALYTICS: '/analytics',
  BRANDS: '/brands',
  BRAND_DETAIL: '/brands/:id',
  REPORTS: '/reports',
  SETTINGS: '/settings',
} as const;

export const ROUTE_TITLES: Record<keyof typeof ROUTES, string> = {
  HOME: 'Home',
  OVERVIEW: 'Overview',
  CAMPAIGNS: 'Campaigns',
  CAMPAIGN_DETAIL: 'Campaign Details',
  CAMPAIGN_CREATE: 'Create Campaign',
  BUDGETS: 'Budgets',
  BUDGET_DETAIL: 'Budget Details',
  ANALYTICS: 'Analytics',
  BRANDS: 'Brands',
  BRAND_DETAIL: 'Brand Details',
  REPORTS: 'Reports',
  SETTINGS: 'Settings',
};
