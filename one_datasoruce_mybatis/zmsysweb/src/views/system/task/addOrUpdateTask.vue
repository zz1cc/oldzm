<template>
  <div class="addCode">
    <div class="addCode-from">
      <el-form :model="addOrUpdateTaskForm" status-icon
               :rules="rules" ref="addOrUpdateTaskForm"
               label-width="110px">
        <el-form-item label="任务名：" prop="name">
          <el-input v-model="addOrUpdateTaskForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组：" prop="groupName">
          <el-input v-model="addOrUpdateTaskForm.groupName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="执行方法：" prop="invokeName">
          <el-input v-model="addOrUpdateTaskForm.invokeName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="cron表达式：" prop="cronExpression">
          <el-input v-model="addOrUpdateTaskForm.cronExpression" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="并发执行：" prop="concurrent">
          <el-radio-group v-model="addOrUpdateTaskForm.concurrent">
            <el-radio v-for="(item, index) in validList" :key="index" :label="item.value">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态：" prop="status">
          <el-radio-group v-model="addOrUpdateTaskForm.status">
            <el-radio v-for="(item, index) in statusList" :key="index" :label="item.value">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注：" prop="remarks">
          <el-input type="textarea" v-model="addOrUpdateTaskForm.remarks" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item style="margin: 35px 0 0px 120px;">
          <el-button type="primary" @click="submitForm('addOrUpdateTaskForm')">确定</el-button>
          <el-button @click="resetForm('addOrUpdateTaskForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'AddOrUpdateUser',
    data() {
      return {
        type: '',
        validList: [{value: 1, name: '是'}, {value: 0, name: '否'}],
        statusList: [{value: 1, name: '立即执行'}, {value: 2, name: '执行一次'},{value: 3, name: '不执行'}],
        addOrUpdateTaskForm: {
          name: '',
          invokeName: '',
          groupName: '',
          cronExpression: '',
          concurrent: 0,
          status: 1,
        },
        roles: [],
        rules: {
          name: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          invokeName: [
            { required: true, message: '请输入执行方法',  trigger: 'blur'}
          ],
          groupName: [
            { required: true, message: '请输入分组名',  trigger: 'blur'},
          ],
          cronExpression: [
            { required: true, message: '请输入cron表达式',  trigger: 'blur'}
          ],
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let url = "";
            let success = '';
            let error = '';
            if(this.type === 'add'){
              url = "task/saveObject";
              success = '新增成功！';
              error = '新增失败！';
            }
            if(this.type === 'update'){
              url = "task/updateObject";
              success = '修改成功！';
              error = '修改失败！';
            }
            this.$confirm('您确定执行此操作吗？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              let checkedKeys = '';
              if(this.roles.length > 0){
                for(let i in this.roles){
                  checkedKeys += this.roles[i] + ",";
                }
                if(checkedKeys !== ''){
                  checkedKeys = checkedKeys.substring(0, checkedKeys.length-1);
                }
              }
              this.$http.post(url, {
                task: this.addOrUpdateTaskForm
              }).then(res=>{
                let result = res.data;
                if(result.status == 1){
                  alert(success);
                  window.location.reload();
                }else{
                  alert(result.desc);
                }
              })
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消'
              });
            })
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      initData (type, data) {
        this.type = type;
        if(type == 'update'){
          this.addOrUpdateTaskForm = data;
        }
      }
    }
  }
</script>
<style scoped>
  .addCode{
    width: 500px;
  }
</style>
