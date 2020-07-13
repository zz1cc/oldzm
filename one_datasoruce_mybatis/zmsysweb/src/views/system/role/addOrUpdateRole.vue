<template>
  <div class="addCode">
    <div class="addCode-from">
      <el-form :model="addOrUpdateRoleForm" status-icon :rules="rules" ref="addOrUpdateRoleForm" label-width="100px" class="demo-addOrUpdateRoleForm">
        <el-form-item label="名称：" prop="name">
          <el-input v-model="addOrUpdateRoleForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序：" prop="sort">
          <el-input v-model="addOrUpdateRoleForm.sort" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="valid">
          <el-radio-group v-model="addOrUpdateRoleForm.valid">
            <el-radio v-for="(item, index) in validList" :key="index" :label="item.value">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input type="textarea" v-model="addOrUpdateRoleForm.note"></el-input>
        </el-form-item>
        <el-form-item label="菜单权限" prop="note">
          <el-tree
            :data="menuTreeList"
            ref="menuTree"
            show-checkbox
            node-key="id"
            :default-checked-keys="defaultCheckedList"
            :props="defaultProps">
          </el-tree>
        </el-form-item>
        <el-form-item style="margin: 35px 0 0px 120px;">
          <el-button type="primary" @click="submitForm('addOrUpdateRoleForm')">确定</el-button>
          <el-button @click="resetForm('addOrUpdateRoleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'AddOrUpdateRole',
    data() {
      let checkSort = (rule, value, callback) => {
        if (value) {
          if (!/^\d+$/.test(value)) {
            callback(new Error('请输入数字'));
          }
        }
        callback();
      };
      return {
        type: '',
        validList: [{value: 1, name: '有效'}, {value: 0, name: '无效'}],
        menuTreeList: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        defaultCheckedList: [],
        addOrUpdateRoleForm: {
          name: '',
          sort: '',
          valid: 1,
          note: "",
        },
        rules: {
          name: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          sort: [
            { validator: checkSort,  trigger: 'blur'}
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
            }
            if(this.type === 'update'){
              success = '修改成功！';
              error = '修改失败！';
            }
            this.$confirm('您确定执行此操作吗？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              let checkedKeys = '';
              let halfCheckKeys = '';
              //获取子类和选中的所有父类id
              let menuIdList = this.$refs.menuTree.getCheckedKeys();
              let halfMenuIdList = this.$refs.menuTree.getHalfCheckedKeys();
              if(menuIdList){
                for (let i in menuIdList) {
                  checkedKeys += menuIdList[i] + ",";
                }
                if(checkedKeys !== ''){
                  checkedKeys = checkedKeys.substring(0, checkedKeys.length-1);
                }
              }
              if(halfMenuIdList){
                for (let i in halfMenuIdList) {
                  halfCheckKeys += halfMenuIdList[i] + ",";
                }
                if(halfCheckKeys !== ''){
                  halfCheckKeys = halfCheckKeys.substring(0, halfCheckKeys.length-1);
                }
              }
              this.$http.post('role/addOrUpdateObject', {
                role: this.addOrUpdateRoleForm,
                checkedKeys: checkedKeys,
                halfCheckKeys: halfCheckKeys,
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
        this.defaultCheckedList = [];
      },
      getMenuTree () {
        this.$http.post('menu/getAllMenuList', {}).then(res=>{
          let result = res.data;
          if(result.status == 1){
            this.menuTreeList = result.data;
          }
        })
      },
      initData (type, data) {
        this.getMenuTree();
        if(type == 'update'){
          this.addOrUpdateRoleForm = data;
          let defaultCheckedList = [];
          let menuList = data.menuList;
          if(menuList){
            for(let i in menuList){
              //半选中菜单不默认选中，否则没有选中的子菜单也会被选中
              if(menuList[i].halfCheck !== 1){
                defaultCheckedList.push(menuList[i].id);
              }
            }
            this.defaultCheckedList = defaultCheckedList;
          }
        }
        this.type = type;
      }
    }
  }
</script>
<style scoped>
  .addCode{
    width: 500px;
  }
</style>
