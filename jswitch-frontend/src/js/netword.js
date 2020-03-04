import axios from 'axios'
import iView from 'iview';

const network = {
    host: "http://127.0.0.1:8080",
    get: function (uri, params, func, exceptionFunc) {
      axios.get(this.host+uri, params)
        .then(response => {
          if (response.data.code != 100000) {
            iView.Notice.error({
              title: response.data.code,
              desc:  response.data.message,
              duration: 10
            });
            if (exceptionFunc!=null){
              exceptionFunc(response)
            }
            return null
          }
          return func(response.data.data)
        })
        .catch(err => {
          if (err){
            console.log(err)
            iView.Notice.error({
              title: "请求错误",
              desc:  "请求错误",
              duration: 10
            })
          }
        })
    },
    post: function (uri, params, func, exceptionFunc) {
      axios.post(this.host+uri, params)
        .then(response => {
          if (response.data.code != 100000) {
            iView.Notice.error({
              title: response.data.code,
              desc:  response.data.message,
              duration: 10
            });
            if (exceptionFunc!=null){
              exceptionFunc(response)
            }
            return null
          }
          return func(response.data.data)
        })
        .catch(err => {
          if (err){
            console.log(err)
            iView.Notice.error({
              title: "请求错误",
              desc:  "请求错误",
              duration: 10
            })
          }
        })
    },
}
export default network
