<template>
  <section class="chart">
    <header class="header">
      图文数据
    </header>
    <div ref="chart" :style="{height:height,width:width}"/>
    <el-row class="legend">
      <el-col :span="8">
        <div>23</div>
        <span>平均阅读进度</span>
      </el-col>
      <el-col :span="8">
        <div>283</div>
        <span>跳出率</span>
      </el-col>
      <el-col :span="8">
        <div>123</div>
        <span>平均阅读速度</span>
      </el-col>
    </el-row>
  </section>
    
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
export default {
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '230px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.initChart()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        series: [{
          name:'图文数据',
          type:'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
                show: false,
                position: 'center'
            },
            emphasis: {
                show: false,
                textStyle: {
                    fontSize: '30',
                    fontWeight: 'bold'
                }
            }
          },
          labelLine: {
              normal: {
                  show: false
              }
          },
          data:[
              {value:335, name:'平均阅读进度'},
              {value:310, name:'跳出率'},
              {value:234, name:'平均阅读速度'},
          ]
        }]
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .chart {
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
    .legend {
      border: 1px solid #e9e9e9;
      margin: 0 10px 10px 10px;
      .el-col {
        padding: 10px 0;
        border-right: 1px solid #e9e9e9;
        &>div {
          color: #444444;
        }
        &:nth-child(1){
          span:before {
            background-color: blue;
          }
        }
        &:nth-child(2){
          span:before {
            background-color: yellow;
          }
        }
        &:nth-child(3){
          span:before {
            background-color: green;
          }
        }
        span {
          color: #afafaf;
          font-size: 12px;
          position: relative;
          &:before {
            content: '';
            position: absolute;
            width: 10px;
            height: 10px;
            border-radius: 50%;
            left: -16px;
            top: 4px;
          }
        }
        &:last-child {
          border: none
        }
      }
    }
  }
</style>