<template>
  <div class="left-sidebar">
    <el-menu class="aside-menu">
      <div class="menu-logo">
        <div class="aside-top">XXX管理后台系统</div>
      </div>
    </el-menu>
    <!--   default-openeds:['1' , '2']： 默认打展开el-submenu index为1和2的 :default-active="6-1" 默认index=6-1的菜单高亮 -->
    <el-menu
      :default-openeds="defaultShowList"
      :default-active="this.$route.path"
      :unique-opened="false"
      :collapse-transition="true"
      class="aside-menu"
      @open="handleOpen"
      @close="handleClose"
      background-color="#001529"
      text-color="#ffffffb3">

      <!-- 首页 -->
      <el-submenu index="-1">
        <template slot="title">
          <i class="el-icon-s-tools"></i>
          <span class="aside-menu-title">首页</span>
        </template>
        <el-menu-item index="/home/index" v-on:click="selectMenu({url:'/home/index'})">
          <template slot="title">
            <i class="el-icon-s-tools"></i>
            <span class="aside-menu-title">主页</span>
          </template>
        </el-menu-item>
      </el-submenu>

      <!-- 动态菜单 -->
      <el-submenu
        ref="menu"
        :index="index+''"
        :data="item"
        v-for="(item,index) in addRouters"
        v-if="!item.parentId && !item.hidden"
        :key="index">

        <template slot="title">
          <i :class="item.icon"></i>
          <span class="aside-menu-title">{{item.name}}</span>
        </template>

        <el-menu-item :index="son.url"
                      :open="61"
                      v-for="(son,si) in item.children"
                      :key="si"
                      v-on:click="selectMenu(son)">
          <template slot="title">
            <i :class="son.icon"></i>
            <span class="aside-menu-title">{{son.name}}</span>
          </template>
        </el-menu-item>
      </el-submenu>
    </el-menu>

  </div>
</template>

<script>
    import {mapState,mapGetters,mapActions} from 'vuex';
    export default {
        name: '',
        data () {
            return {
                menuList: [],
                //刷新之后默认展开的菜单的index数组
                defaultShowList: [],
                //被选中的子菜单的index
                deafultActive: '',
            };
        },
        methods: {
            handleOpen: function(key, keyPath){
              let $menu = this.$refs['menu'][key];
              if($menu && $menu.$attrs && $menu.$attrs.data){
                let data = $menu.$attrs.data;
                if(data.isJumpLink === 1){
                  window.open(data.url);
                }
              }
            },
            handleClose: function(key, keyPath){
              let $menu = this.$refs['menu'][key];
              if($menu && $menu.$attrs && $menu.$attrs.data){
                let data = $menu.$attrs.data;
                if(data.isJumpLink === 1){
                  window.open(data.url);
                }
              }
            },
            //页面跳转
            selectMenu: function(m){
              if(m.isJumpLink === 1){
                window.open(m.url);
              }else{
                this.$router.push({ path: m.url  })
              }
            },
            //tags标签
            addTags () {
              const route = this.$route;
              if(route){
                this.$store.dispatch('tagsPage/addTagsPage', route);
              }
            },
            updateBreadcrumbList () {
              const route = this.$route;
              if(route){
                this.$store.dispatch('breadcrumb/updateBreadcrumbList', route);
              }
            }
        },
        computed: {
          //tags标签
          ...mapGetters(['addRouters']),
        },
        watch:{
          //监听地址变化
          $route(to,from){
            //tags标签
            this.addTags();
            //
            this.updateBreadcrumbList();

          },
        },
        //页面初始化执行函数
        mounted: function(){
          this.addTags();
          this.updateBreadcrumbList();
        },
        created() {
        }
    }
</script>

<style scoped>
  .left-sidebar .el-menu{
    background-color: transparent;
    border-right:none;
  }

  .left-sidebar .el-menu--inline{
    background-color: #000c17;
  }

  .left-sidebar .menu-logo{
    height: 60px;
    line-height: 60px;
    font-size:22px;
    text-align: center;
  }

  .left-sidebar .aside-menu-title{
    color: #ccc;
  }

  .left-sidebar .el-submenu__title:hover,
  .left-sidebar .el-menu-item:hover{
    background-color:transparent;
  }

  .left-sidebar .el-submenu .el-menu-item{
    height: 40px;
    line-height: 40px;
    padding-left:70px!important;
    background: url("../../assets/img/menu.png") 30px 0 no-repeat;
  }
  .left-sidebar .el-menu-item.is-active{
    color:#fff;
    background: url("../../assets/img/menu-active.png") 30px 0 no-repeat;
    border-right:5px solid #2d8cf0;
  }
</style>
