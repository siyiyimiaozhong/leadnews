<template>
  <div class="chart-content">
    <header>头条粉丝</header>
    <div class="chart">
      <div ref="chart" :style="{height:height,width:width}"/>
      <el-table :data="fansAgePerc">
        <el-table-column
          prop="age"
          label="年龄">
        </el-table-column>
        <el-table-column
          prop="perc"
          label="粉丝占比">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme

export default {
  props: ["className","width","height","autoResize","ageRangeValue","ageRangeKey", "fansAgePerc"],
  data() {
    return {
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    },
    ageRangeValue: {
      deep: true,
      handler (val) {
        this.ageRangeValue = val;
        this.initChart()
      }
    },
    ageRangeKey: {
      deep: true,
      handler (val) {
        this.ageRangeKey = val;
        this.initChart()
      }
    }
  },
  mounted() {
    this.initChart()
    // 监听侧边栏的变化
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.__resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setOptions() {
      this.chart.setOption({
        color: ['#3398DB'],
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data : this.ageRangeKey,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'直接访问',
                type:'bar',
                barWidth: '30%',
                color: '#69b4ff',
                data: this.ageRangeValue
            }
        ]
      })
    },
    initChart() {
      this.chart = echarts.init(this.$refs.chart, 'macarons')
      this.setOptions(this.chartData)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .chart-content {
    font-size: 14px;
    header {
      margin: 20px 0;
    }
    .chart {
      border: 1px solid #e8e8e8;
      padding-bottom: 30px;
      display: flex;
      justify-content: space-between;
      .el-table {
        border-left: 1px solid #e8e8e8;
        margin-bottom: -31px;
        margin-left: 40px;
      }
    }
  }
</style>
