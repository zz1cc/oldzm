<template>
  <div class="" style="border-color: #ccc">
    <div class="search-collapse">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="searchForm.type" placeholder="类型"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="pageChange(1)">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="search-collapse" style="margin-top: 10px;min-height: 800px;">
      <div style="margin: 0 40px 20px 0;float: right;">
        <el-button @click="openModifyUser('add')" class="btn-code" style="background-color: #1f90d8;">新增</el-button>
        <el-button @click="deleteUser()" class="btn-code" style="background-color: #ec2d2d;">删除</el-button>
      </div>
      <!-- 数据表格 -->
      <el-table
        style="width: 100%;"
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        border="border"
        ref="userTable"
        :data="userList"
        tooltip-effect="dark"
        :cell-style="cellStyle"
        :header-cell-style="headClass">

        <el-table-column type="selection" width="80" align="center" :selectable="isDisabled"></el-table-column>
        <el-table-column prop="loginName" label="登陆名" width="180" align="center"></el-table-column>
        <el-table-column prop="userName" label="姓名" width="180" align="center"></el-table-column>
        <el-table-column prop="mobile" label="手机" width="150" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200" align="center"></el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">{{scope.row.valid==1?'有效':'无效'}}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="openModifyUser('update', scope.row)">编辑</el-button>
              <el-button
                size="mini"
                @click="openModifyPassword(scope.row)">修改密码</el-button>
            <el-button
              size="mini"
              type="danger"
              :disabled="scope.row.id===1"
              @click="deleteUser(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <Page ref="page" :pageSize="pageSize" @pageChange="pageChange" :total="total"></Page>
    </div>

    <!-- 新增修改用户弹出框 -->
    <el-dialog :title="userTitle" :visible.sync="showAddOrUpdate"
               :close-on-click-modal="false" :destroy-on-close="true" width="40%">
      <AddOrUpdateUser ref="addOrUpdateUser"></AddOrUpdateUser>
    </el-dialog>

    <!-- 修改密码弹出框 -->
    <el-dialog title="修改密码" :visible.sync="showModifyPassword"
               :close-on-click-modal="false" :destroy-on-close="true" width="40%">
      <ModifyPassword ref="modifyPassword"></ModifyPassword>
    </el-dialog>
  </div>
</template>

<script>
  import Page from "../../../components/page/Page";
  import AddOrUpdateUser from './addOrUpdateUser';
  import ModifyPassword from './modifyPassword'

  export default {
    name: 'User',
    data() {
      return {

        searchForm: {
          name: '',
          type: '',
        },
        //table
        loading: true,
        border: true,

        //分页
        pageNum: 1, //当前页
        pageSize: 10,
        total: 0, //总条数
        userList: [], //分页数据,

        userTitle: '',
        //adduser
        showAddOrUpdate: false,
        showModifyPassword: false,

      }
    },
    methods: {
      isDisabled(row,index){
        if(row.id === 1){
          return 0;
        }
        return 1;
      },
      deleteUser: function (id){
        if(id){
          this.deleteV(id);
        }else{
          let arr = this.$refs.userTable.selection;
          if(arr && arr.length>0){
            let ids = '';
            for(let i in arr){
              ids += arr[i].id + ",";
            }
            if(ids !== ''){
              ids = ids.substring(0, ids.length-1);
            }
            this.deleteV(ids);
          }else{
            this.$message({
              message: '请至少选择一项删除！',
              type: 'warning',
              showClose: true,
              offset: 300,
              customClass: 'show-message',
            });
          }
        }
      },
      deleteV: function (id) {
        console.log(id)
        this.$confirm('您确认删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if(JSON.stringify(id).split(",").indexOf(1) != -1){
            alert("无法删除管理员！");
            return;
          }
          this.$http.post('user/deleteObjectByIds', {ids: id}).then(res=>{
            let result = res.data;
            if(result.status == 1){
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.$router.go(0);
            }else{
              alert(result.desc);
            }
          })
        }).catch((e) => {
          console.log(e)
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
      openModifyPassword: function (row) {
        this.showModifyPassword = true;
        setTimeout(() => {
          this.$refs.modifyPassword.initData(JSON.parse(JSON.stringify(row)));
        },0)
      },
      openModifyUser: function (type, data) {
        let param = {};
        if(type === 'add'){
          this.userTitle = '新增用户';
        }else if(type === 'update'){
          this.userTitle = '修改用户';
          param = JSON.parse(JSON.stringify(data));
        }
        this.showAddOrUpdate = true;
        setTimeout(() => {
          this.$refs.addOrUpdateUser.initData(type, param);
        },0)
      },
      closeAddOrUpdateUser(){
      },
      closeModifyPassword(){
        this.showModifyPassword = false;
      },
      closeAddUser: function (done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      //分页组件pageNum或者pageSize改变时触发此方法
      pageChange: function (pageNum, pageSize) {
        this.pageNum = pageNum;
        if(pageSize){
          this.pageSize = pageSize;
        }
        this.getList();
      },
      getList: function () {
        this.loading = true;
        this.$http.post('user/getObjectListByPage', {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }).then(res=>{
          this.loading = false;
          let result = res.data;
          if(result.status == 1){
            const data = result.data;
            this.total = data.total;
            this.userList = data.list;
          }
        })
      },
      //头部样式
      headClass: function (row, index) {
        return "font-size: 15px;text-align:center;font-weight:bold;";
      },
      //Cell样式
      cellStyle: function (row, column, rowIndex, columnIndex) {
        return "font-size: 15px;";
      }
    },
    components: {
      Page,
      AddOrUpdateUser,
      ModifyPassword,
    },
    //页面初始化执行函数
    mounted: function(){
    },
  }
</script>

<style scoped>
  .search-collapse {
    width: 100%;
    background: #fff;
    border-radius: 6px;
    padding: 20px 0 5px 10px;
    box-shadow: 1px 1px 3px rgba(0,0,0,.2);
  }
  .btn-code{
    margin-right: 5px;
    color: #fff;
  }
</style>
