

const getters = {
  tagsPageSelected: state => state.tagsPage.tagsPageSelected,
  tagsPageList: state => state.tagsPage.tagsPageList,
  breadcrumbList: state => state.breadcrumb.breadcrumbList,
  routerMaps: state => state.permission.routerMaps,
  addRouters: state => state.permission.addRouters,
  userInfo: state => state.user.userInfo,
}
export default getters;
