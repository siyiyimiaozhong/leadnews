<template>
    <div class="wcore" :style="{height:getHeightPx()}">
      <div class="spirographs" ref="spirographs" :style="{height:getHeightPx()}"></div>
      <div class="text" :style="{height:getHeightPx(),lineHeight:getHeightPx()}">
        <span ref="text">WELCOME-TO-SCREEN</span>
      </div>
    </div>
</template>

<script>
    import * as d3 from 'd3'
    import D3To from './data/d3ToCore'
    import animate from 'animate.css'
    import jQuery from 'jquery'
    require( './data/lettering.js')
    require('./data/textillate')
    export default {
        name: "wcore",
        props:['width','value'],
        data(){
            return {
              chaged: false,
              svg : null
            }
        },
        watch:{
          value : function(){
            this.chaged = true
          }
        },
        mounted(){
            this.init()
            let _this = this
            jQuery(this.$refs['text']).textillate({
              selector: '.texts',
              loop: true,
              minDisplayTime: 5000,
              initialDelay: parseInt(Math.random()*500),
              in: {
                effect: 'bounceInLeft',
                shuffle: true
              },
              out: {
                effect:'bounceOutDown',
                shuffle: true,
                callback:_this.restartAnn
              },
            })
        },
        methods: {
          getValue:function(){
            let p = parseInt(Math.random()*1000)%5
            if(p==0){
              return "WELCOME-TO-HEIMA-SCREEN"
            }else
              return (this.value || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
          },
          // 重新渲染样式
          restartAnn : function(){
            if(this.chaged) {
              let tmp = jQuery(this.$refs['text'])
              let val = this.getValue()
              tmp.html(val)
              tmp.textillate('restart')
              tmp.fadeOut(1,function(){tmp.fadeIn('slow') })
              this.chaged = false
            }
          },
          getHeightPx: function () {
            return this.getHeight() + "px";
          },
          getHeight: function () {
            return 255;
          },
          init:function () {
            let height = this.getHeight()
            let width =  this.$refs['spirographs'].offsetWidth*0.9;
            this.svg = d3.select(this.$refs['spirographs']).append('svg').attr('width',width).attr('height',height);
            D3To.init(d3,this.svg,width,this.getHeight())
            D3To.draw()
          }
        }
    }
</script>

<style scoped>
  .wcore{
    overflow: hidden;
    position: relative;
    margin: 20px 0px 15px;
    width: 100%;
  }
  .spirographs{
    width: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 998;
  }
  .text{
    z-index: 999;
    position: absolute;
    align-items:center;
    vertical-align: middle;
    height: 100%;
    width: 100%;
    background-color: transparent;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }
  .text span{
    animation: pulse 3s infinite ease-in-out;
    display: inline-block;
    font-weight: bold;
    font-size: 56px;
    font-family: "Impact", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  @keyframes breathe
  {
    50%{transform: scale(1.04);
      transform-origin: 50% 80%;}
  }
  @keyframes pulse {
    50% {
      transform: scale(1.05);
      transform-origin: 50% 70%;
    }
  }
</style>
