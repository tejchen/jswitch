import Vue from 'vue'
import Router from 'vue-router'
import App from '@/App.vue'
import Apps from '@/components/Apps.vue'
import Home from '@/components/Home.vue'
import Configs from '@/components/Configs.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/apps',
      name: 'Apps',
      component: Apps
    },
    {
      path: '/configs',
      name: 'Configs',
      component: Configs
    }
  ]
})
