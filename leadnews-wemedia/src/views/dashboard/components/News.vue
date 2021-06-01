<template>
  <section class="dashboard-article">
    <header class="header">
      动态
      <a @click="noAction" href="#" class="">更多</a>
    </header>
    <ul>
      <li>
        <img class="article-img" src="@/assets/avatar.jpg">
        <dl class="article-content">
          <dt>林东东 在 <a @click="noAction" href="#" class="">Ant Design Pro</a> 发布了 评论</dt>
          <dd>三天前</dd>
        </dl>
      </li>
      <li>
        <img class="article-img" src="@/assets/avatar.jpg">
        <dl class="article-content">
          <dt>周星星 收藏了  <a @click="noAction" href="#" class="">项目管理感悟（五）</a> </dt>
          <dd>三天前</dd>
        </dl>
      </li>
      <li>
        <img class="article-img" src="@/assets/avatar.jpg">
        <dl class="article-content">
          <dt>林东东 在 <a @click="noAction" href="#" class="">Ant Design Pro</a> 发布了 评论</dt>
          <dd>三天前</dd>
        </dl>
      </li>
    </ul>
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
    noAction:function(){
      alert('该功能暂未实现！')
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-article {
    background-color: #ffffff;
    border: 1px solid #e7e7e9;
    margin-top: 10px;
    .header {
      display: flex;
      justify-content: space-between;
      color: #323745;
      font-size: 14px;
      height: 40px;
      line-height: 40px;
      padding: 0 15px;
      background-color: #fbfbfb;
      border-bottom: 1px solid #e8e8e8;
    }
    li {
      overflow: hidden;
      padding: 15px 5px;
      margin: 0 10px;
      border-top: 1px solid #f2f3f5;
    }
    li:first-child {
      border: none;
    }
    .article-img {
      float: left;
      width: 50px;
      height: 50px;
      border-radius: 50%;
    }
    .article-content {
      margin-left: 60px;
      text-align: left;
      dt {
        color: #333333;
        font-size: 14px;
        margin: 2px 0;
        a {
          color: #3296fa;
        }
      }
      dd {
        font-size: 14px;
        margin: 5px 0;
        color: #999999;
      }
    }
  }
</style>
