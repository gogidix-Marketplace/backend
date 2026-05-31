// Regional Dashboard Mock Data

export const regions = [
  {
    id: 'na',
    name: 'North America',
    code: 'NA',
    manager: 'Sarah Johnson',
    headquarters: 'New York, USA',
    countries: ['US', 'CA', 'MX'],
    revenue: 12500000,
    growth: 14.2,
    profit: 4200000,
    customers: 18756,
    satisfaction: 4.7,
    trend: [11200000, 11500000, 11800000, 12000000, 12300000, 12500000],
  },
  {
    id: 'eu',
    name: 'Europe',
    code: 'EU',
    manager: 'Hans Mueller',
    headquarters: 'Berlin, Germany',
    countries: ['GB', 'DE', 'FR'],
    revenue: 8750000,
    growth: 8.5,
    profit: 2625000,
    customers: 14234,
    satisfaction: 4.5,
    trend: [8200000, 8300000, 8400000, 8500000, 8600000, 8750000],
  },
  {
    id: 'apac',
    name: 'Asia Pacific',
    code: 'APAC',
    manager: 'Wei Chen',
    headquarters: 'Singapore',
    countries: ['CN', 'JP', 'AU'],
    revenue: 5200000,
    growth: 18.7,
    profit: 1248000,
    customers: 11234,
    satisfaction: 4.4,
    trend: [4500000, 4700000, 4800000, 4900000, 5000000, 5200000],
  },
  {
    id: 'latam',
    name: 'Latin America',
    code: 'LATAM',
    manager: 'Maria Garcia',
    headquarters: 'São Paulo, Brazil',
    countries: ['BR', 'AR'],
    revenue: 1425000,
    growth: 6.3,
    profit: 285000,
    customers: 3522,
    satisfaction: 4.3,
    trend: [1350000, 1370000, 1380000, 1390000, 1410000, 1425000],
  },
  {
    id: 'mea',
    name: 'Middle East & Africa',
    code: 'MEA',
    manager: 'Ahmed Hassan',
    headquarters: 'Dubai, UAE',
    countries: ['AE', 'ZA'],
    revenue: 600000,
    growth: 22.4,
    profit: 67000,
    customers: 1010,
    satisfaction: 4.2,
    trend: [500000, 520000, 540000, 560000, 580000, 600000],
  },
];

export const countries = {
  US: { id: 'US', name: 'United States', code: 'US', regionId: 'na', currency: 'USD', flag: '🇺🇸', revenue: 9500000, growth: 12.5, customers: 14500, cities: ['New York', 'Los Angeles', 'Chicago'] },
  CA: { id: 'CA', name: 'Canada', code: 'CA', regionId: 'na', currency: 'CAD', flag: '🇨🇦', revenue: 2200000, growth: 9.8, customers: 3200, cities: ['Toronto', 'Vancouver', 'Montreal'] },
  MX: { id: 'MX', name: 'Mexico', code: 'MX', regionId: 'na', currency: 'MXN', flag: '🇲🇽', revenue: 800000, growth: 15.2, customers: 1056, cities: ['Mexico City', 'Guadalajara', 'Monterrey'] },
  GB: { id: 'GB', name: 'United Kingdom', code: 'GB', regionId: 'eu', currency: 'GBP', flag: '🇬🇧', revenue: 3500000, growth: 7.2, customers: 5800, cities: ['London', 'Manchester', 'Birmingham'] },
  DE: { id: 'DE', name: 'Germany', code: 'DE', regionId: 'eu', currency: 'EUR', flag: '🇩🇪', revenue: 3200000, growth: 8.9, customers: 5100, cities: ['Berlin', 'Munich', 'Frankfurt'] },
  FR: { id: 'FR', name: 'France', code: 'FR', regionId: 'eu', currency: 'EUR', flag: '🇫🇷', revenue: 2050000, growth: 6.5, customers: 3334, cities: ['Paris', 'Lyon', 'Marseille'] },
  CN: { id: 'CN', name: 'China', code: 'CN', regionId: 'apac', currency: 'CNY', flag: '🇨🇳', revenue: 2800000, growth: 20.1, customers: 6200, cities: ['Shanghai', 'Beijing', 'Shenzhen'] },
  JP: { id: 'JP', name: 'Japan', code: 'JP', regionId: 'apac', currency: 'JPY', flag: '🇯🇵', revenue: 1800000, growth: 12.3, customers: 3800, cities: ['Tokyo', 'Osaka', 'Yokohama'] },
  AU: { id: 'AU', name: 'Australia', code: 'AU', regionId: 'apac', currency: 'AUD', flag: '🇦🇺', revenue: 600000, growth: 8.7, customers: 1234, cities: ['Sydney', 'Melbourne', 'Brisbane'] },
  BR: { id: 'BR', name: 'Brazil', code: 'BR', regionId: 'latam', currency: 'BRL', flag: '🇧🇷', revenue: 1150000, growth: 5.8, customers: 2800, cities: ['São Paulo', 'Rio de Janeiro', 'Brasília'] },
  AR: { id: 'AR', name: 'Argentina', code: 'AR', regionId: 'latam', currency: 'ARS', flag: '🇦🇷', revenue: 275000, growth: 4.2, customers: 722, cities: ['Buenos Aires', 'Córdoba', 'Rosario'] },
  AE: { id: 'AE', name: 'United Arab Emirates', code: 'AE', regionId: 'mea', currency: 'AED', flag: '🇦🇪', revenue: 380000, growth: 25.6, customers: 650, cities: ['Dubai', 'Abu Dhabi', 'Sharjah'] },
  ZA: { id: 'ZA', name: 'South Africa', code: 'ZA', regionId: 'mea', currency: 'ZAR', flag: '🇿🇦', revenue: 220000, growth: 15.8, customers: 360, cities: ['Johannesburg', 'Cape Town', 'Durban'] },
};

