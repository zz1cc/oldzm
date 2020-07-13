<template>
  <div id="login" class="signin">
    <div id="login-from" class="login-from">
      <div class="source">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm" style="color: #fff">
          <el-form-item label="用户名" prop="loginName">
            <el-input v-model="ruleForm.loginName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
      data() {
          var checkLoginName = (rule, value, callback) => {
              if (value === '') {
                  callback(new Error('请输入用户名'));
              } else {
                  callback();
              }
          };
          var checkPassword = (rule, value, callback) => {
              if (value === '') {
                  callback(new Error('请再次输入密码'));
              } else {
                  callback();
              }
          };
          return {
              ruleForm: {
                  loginName: '',
                  password: '',
              },
              rules: {
                  loginName: [
                      { validator: checkLoginName, trigger: 'blur' }
                  ],
                  password: [
                      { validator: checkPassword, trigger: 'blur' }
                  ],
              }
          };
      },
      methods: {
          submitForm(formName) {
              this.$refs[formName].validate((valid) => {
                  if (valid) {
                    this.$http.post('u/login', {
                      loginName: this.ruleForm.loginName,
                      password: this.ruleForm.password,
                    }).then(res=>{
                      let result = res.data;
                      if(result.status == 1){
                        this.$router.push({ path:'/home/index'})
                      }else{
                        alert(result.desc);
                      }
                    })
                  } else {
                      console.log('error submit!!');
                      return false;
                  }
              });
          },
          resetForm(formName) {
              this.$refs[formName].resetFields();
          }
      }
  }
</script>

<style>
  .signin {
    background:url(../../assets/img/bg/bg_2.jpg) no-repeat center fixed;
    -webkit-background-size:cover;
    -moz-background-size:cover;
    -o-background-size:cover;
    background-size:cover;
    color: rgba(255,255,255,.95);
    height:100%;
  }

  .login-from{
    width:456px;
    position: absolute;
    top: 40%;
    left:54%;
    z-index:2;
    /*border: 1px solid #ebebeb;
    border-radius: 3px;
    transition: .2s;*/
  }

  .login-from .fa-unlock-alt,.login-from .fa-user{
    position:absolute;
    top:2px;
    color:#d7dee3;
    font-size:28px;
    z-index:2;
  }


  /*自定义element-ui的表单CSS*/
  .login-from .el-form-item--small.el-form-item{
    margin-bottom:35px;
  }
  .el-form-item__label,.el-input__inner{color: #fff}


  .login-from .el-input__inner{
    border-top:none;
    border-left:none;
    border-right:none;
    border-radius: 0;
    background-color: transparent;
    padding:0 26px;
    font-size:14px;
    margin-top:6px;
  }

  .login-from .el-form-item__error{
    padding-top:6px;
  }

  .login-from .el-input--small{
    font-size: 18px;
  }




</style>
