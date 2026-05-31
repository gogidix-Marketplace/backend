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
      '/api/v1/gateway': {
        target: 'http://localhost:8907',
        changeOrigin: true,
      },
      '/api/v1/charts': {
        target: 'http://localhost:8909',
        changeOrigin: true,
      },
      '/api/v1/websocket': {
        target: 'http://localhost:8908',
        changeOrigin: true,
      },
      '/ws': {
        target: 'ws://localhost:8908',
        ws: true,
      },
    },
  },
})
