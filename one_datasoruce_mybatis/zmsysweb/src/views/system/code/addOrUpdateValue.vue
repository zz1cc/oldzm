<template>
  <div class="addCode">
    <div class="addCode-from">
      <el-form :model="addOrUpdateTypeForm" status-icon :rules="rules" ref="addOrUpdateTypeForm" label-width="100px" class="demo-addOrUpdateTypeForm">
        <el-form-item label="名称：" prop="name">
          <el-input v-model="addOrUpdateTypeForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="值：" prop="value">
          <el-input v-model.number="addOrUpdateTypeForm.value" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型：" prop="typeName">
          <el-input v-model="addOrUpdateTypeForm.typeName" :disabled="isTypeNameDisabled" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态：" prop="valid">
          <el-radio-group v-model="addOrUpdateTypeForm.valid">
            <el-radio v-for="(item, index) in validList" :key="index" :label="item.value">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序：" prop="sort">
          <el-input v-model="addOrUpdateTypeForm.sort" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input type="textarea" v-model="addOrUpdateTypeForm.remarks"></el-input>
        </el-form-item>
        <el-form-item style="margin: 35px 0 0px 120px;">
          <el-button type="primary" @click="submitForm('addOrUpdateTypeForm')">确定</el-button>
          <el-button @click="resetForm('addOrUpdateTypeForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'AddCode',
    data() {

      const checkSort = (rule, value, callback) => {
          if (value) {
            if (!/^\d+$/.test(value)) {
              callback(new Error('请输入数字'));
            }
          }
          callback();
        };

      return {
        type: '',
        isTypeNameDisabled: false,
        oldTypeName: '',
        validList: [{value: 1, name: '有效'}, {value: 0, name: '无效'}],
        addOrUpdateTypeForm: {
          name: '',
          value: '',
          typeName: '',
          valid: 1,
          sort: '',
          remarks: "",
        },

        rules: {
          name: [
            { required: true, message: '请输入名称', trigger: 'blur'}
          ],
          value: [
            { required: true, message: '请输入值', trigger: 'blur'},
            { type: 'number', message: '请输入数字值', trigger: 'blur'}
          ],
          typeName: [
            { required: true, message: '请输入类型名称', trigger: 'blur'}
          ],
          valid: [
            { required: true, message: '请选择状态', trigger: 'change '}
          ],
          sort: [
            { validator: checkSort,  trigger: 'blur '}
          ],
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let url = '';
            let success = '';
            let error = '';
            if(this.oldTypeName !== this.addOrUpdateTypeForm.typeName){
              alert("请勿改动不可修改的值");
              return;
            }
            if(this.type === 'add'){
              url = 'code/addOneValueType';
              success = '新增成功！';
              error = '新增失败！';
            }
            if(this.type === 'update'){
              url = 'code/updateOneCodeValueById';
              success = '修改成功！';
              error = '修改失败！';
            }
            this.$confirm('您确定执行此操作吗？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.$http.post(url, {
                codeValue: this.addOrUpdateTypeForm
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
        this.addOrUpdateTypeForm.remarks = '';
      },
      getList: function () {

      },
      initData (type, data) {
        if(type == 'update'){
          this.getList();
          this.addOrUpdateTypeForm = data;
        }else{
          this.addOrUpdateTypeForm.typeName = data.typeName;
          this.addOrUpdateTypeForm.typeId = data.typeId;
        }
        this.oldTypeName = this.addOrUpdateTypeForm.typeName;
        this.isTypeNameDisabled = true;
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
