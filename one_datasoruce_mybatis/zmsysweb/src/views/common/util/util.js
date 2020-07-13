
import http from "../../../utils/http";
import axios from "axios";

export default {
  //获取代码值
  // typename exp  "a"
  getCodeList(typeName = {}) {
    return new Promise((resolve, reject) => {
      http.post('code/getCodeValueList', {typeName: typeName}).then(res => {
        resolve(res.data.data);
      }).catch(error => {
        reject(null);
      })
    })
  },
  // typename exp  "a,b,c"
  getCodeValueListByMoreType(typeName = {}) {
    return new Promise((resolve, reject) => {
      http.post('code/getCodeValueListByMoreType', {typeName: typeName}).then(res => {
        resolve(res.data.data);
      }).catch(error => {
        reject(null);
      })
    })
  },
}
