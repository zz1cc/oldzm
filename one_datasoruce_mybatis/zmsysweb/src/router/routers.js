import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/views/layout/Layout';

Vue.use(Router)

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export const routerMaps = [
  {
    path: '/',
    hidden: true, //是否隐藏显示
    redirect: '/login',
  },
  {
    path: '/login',
    hidden: true, //是否隐藏显示
    component: () => import("@/views/login/login"),
    meta: {
      title: '登陆',
      noAuthentication: true, //自定义参数不需要登录就能访问
    }
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/home',
    component: Layout,
    hidden: false,
    children: [
      {
        path: 'index',
        name: 'index',
        component: () => import("@/views/home/index"),
        meta: {
          title: '主页',
        }
      },
    ]
  },
  {
    path: '/center',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'selfCenter',
        name: 'selfCenter',
        component: () => import("@/views/center/SelfCenter"),
        meta: {
          title: '个人中心',
        }
      },
    ]
  },
]

export default new Router({
  //mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: routerMaps
})
