<template>
  <div class="triangled" :style="{height:getHeightPx()}" :title="title">
    <div class="text" :style="{top:(top-25)+'px'}">{{title}}:{{value}} %</div>
    <div class="spirographs" ref="spirographs" :style="{height:getHeightPx()}"></div>
  </div>
</template>

<script>
  import * as d3 from 'd3'
  import D3To from './data/d3ToTriangle'
  export default {
    name: "triangle",
    props:['width','color','value','title'],
    data(){
      return {
        svg : null,
        top :0
      }
    },
    mounted(){
      this.init()
    },
    methods: {
      getHeightPx: function () {
        return this.getHeight() + "px";
      },
      getHeight: function () {
        return (this.width / 2800) * 300;
      },
      init:function () {
        let height = this.getHeight()
        let width =  this.$refs['spirographs'].offsetWidth;
        this.top = height*(1-this.value/110)
        this.svg = d3.select(this.$refs['spirographs']).append('svg').attr('width',width).attr('height',height);
        D3To.init(d3,this.svg,width,this.getHeight(),this.color,this.top,10,0,10)
      }
    }
  }
</script>

<style scoped>
  .triangled{
    overflow: hidden;
    position: relative;
    width: 100%;
  }
  .spirographs{
    background: transparent;
    width: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 998;
  }
  .text{
    z-index: 999;
    position: absolute;
    width: 100%;
    font-size: 10px;
    background-color: transparent;
  }
</style>
