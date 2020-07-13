<template>
    <div class="">
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
          <el-button @click="openModifyRole('add')" class="btn-code" style="background-color: #1f90d8;">新增</el-button>
          <el-button @click="deleteRole()" class="btn-code" style="background-color: #ec2d2d;">删除</el-button>
        </div>
        <!-- 数据表格 -->
        <el-table
          style="width: 100%;"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          border="border"
          ref="roleTable"
          :data="roleList"
          tooltip-effect="dark"
          :cell-style="cellStyle"
          :header-cell-style="headClass">

          <el-table-column type="selection" width="100" align="center"></el-table-column>
          <el-table-column prop="name" label="名称" width="220" align="center"></el-table-column>
          <el-table-column prop="note" label="备注" width="220" align="center"></el-table-column>
          <el-table-column prop="sort" label="排序" width="150" align="center"></el-table-column>
          <el-table-column label="状态" width="180" align="center">
            <template slot-scope="scope">{{scope.row.valid==1?'有效':'无效'}}</template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="openModifyRole('update', scope.row)">编辑</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="deleteRole(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <Page ref="page" :pageSize="pageSize" @pageChange="pageChange" :total="total"></Page>
      </div>

      <!-- 新增修改弹出框 :close-on-click-modal：false 点击遮罩不会关闭弹框
      :destroy-on-close="true"  关闭弹框时销毁所有元素-->
      <el-dialog :title="title" :visible.sync="showAddOrUpdate"
                 :close-on-click-modal="false" :destroy-on-close="true" width="40%">
        <AddOrUpdateRole ref="addOrUpdateRole"></AddOrUpdateRole>
      </el-dialog>
    </div>
</template>

<script>
    import Page from '../../../components/page/Page';
    import AddOrUpdateRole from './addOrUpdateRole';

    export default {
        name: 'Role',
        data() {
          return {
            searchForm: {
              name: '',
              type: ''
            },
            title: '',
            showAddOrUpdate: false,
            loading: true,
            border: true,
            //分页
            pageNum: 1, //当前页
            pageSize: 10, //每页显示几条
            total: 0, //总条数
            roleList: [], //分页数据,
          }
      },
      methods: {
        openModifyRole: function (type, data) {
          let param = {};
          if(type === 'add'){
            this.title='新增角色';
          }else if(type === 'update'){
            this.title='修改角色';
            param = JSON.parse(JSON.stringify(data));
          }
          this.showAddOrUpdate = true;
          setTimeout(() => {
            this.$refs.addOrUpdateRole.initData(type, param);
          },0)
        },
        deleteRole: function (id){
          if(id){
            this.deleteV(id);
          }else{
            let arr = this.$refs.roleTable.selection;
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
            this.$http.post('role/deleteObjectByIds', {ids: id}).then(res=>{
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
        //分页组件pageNum或者pageSize改变时触发此方法
        pageChange: function (pageNum, pageSize) {
          this.pageNum = pageNum;
          if(pageSize){
            this.pageSize = pageSize;
          }
          this.getList();
        },
        getList: function () {
          this.roleList = [];
          this.$http.post('role/getObjectListByPage', {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          }).then(res=>{
            this.loading = false;
            let result = res.data;
            if(result.status == 1){
              const data = result.data;
              this.total = data.total;
              this.roleList = data.list;
            }else{
              this.total = 0;
            }
          })
        },
        /** 表格样式 **/
        //头部样式
        headClass: function (row, index) {
          return "font-size: 15px;text-align:center;font-weight:bold;";
        },
        //Cell样式
        cellStyle: function (row, column, rowIndex, columnIndex) {
          return "font-size: 15px;";
        },
      },
      components: {
        Page,
        AddOrUpdateRole
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
