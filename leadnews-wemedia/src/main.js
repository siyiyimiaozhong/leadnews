import Vue from 'vue'
import App from './App.vue'
import './permission' // permission control

import router from './router'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/index.scss' // global css
import 'font-awesome/css/font-awesome.min.css'
import Meta from 'vue-meta'

Vue.use(Meta)
Vue.config.productionTip = false
Vue.use(Element)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
