import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// While developing, anything starting with /api is forwarded to Spring Boot on port 8080.
// So the browser only ever talks to one address and never complains about CORS.
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
