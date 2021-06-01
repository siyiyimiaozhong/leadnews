<template>
  <section class="filter">
    <div class="filter-container">
    <el-form ref="form">
      <el-form-item label-width="100px" label="搜索用户">
        <el-input
          v-model="name"
          placeholder="请输入标签名称"
          style="width: 400px;"
          class="filter-item"
        />
        <el-button type="primary" @click="queryData">搜 索</el-button>
      </el-form-item>
    </el-form>
    </div>
  </section>
</template>

<script>
  export default {
    props:["changeParam","addData"],
    data() {
      return {
        name: ''
      }
    },
    methods:{
      queryData() {
        let params = {
          filed:'name',
          type:'like',
          value: this.name
        }
        this.changeParam(params)
      }
    }
  }
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
  .filter {
    background-color: #ffffff;
    text-align: left;
    border: 1px solid #e7e7e9;
  header {
    color: #323745;
    font-size: 14px;
    height: 55px;
    line-height: 55px;
    padding: 0 15px;
    background-color: #fbfbfb;
    border-bottom: 1px solid #e8e8e8;
  }
  .el-form {
    padding: 20px 30px 0 0;
  .el-form-item {
    margin: 20px 0;
  }
  .btn {
    border-top: 1px solid #e8e8e8;
    margin: 0 15px;
    padding: 30px 0;
  }
  }
  }
  .editor-content {
    margin-top: 20px;
  }
  .img_list {
    width: 128px;
    height: 100px;
    float: left;
    margin: 0px auto;
    border: 1px solid #eee;
    overflow: hidden;
    border-radius: 4px;
    margin: 0px 20px 20px 0;
    position: relative;
  }
  .img_list_con {
    overflow: hidden;
    margin-left: 20px;
    height: 250px;
  }
  .selected {
    width: 60px !important;
    height: 60px !important;
    position: absolute;
    bottom: 0;
    left: 0;
    margin-left: 50%;
    margin-bottom: 50%;
    transform: translate(-30px, 50px);
  }
  .img_list img {
    width: 128px;
    height: 100px;
    display: block;
    cursor: pointer;
  }
  .pagination {
    text-align: center;
  }
  .upload_pic_show {
    display: block;
    width: 240px;
    height: 180px;
    margin: 15px auto 10px;
  }
  .single_pic {
    border: 1px solid #cccccc;
    width: 280px;
    height: 280px;
    border-radius: 4px;
  .title {
    text-align: center;
  }
  img {
    width: 220px;
    height: 220px;
    margin: 0 auto;
    display: block;
  }
  }
  .three_pic {
    display: flex;
    flex-direction: row;
    border: 1px solid #cccccc;
    width: 840px;
  .three_pic_item {
    width: 280px !important;
  .title {
    text-align: center;
  }
  img {
    width: 220px;
    height: 220px;
    margin: 0 auto;
    display: block;
  }
  }
  }
</style>
