<template>
  <div class="">
    <user-info/>
    <el-row :gutter="10">
      <el-col :span="15">
        <Article/>
        <News/>
      </el-col>
      <el-col :span="9">
          <raddar-chart/>
          <doughnut-chart/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import UserInfo from './components/UserInfo.vue'
import Article from './components/Article.vue'
import RaddarChart from './components/RaddarChart.vue'
import DoughnutChart from './components/DoughnutChart.vue'
import News from './components/News.vue'

export default {
  name: 'ContentManage',
  data() {
    return {
        
    }
  },
  components: {
    UserInfo,
    Article,
    RaddarChart,
    DoughnutChart,
    News
  }
}
</script>
