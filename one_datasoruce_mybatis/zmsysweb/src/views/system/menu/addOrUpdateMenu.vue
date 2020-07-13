<template>
    <div class="">
      <el-form :model="addOrUpdateMenu"
               status-icon
               :rules="rules" ref="addOrUpdateMenu"
               label-width="100px"
               class="demo-addOrUpdateMenu">
        <el-form-item label="菜单名称：" prop="name">
          <el-input v-model="addOrUpdateMenu.name" autocomplete="off" class="short_from"></el-input>
        </el-form-item>

        <!--  -->
        <el-form-item label="菜单类型：" prop="type">
          <el-radio-group v-model="addOrUpdateMenu.type" >
            <el-radio v-for="(item, index) in menuTypeList"
                      :key="index"
                      :disabled="type=='update'&&addOrUpdateMenu.type?true:false"
                      :label="item.value">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="图标：" prop="icon" v-if="addOrUpdateMenu.type!==3">
          <el-input v-model="addOrUpdateMenu.icon" autocomplete="off" class="short_from"></el-input>
        </el-form-item>

        <el-form-item label="排序：" prop="sort" v-if="addOrUpdateMenu.type!==3">
          <el-input v-model="addOrUpdateMenu.sort" autocomplete="off" class="short_from"></el-input>
        </el-form-item>

        <el-form-item label="父菜单：" prop="parentId" v-if="addOrUpdateMenu.type!==1">
          <treeSelect :multiple="false"
                      :searchable="true"
                      :defaultExpandLevel="1"
                      :options="menuTreeList"
                      :close-on-select="true"
                      :flat="true"
                      style="border-radius: 4px;width: 88%;"
                      v-model="addOrUpdateMenu.parentId"
                      :disabled="type=='update'&&addOrUpdateMenu.parentId?true:false"
                      :normalizer="normalizer" placeholder="请选择..." />
        </el-form-item>

        <el-form-item label="权限标识：" prop="permission" v-if="addOrUpdateMenu.type===3||addOrUpdateMenu.type===2">
          <el-input v-model="addOrUpdateMenu.permission" class="short_from"></el-input>
        </el-form-item>

        <el-form-item label="外部链接：" label-width="20%" prop="isJumpLink" v-if="addOrUpdateMenu.type!==3">
          <el-switch v-model="addOrUpdateMenu.isJumpLink"
                     active-text="是" :active-value="1"
                     inactive-text="否" :inactive-value="0">
          </el-switch>
        </el-form-item>

        <el-form-item :label="addOrUpdateMenu.type==2?'* 跳转地址：':'跳转地址：'" prop="url" v-if="addOrUpdateMenu.type!==3">
          <el-input v-model="addOrUpdateMenu.url" autocomplete="off" class="short_from"></el-input>
        </el-form-item>

        <el-form-item label="组件地址" prop="component" v-if="addOrUpdateMenu.type===2">
          <el-input v-model="addOrUpdateMenu.component" autocomplete="off" class="short_from"></el-input>
        </el-form-item>

        <el-form-item label="备注：" prop="remarks">
          <el-input type="textarea" v-model="addOrUpdateMenu.remarks" class="short_from"></el-input>
        </el-form-item>

        <el-form-item style="margin: 35px 0 0px 120px;">
          <el-button type="primary" @click="submitForm('addOrUpdateMenu')">确定</el-button>
          <el-button @click="resetForm('addOrUpdateMenu')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
  // import the component
  import TreeSelect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: '',
    data() {
      let checkSort = (rule, value, callback) => {
        if (value) {
          if (!/^\d+$/.test(value)) {
            callback(new Error('请输入数字'));
          }
        }
        callback();
      };
      let checkPermission = (rule, value, callback) => {
        if(this.addOrUpdateMenu.type === 3){
          if (!value) {
            callback(new Error('请输入权限标识'));
          }
        }
        callback();
      };
      let checkType = (rule, value, callback) => {
        if (value !== 3) {
        }
        if (!value) {
          callback(new Error('请输入数字'));
        }
        callback();
      };
      let checkUrl = (rule, value, callback) => {
        if (this.addOrUpdateMenu.type == 2) {
          if (!value) {
            callback(new Error('请输入跳转地址'));
          }
        }
        callback();
      };
      return {
        //修改还是新增
        type: '',
        oldMenuType: '',
        oldParentId: '',
        //菜单类型（目录、菜单、按钮）
        menuTypeList: [],
        //菜单树
        menuTreeList: [],
        isDisabled: true,
        validList: [{value: 1, name: '有效'}, {value: 0, name: '无效'}],
        addOrUpdateMenu: {
          name: '',
          type: 1,
          url: '',
          component: '',
          icon: '',
          sort: '',
          remarks: '',
          parentId: 1,
          permission: '',
          isJumpLink: 0,
        },
        rules: {
          name: [
            { required: true, message: '请输入名称',  trigger: 'blur'}
          ],
          type: [
            { validator: checkType,  trigger: 'change'}
          ],
          url: [
            { validator: checkUrl, trigger: 'blur'}
          ],
          component: [
            { required: true, message: '请输入组件地址',  trigger: 'blur'}
          ],
          sort: [
            { validator: checkSort,  trigger: 'blur'}
          ],
          parentId: [
            { required: true, message: '请输入地址',  trigger: 'blur'}
          ],
          permission: [
            { validator: checkPermission,  trigger: 'blur'}
          ],
        }
      };
    },
    methods: {
      //每生成一个节点都会调动
      normalizer (node) {
        if(!node.children || node.children.length==0){
          delete node.children;
        }
        //是否禁用
        node.isDisabled = false;
        if(this.addOrUpdateMenu.type === 2){
          //选择菜单时禁用菜单和按钮
          if(node.type == 2 || node.type == 3){
            node.isDisabled = true;
          }
        }else if(this.addOrUpdateMenu.type === 3){
          //选择按钮时禁用按钮和目录
          if(node.type == 1 || node.type == 3){
            node.isDisabled = true;
          }
        }
        return {
          id: node.id,
          label: node.name,
          children: node.children
        }
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let url = '';
            let success = '';
            let error = '';
            if(this.type === 'add'){
              url = 'menu/saveObject';
              success = '新增成功！';
              error = '新增失败！';
            }
            if(this.type === 'update'){
              if(this.addOrUpdateMenu.type !== this.oldMenuType){
                this.$message({
                  type: 'info',
                  message: '请勿修改菜单类型！'
                });
                return;
              }
              if(this.addOrUpdateMenu.parentId !== this.oldParentId){
                this.$message({
                  type: 'info',
                  message: '请勿修改父菜单！'
                });
                return;
              }

              url = 'menu/updateObject';
              success = '修改成功！';
              error = '修改失败！';
            }
            this.$confirm('您确定执行此操作吗？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {

              if(this.addOrUpdateMenu.type === 1){
                this.addOrUpdateMenu.parentId = null;
                this.addOrUpdateMenu.permission = null;
              }else if(this.addOrUpdateMenu.type === 3){
                this.addOrUpdateMenu.icon = '';
                this.addOrUpdateMenu.sort = '';
                this.addOrUpdateMenu.isJumpLink = 0;
                this.addOrUpdateMenu.url = '';
              }
              this.$http.post(url, {menu: this.addOrUpdateMenu}).then(res=>{
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
        this.addOrUpdateMenu.remarks = '';
      },
      getCodeValueList: function () {
        this.$utils.getCodeList("sys_menuType").then(res=>{
          this.menuTypeList = res;
        })
      },
      getMenuTree () {
        this.$http.post('menu/getAllMenuList', {}).then(res=>{
          let result = res.data;
          if(result.status == 1){
            this.menuTreeList = result.data;
          }else{
            alert(result.desc);
          }
        })
      },
      initData (type, data) {
        //加载菜单类型字典值
        this.getCodeValueList();
        //加载父菜单树
        this.getMenuTree();
        if(type == 'update'){
          this.addOrUpdateMenu = data;
          this.oldParentId = data.parentId;
          this.oldMenuType = data.type;
        }else{
          this.addOrUpdateMenu.type = data.type;
          this.addOrUpdateMenu.parentId = data.id;
        }
        this.type = type;
      }
    },
    computed: {
      dateChange() {
        return this.addOrUpdateMenu.type;
      }
    },
    watch: {
      dateChange(newValue, oldValue) {
        this.$refs['addOrUpdateMenu'].clearValidate();
        if(newValue == 3){
        }
      }
    },
    components: {
      TreeSelect
    }
  }
</script>

<style scoped>
  .short_from{
    width: 88%;
  }
</style>
