<template>
    <div class="wtitle">
      <div class="text">{{text}}</div>
      <div class="area topR"></div>
      <div class="area topL"></div>
      <div class="area bottomL"></div>
      <div class="area bottomR"></div>
    </div>
</template>

<script>
    export default {
        name: "btitle",
        props:['text']
    }
</script>

<style scoped>
  .wtitle{
    overflow: hidden;
    position: relative;
    width: 100%;
  }
  .area{
    height: 1px;
    width: 3%;
    border: 2px solid #39c6da;
    position: absolute;
  }
  .topL{top: 1px;left: 3px;}
  .topR{top: 1px;right: 3px;}
  .bottomL{bottom: 1px;left: 3px;}
  .bottomR{bottom: 1px;right: 3px;}
  .text{
    border-top: 1px solid #39c6da;
    border-bottom: 1px solid #39c6da;
    line-height: 35px;
    margin: 3px;
    font-size: 13px;
    color: #ffffff;
    font-family: "Impact","Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }
</style>
