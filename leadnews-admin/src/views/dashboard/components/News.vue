<template>
  <section class="dashboard-article">
    <header class="header">
      动态
      <router-link to="/" class="">更多</router-link>
    </header>
    <ul>
      <li>
        <img class="article-img" src="@/assets/avatar.jpg">
        <dl class="article-content">
          <dt>林东东 在 <router-link to="/" class="">Ant Design Pro</router-link> 发布了 评论</dt>
          <dd>三天前</dd>
        </dl>
      </li>
      <li>
        <img class="article-img" src="@/assets/avatar.jpg">
        <dl class="article-content">
          <dt>周星星 收藏了  <router-link to="/" class="">项目管理感悟（五）</router-link> </dt>
          <dd>三天前</dd>
        </dl>
      </li>
      <li>
        <img class="article-img" src="@/assets/avatar.jpg">
        <dl class="article-content">
          <dt>林东东 在 <router-link to="/" class="">Ant Design Pro</router-link> 发布了 评论</dt>
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