# Localization Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/localization
```

## Endpoints

### Translations

#### Get Translation
```http
GET /api/v1/localization/translations/{locale}/{key}
```

#### Get Batch Translations
```http
POST /api/v1/localization/translations/batch
Content-Type: application/json

{
  "locale": "en-US",
  "keys": ["welcome", "goodbye", "error.message"]
}
```

#### Create/Update Translation
```http
PUT /api/v1/localization/translations
Content-Type: application/json

{
  "locale": "en-US",
  "key": "welcome.message",
  "value": "Welcome to Global Business Management"
}
```

### Locale Information

#### Get Supported Locales
```http
GET /api/v1/localization/locales/supported
```

#### Get Default Locale
```http
GET /api/v1/localization/locales/default
```

### Time Zones

#### Get Supported Time Zones
```http
GET /api/v1/localization/timezones/supported
```

#### Convert Time Zone
```http
GET /api/v1/localization/timezones/convert?dateTime=2024-01-01T12:00:00Z&from=UTC&to=America/New_York
```
