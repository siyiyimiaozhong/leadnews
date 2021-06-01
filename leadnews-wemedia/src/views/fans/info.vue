<template>
  <div class="fans-container">
    <header>粉丝列表</header>
    <div class="tabBar">
      <router-link to="/fans/index">粉丝概况</router-link>
      <router-link to="/fans/info" class="active">粉丝画像</router-link>
      <router-link to="/fans/list">粉丝列表</router-link>
    </div>
    <div class="tabView">
      <Progress name="粉丝性别分布" :percentage="manPercent" :legend="['男', '女']"/>
      <category-chart
        :ageRangeKey="ageRangeKey"
        :ageRangeValue="ageRangeValue"
        :width="width"
        :height="height"
        :autoResize="autoResize"
        :fansAgePerc="fansAgePerc"
      />
      <Progress name="粉丝终端分布" :percentage="iosPercent" :legend="['Android', 'IOS']" />
      <category-chart-big
        :chartData="readData"
      />
    </div>
  </div>
</template>

<script>
import Progress from './components/info/Progress.vue'
import CategoryChart from './components/info/CategoryChart.vue'
import CategoryChartBig from './components/info/CategoryChartBig.vue'
import { getFollowersPortrait }  from '@/api/fans'

export default {
  name: 'ContentManage',
  data() {
    return {
      gender: {
        male:0,
        female:0
      },
      mobile: {
        android: 0,
        ios:0
      },
      ageRangeValue:[],
      ageRangeKey:[],
      width: '75%',
      height: '350px',
      autoResize: true,
      fansAgePerc: [],
      readData: {
        key: [],
        value: []
      },
    }
  },
  components: {
    Progress,
    CategoryChart,
    CategoryChartBig
  },
  created () {
    this.getFansPortrait();
  },
  computed: {
     manPercent () {
       if((this.gender.male + this.gender.female) > 0){
          return Math.round(parseFloat(this.gender.male / (this.gender.male + this.gender.female)) * 100)
       }
       //目前没有粉丝数据 所以 男粉丝+女粉丝是0  所以 模拟一下数据50
       return 50
     },
    iosPercent () {
       if ((this.mobile.android + this.mobile.ios) > 0) {
         return Math.round(parseFloat(this.mobile.android / (this.mobile.android + this.mobile.ios)) * 100);
       }
       return 50;
    }
  },
  methods: {
    //获取粉丝性别分布
    async  getFansPortrait () {
       let result = await getFollowersPortrait();
       if (result.code == 0) {

         let portraits = result.data;
         let ageItems = [];
         let readItems = [];
         let totalFans = 0;
         portraits.forEach((item) => {
           var key = item.name.split(":")[1];
           var value = Number(item.value);
           //提取性别
           if (item.name.startsWith("sex")) {
             this.gender[key] = value;
           }
           //提取手机
           if (item.name.startsWith("mobile")) {
             this.mobile[key] = value;
           }
           //年龄分布
           if (item.name.startsWith("age")) {
              ageItems.push(item);
              totalFans += value;
           }
           //阅读统计
           if (item.name.startsWith("read")) {
             readItems.push(item);
           }
         });
         this.setAges(ageItems, totalFans)
         this.statisticRead(readItems)
       } else {
         this.$message({type: "error", message: result.error_message})
       }
    },
    setAges(ageItems, total) {
      //排序年龄项
      ageItems.sort((a, b) => {
        if (a.name > b.name)
          return 1;
        else if (a.name < b.name)
          return -1;
        else
          return 0;
      });
      ageItems.forEach((item)=>{
        var key = item.name.split(":")[1];
        var value = Number(item.value);
        this.ageRangeKey.push(`${key}岁`);
        this.ageRangeValue.push(Number(value));
        let perc = Math.round(parseFloat((value / total) * 100));
        this.fansAgePerc.push({age: key, perc: `${perc}%`})
      })
    },
    statisticRead(readItems) {
      readItems.sort((a, b) => {
        if (a.name > b.name)
          return 1;
        else if (a.name < b.name)
          return -1;
        else
          return 0;
      });
      readItems.forEach((item) => {
        var key = item.name.split(":")[1];
        var value = Number(item.value);
        this.readData.key.push(key)
        this.readData.value.push(value)
      })
    }
  }

}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.fans-container {
  background-color: #ffffff;
  text-align: left;
  border: 1px solid #e7e7e9;
  header {
    color: #323745;
    font-size: 14px;
    height: 55px;
    line-height: 55px;
    padding: 0 15px;
    background-color: #fbfbfb;
    border-bottom: 1px solid #e8e8e8;
  }
  .tabBar {
    font-size: 14px;
    padding: 0 15px;
    height: 55px;
    line-height: 55px;
    border-bottom: 1px dashed #cccccc;
    a {
      margin-right: 35px;
      color: #323745;
      &.active {
        color: #3296fa;
      }
    }
  }
  .tabView {
    padding: 0 30px;
  }
}
</style>

