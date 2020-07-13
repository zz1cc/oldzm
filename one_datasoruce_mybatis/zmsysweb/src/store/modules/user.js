
import { getUserInfo } from "../../api/login";

const state = {
  userInfo: '',
  role: '',
  permission: '',
}
const mutations = {
  SET_userInfo(state, user) {
    state.userInfo = user;
  },
  SET_role(state, role) {
    state.role = role;
  },
  SET_permission(state, permission) {
    state.permission = permission;
  },
}
const actions = {
  getInfo({ commit }, data) {
    return new Promise((resolve, reject) => {
      getUserInfo().then(res => {
        let result = res.data;
        if(result.status == 1){
          let user = result.data;
          commit('SET_userInfo', user);
          resolve(user);
        }
        reject(null);
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
