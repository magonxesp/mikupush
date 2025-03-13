import { defineConfig } from 'eslint/config'
import pluginVue from 'eslint-plugin-vue'
import neostandard from 'neostandard'
import { vueTsConfigs, defineConfigWithVueTs } from '@vue/eslint-config-typescript'

export default defineConfig([
  ...pluginVue.configs['flat/recommended'],
  ...defineConfigWithVueTs(vueTsConfigs.recommended),
  ...neostandard(),
  {
    rules: {
      'vue/no-deprecated-slot-attribute': 'off',
    }
  }
])
