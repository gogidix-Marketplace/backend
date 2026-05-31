import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      '@domain': path.resolve(__dirname, './src/domain'),
      '@application': path.resolve(__dirname, './src/application'),
      '@infrastructure': path.resolve(__dirname, './src/infrastructure'),
      '@presentation': path.resolve(__dirname, './src/presentation'),
      '@shared': path.resolve(__dirname, './src/shared'),
      '@components': path.resolve(__dirname, './src/presentation/components'),
      '@layouts': path.resolve(__dirname, './src/presentation/layouts'),
      '@pages': path.resolve(__dirname, './src/presentation/pages'),
      '@store': path.resolve(__dirname, './src/shared/store'),
      '@api': path.resolve(__dirname, './src/shared/api'),
      '@utils': path.resolve(__dirname, './src/shared/utils'),
      '@types': path.resolve(__dirname, './src/shared/types'),
    },
  },
  server: {
    port: 3016,
    open: true,
  },
  build: {
    outDir: 'dist',
    sourcemap: true,
  },
})
