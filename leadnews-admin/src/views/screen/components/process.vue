<template>
    <div ref="process" class="wtitle" :style="{height:getHeight()}">
      <div class="text-body">
        <div class="text" ref="text">{{value}}</div>
        <div class="info">
          <div class="word-up">Total</div>
          <div class="word-down">Rate : {{getRate()}} %</div>
        </div>
      </div>
      <div class="area bottomL"></div>
      <div :style="{width:getProcess()}" class="area bottomH"></div>
    </div>
</template>

<script>
  import animate from 'animate.css'
  import jQuery from 'jquery'
  require( './data/lettering.js')
  require('./data/textillate')
    export default {
      name: "process",
      props:['width','value','yesValue'],
        data(){
          return {
            chaged:false,
            process:"50px"
          }
        },
        watch:{
          value : function(){
            this.chaged = true;
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
        },
      methods:{
        getHeight : function(){
          return "65px";
        },
        getRate : function(){
          return (this.value*100/this.yesValue).toFixed(2)
        },
        getProcess:function(){
            return this.getRate()+"%"
        },
        // 重新渲染样式
        restartAnn : function(){
          if(this.chaged) {
            let tmp = jQuery(this.$refs['text'])
            tmp.html(this.value)
            tmp.textillate('restart')
            tmp.fadeOut('slow',function(){tmp.fadeIn('slow') })
            this.chaged = false
          }
        }
      }
    }
</script>


<style scoped>
  .wtitle{
    overflow: hidden;
    position: relative;
    margin: 25px 0px 0px;
    width: 100%;
    height: 65px;
  }
  .area{
    height: 1px;
    width: 100%;
    border: 2px solid #39c6da;
    position: absolute;
  }
  .bottomL{bottom: 1px;left: 3px; border-color: #0d2833;}
  .bottomH{bottom: 1px;left: 3px; border-color: #ff4f41;width: 60%}
  .text-body{
    height: 100%;
  }
  .text{
    width: 58%;
    float: left;
    line-height: 36px;
    font-size: 36px;
    font-weight: bold;
    color: #ffffff;
    text-align: right;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }
  .info{
    float: right;
    width: 38%;
    text-align: left;
    height: 100%;
  }
  .word-up{
    font-weight: bold;
    font-size: 13px;
    margin-top: 3px;
  }
  .word-down{
    color: #ababab;
    font-style: italic;
    font-size: 11px;
    margin-top: 5px;
  }

</style>
