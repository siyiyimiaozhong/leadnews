import axios from 'axios'
import BigInt from 'json-bigint'
import Router from  '@/router'
import { Message } from 'element-ui'
import { getUser,setUser } from   '@/utils/store'
import { API_USERIMAGES_ADD , API_HEAD} from  '@/constants/api'
import LoadingManage from './loading'
// create an axios instance
const service = axios.create({
  baseURL: '/wemedia/MEDIA', // api 的 base_url
  timeout: 10000, // request timeout
  transformResponse (data) {
    if(data)
       return BigInt.parse(data)  //由于后端的数据库对id进行了变更 所以这里必须采用json-bigint插件来进行处理 保证数据正确
  }

})
const IsImgUpload = (config) => {
    return  ((config.url == API_USERIMAGES_ADD && config.method == 'post') || (config.url == API_HEAD && config.method == 'patch'))
}
//request
service.interceptors.request.use(
  config => {
      //LoadingManage.openLoading(); //打开弹层
      let Authorization = getUser () ;
      if(Authorization && Authorization.token){
        //如果当前缓存中存在用户令牌 后台接口的参数格式
        if(!IsImgUpload(config)){
          /****在非图片上传场景下 给默认的上传格式****/
          config.headers['Content-Type'] = 'application/json'
        }else{
          config.headers['Content-Type'] = 'multipart/form-data'
        }
        config.headers['token'] =Authorization.token
      }
      // 让每个请求携带token-- ['X-Token']为自定义key
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response
service.interceptors.response.use(
  /**
   * 监控拦截 如果出现 异常 则直接终止请求链
   */
  response => {
    if(response.headers&&response.headers.refresh_token){
      let user = getUser();
      if(user){
        user.token = response.headers.refresh_token
        setUser(user)
      }
    }
    //LoadingManage.closeLoading() //关闭loading弹层
    //直接返回返回体中的结构
    if(response.data&&response.data.code){
      let code = response.data.code
      if(code>49&&code<70){
        Router.replace({path:'/login'})
      }
    }
    return response.data
  },
  error => {
    LoadingManage.closeLoading() //关闭loading弹层
    let message = ''
    let  code = error.response ? error.response.status : ''
    switch(code){
      case 400:
       message = '请求参数错误'
      break;
      case 401:
      message = 'token过期或未传'
      break;
      case 403:
       message = '操作失败'
       break;
      case 404:
       message = '找不到请求的url'
       // message = '手机号不正确'
       break;
      case 500:
      message = '服务器异常'
      break;
      case 507:
      message = '服务器数据库异常'
       break;
       default :
       message = '处理异常'
    }
   // message = message + ':' +  error.response ? error.response.data.message : error.response.data.message
      Message({
        message,
        type: 'warning',
        onClose:code  == '401' ? () =>   Router.replace({path:'/login'}) : null
      })
     return new Promise(function(){}) //终止当前的promise链
  }
)


export default service
