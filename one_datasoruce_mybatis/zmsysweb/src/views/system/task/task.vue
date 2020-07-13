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
        <el-button @click="openModifyTask('add')" class="btn-code add"
                   style="background-color: #1f90d8;">新增</el-button>
        <el-button @click="deleteTask()" class="btn-code"
                   :disabled="multipleSelection.length<1"
                   style="background-color: #ec2d2d;">删除</el-button>
      </div>
      <!-- 数据表格 -->
      <el-table
        style="width: 100%;"
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        border="border"
        ref="taskTable"
        :data="taskList"
        tooltip-effect="dark"
        :cell-style="cellStyle"
        @selection-change="handleSelectionChange"
        :header-cell-style="headClass">

        <el-table-column type="selection" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="任务名" width="200" align="center"></el-table-column>
        <el-table-column prop="invokeName" label="执行方法" width="250" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="cronExpression" label="表达式" width="150" align="center"></el-table-column>
        <el-table-column label="运行状态" width="150" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="3"
              :inactive-value="1"
              @change="startOrPause(scope.row)"
              active-color="#13ce66"
              inactive-color="#dcdfe6">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="并发执行" width="120" align="center">
          <template slot-scope="scope">{{scope.row.concurrent==1?'是':'否'}}</template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="startOne(scope.row)">运行一次</el-button>
            <el-button
              size="mini"
              @click="openModifyTask('update', scope.row)">编辑</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="deleteTask(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <Page ref="page" :pageSize="pageSize" @pageChange="pageChange" :total="total"></Page>
    </div>

    <!-- 新增修改用户弹出框 -->
    <el-dialog :title="taskTitle" :visible.sync="showAddOrUpdate"
               :close-on-click-modal="false" :destroy-on-close="true" width="40%">
      <AddOrUpdateTask ref="addOrUpdateTask"></AddOrUpdateTask>
    </el-dialog>
  </div>
</template>

<script>
  import Page from "../../../components/page/Page";
  import AddOrUpdateTask from "./addOrUpdateTask";

  export default {
    name: 'Task',
    data() {
      return {
        searchForm: {
          name: '',
          type: '',
        },
        //选中的行
        multipleSelection: [],
        //table
        loading: true,
        border: true,

        //分页
        pageNum: 1, //当前页
        pageSize: 10,
        total: 0, //总条数
        taskList: [], //分页数据,

        taskTitle: '',
        //addtask
        showAddOrUpdate: false,

      }
    },
    methods: {
      deleteTask: function (id){
        if(id){
          this.deleteV(id);
        }else{
          let arr = this.multipleSelection;
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
        this.$confirm('您确认删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('task/deleteObjectByIds', {ids: id}).then(res=>{
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
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      openModifyTask: function (type, data) {
        let param = {};
        if(type === 'add'){
          this.taskTitle = '新增用户';
        }else if(type === 'update'){
          this.taskTitle = '修改用户';
          param = JSON.parse(JSON.stringify(data));
        }
        this.showAddOrUpdate = true;
        setTimeout(() => {
          this.$refs.addOrUpdateTask.initData(type, param);
        },0)
      },
      startOne: function(data){
        this.$confirm('您确定执行此操作吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('task/startOne', {task: data}).then(res=>{
            let result = res.data;
            if(result.status == 1){
              alert("启动成功！");
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          });
        })
      },
      startOrPause: function(param){
        this.$confirm('您确定执行此操作吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('task/updateObject', {task: param}).then(res=>{
            let result = res.data;
            if(result.status == 1){
              alert("操作成功！");
            }
          })
        }).catch(() => {
          if(param.status == 1){
            param.status = 3;
          }else if(param.status == 3){
            param.status = 1;
          }
          this.$message({
            type: 'info',
            message: '已取消'
          });
        })
      },
      closeAddTask: function (done) {
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
      runTaskOnce: function (task) {

      },
      getList: function () {
        this.loading = true;
        this.$http.post('task/getObjectListByPage', {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }).then(res=>{
          this.loading = false;
          let result = res.data;
          if(result.status == 1){
            const data = result.data;
            this.total = data.total;
            this.taskList = data.list;
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
      AddOrUpdateTask,
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
  .disabled{
    background-color: #1ab394;
    border-color: #1ab394;
    color: #FFFFFF;
    margin-right: 5px;
  }
</style>
