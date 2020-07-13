<template>
  <div class="addCode">
    <div class="addCode-from">
      <el-form :model="addOrUpdateUserForm" status-icon :rules="rules" ref="addOrUpdateUserForm" label-width="100px">
        <el-form-item label="登录名：" prop="loginName">
          <el-input v-model="addOrUpdateUserForm.loginName" :disabled="type==='update'" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password" v-if="type==='add'">
          <el-input type="password" v-model="addOrUpdateUserForm.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码：" prop="confirmPwd" v-if="type==='add'">
          <el-input type="password" v-model="addOrUpdateUserForm.confirmPwd" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名：" prop="userName">
          <el-input v-model="addOrUpdateUserForm.userName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱：" prop="email">
          <el-input v-model="addOrUpdateUserForm.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号：" prop="mobile">
          <el-input v-model="addOrUpdateUserForm.mobile" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态：" prop="valid">
          <el-radio-group v-model="addOrUpdateUserForm.valid">
            <el-radio :disabled="type==='update'&&addOrUpdateUserForm.id===1?true:false"
              v-for="(item, index) in validList" :key="index" :label="item.value">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色：">
          <el-checkbox-group v-model="roles">
            <el-checkbox v-for="(item, index) in roleList" :key="index" :label="item.id">
              {{item.name}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item style="margin: 35px 0 0px 120px;">
          <el-button type="primary" @click="submitForm('addOrUpdateUserForm')">确定</el-button>
          <el-button @click="resetForm('addOrUpdateUserForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'AddOrUpdateUser',
    data() {
      let checkConfirmPwd = (rule, value, callback) => {
          if (value) {
            if (this.addOrUpdateUserForm.password && this.addOrUpdateUserForm.password!==value) {
              callback(new Error('两次输入密码不一致'));
            }
          } else {
            callback(new Error('请输入确认密码'));
          }
          callback();
      };
      return {
        type: '',
        oldLoginName: '',
        validList: [{value: 1, name: '有效'}, {value: 0, name: '无效'}],
        roleList: [],
        addOrUpdateUserForm: {
          loginName: '',
          password: '',
          confirmPwd: '',
          userName: '',
          email: '',
          mobile: "",
          valid: 1,
        },
        roles: [],
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
          userName: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          email: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          mobile: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          valid: [
            { required: true, message: '请选择状态',  trigger: 'change '}
          ],
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let success = '';
            let error = '';
            if(this.type === 'add'){
              success = '新增成功！';
              error = '新增失败！';
              //新增时删除确认密码属性。
              this.addOrUpdateUserForm.confirmPwd = null;
            }
            if(this.type === 'update'){
              if(this.oldLoginName !== this.addOrUpdateUserForm.loginName){
                alert("请勿修改登陆名");
                return;
              }
              if(this.type=='update' && this.addOrUpdateUserForm.id===1 && this.addOrUpdateUserForm.valid!==1){
                alert("超级管理员用户无法禁用！");
                return;
              }
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
              this.$http.post('user/addOrUpdateObject', {
                user: this.addOrUpdateUserForm,
                checkedKeys: checkedKeys
              }).then(res=>{
                let result = res.data;
                if(result.status == 1){
                  alert(success);
                  location.reload();
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
      getRoleList () {
        this.roleList = [];
        this.$http.post('role/getObjectListByPage', {valid: 1}).then(res=>{
          let result = res.data;
          if(result.status == 1){
            this.roleList = result.data.list;
          }
        })
      },
      initData (type, data) {
        this.type = type;
        this.getRoleList();
        if(type == 'update'){
          this.addOrUpdateUserForm = data;
          this.oldLoginName = data.loginName;
          let roles = [];
          if(data.roleList && data.roleList.length>0){
            for (let i in data.roleList) {
              roles.push(data.roleList[i].id);
            }
            this.roles = roles;
          }
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
