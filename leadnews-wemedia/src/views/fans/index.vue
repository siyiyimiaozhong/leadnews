<template>
  <div class="fans-container">
    <div class="detail-container">
      <header>详情分析</header>
      <div class="filter">
        <el-radio-group  @change="loadDataByButton" v-model="parms.type">
          <el-radio-button  label="0">今日</el-radio-button>
          <el-radio-button  label="1">本周</el-radio-button>
          <el-radio-button  label="7">近7天</el-radio-button>
          <el-radio-button  label="30">近30天</el-radio-button>
        </el-radio-group>
        <el-date-picker v-model="parms.time" type="datetimerange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"  @change="loadDataByTimeRange" :picker-options="pickerOptions" format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"/>
      </div>
      <Statist :article="all.article" :likes="all.likes" :collection="all.collection"/>
      <line-chart ref="lineChart"/>
      <div class="chart">
        <template v-for="item in pie">
          <doughnut-chart :data="item" v-if="item.title !='发文量-转化率'"/>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import Statist from './components/index/Statist.vue'
import LineChart from './components/index/LineChart.vue'
import DoughnutChart from './components/index/DoughnutChart.vue'
import {getFansStatistics} from '@/api/fans'
import DateUtil from '@/utils/date'

export default {
  name: 'ContentManage',
  data() {
    return {
      parms:{
        type:'0',
        stime:'',
        etime:''
      },
      all:{},
      list:'',
      graph:'',
      pie:{},
      lineInfo : [
        {name:'发文量',type:'article'},
        {name:'阅读量',type:'read_count'},
        {name:'点赞量',type:'likes'},
        {name:'评论量',type:'comment'},
        {name:'收藏量',type:'collection'},
        {name:'转发量',type:'follow'},
        {name:'不喜欢',type:'unlikes'}
      ],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      }
    }
  },
  components: {
    Statist,
    LineChart,
    DoughnutChart
  },
  created(){
    this.getFansStatistics()
  },
  methods : {
    loadDataByTimeRange:function(e){
      this.parms.type=-1
      this.parms.stime=e[0].getTime()
      this.parms.etime=e[1].getTime()
      this.getFansStatistics();
    },
    loadDataByButton:function(e){
      if(e=='1'){// 本周
        this.parms.stime=DateUtil.getWeekSTime()
        this.parms.etime=DateUtil.getWeekETime()
      }else{
        this.parms.stime=DateUtil.getNearTime(0)
        this.parms.etime=DateUtil.getNearTime(e)
      }
      this.getFansStatistics();
    },
    async getFansStatistics (){
      console.log(this.parms)
      let result = await getFansStatistics(this.parms)
      this.list = result.data
      let all = {article:0,likes:0,collection:0,forward:0,comment:0,read_count:0}
      let chats = {}
      for (let i = 0; i < result.data.length; i++) {
        let tmp = result.data[i];
        let time = DateUtil.format13(tmp.created_time)
        let data = chats[time]?chats[time]:{}
        for (let j = 0; j <this.lineInfo.length ; j++) {
          let k=this.lineInfo[j].type
          all[k]+=tmp[k]
          let val = data[k]?data[k]:0
          val +=tmp[k]
          data[k]=val
        }
        chats[time]=data
      }
      this.all = all
      this.graph = chats
      this.parseToLine(chats,all)
    },
    parseToLine : function(chats,all){
      // 排序
      var name = [];
      for (let k in chats) {
        name.push(k)
      }
      name.sort()
      let series = {}//折线图数据
      let pie = {}//饼图数据
      for (let i = 0; i <name.length ; i++) {
        for (let j = 0; j <this.lineInfo.length ; j++) {
          let k=this.lineInfo[j].type
          series[k] = series[k]?series[k]:[]
          series[k].push(chats[name[i]][k])
          pie[k] = pie[k]?pie[k]:{}
          pie[k]['title'] = this.lineInfo[j].name+'-转化率'
          pie[k]['data'] = pie[k]['data']?pie[k]['data']:[]
          pie[k]['legend'] = pie[k]['legend']?pie[k]['legend']:[]
          pie[k]['legend'].push(name[i])
          //pie[k]['data'].push({value:(chats[name[i]][k]/chats[name[i]]['article']).toFixed(2),name:name[i]})
          pie[k]['data'].push({value:chats[name[i]][k],name:name[i]})
        }
      }
      let data = []
      let legend=[]
      for (let i = 0; i <this.lineInfo.length ; i++) {
        data.push({
          name:this.lineInfo[i].name,
          type:'line',
          //stack: '总量',
          areaStyle: {},
          data:series[this.lineInfo[i].type]
        })
        legend.push(this.lineInfo[i].name)
      }
      let lineOption = {
        title: {text: '明细数据'},
        tooltip: {trigger: 'axis'},
        legend: {data:legend},
        //grid: {left: '2%',right: '2%', bottom: '2%',containLabel: true},
        xAxis: {type: 'category',boundaryGap: false,data: name},
        yAxis: {type: 'value'},
        series: data
      }
      this.pie = pie
      this.$refs['lineChart'].setOptions(lineOption)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.fans-container {
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
  .tabBar {
    font-size: 14px;
    padding: 0 15px;
    height: 55px;
    line-height: 55px;
    border-bottom: 1px dashed #cccccc;
    a {
      margin-right: 35px;
      color: #323745;
      &.active {
        color: #3296fa;
      }
    }
  }
  .filter {
    font-size: 14px;
    padding: 20px 0 20px 20px;
    span {
      border: 1px solid #3296fa;
      color: #3296fa;
      padding: 5px 10px;
      cursor: pointer;
      &:nth-child(1){
        border-right: none;
      }
      &:nth-child(2){
        border-right: none;
      }
      &.active {
        background-color: #3296fa;
        color: #ffffff;
      }
    }
    .el-date-editor {
      margin-left: 20px;
    }
  }
  .chart {
    padding: 0 20px;
    overflow: hidden;
    margin-bottom: 30px;
  }
}
</style>

