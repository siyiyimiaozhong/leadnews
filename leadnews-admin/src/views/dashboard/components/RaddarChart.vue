<template>
  <section class="chart">
    <header class="header">
      粉丝数据
      <!-- <router-link to="/" class="">更多</router-link> -->
    </header>
    <div ref="chart" :style="{height:height,width:width}"/>
    <el-row class="legend">
      <el-col :span="8">
        <div>23</div>
        <span>新增粉丝</span>
      </el-col>
      <el-col :span="8">
        <div>283</div>
        <span>活跃粉丝</span>
      </el-col>
      <el-col :span="8">
        <div>123</div>
        <span>总粉丝数</span>
      </el-col>
    </el-row>
  </section>
    
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
// import { debounce } from '@/utils'

const animationDuration = 3000

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
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        radar: {
          radius: '66%',
          indicator: [
            { name: '引用', max: 10000 },
            { name: '产量', max: 20000 },
            { name: '贡献', max: 20000 },
            { name: '热度', max: 20000 },
            { name: '口碑', max: 20000 },
          ]
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
          data: [
            {
              value: [5000, 7000, 12000, 11000, 15000, 14000],
              name: 'Allocated Budget'
            },
            {
              value: [4000, 9000, 15000, 15000, 13000, 11000],
              name: 'Expected Spending'
            },
            {
              value: [5500, 11000, 12000, 15000, 12000, 12000],
              name: 'Actual Spending'
            }
          ],
          animationDuration: animationDuration
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