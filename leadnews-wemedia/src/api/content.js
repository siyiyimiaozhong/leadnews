import Request from '@/utils/request'
import  { API_ARTICLES_INFO,API_STATISTICS_NEWS ,API_ARTICLES_UPDOWN, API_SEARCHARTICELS,API_ARTICLES_DELETE } from  '@/constants/api'

//获取统计数据
export function getNewsStatistics(data) {
  return Request({
    url:API_STATISTICS_NEWS,
    method:'post',
    params:{},
    data:data
  })
}

//获取文章列表
export function getArticleById (articlesId) {
  return Request({
    url:API_ARTICLES_INFO,
    method:'post',
    params:{},
    data:{id:articlesId}
  })
}
export function deleteArticles (articlesId) {
  return  Request({
    url:API_ARTICLES_DELETE ,
    method:'post',
    params:{},
    data:{id:articlesId}
  })
}
//搜索文章
export function  searchArticle (data) {
  return   Request({
    url:API_SEARCHARTICELS,
    method:'post',
    data,
    params:{}
  })
}
//上下架
export function  upDownArticle (data) {
  return   Request({
    url:API_ARTICLES_UPDOWN,
    method:'post',
    data,
    params:{}
  })
}
