<template>
  <div class="">
    <div>
      <!-- 字典搜索 -->
      <!--  -->
      <div class="search-collapse" v-if="!isShowValueList">
        <el-form :inline="true" :model="typeForm" class="demo-form-inline">
          <el-form-item label="名称">
            <el-input v-model="typeForm.name" placeholder="名称"></el-input>
          </el-form-item>
          <el-form-item label="类型">
            <el-input v-model="typeForm.type" placeholder="类型"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="pageChange(1)">查询</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="search-collapse" style="margin-top: 10px;min-height: 800px;">
        <!-- table -->
        <!-- 字典 -->
        <div class="type" v-if="!isShowValueList">
          <div style="margin: 0 40px 20px 0;float: right;">
            <el-button @click="openModifyType('add')" class="btn-code" style="background-color: #1f90d8;">新增</el-button>
            <el-button @click="deleteType()" class="btn-code" style="background-color: #ec2d2d;">删除</el-button>
          </div>
          <!-- 数据表格 -->
          <el-table
            style="width: 100%;"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            border="border"
            ref="typeTable"
            :data="typeList"
            tooltip-effect="dark"
            :cell-style="cellStyle"
            :header-cell-style="headClass">

            <el-table-column type="selection" width="100" align="center"></el-table-column>
            <el-table-column prop="name" label="名称" width="150" align="center"></el-table-column>
            <el-table-column prop="type" label="类型" width="180" align="center"></el-table-column>
            <el-table-column label="状态" width="130" align="center">
              <template slot-scope="scope">{{scope.row.valid==1?'有效':'无效'}}</template>
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" width="200" align="center"></el-table-column>
            <el-table-column prop="remarks" label="备注" align="center" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="openModifyType('update', scope.row)">编辑</el-button>
                <el-button
                  size="mini"
                  @click="showValueList(scope.row)">列表</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  @click="deleteType(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 新增修改弹出框 -->
          <el-dialog :title="title" :visible.sync="showAddOrUpdateType" :before-close="closeModifyType"
                     :close-on-click-modal="modalClickHide" :destroy-on-close="true" width="40%">
            <AddOrUpdateType ref="addOrUpdateType"></AddOrUpdateType>
          </el-dialog>
        </div>
        <!-- 字典值 -->
        <div class="val" v-if="isShowValueList">
          <div style="margin: 0 40px 20px 0;float: right;">
            <el-button @click="goBack()" class="btn-code" style="background-color: #d8c61fe0;">返回</el-button>
            <el-button @click="openModifyValue('add')" class="btn-code" style="background-color: #1f90d8;">新增</el-button>
            <el-button @click="deleteValue()" class="btn-code" style="background-color: #ec2d2d;">删除</el-button>
          </div>
          <!-- 数据表格 -->
          <el-table
            style="width: 100%;"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            border="border"
            ref="valueTable"
            :data="valueList"
            tooltip-effect="dark"
            :cell-style="cellStyle"
            :header-cell-style="headClass">

            <el-table-column type="selection" width="100" align="center"></el-table-column>
            <el-table-column prop="name" label="名称" width="150" align="center"></el-table-column>
            <el-table-column prop="value" label="值" width="100" align="center"></el-table-column>
            <el-table-column label="字典类型" width="200" align="center">
              <template slot-scope="scope">{{scope.row.typeName}}</template>
            </el-table-column>
            <el-table-column label="状态" width="120" align="center">
              <template slot-scope="scope">{{scope.row.valid==1?'有效':'无效'}}</template>
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" width="180" align="center"></el-table-column>
            <el-table-column label="备注" align="center" show-overflow-tooltip>
              <template slot-scope="scope">{{scope.row.remarks}}</template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="openModifyValue('update', scope.row)">编辑</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  @click="deleteValue(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 新增修改弹出框 -->
          <el-dialog :title="title" :visible.sync="showAddOrUpdateValue" :before-close="closeModifyValue"
                     :close-on-click-modal="modalClickHide" :destroy-on-close="true" width="40%">
            <AddOrUpdateValue ref="addOrUpdateValue"></AddOrUpdateValue>
          </el-dialog>
        </div>
        <!-- 分页 -->
        <Page ref="page" :pageSize="pageSize" @pageChange="pageChange" :total="total"></Page>
      </div>
    </div>
  </div>
</template>

