<template>
  <div class="content">
    <header>{{name}}
      <div class="legend">
        <span class="m">{{legend[0]}}（{{percentage}}%）</span>
        <span class="f">{{legend[1]}}（{{100-percentage}}%）</span>
      </div>
    </header>
    <el-progress :text-inside="true" :stroke-width="30" :percentage="percentage" color="#69b4ff"></el-progress>
  </div>
</template>

<script>
export default {
  data() {
    return {
        
    }
  },
  props: {
    name: {
      type: String,
      required: true
    },
    percentage: {
      type: Number,
      required: true
    },
    legend: {
      type: Array,
      default:  () => ['','']
    },
  },
  components: {
  },
  computed: {

  },
  methods: {

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .content {
    font-size: 14px;
    header {
      margin: 20px 0;
      display: flex;
      justify-content:space-between;
      .legend span {
        position: relative;
        margin-left: 20px;
        &.m:before {
          background-color: #69b4ff;
        }
        &.f:before {
          background-color: #fa8665;
        }
        &:before {
          content: '';
          position: absolute;
          width: 10px;
          height: 10px;
          left: -15px;
          top: 6px;
          border-radius: 3px;
        }
      }
    }
  }
</style>

