<template>
  <div class="header-info">
    <div class="header-bar clearfix">
       <div class="left-title">
         <!--<a href="javascript:;">
           <img src="../../assets/img/push-btn.png" height="30">
         </a>-->
         <el-breadcrumb separator-class="el-icon-arrow-right" style="padding: 18px;">
             <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
               <span :to="{path: item.meta.path}">{{item.meta.title}}</span>
             </el-breadcrumb-item>
         </el-breadcrumb>
       </div>
       <div class="right-login clearfix">
          <div class="search-panel clearfix">
             <div class="search-hidden">
               <a class="search-btn"><i class="fa fa-search"></i></a>
             </div>
          </div>
          <div class="news-panel clearfix">
            <el-badge :value="3" :max="99" class="item">
              <i class="fa fa-bell"></i>
            </el-badge>
          </div>
          <div class="login-panel clearfix">
              <div class="login-info-show">

                <el-dropdown @command="handleCommand"  :hide-on-click="false">
                  <span class="el-dropdown-link">
                    admin
                    <i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="1">个人中心</el-dropdown-item>
                    <el-dropdown-item command="2">修改密码</el-dropdown-item>
                    <el-dropdown-item command="3">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
          </div>
       </div>
    </div>

    <div class="header-bar clearfix" style="height: 40px;line-height: 40px;">
      <el-tabs v-model="tagsPageSelected" type="card" @tab-remove="tabRemove">
        <el-tab-pane
          v-for="(item, index) in tagsPageList"
          :key="item.path"
          :label="item.title"
          :name="item.path"
          :closable="item.closable">
          <span slot="label">{{item.title}}</span>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>

    import {mapState,mapGetters,mapActions} from 'vuex';
    export default {
        data() {
            return {
                activeIndex: '1',
                activeIndex2: '1',
                time: 100,
                isCollapse: true,
            };
        },
        methods: {
          handleCommand(command) {
            switch (command) {
              case '1':
                this.$router.push({path:'/center/SelfCenter'})
                break;
              case '2':
                break;
              case '3':
                this.logout();
                break;
            }
          },
          logout() {
            if(window.confirm("您确定要退出登陆吗")){
              this.$http.post('u/logout', {}).then(res=>{
                this.$router.push({ path:'/login'})
              })
            }
          },
          //删除一个  tag
          tabRemove (path) {
            this.$store.dispatch("tagsPage/removeTagsPage", path);
          },
        },
        computed: {
          //tags标签
          ...mapState({
            //直接调用属性
            tagsPageList: state => state.tagsPage.tagsPageList,
          }),
          tagsPageSelected: {
            get() {
              //通过getters方法调用属性获取返回值
              return this.$store.getters.tagsPageSelected
            },
            set(v) {
              // 使用vuex中的mutations中定义好的方法来改变
              this.$store.dispatch('tagsPage/setTagsPageSelected', v);
            }
          },
          //顶部面包屑
          ...mapGetters(['breadcrumbList']),
        },
        watch: {
          tagsPageSelected: {
            handler(newValue, oldValue) {
              this.$router.push(newValue)
            },
            //deep: true
          }
        }
    }
</script>

<style scoped>
  /*清除浮动、设置浮动*/
  .left-title{float:left}
  .right-login{float:right}
  .search-panel,.news-panel{float: left;margin-right:18px}
  .login-panel{float:right}
  .clearfix:after {visibility: hidden;display: block;content: "";clear: both;height: 0;}
  .clearfix:after {display: block;content: " ";clear: both;}
  .clearfix {*zoom: 1}

  /*搜索*/
  .search-panel .search-hidden {position:relative;width:200px;height: 50px}
  .search-panel .search-hidden .input{width:90%;height: 30px;line-height:30px;padding-left:12px;border-radius: 5px;border:1px solid #ccc}
  .search-panel .fa-search{position: absolute;top:15px;right:5px;font-size:22px;color:#7a7a7a}
  .search-panel .search-btn{cursor: pointer}

  /*消息*/
  .news-panel .fa-bell{font-size:22px;color:#7a7a7a}
  .news-panel .el-badge__content.is-fixed{top: 22px}

  .el-dropdown {
    font-size: 16px;
  }
  .el-dropdown-link {
    cursor: pointer;
    margin-right: 30px;
  }

  .source{
    border: 1px solid #f6f6f6;
    border-radius: 3px;
    transition: .2s;
    padding: 20px;
    border-left: none;
  }
  .header-bar{
    border-bottom: 1px solid #7a75731f;
  }
  .header-bar .custom-content-con {
    float: right;
    height: auto;
    padding-right: 20px;
  }
  .custom-bread-crumb {
    display: inline-block;
    vertical-align: top;
  }
  .custom-content-con {
    float: right;
    height: auto;
    padding-right: 20px;
  }
  .pagetabs {
    height: 40px;
    line-height: 40px;
    background-color: #fff;
    box-sizing: border-box;
    box-shadow: 0 1px 2px 0 rgba(0,0,0,.1);
  }
  .tabs-control{
    position: absolute;
    top: 56px;
    width: 40px;
    text-align: center;
    cursor: pointer;
    transition: all .3s;
    -webkit-transition: all .3s;
    box-sizing: border-box;
    border-left: 1px solid #f6f6f6;
    color: #ccc;
  }
  .tabs-control:hover{
    background-color: #f6f6f6;
  }
  .prev{
    border-left: none;
    border-right: 1px solid #f6f6f6;
  }
  .next{
    right: 40px;
  }
  .down{
    right: 0;
  }
  .tab{
    margin: 0;
    overflow: hidden;
    text-align: left!important;
    padding: 0;
  }
  .tab-title{
    position: relative;
    left: 0;
    white-space: nowrap;
    font-size: 0;
    border-bottom-width: 1px;
    transition: all .2s;
    -webkit-transition: all .2s;
    height: 50px;
    border: none;
  }
  .tab-title li {
     min-width: 0;
     line-height: 50px;
     max-width: 160px;
     text-overflow: ellipsis;
     overflow: hidden;
     border-right: 1px solid #f6f6f6;
     vertical-align: top;
     padding: 0 15px;
     text-align: center;
     cursor: pointer;
     position: relative;
     -webkit-transition: all .2s;
     display: inline-block;
     font-size: 10px;
   }
  .tab-title li:hover{
    background-color: #f6f6f6;
  }
  .tab-title li:first-child {
    padding-right: 15px;
  }
  .el-menu--horizontal>.el-submenu .el-submenu__title{
    height: 50px;
  }

  .el-tabs--card>.el-tabs__header .el-tabs__item:hover{
    background-color: rgb(242, 242, 242);
    color: #23508e;
  }
  .el-tabs--card>.el-tabs__header .el-tabs__item.is-active{
    background-color: rgb(242, 242, 242);
    color: #23508e;
  }
  .el-tabs--card>.el-tabs__header .el-tabs__item.is-active:hover{
    background-color: rgb(242, 242, 242);
    color: #23508e;
  }
</stylescoped>
