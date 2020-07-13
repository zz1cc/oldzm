

const state = {
  //面包屑
  breadcrumbList: []
}

const mutations = {
  UPDATE_breadcrumbList(state, route) {
    let param = {
      title: route.meta.title,
      path: route.path
    }
    let matched = route.matched;
    matched = matched.filter(item => item.meta && item.meta.title)
    state.breadcrumbList = [{meta:{title: "首页", path: "/home/index"}}].concat(matched);
  }
}

const actions = {
  updateBreadcrumbList({ commit }, route) {
    commit('UPDATE_breadcrumbList', route);
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
