<template>
  <section class="dashboard-user">
    <el-row>
      <el-col :span="15">
        <dl class="user-info">
          <dd class="user-avatar"><img src="@/assets/avatar.jpg"></dd>
          <dt>头条官方账号</dt>
          <dd class="tips">视频发布</dd>
        </dl>
      </el-col>
      <el-col :span="3">
        <div class="num">
          222
        </div>
        <div class="text">粉丝数</div>
      </el-col>
      <el-col :span="3">
        <div class="num">
          333
        </div>
        <div class="text">累计阅读量</div>
      </el-col>
      <el-col :span="3">
        <div class="num">
          444
        </div>
        <div class="text">累计评论数</div>
      </el-col>
    </el-row>
  </section>
</template>

<script>

export default {
  props: {

  },
  data() {
    return {
    }
  },
  mounted() {
  },
  methods: {

  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-user {
    background-color: #fff;
    border: 1px solid #e7e7e9;
    .user-info {
      text-align: left;
      padding: 8px;
      overflow: hidden;
      dt {
        font-size: 14px;
        margin:4px 0 4px 60px;
      }
      .tips {
        margin-left: 60px;
        font-size: 12px;
        color: #999999;
      }
      .user-avatar{
        vertical-align: middle;
        font-size: 0;
        float: left;
        img {
          width: 50px;
          height: 50px;
          border-radius: 50%;
        }
      }
    }
    .num {
      color: #333333;
    }
    .text {
      color: #999999;
      font-size: 12px;
      margin-top: 2px;
    }
    .el-col-3 {
      border-right: 1px solid #eeeeee;
      margin-top: 13px;
    }
  }
</style>
