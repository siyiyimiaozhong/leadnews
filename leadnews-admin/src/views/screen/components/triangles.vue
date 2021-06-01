<template>
  <div class="triangles" :style="{height:getHeightPx()}">
    <div class="ts ts1"><Triangle :width="width" color="#4b99a5" value="50" title="JAVA"/></div>
    <div class="ts ts2"><Triangle :width="width" color="#df5a5b" value="40" title="PYTHON"/></div>
    <div class="ts ts3"><Triangle :width="width" color="#6fb382" value="20" title="VUE"/></div>
    <div class="ts ts4"><Triangle :width="width" color="#8b51a8" value="80" title="Docker"/></div>
    <div class="ts ts5"><Triangle :width="width" color="#f7d66b" value="90" title="大数据"/></div>
  </div>
</template>

<script>
  import Triangle from './triangle'
  export default {
    name: "triangles",
    components:{Triangle},
    props:['width'],
    mounted(){
    },
    methods: {
      getHeightPx: function () {
        return this.getHeight() + "px";
      },
      getHeight: function () {
        return (this.width / 2800) * 300;
      }
    }
  }
</script>

<style scoped>
  .triangles{
    overflow: hidden;
    position: relative;
    width: 100%;
  }
  .ts{
    position: absolute;
    width: 40%;
    top: 0px;
    height: 100%;
  }
  .ts1{left: 0%;}
  .ts2{left: 15%;}
  .ts3{left: 30%;}
  .ts4{left: 45%;}
  .ts5{left: 60%;}
</style>
