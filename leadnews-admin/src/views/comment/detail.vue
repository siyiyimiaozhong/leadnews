<template>
  <div class="comment-container">
    <header><a>返回全部图文</a></header>
    <div class="detail-wrap">
      <h1>{{art_title}}</h1>
      <span>{{art_pubdate}}</span>
    </div>
    <div class="comments_list">
       <div class="comment_item" v-for="(item,index) in  comment_list" :key="index">
           <div class="comment_article">
           <img class="head_img" :src="item.aut_photo ? item.aut_photo : require('@/assets/avatar.jpg')" />
            <div class="comment_info">
              <div class='comment_info_first'><span class='comment_info_title'>{{item.aut_name}}</span><span class='comment_info_date'>{{item.pubdate}}</span></div>
              <div class='comment_info_second'>
                {{
                  item.content
                }}
              </div>
              <div class='comment_info_third'>
                 <div >
                   <img class='admire' src='@/assets/admire.png' />
                    <span class='admire_count'>
                      {{item.like_count}}
                    </span>
                 </div>
                 <div>
                   <img class='reply' src='@/assets/reply.png' />
                    <span class='reply_count'>
                      {{item.reply_count}}
                    </span>
                 </div>
              </div>
            </div>
            <div class="comment_operate" @click="commentOperate(item,$event)">
              <a data-type='top' href="javascript:void(0)">{{item.is_top ? '取消推荐' : '推荐'}}</a>
              <a data-type='reply' href="javascript:void(0)">回复</a>
              <a data-type='admire' href="javascript:void(0)">{{item.is_liking ? '取消点赞' : '点赞'}}</a>
            </div>
           </div>
          <div class='reply_content' v-if="item.showReply">
               <div class='replt_editor'>
                 <div class='replt_editor_content'>
                 <img class="head_img" :src="require('@/assets/avatar.jpg')" />
                 <el-input type="textarea" v-model="replyContent" class="textareaContent" :rows="3"  placeholder="请输入内容" ></el-input>
                 </div>
                 <div class='btn_group'>
                   <el-button size="small" @click="cancelReply(item)">取消</el-button>
                   <el-button size="small" @click="okReply(item)" type="primary">回复</el-button>
                 </div>
               </div>
               <div class='comment_article replylist' v-for="(reply,rindex) in item.replyList" :key="rindex">
                 <img class="head_img" :src="reply.aut_photo ? reply.aut_photo : require('@/assets/avatar.jpg')" />
                  <div class="comment_info">
                   <div class='comment_info_first'><span class='comment_info_title'>{{reply.aut_name}}</span><span class='comment_info_date'>{{reply.pubdate}}</span></div>
                     <div class='comment_info_second'>
                      {{reply.content}}
                   </div>
                 </div>
               </div>
              <div @click="loadMoreReply(item)" :class="['replylist_last_more',(item.last_id !== item.end_id) || 'nomore']">{{(item.last_id !== item.end_id) ? '加载更多': '没有了' }}  </div>
            </div>
       </div>
         <div @click="loadMoreComments" :class="['replylist_last_more',showLoadMore || 'nomore']">{{showLoadMore ? '加载更多' : '没有了' }}  </div>
    </div>
  </div>
