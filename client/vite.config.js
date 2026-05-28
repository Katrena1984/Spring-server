import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/auth': {
        target: 'https://spring-server-yo18.onrender.com',
        changeOrigin: true,
        secure: false,
      },
      '/user': {
        target: 'https://spring-server-yo18.onrender.com',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})