import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 3000,
    proxy: {
      // Audit Trail Service
      '^/api/v1/audit': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      // Onboarding Tracker Service
      '^/api/v1/onboarding': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
      // Progress Step Service
      '^/api/v1/progress-steps': {
        target: 'http://localhost:8083',
        changeOrigin: true,
      },
      // Status Broadcast Service
      '^/api/v1/status-broadcast': {
        target: 'http://localhost:8084',
        changeOrigin: true,
        ws: true,
      },
      // Transaction Monitoring Service
      '^/api/v1/monitoring': {
        target: 'http://localhost:8085',
        changeOrigin: true,
      },
    },
  },
})
