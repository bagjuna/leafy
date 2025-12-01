import {createApp} from 'vue'
import App from './App.vue'
import router from './router/router.js'
import store from './store'
import {createPinia} from 'pinia'


// Vuetify
import 'vuetify/styles'
import {createVuetify} from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

const vuetify = createVuetify({
    components,
    directives,
})

createApp(App)
    .use(router)
    .use(store)
    .use(createPinia())
    .use(vuetify)
    .mount('#app')
