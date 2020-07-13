<template>
  <div class="addCode">
    <div class="addCode-from">
      <el-form :model="modifyPasswordForm" status-icon :rules="rules" ref="modifyPasswordForm" label-width="100px">
        <el-form-item label="登录名：" prop="loginName">
          <el-input v-model="modifyPasswordForm.loginName" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码：" prop="password">
          <el-input type="password" v-model="modifyPasswordForm.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码：" prop="confirmPwd">
          <el-input type="password" v-model="modifyPasswordForm.confirmPwd" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item style="margin: 35px 0 0px 120px;">
          <el-button type="primary" @click="submitForm('modifyPasswordForm')">确定</el-button>
          <el-button @click="resetForm('modifyPasswordForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ModifyPasswordForm',
    data() {
      let checkConfirmPwd = (rule, value, callback) => {
          if (value) {
            if (this.modifyPasswordForm.password && this.modifyPasswordForm.password!==value) {
              callback(new Error('两次输入密码不一致'));
            }
          } else {
            callback(new Error('请输入确认密码'));
          }
          callback();
      };
      return {
        modifyPasswordForm: {
          loginName: '',
          password: '',
          confirmPwd: '',
        },
        rules: {
          loginName: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          password: [
            { required: true, message: '请输入密码',  trigger: 'blur'}
          ],
          confirmPwd: [
            { validator: checkConfirmPwd,  trigger: 'blur'},
          ],
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('您确定执行此操作吗？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.modifyPasswordForm.confirmPwd = null;
              this.$http.post('user/modifyPassword', {
                user: this.modifyPasswordForm
              }).then(res=>{
                let result = res.data;
                if(result.status == 1){
                  alert(result.desc);
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
      initData (data) {
        this.modifyPasswordForm = data;
      }
    }
  }
</script>
<style scoped>
  .addCode{
    width: 500px;
  }
</style>
