/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  env: {
    NEXT_PUBLIC_GLOBAL_HR_API_URL: process.env.NEXT_PUBLIC_GLOBAL_HR_API_URL || 'http://localhost:8080',
  },
  async rewrites() {
    return [
      {
        source: '/api/v1/global-hr/:path*',
        destination: `${process.env.NEXT_PUBLIC_GLOBAL_HR_API_URL || 'http://localhost:8080'}/api/v1/global-hr/:path*`,
      },
    ];
  },
};

module.exports = nextConfig;
