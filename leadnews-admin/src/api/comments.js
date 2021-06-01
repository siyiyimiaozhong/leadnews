import Request from  '@/utils/request'
import { API_COMMENT_LIST , API_CLOSECOMMENTS , API_ADMIRECOMMENT , API_CANCELADMIRECOMMENT} from  '@/constants/api'

/****获取评论列表*****/
export function getCommentList (data) {
    return Request({
        url:API_COMMENT_LIST,
        method:'post',
        params:{},
        data
    })

}
/***关闭或打开某个文章的评论  params 和data为不同类型参数*****/
export function  closeOrOpenComment (params,data) {
   return Request({
          url:API_CLOSECOMMENTS,
          method:'put',
          params,
          data
    })
}
//对某个评论进行点赞
export function admireComment (data) {
    return Request({
        url:API_ADMIRECOMMENT,
        method:'post',
        data
  })
}
//取消点赞
export function  cancleAdmire (target) {
    return Request({
        url:API_CANCELADMIRECOMMENT + target,
        method:'delete',

  })
}
//修改置顶
export function  changeTop (target,sticky) {
   return Request({
       url:`/comments/${target}/sticky`,
       method:'put',
       data:{sticky}
     })
}
//添加回复内容
export function  addComments (data) {
    return Request({
        url:API_COMMENTS,
        method:'post',
        data
    })
}
