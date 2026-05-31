export interface CurrencyFormatOptions {
  locale?: string;
  minimumFractionDigits?: number;
  maximumFractionDigits?: number;
  style?: 'decimal' | 'currency' | 'percent';
  showSymbol?: boolean;
}

const currencySymbols: Record<string, string> = {
  USD: '$',
  EUR: '€',
  GBP: '£',
  JPY: '¥',
  CNY: '¥',
  INR: '₹',
  AUD: 'A$',
  CAD: 'C$',
  CHF: 'Fr',
  KRW: '₩',
  BRL: 'R$',
  RUB: '₽',
  ZAR: 'R',
  SGD: 'S$',
  HKD: 'HK$',
  NOK: 'kr',
  SEK: 'kr',
  DKK: 'kr',
  PLN: 'zł',
  MXN: '$',
};

const currencyLocales: Record<string, string> = {
  USD: 'en-US',
  EUR: 'de-DE',
  GBP: 'en-GB',
  JPY: 'ja-JP',
  CNY: 'zh-CN',
  INR: 'en-IN',
  AUD: 'en-AU',
  CAD: 'en-CA',
  CHF: 'de-CH',
  KRW: 'ko-KR',
  BRL: 'pt-BR',
  RUB: 'ru-RU',
  ZAR: 'en-ZA',
  SGD: 'en-SG',
  HKD: 'zh-HK',
  NOK: 'nb-NO',
  SEK: 'sv-SE',
  DKK: 'da-DK',
  PLN: 'pl-PL',
  MXN: 'es-MX',
};

// Exchange rates (base: USD) - In production, fetch from API
const exchangeRates: Record<string, number> = {
  USD: 1,
  EUR: 0.92,
  GBP: 0.79,
  JPY: 149.50,
  CNY: 7.19,
  INR: 83.12,
  AUD: 1.53,
  CAD: 1.36,
  CHF: 0.88,
  KRW: 1320.45,
  BRL: 4.97,
  RUB: 92.50,
  ZAR: 18.65,
  SGD: 1.34,
  HKD: 7.83,
  NOK: 10.65,
  SEK: 10.42,
  DKK: 6.87,
  PLN: 3.95,
  MXN: 17.15,
};

export function formatCurrency(
  value: number,
  currency: string = 'USD',
  options: CurrencyFormatOptions = {}
): string {
  const {
    locale,
    minimumFractionDigits = 2,
    maximumFractionDigits = 2,
    style = 'currency',
    showSymbol = true,
  } = options;

  const effectiveLocale = locale || currencyLocales[currency] || 'en-US';
  const symbol = currencySymbols[currency] || currency;

  if (style === 'percent') {
    return new Intl.NumberFormat(effectiveLocale, {
      style: 'percent',
      minimumFractionDigits,
      maximumFractionDigits,
    }).format(value / 100);
  }

  if (!showSymbol) {
    return new Intl.NumberFormat(effectiveLocale, {
      minimumFractionDigits,
      maximumFractionDigits,
    }).format(value);
  }

  return new Intl.NumberFormat(effectiveLocale, {
    style: 'currency',
    currency,
    minimumFractionDigits,
    maximumFractionDigits,
  }).format(value);
}

export function convertCurrency(
  amount: number,
  fromCurrency: string,
  toCurrency: string
): number {
  if (fromCurrency === toCurrency) return amount;

  const fromRate = exchangeRates[fromCurrency] || 1;
  const toRate = exchangeRates[toCurrency] || 1;

  // Convert to USD first, then to target currency
  const amountInUSD = amount / fromRate;
  return amountInUSD * toRate;
}

export function formatWithConversion(
  value: number,
  fromCurrency: string,
  toCurrency: string,
  options?: CurrencyFormatOptions
): string {
  const convertedValue = convertCurrency(value, fromCurrency, toCurrency);
  return formatCurrency(convertedValue, toCurrency, options);
}

export function getCurrencySymbol(currency: string): string {
  return currencySymbols[currency] || currency;
}

export function getCurrencyLocale(currency: string): string {
  return currencyLocales[currency] || 'en-US';
}

export function getExchangeRate(fromCurrency: string, toCurrency: string): number {
  if (fromCurrency === toCurrency) return 1;
  const fromRate = exchangeRates[fromCurrency] || 1;
  const toRate = exchangeRates[toCurrency] || 1;
  return (toRate / fromRate);
}

export function formatNumber(
  value: number,
  locale: string = 'en-US',
  options?: Intl.NumberFormatOptions
): string {
  return new Intl.NumberFormat(locale, options).format(value);
}

export function formatPercent(value: number, decimals: number = 1): string {
  return `${value.toFixed(decimals)}%`;
}

export function formatCompactNumber(value: number, locale: string = 'en-US'): string {
  return new Intl.NumberFormat(locale, {
    notation: 'compact',
    compactDisplay: 'short',
    maximumFractionDigits: 1,
  }).format(value);
}

export function formatLargeCurrency(value: number, currency: string = 'USD'): string {
  const symbol = getCurrencySymbol(currency);
  const absValue = Math.abs(value);
  const sign = value < 0 ? '-' : '';

  if (absValue >= 1e9) {
    return `${sign}${symbol}${(absValue / 1e9).toFixed(1)}B`;
  }
  if (absValue >= 1e6) {
    return `${sign}${symbol}${(absValue / 1e6).toFixed(1)}M`;
  }
  if (absValue >= 1e3) {
    return `${sign}${symbol}${(absValue / 1e3).toFixed(1)}K`;
  }

  return `${sign}${symbol}${absValue.toFixed(2)}`;
}

export function parseCurrencyValue(value: string, locale: string = 'en-US'): number {
  const cleaned = value.replace(/[^\d.,-]/g, '');
  const parts = cleaned.match(/(-)?\d+([.,]\d+)?/);
  if (!parts) return 0;

  const separator = locale.includes(',') ? ',' : '.';
  const numberStr = parts[0].replace(/[.,]/g, (match) =>
    match === separator ? '.' : ''
  );

  return parseFloat(numberStr) || 0;
}

export function getCurrencies(): Array<{ code: string; symbol: string; name: string }> {
  return Object.entries(currencySymbols).map(([code, symbol]) => ({
    code,
    symbol,
    name: code,
  }));
}

export function isValidCurrency(code: string): boolean {
  return code in currencySymbols;
}
