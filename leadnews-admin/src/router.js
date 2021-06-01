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
        path:'/content/index',//内容管理
        component: () => import('@/views/content/index.vue'),
      },
      {
        path:'/content/auth_crawler',//爬虫内容审核
        component: () => import('@/views/content_crawler/index.vue'),
      },
      {
        path:'/content/auth_media',//媒体内容审核
        component: () => import('@/views/content_media/index.vue'),
      },
      {
        path:'/users/index',//用户列表
        component: () => import('@/views/users/index.vue'),
      },
      {
        path:'/sensitive/index',//敏感词
        component: () => import('@/views/sensitive/index.vue'),
      },
      {
        path:'/channel/index',//频道管理
        component: () => import('@/views/channel/index.vue'),
      },
      {
        path:'/auth/index',
        component: () => import('./views/auth/index.vue'),
      }
    ]
  },{
    path:'/screen',
    target:'_blank',
    component: () => import('@/views/screen/index.vue'),
  },
  {
    path: '/heima',
    component: () => import('@/components/editor/heima.vue'),
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
