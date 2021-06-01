<template>
  <section class="chart">
    <div class="box">
      <div ref="chart" :style="{height:height,width:width}" class="doughnut"/>
    </div>
  </section>
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
export default {
  props: {
    width: {
      type: String,
      default: "100%"
    },
    height: {
      type: String,
      default: "380px"
    },
    data : {
      type:Object
    }
  },
  data() {
    return {
      chart: null
    };
  },
  watch:{
    data:function(val){
      this.initChart()
    }
  },
  mounted() {
    this.initChart();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart, "macarons");
      let temp = this.data.legend
      if(temp.length>13){
        temp = temp.splice(0,14)
      }
      this.chart.setOption({
        title: {text: this.data.title},
        tooltip: {trigger: "item",formatter: "{a} <br/>{b}: {c} ({d}%)"},
        legend: {y: 'bottom',data:temp},
        series: [
          {
            name: this.data.title,
            type: "pie",
            radius: ["50%","70%"],
            roseType: 'radius',
            avoidLabelOverlap: true,
            itemStyle:{
              color: function(){return "#"+("00000"+((Math.random()*16777215+0.5)>>0).toString(16)).slice(-6); }
            },
            label: {
              normal: {show: false,position: "center"},
              emphasis: {show: true,textStyle: {fontSize: "30",fontWeight: "bold"}}
            },
            labelLine: {normal: {show: false}},
            data: this.data.data
          }
        ]
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.chart {
  margin-top: 10px;
  width: 50%;
  float: left;
  font-size: 14px;
  .box {
    background-color: #fbfbfb;
    overflow: hidden;
    .doughnut {
      float: left;
    }
  }
  .legend {
    margin: 0 10px 10px 10px;
    margin-left: 60%;
  }
}
</style>
