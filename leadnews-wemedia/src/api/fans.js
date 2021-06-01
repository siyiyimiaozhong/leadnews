import Request from  '@/utils/request'
import { API_FANS , API_FOLLOWER_PORTRAIT, API_FANS_AVATAR, API_CHANGE_FOLLOW_STATE, API_GET_FANS_STATISTIC} from  '@/constants/api'

//获取粉丝列表
export function getFollowers (data) {
   return Request({
       url:API_FANS,
       method:'post',
       data
   })
}
//获取性别粉丝统计数据
export function getFollowersPortrait () {
    return Request({
        url:API_FOLLOWER_PORTRAIT,
        method:'get'
    })
}

export function getFollowersAvatar(data) {
  return Request({
    url: API_FANS_AVATAR,
    method: 'post',
    data
  }).then(result => {
    if (result.code == 0) {
      return result.data;
    }
  })
}

export function changeFollowState(data) {
  return Request({
    url: API_CHANGE_FOLLOW_STATE,
    method: 'post',
    data
  })
}

export function getFansStatistics(data) {
  return Request({
    url: API_GET_FANS_STATISTIC,
    method: 'post',
    data
  })
}
