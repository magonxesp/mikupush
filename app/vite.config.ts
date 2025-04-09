import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import electron from 'vite-plugin-electron/simple'
import { viteStaticCopy } from 'vite-plugin-static-copy'

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
                            external: [
                                'sequelize',
                            ],
                        },
                    },
                }
            },
            preload: {
                input: 'src/preload/preload.ts',
            },
        }),
        viteStaticCopy({
            targets: [
                {
                    src: 'src/main/assets',
                    dest: '../dist-electron',
                }
            ]
        })
    ],
    css: {
        modules: {
            localsConvention: "camelCase",
            generateScopedName: "[local]_[hash:base64:15]"
        }
    },
    base: '',
})
