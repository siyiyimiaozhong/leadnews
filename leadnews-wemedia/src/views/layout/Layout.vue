<template>
  <div class="app-wrapper">
    <sidebar :collapse="collapse" :class="['sidebar-container',collapse ? 'hidden-slidecontainer': '']"/>
    <div :class="['main-container',collapse ? 'hidden-container': '']">
      <navbar />
      <app-main/>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from "./components";
import emitter from  '@/utils/event'

export default {
  name: "Layout",
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  data () {
    return {
      collapse:false
    }
  },
  created () {
     emitter.$on('changeCollapse',() => {
         this.collapse = !this.collapse
     })  
    }
};
</script>
<style lang="scss" scoped>
  .hidden-container {
     margin-left: 50px !important;
  }
  .hidden-slidecontainer {
    width:50px !important;
   .logo img{
        display: none;
    }
  }
</style>

