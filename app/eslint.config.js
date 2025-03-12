import { defineConfig } from 'eslint/config'
import pluginVue from 'eslint-plugin-vue'
import neostandard from 'neostandard'

export default defineConfig([
  ...pluginVue.configs['flat/recommended'],
  ...neostandard(),
  {
    rules: {
      'vue/no-deprecated-slot-attribute': 'off',
    }
  }
])
