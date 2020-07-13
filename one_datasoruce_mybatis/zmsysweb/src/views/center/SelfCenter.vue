<template>
  <div class="">
    <div class="source">
      <div class="search-collapse-l">
        <div class="show-header">
          <span>个人信息</span>
        </div>
        <div class="photo">
          <el-upload
            class="avatar-uploader"
            action="uploadPhoto"
            :show-file-list="false"
            :http-request="uploadPhoto"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="user.photo" :src="user.photo" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <div class="msg">
          <ul class="list-group list-group-striped">
            <li class="list-group-item">
              <i class="el-icon-s-tools"></i>
              <b class="font-noraml">登录账号</b>
              <p class="pull-right">{{user.loginName}}</p>
            </li>
            <li class="list-group-item">
              <i class="el-icon-s-tools"></i>
              <b class="font-noraml">昵称</b>
              <p class="pull-right">{{user.userName}}</p>
            </li>
            <li class="list-group-item">
              <i class="el-icon-s-tools"></i>
              <b class="font-noraml">手机</b>
              <p class="pull-right">{{user.mobile}}</p>
            </li>
            <li class="list-group-item">
              <i class="el-icon-s-tools"></i>
              <b class="font-noraml">邮箱</b>
              <p class="pull-right">{{user.email}}</p>
            </li>
            <li class="list-group-item">
              <i class="el-icon-s-tools"></i>
              <b class="font-noraml">创建时间</b>
              <p class="pull-right">{{user.createdTime?user.createdTime.substring(0, 10):''}}</p>
            </li>
          </ul>
        </div>
      </div>

      <div class="search-collapse-l" style="margin-left: 20px;width: 65%;min-height: 350px;">
        <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 20px;">
          <el-tab-pane label="用户信息" name="first">

            <el-form ref="form" :model="user" :rules="rules" label-width="30%" style="margin-top: 25px;">
              <el-form-item label="昵称：" prop="userName" label-width="100px" style="width: 60%">
                <el-input v-model="user.userName"></el-input>
              </el-form-item>
              <el-form-item label="手机号：" prop="mobile" label-width="100px" style="width: 60%">
                <el-input v-model="user.mobile"></el-input>
              </el-form-item>
              <el-form-item label="邮箱：" prop="email" label-width="100px" style="width: 60%">
                <el-input v-model="user.email"></el-input>
              </el-form-item>
              <el-form-item style="margin-top: 40px;margin-left: 180px;width: 10%">
                <el-button type="primary" @click="onSubmit" style="width: 80px;">保存</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="second">

          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        imageUrl: 'http://demo.ruoyi.vip/img/profile.jpg',
        user: {},

        activeName: 'first',
        rules: {
          userName: [
            { required: true, message: '请输入昵称', trigger: 'blur' }
          ],
          mobile: [
            { required: true, message: '请输入手机号', trigger: 'blur' }
          ],
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' }
          ],
        }
      };
    },
    methods: {
      openModifyBind (type) {
        alert(type)
      },
      handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      uploadPhoto(fileData) {
        let param = new FormData();
        param.append('file',fileData.file);
        this.$http.upload(param).then(res=>{
          let result = res.data;
          if(result.status == 1){
            alert("上传成功");
            this.user.photo = result.data[0].path;
          } else {
            alert("上传失败");
          }
        })
      },
      getCurrentUser: function () {
        this.$http.post('center/getCurrentUser', {}).then(res=>{
          let result = res.data;
          if(result.status == 1){
            this.user = result.data;
          }
        })
      },

      handleClick(tab, event) {
        console.log(tab, event);
      },
      onSubmit () {
        alert(1)
      }
    },
    //页面初始化执行函数
    mounted: function(){
      this.getCurrentUser();
    },
  }
</script>
<style scoped>
  .search-collapse-l {
    width: 30%;
    float: left;
    min-height: 480px;
    background: #fff;
    border-radius: 6px;
    padding: 20px 0 5px 10px;
    box-shadow: 1px 1px 3px rgba(0,0,0,.2);
  }
  .show-header{
    padding: 0 0 18px 10px;
    border-bottom: 1px solid #ebeef5;
    box-sizing: border-box;
  }
  .photo{
    margin: 20px auto;
    padding-right: 5px;
    text-align: center;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 150px;
    height: 150px;
    line-height: 150px;
    text-align: center;
  }
  .avatar {
    width: 150px;
    height: 150px;
    display: block;
  }
  .list-group {
    padding-left: 0;
    margin-bottom: 20px;
    width: 92%;
  }
  .list-group-item {
    background-color: inherit;
    border: 1px solid #e7eaec;
    display: block;
    margin-bottom: -1px;
    padding: 10px 15px;
    position: relative;
  }
  .list-group-striped > .list-group-item {
    border-left: 0;
    border-right: 0;
    border-radius: 0;
    padding-left: 0;
    padding-right: 0;
  }
  .list-group-item i{
    margin-left: 10px;
  }
  .font-noraml{
    font-weight: 400;
  }
  .pull-right{
    float: right;
    margin: 0 0 10px;
  }

  .list-info{

  }
  .list-info .list-info-item{
    list-style: none;
    padding: 10px;
  }
</style>
