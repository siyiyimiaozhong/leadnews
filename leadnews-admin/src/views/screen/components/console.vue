<template>
  <div ref="console" class="console" :style="{height:getHeight()}">
    <template v-for="a in texts">
      <div class="br" v-if="a=='\n'"></div>
      <div class="tab" v-else-if="a=='\t'"></div>
      <div v-else-if="typeof a !='string'" :style="a.s">{{a.t}}</div>
      <div v-else>{{a}}</div>
    </template>
    <div ref="pos" class="pos"> </div>
  </div>
</template>

<script>
  import DateUtil from '@/utils/date'

  export default {
    props:['width','value','color','type'],
    data() {
      return {
        texts: [],
        letter: [],
        count:0,
        timer:null
      }
    },
    mounted() {
      this.timer = setInterval(this.print,30)
      this.addText("====================================\n\t\t\t\t欢迎使用头条大屏程序\n====================================\n");
    },
    watch : {
      "value.t":function(){
        let tmp = this.value.t.split('');
        let style = this.value.s
        if(style!='') {
          let arrs = []
          for (let i = 0; i < tmp.length; i++) {
            if(tmp[i]=='\n'||tmp[i]=='\t'){
              arrs.push(tmp[i])
            }else {
              arrs.push({s: style, t: tmp[i]})
            }
          }
          this.addText(arrs)
        }else{
          this.addText(this.value.t)
        }
      }
    },
    destroyed(){
      clearInterval(this.timer)
    },
    methods: {
      getHeight : function(){
        return (this.width/2800)*380+"px";
      },
      addText:function(text){
        if(typeof text == 'string') {
          this.letter = this.letter.concat(text.split(''))
        }else{
          this.letter = this.letter.concat(text)
        }
      },
      print : function(){
        if(this.letter.length>0){
          let num = 1+parseInt(this.letter.length/20);
          for (let i = 0; i < num; i++) {
            let tmp = this.letter[0]
            this.letter.splice(0,1)
            this.texts.push(tmp)
          }
        }
        let obj = this.$refs['console']
        obj.scrollTop = obj.scrollHeight;
        this.count++
        if(this.count%50==0){
          this.addText('\n现在时间是：'+DateUtil.format13HH(new Date().getTime()))
        }
        // 切割字符串，控制页面内存
        if(this.texts.length>1000){
          this.count=this.count-200;
          this.texts.splice(0,200)
        }
      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .console{
    padding: 10px;
    margin-top: 18px;
    overflow-y: auto;
    overflow-x: hidden;
    position: relative;
    width: 100%;
    border-top-left-radius: 30px;
    border-bottom-right-radius: 30px;
    box-shadow:#102331 0px 0px 8px inset;
    font-family: "Impact", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }
  .console::-webkit-scrollbar {/*滚动条整体样式*/
    width: 0px;     /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }
  .console::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
    background: #dbdbdb;
  }
  .console::-webkit-scrollbar-track {/*滚动条里面轨道*/
    background: transparent;
  }
  .console div{
    float: left;
    color: #a4da89;
    padding: 1px;
    font-weight: normal;
    font-size: 12px;
  }
  .console .tab{
    width: 20px;
  }
  .console .br{
    padding: 0px;
    margin: 0px;
    width: 0px;height: 0px;
    display: block;
    clear: left;
  }
  .console .pos{
    width: 8px;
    height: 12px;
    border-bottom: #5daf34 2px solid;
    animation: fade 600ms infinite;
  }
  @keyframes fade {
    from {
      opacity: 1.0;
    }
    50% {
      opacity: 0.2;
    }
    to {
      opacity: 1.0;
    }
  }


</style>
