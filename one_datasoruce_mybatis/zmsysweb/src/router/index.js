import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import router from './routers'
import store from '../store'
import http from "@/utils/http";
import { filterAsyncRouter } from '../store/modules/permission';

//全局前置守卫： 导航跳转前调用次此函数(必须放在new Vue前面，不然刷新页面、地址栏直接输入的跳转无法触发)
//to: Route: 即将要进入的目标 路由对象
//from: Route: 当前导航正要离开的路由
//next: Function: 一定要调用该方法来 resolve 这个钩子。执行效果依赖 next 方法的调用参数。
router.beforeEach((to, from, next) => {
  NProgress.start();
  if (to.path === "/login") {
    next();
  } else if (to.path === "/404") {
    next();
  } else {

    if(!store.getters.userInfo){
      //userInfo为空说明是登录或者刷新页面，vuex中无数据，要重新添加路由
      store.dispatch('user/getInfo', {}).then((user) => { // 存储路由
        http.post('menu/getAsideMenuList', {}).then(res=> {
          let result = res.data;
          if (result.status == 1) {
            let addRouters = filterAsyncRouter(result.data);
            store.dispatch('permission/GenerateRoutes', addRouters).then(() => { // 存储路由
              addRouters.push({ path: '*', redirect: '/404', hidden: true })
              router.addRoutes(addRouters) // 动态添加可访问路由表
              next({ ...to, replace: true })
            })
          }
        })
      })

    } else {
      next();
    }

  }
  //设置标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
})

router.afterEach(() => {
  NProgress.done();
})
