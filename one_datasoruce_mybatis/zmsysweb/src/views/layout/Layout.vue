<template>
  <el-container>
    <el-aside>
      <Aside></Aside>
    </el-aside>
    <el-container>
      <el-header>
        <Header></Header>
      </el-header>
      <el-main><Main></Main></el-main>
    </el-container>
  </el-container>
</template>

<script>
  import Header from "./Header";
  import Aside from "./Aside";
  import Main from "./Main";
  export default {
      name: 'Layout',
      components: {
          "Aside": Aside,
          "Header": Header,
          "Main": Main
      },
  }
</script>

<style scoped>
  .el-container{
    height: 100%;
  }
  .el-header {
    color: #333;
    height: auto!important;
    line-height: 50px;
    padding: 0;
    margin: 0;
  }
  .el-aside {
    color: #fff;
  }
  .el-aside {
    display: block;
    left: 0;
    top: 0;
    bottom: 0;
    overflow-y: scroll;
    width: 280px!important;
    background-color: #001529;
  }
  .el-main{
    /*margin: 37px 0 0 0;*/
    background-color: rgb(242, 242, 242);
  }
  .el-aside::-webkit-scrollbar {display:none}
  .el-aside::-webkit-scrollbar { width: 0 !important }
</style>
