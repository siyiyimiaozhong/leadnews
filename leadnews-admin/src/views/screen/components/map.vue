<template>
  <div ref="chart" class="chart" :style="{height:getHeight()}"></div>
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  require('echarts/map/js/world')

  export default {
    props:['width','value'],
    data(){
      return {
        maps : []
      }
    },
    watch:{
      'value.value' : function(){
        if(this.maps.length>50){
          this.maps.splice(0,10)
        }
        this.maps.push({name:this.value.name,value:this.value.value})
        this.reDraw()
      }
    },
    mounted() {
      this.initChart()
    },
    methods: {
      getHeight : function(){
        return (this.width/2800)*270+"px";
      },
      reDraw : function(){
        this.chart.setOption({
          backgroundColor: 'transparent',
          geo: {
            map: 'world',
            silent: true,
            label: {emphasis: {show: true,areaColor: '#39c6da'}},
            itemStyle: {
              normal: {borderWidth: 0.3,color:'#39c6da',borderColor: '#ffffff'},
              emphasis: {borderWidth: 0.3,color:'#fe4f3c',borderColor: '#ffffff'}
            },
            left: 3,right:3,top:0,zoom:1,roam: false
          },
          series:[{
            type: 'effectScatter',
            coordinateSystem: 'geo',
            symbolSize:20,
            symbol:'pin',
            data: this.maps,
            showEffectOn: 'render',
            rippleEffect: {
              brushType: 'stroke'
            },
            hoverAnimation: true,
            itemStyle:{
              normal: {
                color: '#fff',
                shadowBlur: 30,
                shadowColor: '#fe4f3c'
              }
            }
          }]
        })
      },
      initChart() {
        this.chart = echarts.init(this.$refs.chart, 'macarons')
        this.reDraw()
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .chart {
    margin: 15px 3px 0px;
  }
</style>
