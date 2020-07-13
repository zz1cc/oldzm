
const state = {
//标签页默认选中标识
  tagsPageSelected: '/home/index',
  //标签页数组
  tagsPageList: [
    {
      title: '首页',
      path: '/home/index',
      closable: false, // 是否可关闭当前标签
    },
  ],
}

const mutations = {
  SET_pageSelected(state, v){
    state.tagsPageSelected = v;
  },
  //新增一个tag
  ADD_tagsPage(state, route){
    let title = route.meta.title;
    let path = route.path;
    let data = {
      title: title,
      path: path,
      closable: true,
    }
    let index = -1;
    let tagsPageList = state.tagsPageList;
    for (let i in tagsPageList){
      let element = tagsPageList[i];
      //判断重复
      if(element.path == path){
        index = i;
      }
    }
    if(index != -1) {
      //如果添加的标签已存在，那么修改为已存在的那个标签选中
      state.tagsPageSelected = tagsPageList[index].path;
    } else {
      //不存在添加，新加的默认选中
      tagsPageList.push(data);
      state.tagsPageList = tagsPageList;
      state.tagsPageSelected = tagsPageList[tagsPageList.length-1].path;
    }

  },
  //删除一个tag
  REMOVE_tagsPage(state, path){
    let tagsPageList = state.tagsPageList;
    let i = 0;
    while (i < tagsPageList.length) {
      let element = tagsPageList[i];
      //找到数据删除
      if(element.path == path){
        //删除的是当前选中就要变更选中状态，不是选中的就不管
        if(state.tagsPageSelected == path){
          if(i == tagsPageList.length-1){
            //当前选中的是最后一个元素，默认选中改为前一个
            state.tagsPageSelected = tagsPageList[i-1].path;
          }else{
            //当前选中的不是最后一个元素，默认选中改为后一个
            state.tagsPageSelected = tagsPageList[i+1].path;
          }
        }
        tagsPageList.splice(i, 1);
        i--;
      }
      i++;
    }
    state.tagsPageList = tagsPageList;
  }
}

const actions = {
  setTagsPageSelected(context, v){
    context.commit('SET_pageSelected', v);
  },
  addTagsPage(context, route){
    context.commit('ADD_tagsPage', route);
  },
  removeTagsPage(context, path){
    context.commit('REMOVE_tagsPage', path);
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
