<template>
  <div class="pwapper" :style="{height:getHeight()}">
    <div ref="chart" class="chart"></div>
    <div ref="text" class="textCenter" :style="{top:getTextHeight(),fontSize:getFontSize(),color:color}">{{value}}</div>
    <div class="text">{{type}}</div>
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
    props:['width','value','color','type'],
    data() {
      return {
        chart: null,
        chaged:false
      }
    },
    mounted() {
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
      this.initChart()
    },
    watch:{
      value : function(){
        //this.chageOption()
        this.chaged = true
      }
    },
    methods: {
      getFontSize : function(){
        return (this.width/2800)*32+"px";
      },
      getHeight : function(){
        return (this.width/2800)*235+"px";
      },
      getTextHeight : function(){
        return ((this.width/2800)*165)/2+"px";
      },
      chageOption:function(){
        let option = {
          backgroundColor: 'transparent',
          series:[{
            type: 'pie',
            radius: ['78%', '85%'],
            avoidLabelOverlap: false,
            label: {normal: {position: 'inner'}},
            labelLine: {normal: {show: false}},
            data:[
              {value:100,itemStyle:{color:this.color}}
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
<style scoped>
  .pwapper{
    position: relative;
  }
  .chart {
    margin: 0px 3px;
    height: 87%;
  }
  .textCenter{
    position: absolute;
    font-weight: bold;
    font-size: 32px;
    align-items:center;
    vertical-align: middle;
    color: green;
    height: 100%;
    width: 100%;
    background-color: transparent;
  }
  .text{
    color: #ffffff;
    font-size: 12px;
    font-family: "Impact", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
</style>
