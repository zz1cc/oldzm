<template>
  <div class="" style="border-color: #ccc">
    <!-- 数据表格 -->
    <el-table
      style="width: 100%;"
      border="border"
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark">

      <el-table-column type="selection" width="100" align="center"></el-table-column>
      <el-table-column prop="userName" label="姓名" width="150" align="center"></el-table-column>
      <el-table-column prop="loginName" label="登录名" width="200" align="center"></el-table-column>
      <el-table-column label="邮箱" width="200" align="center">
        <template slot-scope="scope">{{ scope.row.email }}</template>
      </el-table-column>
      <el-table-column prop="mobile" label="手机号" width="200" align="center" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createdTime" label="创建时间" align="center"></el-table-column>
    </el-table>

    <!-- 分页 -->
    <Page @pageChange="pageChange"
          :pageSize="pageSize"
          :pageSizeList="pageSizeList"
          :total="total">
    </Page>
  </div>
</template>

<script>
  import Page from "../../../components/page/Page";
  export default {
    name: 'TestPage',
    data() {
      return {
        //分页
        pageNum: 1, //当前页
        pageSize: 1,
        pageSizeList: [2,8,9],
        total: 0, //总条数
        list: [], //分页数据,
      }
    },
    methods: {
      //分页组件pageNum或者pageSize改变时触发此方法
      pageChange: function (pageNum, pageSize) {
        this.pageNum = pageNum;
        if(pageSize){
          this.pageSize = pageSize;
        }
        this.getList();
      },
      getList: function () {
        this.$http.post('user/getObjectListByPage', {pageNum: this.pageNum,pageSize: this.pageSize}).then(res=>{
          let result = res.data;
          if(result.status == 1){
            let data = result.data;
            this.total = data.total;
            this.list = data.list;
          }
        })
      },
    },
    components: {
      Page,
    }
  }
</script>

<style scoped>
</style>
