<template>
  <div>
    <ul class="list">
      <li v-for="item in fanList" :key="item.id">
        <img class="article-img" :src="item.photo ? item.photo : require('@/assets/avatar.jpg') ">
        <div>{{item.fans_name}}</div>
        <el-button type="primary" v-if="!item.is_follow" @click="followFan(item.fans_id, $event)">关注</el-button>
        <el-button type="warning" v-if="item.is_follow" @click="cancleFollowFan(item.fans_id, $event)">取消关注</el-button>
      </li>
      <!--因为目前的接口没有数据 所以为了体验 这里采用模拟的数据-->
      <!--    <div v-if="!fansList || !fansList.length">
            <li v-for="(item,index) in list" :key="index">
             <img class="article-img" src="@/assets/avatar.jpg">
             <div>测试粉丝</div>
             <el-button type="primary">关注</el-button>
            </li>
          </div>-->
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
  </div>
</template>

<script>
export default {
  data() {
    return {
       list:[1,2,3,4,5,6,7,8,8,9,9],
       listPage:{
         currentPage:1
       }
    }
  },
  props: ["name", "fanList", "total", "pageSize", "changePage", "followOperate"],
  components: {
  },
  computed: {

  },
  methods: {
    pageChange: function (newPage) {
      this.changePage && this.changePage({page: newPage})
    },
    cancleFollowFan: function (fanId, event) {
      this.followOperate && this.followOperate({fans_id: fanId, switch_state: false})
    },
    followFan: function (fanId, event) {
      this.followOperate && this.followOperate({fans_id: fanId, switch_state: true})
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .list {
    overflow: hidden;
    padding: 15px;
    li {
      border: 1px solid #e7e7e9;
      width: 17%;
      float: left;
      margin:10px 1.5%;
      text-align: center;
      padding: 20px 0;
      img {
        border-radius: 50%;
        width: 70px;
        height: 70px;
      }
      div {
        font-size: 14px;
        color: #666666;
        margin: 10px 0;
      }
    }
  }
</style>