export const regionalMetrics = {
  revenue: [
    { month: 'Jan', na: 10500000, eu: 7800000, apac: 4500000, latam: 1200000, mea: 500000 },
    { month: 'Feb', na: 10800000, eu: 8000000, apac: 4700000, latam: 1250000, mea: 520000 },
    { month: 'Mar', na: 11200000, eu: 8200000, apac: 4900000, latam: 1300000, mea: 540000 },
    { month: 'Apr', na: 11500000, eu: 8300000, apac: 4900000, latam: 1320000, mea: 550000 },
    { month: 'May', na: 12000000, eu: 8500000, apac: 5000000, latam: 1370000, mea: 570000 },
    { month: 'Jun', na: 12500000, eu: 8750000, apac: 5200000, latam: 1425000, mea: 600000 },
  ],
  customers: [
    { month: 'Jan', na: 17200, eu: 13500, apac: 10200, latam: 3200, mea: 900 },
    { month: 'Feb', na: 17700, eu: 13700, apac: 10500, latam: 3300, mea: 930 },
    { month: 'Mar', na: 18000, eu: 13900, apac: 10800, latam: 3400, mea: 960 },
    { month: 'Apr', na: 18300, eu: 14000, apac: 11000, latam: 3450, mea: 980 },
    { month: 'May', na: 18500, eu: 14100, apac: 11100, latam: 3500, mea: 1000 },
    { month: 'Jun', na: 18756, eu: 14234, apac: 11234, latam: 3522, mea: 1010 },
  ],
};

export const kpiData = {
  totalRevenue: 28475000,
  revenueGrowth: 7.4,
  totalCustomers: 48756,
  customerGrowth: 7.8,
  avgSatisfaction: 4.5,
  marketPenetration: 42,
  topRegion: 'North America',
  topGrowthRegion: 'Middle East & Africa',
};

export const drillDownData = {
  regional: {
    breakdown: [
      { category: 'Direct Sales', na: 5200000, eu: 3800000, apac: 2100000, latam: 580000, mea: 250000 },
      { category: 'Channel Partners', na: 4800000, eu: 3100000, apac: 1900000, latam: 450000, mea: 180000 },
      { category: 'Online', na: 2500000, eu: 1850000, apac: 1200000, latam: 395000, mea: 170000 },
    ],
    performance: [
      { metric: 'Efficiency', na: 92, eu: 88, apac: 85, latam: 78, mea: 82 },
      { metric: 'Quality', na: 96, eu: 94, apac: 91, latam: 89, mea: 90 },
      { metric: 'Delivery', na: 95, eu: 93, apac: 90, latam: 87, mea: 85 },
    ],
  },
  country: (regionId: string) => {
    const regionCountries = Object.values(countries).filter(c => c.regionId === regionId);
    return regionCountries.map(c => ({
      ...c,
      metrics: {
        efficiency: Math.floor(Math.random() * 15 + 80),
        quality: Math.floor(Math.random() * 10 + 88),
        satisfaction: (Math.random() * 1 + 4).toFixed(1),
      },
    }));
  },
};
