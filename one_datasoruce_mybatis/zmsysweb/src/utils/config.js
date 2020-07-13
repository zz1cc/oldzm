


export default {
  type: 'baseURI_t', // 当前的环境
  baseURI: {
    baseURI_t: 'http://localhost:8090/zmsys/', //本地环境请求地址
    baseURI_dev: 'http://localhost:8090/', //测试环境请求地址
    baseURI_prod: 'http://localhost:8090/zmbk/', //正式环境请求地址
  },
  timeOut: 10000,
}
