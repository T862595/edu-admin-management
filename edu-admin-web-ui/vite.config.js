import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const appTitle = env.VITE_APP_TITLE || '智学教育管理平台'

  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    define: {
      __APP_NAME__: JSON.stringify(env.VITE_APP_NAME || 'edu-admin-web'),
      __APP_TITLE__: JSON.stringify(appTitle)
    },
    server: {
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          secure: false,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ''),
        }
      }
    }
  }
})
