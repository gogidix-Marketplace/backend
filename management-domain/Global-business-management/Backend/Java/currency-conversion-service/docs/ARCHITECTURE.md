# Currency Conversion Service - Architecture Documentation

## Overview
Provides real-time currency conversion and exchange rate management for multi-currency transactions.

## Key Features
- Real-time exchange rates from multiple providers
- Historical rate tracking
- Multi-currency calculation support
- Rate caching for performance
- Automatic rate refresh scheduling

## Architecture
```
API Layer -> Conversion Service -> Exchange Rate Provider
                                      -> Cache (Redis)
                                      -> MongoDB (historical rates)
```

## Supported Currencies
- All major world currencies (USD, EUR, GBP, JPY, etc.)
- Regional currencies
- Crypto currencies (optional)

## Rate Sources
- ECB (European Central Bank)
- Fixer.io
- Open Exchange Rates
- Custom rate feeds
