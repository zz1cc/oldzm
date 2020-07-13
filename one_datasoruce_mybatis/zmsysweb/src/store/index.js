import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import tagsPage from './modules/tagsPage'
import breadcrumb from "./modules/breadcrumb";
import permission from "./modules/permission";
import user from "./modules/user";

//挂载Vuex
Vue.use(Vuex)
//https://baijiahao.baidu.com/s?id=1618794879569468435&wfr=spider&for=pc

//vuex实例
const store = new Vuex.Store({
  modules: {
    tagsPage,
    breadcrumb,
    permission,
    user
  },
  getters
})

export default store
