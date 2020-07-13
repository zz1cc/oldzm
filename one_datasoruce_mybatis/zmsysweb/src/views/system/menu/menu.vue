<template>
  <div class="">
    <div class="source">
      <div>
        <el-table
          :data="menuTreeList"
          style="width: 100%;margin-bottom: 20px;"
          :default-expand-all="true"
          row-key="id"
          border
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
          <el-table-column prop="name" label="名称" width="180"></el-table-column>
          <el-table-column prop="url" label="地址" width="180"></el-table-column>
          <el-table-column prop="typeName" label="类型" width="150"></el-table-column>
          <el-table-column prop="permission" label="权限标识" width="150"></el-table-column>
          <el-table-column prop="icon" label="图标" width="180"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="openDialog(scope.row, 'add')">新增</el-button>
              <el-button
                size="mini"
                @click="openDialog(scope.row, 'update')">修改</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="deleteMenu(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 新增修改弹出框 -->
    <el-dialog :title="title" :visible.sync="showAddOrUpdate" :destroy-on-close="true"
               :close-on-click-modal="false" width="38%">
      <AddOrUpdateMenu ref="addOrUpdateMenu"></AddOrUpdateMenu>
    </el-dialog>
  </div>
</template>

<script>
  import AddOrUpdateMenu from "./addOrUpdateMenu";

  export default {
    data() {
      return {
        //菜单树列表
        menuTreeList: [],
        //菜单类型
        menuTypeList: [],

        title: '',
        showAddOrUpdate: false,
      }
    },
    methods: {

      //打开弹框
      openDialog (data, type) {
        let param = '';
        if(type === 'add'){
          this.title='新增';
        }else if(type === 'update'){
          this.title='修改';
        }
        param = JSON.parse(JSON.stringify(data));
        this.showAddOrUpdate = true;
        setTimeout(() => {
          this.$refs.addOrUpdateMenu.initData(type, param);
        },0)
      },
      deleteMenu: function (id) {
        this.$confirm('您确认删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('menu/deleteObjectByIds', {ids: id}).then(res=>{
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
      getMenuTree () {
        this.$http.post('menu/getObjectListByPage', {}).then(res=>{
          let result = res.data;
          if(result.status == 1){
            this.menuTreeList = result.data;
          }
        })
      },
    },
    mounted: function(){
      this.getMenuTree();
    },
    components: {
      AddOrUpdateMenu
    }
  };
</script>

<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
