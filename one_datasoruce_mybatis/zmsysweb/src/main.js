

import Vue from 'vue'
import App from './App'
import store from "@/store";
import router from '@/router/routers'
import './router/index'; // permission control


/*  引入element-ui */
import './assets/css/font-awesome.min.css';// icon字体图标
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; //引入element-ui
//size 用于改变组件的默认尺寸，zIndex 设置弹框的初始 z-index（默认值：2000）
Vue.use(ElementUI, { size: 'small', zIndex: 3000 });


/*  引入工具类 */
import utils from "@/views/common/util/util";
import http from "@/utils/http";
Vue.prototype.$utils = utils;
Vue.prototype.$http = http;


/**
 * vue同一个链接点两次报错,切换vue-router版本
 * npm i vue-router@3.0 -S
 */

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>',
  render: h => h(App)
})
