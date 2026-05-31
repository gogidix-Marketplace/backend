export const SITE_NAME = 'Gogidix';
export const SITE_URL = process.env.NEXT_PUBLIC_SITE_URL || 'https://gogidix.com';
export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'https://api.gogidix.com';

export const LOCALES = [
  { code: 'en', name: 'English', flag: '🇺🇸' },
  { code: 'fr', name: 'Français', flag: '🇫🇷' },
  { code: 'es', name: 'Español', flag: '🇪🇸' },
  { code: 'pt', name: 'Português', flag: '🇧🇷' },
  { code: 'ar', name: 'العربية', flag: '🇸🇦' },
] as const;

export const REGIONS = [
  { code: 'na', name: 'North America', currency: 'USD' },
  { code: 'eu', name: 'Europe', currency: 'EUR' },
  { code: 'apac', name: 'Asia Pacific', currency: 'SGD' },
  { code: 'latam', name: 'Latin America', currency: 'BRL' },
  { code: 'mea', name: 'Middle East & Africa', currency: 'AED' },
] as const;

export const PRODUCTS = [
  {
    id: 'logistics',
    name: 'Logistics Management',
    slug: 'logistics',
    icon: 'Truck',
    description: 'End-to-end supply chain optimization',
    features: [
      'Real-time tracking',
      'Route optimization',
      'Inventory management',
      'Analytics & reporting',
    ],
  },
  {
    id: 'ecommerce',
    name: 'E-commerce Platform',
    slug: 'ecommerce',
    icon: 'ShoppingCart',
    description: 'Build and scale your online store',
    features: [
      'Headless architecture',
      '100+ payment gateways',
      'Product catalog',
      'Multi-channel selling',
    ],
  },
  {
    id: 'procurement',
    name: 'Procurement Suite',
    slug: 'procurement',
    icon: 'FileText',
    description: 'Streamline purchasing and spend management',
    features: [
      'Automated POs',
      'Vendor management',
      'Punch-out catalogs',
      'Spend analytics',
    ],
  },
  {
    id: 'operations',
    name: 'Business Operations',
    slug: 'operations',
    icon: 'Settings',
    description: 'Automate and optimize your workflows',
    features: [
      'Workflow automation',
      'Project management',
      'Custom dashboards',
      'Team collaboration',
    ],
  },
  {
    id: 'enterprise',
    name: 'Enterprise Solutions',
    slug: 'enterprise',
    icon: 'Building',
    description: 'Scalable platform for large organizations',
    features: [
      'Enterprise security',
      'SSO & SAML',
      'Compliance (SOC 2, GDPR)',
      '24/7 support',
    ],
  },
  {
    id: 'infrastructure',
    name: 'Infrastructure',
    slug: 'infrastructure',
    icon: 'Server',
    description: 'Cloud-native, scalable foundation',
    features: [
      'Multi-cloud deployment',
      'Global CDN',
      '99.99% uptime SLA',
      'Disaster recovery',
    ],
  },
] as const;

export const INDUSTRIES = [
  { id: 'retail', name: 'Retail & E-commerce', icon: 'ShoppingBag' },
  { id: 'manufacturing', name: 'Manufacturing', icon: 'Wrench' },
  { id: 'healthcare', name: 'Healthcare', icon: 'HeartPulse' },
  { id: 'food', name: 'Food & Beverage', icon: 'Apple' },
  { id: 'automotive', name: 'Automotive', icon: 'Car' },
  { id: 'technology', name: 'Technology', icon: 'Cpu' },
] as const;

export const SOCIAL_LINKS = {
  twitter: 'https://twitter.com/gogidix',
  linkedin: 'https://linkedin.com/company/gogidix',
  github: 'https://github.com/gogidix',
  youtube: 'https://youtube.com/@gogidix',
} as const;

export const FOOTER_LINKS = {
  products: [
    { name: 'Logistics', href: '/products/logistics' },
    { name: 'E-commerce', href: '/products/ecommerce' },
    { name: 'Procurement', href: '/products/procurement' },
    { name: 'Operations', href: '/products/operations' },
    { name: 'Enterprise', href: '/products/enterprise' },
    { name: 'Infrastructure', href: '/products/infrastructure' },
  ],
  solutions: [
    { name: 'By Industry', href: '/solutions/industry' },
    { name: 'By Company Size', href: '/solutions/size' },
    { name: 'By Region', href: '/solutions/region' },
    { name: 'Case Studies', href: '/solutions/case-studies' },
  ],
  developers: [
    { name: 'API Reference', href: '/developers/api' },
    { name: 'SDKs', href: '/developers/sdks' },
    { name: 'Webhooks', href: '/developers/webhooks' },
    { name: 'Sandbox', href: '/developers/sandbox' },
  ],
  partners: [
    { name: 'White-Label', href: '/partners/white-label' },
    { name: 'Technology Partners', href: '/partners/technology' },
    { name: 'System Integrators', href: '/partners/integrators' },
    { name: 'Partner Directory', href: '/partners/directory' },
  ],
  company: [
    { name: 'About Us', href: '/company/about' },
    { name: 'Leadership', href: '/company/leadership' },
    { name: 'Careers', href: '/company/careers' },
    { name: 'Press', href: '/company/press' },
    { name: 'Contact', href: '/company/contact' },
  ],
  resources: [
    { name: 'Documentation', href: '/resources/docs' },
    { name: 'Blog', href: '/resources/blog' },
    { name: 'Webinars', href: '/resources/webinars' },
    { name: 'Security', href: '/resources/security' },
  ],
  legal: [
    { name: 'Privacy Policy', href: '/legal/privacy' },
    { name: 'Terms of Service', href: '/legal/terms' },
    { name: 'Cookie Policy', href: '/legal/cookies' },
    { name: 'Security', href: '/resources/security' },
  ],
} as const;
