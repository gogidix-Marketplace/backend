// Country Constants for Global Marketing Dashboard

export const COUNTRIES = [
  { code: 'US', name: 'United States', currency: 'USD', flag: '🇺🇸' },
  { code: 'GB', name: 'United Kingdom', currency: 'GBP', flag: '🇬🇧' },
  { code: 'DE', name: 'Germany', currency: 'EUR', flag: '🇩🇪' },
  { code: 'FR', name: 'France', currency: 'EUR', flag: '🇫🇷' },
  { code: 'ES', name: 'Spain', currency: 'EUR', flag: '🇪🇸' },
  { code: 'IT', name: 'Italy', currency: 'EUR', flag: '🇮🇹' },
  { code: 'NL', name: 'Netherlands', currency: 'EUR', flag: '🇳🇱' },
  { code: 'BE', name: 'Belgium', currency: 'EUR', flag: '🇧🇪' },
  { code: 'AT', name: 'Austria', currency: 'EUR', flag: '🇦🇹' },
  { code: 'CH', name: 'Switzerland', currency: 'CHF', flag: '🇨🇭' },
  { code: 'SE', name: 'Sweden', currency: 'SEK', flag: '🇸🇪' },
  { code: 'NO', name: 'Norway', currency: 'NOK', flag: '🇳🇴' },
  { code: 'DK', name: 'Denmark', currency: 'DKK', flag: '🇩🇰' },
  { code: 'FI', name: 'Finland', currency: 'EUR', flag: '🇫🇮' },
  { code: 'PL', name: 'Poland', currency: 'PLN', flag: '🇵🇱' },
  { code: 'CZ', name: 'Czech Republic', currency: 'CZK', flag: '🇨🇿' },
  { code: 'GR', name: 'Greece', currency: 'EUR', flag: '🇬🇷' },
  { code: 'PT', name: 'Portugal', currency: 'EUR', flag: '🇵🇹' },
  { code: 'IE', name: 'Ireland', currency: 'EUR', flag: '🇮🇪' },
  { code: 'CA', name: 'Canada', currency: 'CAD', flag: '🇨🇦' },
  { code: 'AU', name: 'Australia', currency: 'AUD', flag: '🇦🇺' },
  { code: 'NZ', name: 'New Zealand', currency: 'NZD', flag: '🇳🇿' },
  { code: 'JP', name: 'Japan', currency: 'JPY', flag: '🇯🇵' },
  { code: 'SG', name: 'Singapore', currency: 'SGD', flag: '🇸🇬' },
  { code: 'HK', name: 'Hong Kong', currency: 'HKD', flag: '🇭🇰' },
  { code: 'IN', name: 'India', currency: 'INR', flag: '🇮🇳' },
  { code: 'BR', name: 'Brazil', currency: 'BRL', flag: '🇧🇷' },
  { code: 'MX', name: 'Mexico', currency: 'MXN', flag: '🇲🇽' },
  { code: 'ZA', name: 'South Africa', currency: 'ZAR', flag: '🇿🇦' },
  { code: 'AE', name: 'United Arab Emirates', currency: 'AED', flag: '🇦🇪' },
  { code: 'IL', name: 'Israel', currency: 'ILS', flag: '🇮🇱' },
  { code: 'TR', name: 'Turkey', currency: 'TRY', flag: '🇹🇷' },
  { code: 'KR', name: 'South Korea', currency: 'KRW', flag: '🇰🇷' },
  { code: 'TH', name: 'Thailand', currency: 'THB', flag: '🇹🇭' },
  { code: 'MY', name: 'Malaysia', currency: 'MYR', flag: '🇲🇾' },
  { code: 'ID', name: 'Indonesia', currency: 'IDR', flag: '🇮🇩' },
  { code: 'PH', name: 'Philippines', currency: 'PHP', flag: '🇵🇭' },
  { code: 'VN', name: 'Vietnam', currency: 'VND', flag: '🇻🇳' },
] as const;

export const getCountryByCode = (code: string) =>
  COUNTRIES.find(c => c.code === code);

export const getCountryName = (code: string) =>
  COUNTRIES.find(c => c.code === code)?.name || code;
