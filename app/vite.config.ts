import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue({
      template: {
        compilerOptions: {
          // treat all Material Design Web components as custom elements
          isCustomElement: (tag) => tag.includes('md-')
        }
      }
    })
  ],
  base: '',
  server: {
    port: 5173
  }
})
