
import { routerMaps } from "../../router/routers";
import Layout from '@/views/layout/Layout';


const state = {
  routerMaps: routerMaps,
  addRouters: '',
  defaultActive: '',
}
const mutations = {
  UPDATE_routerMaps (state, addRouters) {
    state.addRouters = addRouters;
    state.routerMaps = routerMaps.concat(addRouters);
  },
  UPDATE_defaultActive (state, data) {

  }
}
const actions = {
  GenerateRoutes({ commit }, addRouters) {
    commit('UPDATE_routerMaps', addRouters);
  },
  updateDefaultActive ({ commit }, data) {
    commit('UPDATE_defaultActive', data);
  }
}

export const filterAsyncRouter = (routerMaps) =>{
   return routerMaps.filter(routerMap => {
     let path = routerMap.url || '/';
     let children = routerMap.children
     routerMap.meta = {
       title: routerMap.name,
     };
     if(children){
       for (let i in children) {
         if(children[i].isJumpLink !== 1){
           if(children[i].url){
             children[i].children = null;
             children[i].meta = {
               title: children[i].name,
             };
             let urlArr = children[i].url.split("/");
             children[i].path = urlArr[2];
             children[i].component = loadView(children[i].component);
             path = "/" + urlArr[1];
           }
         }
       }
     }
     routerMap.path = path;
     routerMap.component = Layout;
     return true;
   })
}

export const loadView = (view) => {
  return () => import(`@/views/${view}`)
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
