<template>
  <section class="filter">
    <header>全部图文</header>
    <div class="filter-container">
      <el-form ref="form">
          <el-form-item label="文章状态：" label-width="110px">
            <a  
            v-for='item in stateList'  
            :key="item.value"
            href="javascript:;" 
            :class="['state_label',(item.value === selectState.value) ? 'active' : '']" 
            @click="changeState(item)">{{item.label}}</a>
          </el-form-item>
        <el-form-item label="频道列表："  label-width="110px">
            <el-select  v-model="channel_id" @change="queryData">
              <el-option               
                v-for="item in channel_list"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>           
        </el-form-item>
         <el-form-item label="时间选择:  " label-width="110px" >
           <el-date-picker  
                type="datetimerange"
                v-model="date"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期" 
                format="yyyy-MM-dd" 
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                @change="queryData"
              />        
        </el-form-item>
      </el-form>
    </div>
  </section>
</template>
<script>

export default {
  props: ['changePage','channel_list'],
  data() {
    return {
      stateList:[
        {label:'全部',value:5},
        {label:'草稿',value:0},
        {label:'待审核',value:1},
        {label:'审核通过',value:2},
        {label:'审核失败',value:3},
      ],
      selectState:{
        //选择的筛选状态
        label:'全部',value:5
      },
      channel_id:null, //频道id
      date:null,
     
    }
  },
  methods: {
     //查询数据 值得注意的是 一旦条件形成 那么页码应该重新设置为1 
    // 因为查询条件的变化  页码应该从第一页开始
    queryData () {
      let params = {
         resetPage:true, //用于判断是否需要重新设置分页器的标记
         channel_id:this.channel_id,
         status: this.selectState.value == 5 ? null : this.selectState.value,
         page:1,
         begin_pubdate:(this.date && this.date.length) ? this.date[0] : null,
         end_pubdate:(this.date && this.date.length > 1) ? this.date[1] : null
      }
     this.changePage && this.changePage(params) //调用上层组件的查询方法
    },
    //切换文章状态
    changeState (state) {
        this.selectState = state //设置状态
        this.queryData() //查询数据
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
      border-bottom: 1px dashed #cccccc;
      margin: 0 5px;
      padding: 0 10px;
      font-size: 14px;
      height: 55px;
      line-height: 55px;
      color: #323745;
    }
    .filter-container {
      overflow: hidden;
      .el-form {
        padding: 20px 20px 0;
        overflow: hidden;
        .el-form-item {
          margin: 20px 0
        }
      }
      .date-filter {
        padding: 25px 20px 20px;
        overflow: hidden;
        span {
          font-size: 14px;
          margin-right: 20px;
          height: 40px;
          line-height: 40px;
          float: left;
          cursor: pointer;
          &.active, &:hover {
            color: #3296fa;
          }
        }
        .time-container {
          float: left;
          position: relative;
        }
      }
    }
  }
  .state_label {
    float:left;
    padding-right:25px;
    font-size:14px;
    color:#333;
  }
   .active{
    color:#3296fa
  }
</style>