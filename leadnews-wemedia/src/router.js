import Vue from "vue";
import Router from "vue-router";
import Layout from '@/views/layout/Layout.vue'
Vue.use(Router);
export const asyncRouterMap = [
  {
    path: "/",
    component: Layout,
    redirect:'/login', //默认子路由
    name:'mainIndex',
    children:[
      {
        path:'/index',
        component: () => import('@/views/dashboard/index.vue'),
      },
      {
        path:'/article/publish',
        component: () => import('@/views/publish/index.vue'),
      },
      {
        path:'/article/list',
        component: () => import('./views/content/index.vue'),
      },
      {
        path:'/comment/list',
        component: () => import('./views/comment/index.vue'),
      },
      {
        path:'/comment/detail',
        component: () => import('./views/comment/detail.vue'),
      },
      {
        path:'/material/list',
        component: () => import('./views/material/material.vue'),
      },
      {
        path:'/material/data',
        component: () => import('./views/content/detail.vue'),
      },
      {
        path:'/fans/index',
        component: () => import('./views/fans/index.vue'),
      },
      {
        path:'/fans/info',
        component: () => import('./views/fans/info.vue'),
      },
      {
        path:'/fans/list',
        component: () => import('./views/fans/list.vue'),
      },
      {
        path:'/user/center',
        component: () => import('./views/user/index.vue'),
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
  },
  {
    path: '*',
    component: () => import('@/views/404.vue'),
  }
]
var  myRouter = new Router({
  routes: asyncRouterMap
})
export default myRouter
