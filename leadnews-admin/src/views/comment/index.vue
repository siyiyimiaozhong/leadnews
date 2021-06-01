<template>
  <div class="comment-container">
    <el-table
      ref="multipleTable"
      :data="commentData"
      tooltip-effect="dark"
      style="width: 100%"
      :header-cell-style="{backgroundColor:'#fbfbfb'}"
      @selection-change="handleSelectionChange">
      <el-table-column
        label="标题"
        prop="title"
       >
      </el-table-column>
      <el-table-column
        label="评论状态"
        prop="comment_status"
        align="center"
        width="120">
        <template slot-scope="scope">{{ scope.row.comment_status ? '正常' : '关闭' }}</template>
      </el-table-column>
      <el-table-column
        prop="total_comment_count"
        label="总评论数"
        align="center"
        width="120"
       >
      </el-table-column>
      <el-table-column
        prop="fans_comment_count"
        label="粉丝评论数"
        align="center"
        width="120"
        >
      </el-table-column>
       <el-table-column
      label="操作"
      width="300">
      <template slot-scope="scope">
        <el-button @click="modifyComment(scope.row)" type="text" size="small">修改</el-button>
        <el-button @click="closeOrOpenComment(scope.row)" type="text" size="small">{{ scope.row.comment_status ? '关闭评论' : '打开评论'}}</el-button>
      </template>
    </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        layout="total, prev, pager, next, jumper"
        :page-size="commentPage.pageSize"
        :current-page.sync="commentPage.currentPage"
        :total="commentPage.total"
        @current-change="pageChange"
        >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { closeOrOpenComment,getCommentList } from  '@/api/comments'
import { searchArticle } from  '@/api/content'
export default {
  name: 'ContentManage',
  data() {
    return {
      commentData: [],
      commentPage:{
        pageSize:10,//每页条数
        currentPage:1, //当前页码
        total:0  //总记录数
      },
      multipleSelection: []
    }
  },
  created () {
    //初始化之后加载数据 文章页码数据为最初的数据
     this.pageChange(1)
  },
  computed: {

  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
   async getArticleList(params) {
      //拉取评论
     let result = await searchArticle(params)
     this.commentPage.total = result.total
     this.commentData = result.data
    },
    //页码发生改变时触发
    pageChange (newPage) {
      this.commentPage.currentPage=newPage;
      this.getArticleList({page:newPage,size:this.commentPage.pageSize})
    },
    //修改评论
    modifyComment (row) {
      if(row.id){
          this.$router.push({path:'/comment/detail?articleId=' + row.id})
      }else {
         this.$message({ message:'当前文章不可查看评论',type:'warning' })
      }
    },
    //关闭或者打开评论
  async  closeOrOpenComment (row) {
      let message = !row.comment_status ? '此操作将开启评论，是否进行此操作?' : '关闭评论将清除所有评论，读者也不能再进行评论，是否进行此操作?'
      let confirmResult = await this.$confirm(message,'提示',{confirmButtonText: '确定',cancelButtonText: '取消', type: 'warning' });
      let  result = await closeOrOpenComment({article_id:row.id},{allow_comment:!row.comment_status })
      this.$message({ type:'success', message:'操作成功' })
      this.getComments({page:this.commentPage.currentPage, per_page:this.commentPage.pageCount })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.comment-container {
  background-color: #ffffff;
  border: 1px solid #e7e7e9;
  .pagination {
    margin: 50px 0;
  }
}
</style>
