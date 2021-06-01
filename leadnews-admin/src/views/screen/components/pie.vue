<template>
  <div class="pwapper" :style="{height:getHeight()}">
    <div ref="chart" class="chart"></div>
    <div class="text">{{type}} - {{value}} %</div>
  </div>
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme

  export default {
    props:['width','value','color','type'],
    data() {
      return {
        chart: null
      }
    },
    mounted() {
      this.initChart()
    },
    watch:{
      value : function(){
        this.chageOption()
      }
    },
    methods: {
      getHeight : function(){
        return (this.width/2800)*230+"px";
      },
      chageOption:function(){
        let option = {
          backgroundColor: 'transparent',
          series:[{
            type:'pie',
            radius: ["0%", '65%'],
            label: {normal: {position: 'inner'}},
            labelLine: {normal: {show: false}},
            data:[
              {value:this.value,itemStyle:{color:this.color}},
              {value:100-this.value,itemStyle:{color:'transparent'}}
            ]
          },{
            type: 'pie',
            radius: ['78%', '85%'],
            avoidLabelOverlap: false,
            label: {normal: {position: 'inner'}},
            labelLine: {normal: {show: false}},
            data:[
              {value:100,itemStyle:{color:'#262a35'}}
            ]
          }]
        }
        this.chart.setOption(option)
      },
      initChart() {
        this.chart = echarts.init(this.$refs.chart, 'macarons')
        this.chageOption()
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .pwapper{
    padding-bottom: 15px;
  }
  .chart {
    margin: 0px 3px;
    height: 90%;
  }
  .text{
    color: #ffffff;
    font-size: 11px;
    font-family: "Impact", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
</style>
