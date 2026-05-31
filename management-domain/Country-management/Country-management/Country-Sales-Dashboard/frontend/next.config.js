/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  env: {
    NEXT_PUBLIC_API_URL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/sales/v1',
    NEXT_PUBLIC_COUNTRY_CODE: process.env.NEXT_PUBLIC_COUNTRY_CODE || 'US',
  },
}

module.exports = nextConfig
