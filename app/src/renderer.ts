import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { aliases, mdi } from 'vuetify/iconsets/mdi-svg'
import { md3 } from 'vuetify/blueprints'
import { VFileUpload } from 'vuetify/labs/VFileUpload'
import '@material/web/all.js'
import { styles as typescaleStyles } from '@material/web/typography/md-typescale-styles.js'

import './style.css'
import App from './App.vue'

const vuetify = createVuetify({
  blueprint: md3,
  components: {
    ...components,
    VFileUpload
  },
  directives,
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi
    }
  }
})

document.adoptedStyleSheets.push(typescaleStyles.styleSheet)

const pinia = createPinia()
const app = createApp(App)

app.use(pinia)
app.use(vuetify)
app.mount('#app')
