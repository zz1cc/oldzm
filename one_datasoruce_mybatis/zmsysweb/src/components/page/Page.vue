<template>
    <div style="width: 100%">
      <el-pagination class="page-list" id="page-list"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="pageNum"
                     :page-sizes="pageSizeList"
                     :page-size="pageSize"
                     :total="total"
                     popper-class="popper-class"
                     v-if="total>0"
                     layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>
</template>

<script>
  export default {
    name: 'PageList',
    props: {
      //定义在这里的参数在本组件不能改变，只能通过父组件传值改变
      // 总条数
      total: {
        type: Number,
        default: 0
      },
      // 每页显示的条数
      pageSize: {
        type: Number,
        default: 10
      },
      // 每页显示的条数数组
      pageSizeList: {
        type: Array,
        default: function () {
          return [5, 10, 20];
        }
      }
    },
    data() {
      return {
        background: true,
        pageNum: 1,
      };
    },
    methods: {
      //每页查看条数变化
      handleSizeChange(val) {
        this.pageNum = 1;
        this.$emit('pageChange', this.pageNum, val);
      },
      // 当前页码变化
      handleCurrentChange(val) {
        this.pageNum = val;
        this.$emit('pageChange', this.pageNum);
      },
      //设置可分页数据，将pageSize包含在里面不然会有问题。
      setPageSizeList() {
        if(this.pageSizeList.indexOf(this.pageSize) == -1){
          this.pageSizeList.push(this.pageSize);
          this.pageSizeList.sort(function (last, next) {
            return last - next;
          })
        }
      }
    },
    watch:{
      //监听父组件传值(pageSize)
      pageSizeList: {
        handler(newValue, oldValue) {
        },
        deep: true
      },
    },
    mounted: function(){
      this.handleCurrentChange(1);
      this.setPageSizeList();
    },
  }
</script>

<style>
.page-list{
  width: 500px;
  margin: 40px auto;
}

 #page-list.el-pagination span:not([class*=suffix]){
   font-size:15px;
 }

 #page-list .el-pagination__sizes .el-input .el-input__inner{
   font-size:15px;
 }

 #page-list .el-pager li{
   padding:0;
   margin-left:4px;
   margin-right:4px;
   border:1px solid #efefef;
   height: 30px;
   line-height: 30px;
   min-width: 32px;
   font-weight: normal;
   border-radius: 3px;
 }

  #page-list .el-pager li.active{
    border:1px solid #409EFF;
    cursor: pointer;
  }

  #page-list .el-pager li:hover{
    border:1px solid #409EFF;
    color:#409EFF;
  }

 #page-list .el-pagination__editor{
   margin:0 4px;
 }


</style>
