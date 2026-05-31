/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_URL: string;
  readonly VITE_API_TIMEOUT: string;
  readonly VITE_JWT_SECRET: string;
  readonly VITE_TOKEN_REFRESH_INTERVAL: string;
  readonly VITE_APP_NAME: string;
  readonly VITE_APP_URL: string;
  readonly VITE_PUBLIC_URL: string;
  readonly VITE_MAX_FILE_SIZE: string;
  readonly VITE_ALLOWED_FILE_TYPES: string;
  readonly VITE_ENABLE_ANALYTICS: string;
  readonly VITE_ENABLE_RICH_TEXT_EDITOR: string;
  readonly VITE_ENABLE_MEDIA_LIBRARY: string;
  readonly VITE_ENABLE_SCHEDULING: string;
  readonly VITE_ENV: string;
  readonly VITE_SENTRY_DSN: string;
  readonly VITE_SENTRY_ENVIRONMENT: string;
  readonly VITE_GA_MEASUREMENT_ID: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
