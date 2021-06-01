<template>
  <section class="result">
    <header>{{`共找到${total}条符合条件的内容`}}</header>
    <ul class="result-container">
      <li v-for="(item,index) in  articleList" :key='index' class='articles-item'>
         <img class="article-img" :src="getImage(item)"/>
         <dl class="article-content">
          <dt>
            <a @click="noAction" href="#" class="">{{item.title}}</a>
            <div  @click="operateBtn(item.id,$event)">
              <i data-type='up'  v-if="item.status == '9'&&item.enable=='0'" class="el-icon-upload2">上架</i>
              <i data-type='down'  v-if="item.status == '9'&&item.enable=='1'" class="el-icon-download">下架</i>
              <i data-type='modify'  v-if="item.status != '9'" class="el-icon-edit">修改</i>
              <i v-if="item.status != '9'" data-type='del' class="el-icon-delete">删除</i>
            </div>
          </dt>
          <dd>
            <el-tag class="draft" v-if="item.status == '0'">草稿</el-tag>
            <el-tag class="audit" v-if="item.status == '1'">待审核</el-tag>
            <el-tag class="audit" v-if="item.status == '3'">待人工审核</el-tag>
            <el-tag class="audit" v-if="item.status == '4'">待发布</el-tag>
            <el-tag class="publish" v-if="item.status == '8'">待发布</el-tag>
            <el-tag class="publish" v-if="item.status == '9'">已发表</el-tag>
            <el-tag class="unaudit" v-if="item.status == '2'">未通过审核:{item.reason}</el-tag>
            <el-tag class="delete" v-if="item.status == '100'">已删除</el-tag>
            <template v-if="item.status == '9'">
            <el-tag class="draft" v-if="item.enable == '0'">下架</el-tag>
            <el-tag class="audit" v-if="item.enable == '1'">上架</el-tag>
            </template>
          </dd>
          <dd class="time">{{dateFormat(item.publish_time)}}</dd>
        </dl>
      </li>
    </ul>
    <div class="pagination">
      <el-pagination
        layout="total,prev, pager, next"
        @current-change='pageChange'
        :current-page.sync='listPage.currentPage'
        :page-size="pageSize"
        :total="total">
      </el-pagination>
    </div>
  </section>
</template>

<script>
import DateUtil from '@/utils/date'
const avatar = require('@/assets/avatar.jpg')
export default {
  props: ['host','articleList','pageSize','total','changePage','deleteArticlesById','upOrDown'],
  data() {
    return {
       listPage:{
          currentPage:1
       }
    }
  },
  methods: {
    noAction : function(){
      alert('该功能暂未实现');
    },
    getImage : function(item){
      if(item.images){
        let temp = item.images.split(",")
        if(temp.length>0){
          return this.host+temp[0];
        }
      }
      return avatar
    },
    //页码变化 调用上层组件的方法
    pageChange (newPage) {
        this.changePage && this.changePage({page:newPage})
    },
    resetPage(){

    },
    //重新设置页码
    dateFormat (time) {
      return DateUtil.format13HH(time)
    },
    //操作
    operateBtn (Id,event) {
      switch(event.target.dataset.type){
        case 'modify':
        this.$router.push({path:'/article/publish',query:{articleId:Id}})
        break;
        case 'down':
          this.upOrDown(Id,0)
          break;
        case 'up':
          this.upOrDown(Id,1)
          break;
        case 'del':
          this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
           this.deleteArticlesById && this.deleteArticlesById(Id) //删除文件
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
        break;
      default :

      }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .result {
    background-color: #ffffff;
    text-align: left;
    border: 1px solid #e7e7e9;
    margin-top: 10px;
    header {
      border-bottom: 1px dashed #cccccc;
      margin: 0 5px;
      padding: 0 10px;
      font-size: 14px;
      height: 55px;
      line-height: 55px;
      color: #323745;
    }
    li {
      overflow: hidden;
      padding: 15px 5px;
      margin: 0 10px;
      border-bottom: 1px solid #f2f3f5;
       .draft {
           color:#FDC2A9;
           border-color: #FDC2A9;
           background: #FFFFFF;
        }
        .delete {
           color:rgb(243, 24, 8);
           border-color: rgb(243, 24, 8);
           background: #FFFFFF;
        }
        .audit {
           color:rgb(53, 160, 17);
           border-color: rgb(53, 160, 17);
           background: #FFFFFF;
        }
        .unaudit {
           color:#FFAEAE;
           border:none;
        }
    }
    .article-img {
      float: left;
      width: 150px;
      height: 100px;
    }
    .article-content {
      margin-left: 160px;
      text-align: left;
      dt {
        color: #333333;
        font-size: 14px;
        margin: 0 0 7px;
        div {
          float: right;
          i {
            margin-left: 20px;
            font-size: 12px;
            cursor: pointer;
          }
        }
      }
      .time {
        margin-bottom: 10px;
        span {
          color:#3296fa;
          margin-right: 8px;
        }
      }
      dd {
        font-size: 12px;
        margin: 5px 0;
        color: #999999;
        b {
          color: #333333;
        }
        span {
          margin-right: 10px;
        }
      }
    }
  }
  .el-pagination {
    text-align: center;
    margin: 20px 0 40px;
  }
</style>
