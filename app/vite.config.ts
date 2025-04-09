import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import electron from 'vite-plugin-electron/simple'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
      react(),
      electron({
        main: {
          entry: 'src/main/main.ts',
          vite: {
            build: {
              rollupOptions: {
                external: [/node_modules/],
              },
            },
          }
        },
        preload: {
          input: 'src/preload/preload.ts',
        },
      })
  ],
  css:{
    modules:{
      localsConvention:"camelCase",
      generateScopedName:"[local]_[hash:base64:15]"
    }
  },
  base: '',
})
