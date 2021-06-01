import Request from  '@/utils/request'
import { API_WEBSOCKET_AUTH} from  '@/constants/api'

/****获取授权client*****/
export function getAuthWs () {
    return Request({
        baseURL:process.env.WS_BASEURL,
        url:API_WEBSOCKET_AUTH,
        method:'post',
        params:{},
        data:{}
    })
}