</template>
<script>
import { getCommentList , admireComment , cancleAdmire , changeTop , addComments} from  '@/api/comments'
import { async } from 'q';
export default {
  name: 'Detail',
  data() {
    return {
      replyContent:null,//当前的回复内容
      art_title:null,//文章标题
      art_pubdate:null,//文章时间
      comment_list:[],//评论列表
      end_id:null,//结束ID
      last_id:null //当页的结束ID 要作为下次的请求参数
    }
  },
  computed:{
      showLoadMore () {
         return this.end_id !== this.last_id  //最后一页的id 不等于最后的id时  说明还有下一页
      }
  },
  created () {
    let { articleId } = this.$route.query;//获取文章Id
    if(!articleId) {
        this.$router.replace({path:'/comment/list'}) //如果不存在id则回到列表页
        return;
    }
    this.getCommentList({
      source:articleId,
      type:'a' //首次查询的是文章评论
    })
  },
  methods: {
    /*****获取评论*****/
    async getCommentList (params) {
       let result = await getComments(params)
       this.art_title = result.art_title  //标题文章
       this.art_pubdate = result.art_pubdate //发布时间
       this.last_id = result.last_id //当页结束ID
       this.end_id = result.end_id  //最后一个ID
       //拉取评论数组  如果偏移量不为空 则需要将此次返回结果和之前的结果合并
       this.comment_list = params.offset ? this.comment_list.concat(result.results) : result.results
     },
     /****取消回复*****/
     cancelReply (item) {
        item.showReply = false //关闭显示回复评论
        this.$forceUpdate() //强制刷新
     },
     /****回复内容******/
    async okReply (item) {
        if(!this.replyContent){
            this.$message({type:'warning',message:'回复内容不能为空'})
            return
        }
        let { articleId } = this.$route.query;//获取文章Id
        //进行评论
        let result =  await  addComments({
           target:item.com_id,
           content:this.replyContent,
           art_id:articleId
           })
           if(result){
              this.replyContent = null
              item.reply_count = item.reply_count + 1; //评论数+1
              //因为不想重新拉取整体的数量 所以这里重新拉取当前的评论内容
            let  newComments = await  getComments({type:'c',source:item.com_id})
            let replyList = newComments.results;//记录当前回复的当页记录
            item.last_id = newComments.last_id; //记录当前回复的当页最后一条id
            item.end_id = newComments.end_id; //记录当前回复的最后一条id
            item.replyList =  replyList; //强制刷新第一屏的内容
            this.$forceUpdate();
        }
     },
     /***推荐 回复 点赞*****/
    async commentOperate (item,event) {
       let { articleId } = this.$route.query;//获取文章Id
       let {target:{dataset:{type:operatetype}}} = event; //解构得到事件操作类型
       if (operatetype == 'top'){
          //推荐置顶
         let result = changeTop(item.com_id,!item.is_top)
         this.$message({ message:'操作成功', type:'success'})
         this.getComments({source:articleId,type:'a'})
       }
       else if (operatetype == 'reply') {
         //注意这种方式 必须采用强制更新 否则 UI会和视图不同步
         this.comment_list.map(item => item.showReply && (item.showReply = false)) //先将其他的回复操作关掉 然后再设置当前的回复打开
         item.showReply = true ; //显示回复
         this.replyContent = null ;//每次切换回复的内容 要将模型的数据设置为空
         //首先查找回复项
         if (item.reply_count && (!item.replyList || !item.replyList.length)) {
           //如果回复数大于零 且数据项中没有回复评论 才去进行拉取新回复数据  否则只进行回复框的显示
          //调用封装的外部函数 而非自身的方法
          let  result = await getComments({type:'c',source:item.com_id})
          let replyList = result.results;//记录当前回复的当页记录
          item.last_id = result.last_id; //记录当前回复的当页最后一条id
          item.end_id = result.end_id; //记录当前回复的最后一条id
          item.replyList = item.replyList ? item.replyList.concat(replyList) : replyList;
        }
         this.$forceUpdate(); //由于改变的是数组内部的对象 所以这里要进行强制刷新 否则UI会和视图不同步
       }
       else if (operatetype == 'admire') {
          //点赞或者取消点赞 根据状态进行判断
         let result = item.is_liking ? await cancleAdmire(item.com_id) : await  admireComment({ target:item.com_id})
         this.$message({message:'操作成功', type:'success'})
         this.getComments({ source:articleId,type:'a'}) //重新拉取文章
       }
     },
      //加载更多地评论
      async loadMoreComments () {
         if(this.showLoadMore){
             let { articleId } = this.$route.query;//获取文章Id
             this.getComments({ source:articleId,type:'a',offset:this.last_id})
             //重新拉取文章  加载更多需要传递偏移id 作为下一次加载的结果 然后将结果追加
         }
     },
     //加载更多地回复
     async loadMoreReply (item) {
        if(item.last_id !== item.end_id) {
            let  newComments = await  getComments({type:'c',source:item.com_id,offset:item.last_id})
            let replyList = newComments.results;//记录当前回复的当页记录
            item.last_id = newComments.last_id; //记录当前回复的当页最后一条id
            item.end_id = newComments.end_id; //记录当前回复的最后一条id
            item.replyList = item.replyList.concat(replyList)  //追加新的内容
            this.$forceUpdate(); //强制刷新
        }
     }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.comment-container {
  background-color: #ffffff;
  text-align: left;
  border: 1px solid #e7e7e9;
  header {
    border-bottom: 1px dashed #cccccc;
    margin: 0 5px;
    padding: 0 10px;
    font-size: 14px;
    height: 55px;
    line-height: 55px;
    color: #323745;
    a {
      color: #3296fa;
    }
  }
  .detail-wrap {
    padding: 30px;
    h1 {
      font-weight: normal;
      padding: 0;
      margin: 0;
      margin-bottom: 5px;
      & + span {
        font-size: 14px;
        color: #888888;
      }
    }
  }
  .comments_list {
    margin: 30px auto;
    width:95%;
    border:1px solid #CCCCCC;
    .replylist_last_more {
      text-align: center;
      height:40px;
      line-height: 40px;
      cursor: pointer;
      &.nomore {
        color:#CCCCCC;
      }
    }
    .comment_item {
      display: flex;
      flex-direction: column;
        .reply_content {
            background: #F4F5F6;
             .replt_editor {

                 display: flex;
                 flex-direction: column;
                 margin-left:100px;
                 margin-top:40px;
                  .btn_group {
                       display: flex;
                       flex-direction: row-reverse;
                       margin-top: 10px;
                     .el-button {
                       margin-right: 20px;
                     }
                  }
                 .replt_editor_content {
                      display: flex;
                      flex-direction: row;
                       .head_img {
                          width: 40px;
                          height: 40px;
                          border-radius: 50%;
                        }
                      .textareaContent {
                          width:90%;
                          margin-left: 20px;
                      }

                  }
               }
             }
       .comment_article {
        display: flex;
        flex-direction:row;
        padding: 20px 20px;
        width:100%;
        min-height: 150px;
        border-bottom: 1px solid #CCCCCC;
        position: relative;
        .head_img {
         width: 50px;
         height: 50px;
         border-radius: 50%;
        }
        &.replylist {
          border-bottom:none;
          padding:10px 100px;
          min-height: 80px;
          background: #F4F5F6;
          box-sizing: border-box;
          .head_img {
             width: 40px;
            height: 40px;
          }

        }
      .comment_info {
        display: flex;
        flex-grow: 1;
        flex-direction: column;
        justify-content: space-between;
        height: 50px;
        margin-left: 20px;
        .comment_info_first {
          width: 100%;
          display: flex;
          flex-direction: row;
          justify-content: space-between;
         .comment_info_title {
          color:rgb(19, 22, 22)
        }
         .comment_info_date {
          font-size: 12px;
          color:#BBBBBB;
         }
        }
        .comment_info_second {
          font-size:13px;
          margin-top:40px;
        }
      }
      .comment_info_third {
        margin-top:20px ;
         display: flex;
          .admire,.reply {
            width: 15px;
            height:15px;
            margin-left:10px;
          }
        }
      .comment_operate {
        position: absolute;
        right: 40px;
        bottom: 20px;
        a {
          color:#3296fa;
          padding: 0 10px;
          font-size: 13px;
        }
      }

    }
   }
  }

}
</style>
