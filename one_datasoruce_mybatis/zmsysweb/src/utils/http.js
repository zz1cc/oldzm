
import config from './config';
import axios from 'axios';
import{ Message } from 'element-ui';  //element库的消息提示
import{ MessageBox } from 'element-ui';  //element库的消息提示
import router from '../router/routers'

axios.defaults.withCredentials = true;  //设置cookie可以跨域
axios.defaults.timeout = 10000;  //相应时间
axios.defaults.baseURL = config.baseURI[config.type];
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

axios.interceptors.request.use(config => {//请求之前(可以设置token)
  return config
},error =>{
  Message.error(error)
  return Promise.reject(error);
});
axios.interceptors.response.use(response => {//数据拿到之后
  if(response){
    if(response.data.status == 100){
      MessageBox.alert('登录已过期，请重新登录！', '系统提示', {
        confirmButtonText: '确定',
        callback: action => {
          //window.location.href = '/login';
          router.push('/login')
        }
      });
    }else if(response.data.status == 101){
      Message.error('权限不足');
      window.history.back();
    }
  }
  return response
},error => {
  console.log(error)
  Message.error('网络异常');
  return Promise.reject(error.response);;
});

export default {
  post(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios.post(url, params).then(res => {
        resolve(res);
      }).catch(error => {
        reject(error);
      })
    })
  },
  post1(url,data){//post请求
    return new Promise((resolve, reject) => {
      axios({
        method:'post',
        url,
        data: data,
      }).then(res => {
        resolve(res);
      },err => {
        reject(error);
      })
    })
  },
  upload(data){//post请求
    return new Promise((resolve, reject) => {
      axios.post("attachment/upload", data ,{headers: {"content-type": "multipart/form-data"}}).then(res => {
        resolve(res);
      }).catch(error => {
        reject(error);
      })
    })
  }
}
