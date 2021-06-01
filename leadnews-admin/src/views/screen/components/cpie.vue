<template>
  <div class="ccwapper" :style="{height:getHeight()}">
    <div class="left-chart">
      <div ref="chart" class="chart"></div>
    </div>
    <div class="right-chart">
      <div class="title">{{tip}} ：</div>
      <div ref="text" class="numer">{{value}}</div>
    </div>
    <div class="bar"><div></div><div></div><div></div><div></div><div></div></div>
    <div class="bar active">
      <div :style="{borderColor:color}" v-for=" i in percentage"></div>
    </div>
  </div>
</template>

<script>
  import echarts from 'echarts'
  import animate from 'animate.css'
  import jQuery from 'jquery'
  require( './data/lettering.js')
  require('./data/textillate')
  require('echarts/theme/macarons') // echarts theme

  export default {
    props:['width','value','color','tip','max'],
    data() {
      return {
        chart: null,
        chaged:false,
        percentage:2
      }
    },
    mounted() {
      this.initChart()
      let _this = this
      jQuery(this.$refs['text']).textillate({
        selector: '.texts',
        loop: true,
        minDisplayTime: 2000,
        initialDelay: parseInt(Math.random()*500),
        in: {
          effect: 'bounceInLeft',
          shuffle: true
        },
        out: {
          effect:'bounceOutDown',
          shuffle: true,
          callback:_this.restartAnn
        }
      })
    },
    watch:{
      value : function(){
        this.chaged = true
        this.chageOption()
      }
    },
    methods: {
      getHeight : function(){
        return (this.width/2800)*300+"px";
      },
      chageOption:function(){
        let val = parseInt(this.value*100/this.max)
        this.percentage = Math.ceil(val/20)
        if(val>100)val=100
        let option = {
          backgroundColor: 'transparent',
          series:[{
            type:'pie',
            radius: ["0%", '65%'],
            label: {normal: {position: 'inner'}},
            labelLine: {normal: {show: false}},
            data:[
              {value:(100-val),itemStyle:{color:'transparent'}},
              {value:3,itemStyle:{color:this.color}},
              {value:val-3,itemStyle:{color:'transparent'}}
            ]
          },{
            type: 'pie',
            radius: ['78%', '85%'],
            avoidLabelOverlap: false,
            label: {normal: {position: 'inner'}},
            labelLine: {normal: {show: false}},
            data:[
              {value:(100-val),itemStyle:{color:'#262a35'}},
              {value:val,itemStyle:{color:this.color}}
            ]
          }]
        }
        this.chart.setOption(option)
      },
      // 重新渲染样式
      restartAnn : function(){
        if(this.chaged) {
          let tmp = jQuery(this.$refs['text'])
          tmp.html(this.value)
          tmp.textillate('restart')
          tmp.fadeOut('fast',function(){tmp.fadeIn('slow') })
          this.chaged = false
        }
      },
      initChart() {
        this.chart = echarts.init(this.$refs.chart, 'macarons')
        this.chageOption()
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .ccwapper{
    overflow: hidden;
    position: relative;
    width: 100%;
    margin-bottom: 15px;
  }
  .chart {
    margin: 0px 3px;
    height: 100%;
    width: 100%;
  }
  .bar{
    position: absolute;
    bottom: 0px;
    width: 100%;
    background: transparent;
  }
  .bar div{
    float: left;
    width: 17%;
    height: 1px;
    border: 5px solid #102331;
    margin: 0px 3px;
  }
  .active div{
    border: 5px solid #3bd5ed;
  }
  .left-chart{
    float: left;
    width: 50%;
    height: 95%;
  }
  .right-chart{
    float: left;
  }
  .title{
    color: #43464b;
    font-size: 12px;
    margin-top: 30px;
    line-height: 30px;
    text-align: left;
    font-weight: bold;
    font-family: "Impact", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  .numer{
    font-size: 20px;
    font-weight: bold;
    color: #3bd5ed;
  }
</style>