<script>
  import Page from "../../../components/page/Page";
  import AddOrUpdateType from "./addOrUpdateType";
  import AddOrUpdateValue from "./addOrUpdateValue";
  export default {
    name: 'Code',
    data() {
      return {
        //是否展示字典值列表
        isShowValueList: false,

        /** 字典值列表开始 **/
        typeData: '',
        showAddOrUpdateValue: false,
        /** 字典值列表结束 **/

        /** 字典列表开始 **/
        typeForm: {
          name: '',
          type: ''
        },
        //table
        loading: false,
        border: true,

        //是否显示新增
        showAddOrUpdateType: false,
        //是否点击遮罩层自动关闭
        modalClickHide: false,
        title: '',
        /** 字典列表结束 **/

        //分页
        pageNum: 1, //当前页
        pageSize: 10, //每页显示几条
        total: 0, //总条数
        typeList: [], //分页数据,
        valueList: [], //分页数据,
      }
    },
    methods: {
      /** 字典值列表开始 **/
      showValueList: function (data) {
        //显示值列表
        this.isShowValueList = true;
        //传入当前选中的字典对象
        this.typeData = data;
        //初始化分页为第一页并加载数据
        this.$refs.page.handleCurrentChange(1);
      },
      openModifyValue:function (type, data) {
        let param = {};
        if(type === 'add'){
          this.title='新增字典值';
          param.typeName = this.typeData.type;
          param.typeId = this.typeData.id;
        }else if(type === 'update'){
          this.title='修改字典值';
          param = JSON.parse(JSON.stringify(data));
        }
        this.showAddOrUpdateValue = true;
        setTimeout(() => {
          this.$refs.addOrUpdateValue.initData(type, param);
        },0)
      },
      closeModifyValue: function (done) {
        //清空for表单
        this.$refs.addOrUpdateValue.resetForm('addOrUpdateTypeForm');
        done();
      },
      goBack () {
        this.isShowValueList = false;
      },
      deleteValue: function (row) {
        if(row){
          this.deleteV(row.id);
        }else{
          let arr = this.$refs.valueTable.selection;
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
      deleteV: function(ids){
        this.$confirm('您确认删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('code/deleteCodeValueByIds', {ids: ids}).then(res=>{
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
      getValueList: function () {
        this.valueList = [];
        this.total = 0;
        this.$http.post('code/getCodeValueListByTypeIdOrTypeName', {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          typeId: this.typeData.id,
        }).then(res=>{
          this.loading = false;
          let result = res.data;
          if(result.status == 1){
            const data = result.data;
            this.total = data.total;
            this.valueList = data.list;
          }
        })
      },
      /** 字典值列表结束 **/

      /** ------------------ **/

      /** 字典列表开始 **/
      openModifyType: function (type, data) {
        let param = '';
        if(type === 'add'){
          this.title='新增字典';
        }else if(type === 'update'){
          this.title='修改字典';
          param = JSON.parse(JSON.stringify(data));
        }
        this.showAddOrUpdateType = true;
        setTimeout(() => {
          this.$refs.addOrUpdateType.initData(type, param);
        },0)
      },
      closeModifyType: function (done) {
        //清空for表单
        this.$refs.addOrUpdateType.resetForm('addOrUpdateTypeForm');
        done();
      },
      deleteType: function (row) {
        if(row){
          this.deleteT(row.id);
        }else{
          let arr = this.$refs.typeTable.selection;
          if(arr && arr.length>0){
            let ids = '';
            for(let i in arr){
              ids += arr[i].id + ",";
            }
            if(ids !== ''){
              ids = ids.substring(0, ids.length-1);
            }
            this.deleteT(ids);
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
      deleteT: function(ids){
        this.$confirm('此操作会删除类别对应的所有值，您确认删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('code/deleteCodeTypeByIds', {ids: ids}).then(res=>{
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
      getTypeList: function () {
        this.total = 0;
        this.typeList = [];
        this.$http.post('code/getCodeTypeList', {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.typeForm.name,
          type: this.typeForm.type,
        }).then(res=>{
          this.loading = false;
          let result = res.data;
          if(result.status == 1){
            const data = result.data;
            this.total = data.total;
            this.typeList = data.list;
          }
        })
      },
      /** 字典列表结束 **/

      //分页组件pageNum或者pageSize改变时触发此方法
      pageChange: function (pageNum, pageSize) {
        this.pageNum = pageNum;
        if(pageSize){
          this.pageSize = pageSize;
        }
        if(this.isShowValueList){
          this.getValueList();
        }else{
          this.getTypeList();
        }
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

      /*getCodeValueList*/
      getCodeValueList: function () {
        /*this.$axios.post('code/getCodeValueList', {
          typeName: "zmbk-sex"
        }).then(successData => {
          let result = successData.data;
          if(result.status == 1){
            const data = result.data;
            console.log(data);
          }
        }).catch(e => {
          console.log(e)
        })*/
      }
    },
    components: {
      Page,
      AddOrUpdateType,
      AddOrUpdateValue
    },
    //页面初始化执行函数
    mounted: function(){
      this.getCodeValueList();
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
