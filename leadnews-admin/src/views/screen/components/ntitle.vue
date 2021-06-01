<template>
    <div class="ntitle">
      <div class="text">/ {{text}}</div>
    </div>
</template>

<script>
    export default {
        name: "ntitle",
        props:['text']
    }
</script>

<style scoped>
  .ntitle{
    overflow: hidden;
    position: relative;
    padding: 10px 0px;
    width: 100%;
  }
  .text{
    line-height: 35px;
    margin: 3px;
    font-size: 13px;
    color: #ffffff;
    text-align: left;
    font-family: "Impact","Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }
</style>
