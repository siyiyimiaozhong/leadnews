<template>
  <div class="wrapper">
    <div class="top-body"><Home_Bar/></div>
    <div class="content-body">
      <wxc-tab-page ref="wxc-tab-page" :tab-titles="tabTitles" :tab-styles="tabStyles" title-type="text" :tab-page-height="tabPageHeight" @wxcTabPageCurrentTabSelected="wxcTabPageCurrentTabSelected">
        <list v-for="(v,index) in tabList"  :key="index" class="item-container" :style="{ height: (tabPageHeight - tabStyles.height) + 'px' }">
          <!-- 下来刷新最新 -->
          <refresh @refresh='loadnew'  :display="shownew?'show':'hide'" class="loading">
            <loading-indicator class="loading-icon"></loading-indicator>
            <text class="loading-text">{{load_new_text}}</text>
          </refresh>
          <!-- 列表项，并绑定显示事件 -->
          <cell v-for="(item,key) in v" class="cell" @appear="show(item.id)" :key="key">
            <wxc-pan-item :ext-id="'1-' + (v) + '-' + (key)" @wxcPanItemClicked="wxcPanItemClicked(item)" @wxcPanItemPan="wxcPanItemPan">
              <Item0 v-if="item.type==0" :data="item"/>
              <Item1 v-if="item.type==1" :data="item"/>
                <Item3 v-if="item.type==2" :data="item"/>
              <Item3 v-if="item.type==3" :data="item"/>
            </wxc-pan-item>
          </cell>
          <!-- 上来加载更多 -->
          <loading @loading="loadmore" :display="showmore?'show':'hide'" class="loading">
            <loading-indicator class="loading-icon"></loading-indicator>
            <text class="loading-text">{{load_more_text}}</text>
          </loading>
        </list>
        <text slot="rightIcon">1212</text>
      </wxc-tab-page>
    </div>
  </div>
</template>

<script>
  import Home_Bar from "@/compoents/bars/home_bar"
  import WxcTabPage from "@/compoents/tabs/home_tabs"
  import {Utils, BindEnv,WxcPanItem } from 'weex-ui'
  import Item0 from '../../compoents/cells/article_0.vue'
  import Item1 from '../../compoents/cells/article_1.vue'
  import Item3 from '../../compoents/cells/article_3.vue'
  import Config from './config'
  import Api from '@/apis/home/api'

  const modal = weex.requireModule("modal")

  export default {
    name: 'Home',
    components: {Home_Bar,WxcTabPage, Item0,Item1,Item3,WxcPanItem},
    data: () => ({
      api:null,// API
      shownew:true,//是否显示loadnew动画
      showmore:false,//是否显示loadmore动画
      tabTitles: Config.tabTitles,//频道配置
      tabStyles: Config.tabStyles,//频道样式
      tabList: [...Array(Config.tabTitles.length).keys()].map(i => []),//列表数据集合
      tabPageHeight: 1334,//列表总高度
      params:{
        loaddir:1,
        index:0,
        tag:"__all__",
        size:10,
        max_behot_time:0,
        min_behot_time:20000000000000
      },//列表数据请求参数
      ashow : {},//列表展示行为记录表
      timer : null//定时函数
    }),
    computed:{
      // 渲染加载最新和更多的国际化语言
      load_new_text:function(){return this.$lang.load_new_text},
      load_more_text:function(){return this.$lang.load_more_text}
    },
    mounted(){
      // 激活推荐按钮
      this.$refs['wxc-tab-page'].setPage(1,null,true);
    },
    destroyed(){
      clearInterval(this.timer)
    },
    created () {
      // 初始化高度，顶部菜单高度120+顶部bar 90
      this.tabPageHeight = Utils.env.getPageHeight()-222;
      Api.setVue(this);
      let _this = this;
      // 每隔5秒提交一次数据
      this.timer = setInterval(function(){
        let result = Api.saveShowBehavior(_this.ashow);
        if(result){
          result.then((d)=>{
            // 标记已经处理完成
            let ids=d.data;
            for(let i=0;i<ids.length;i++){
              _this.ashow[ids[i].id]=false;
            }
          });
        }
      },5000);
    },
    methods: {
      // 列表项在可见区域展示后的事件处理
      show:function(id){
        if(this.ashow[id]==undefined){
          this.ashow[id]=true;
        }
      },
      // 上拉加载更多
      loadmore:function(){
        this.showmore=true;
        this.params.loaddir=2
        this.load();
      },
      // 下来刷新数据
      loadnew:function(){
        this.shownew=true;
        this.params.loaddir=0
        this.load();
      },
      // 正常加载数据
      load : function(){
        Api.loaddata(this.params).then((d)=>{
          this.tanfer(d.data);
        }).catch((e)=>{
          console.log(e)
        })
      },
      // 列表数据转换成View需要的Model对象
      tanfer : function(data){
        if(data.length==0){
          this.showmore=false;
          this.shownew=false;
          modal.toast({message:'没有数据了...',duration:3})
          return ;
        }
        let arr = []
        for(let i=0;i<data.length;i++){
          let ims = []
          if(data[i].images){
            ims = data[i].images.replace(/[\[\]]/ig,'').split(',')
          }
          let tmp = {
            id:data[i].id,
            title:data[i].title,
            comment:data[i].comment,
            authorId:data[i].author_id,
            source:data[i].author_name,
            date:data[i].publish_time,
            type:ims.length==2?1:ims.length,
            image:ims,
            icon:'\uf06d'
          }
          let time = data[i].publish_time;
          if(this.params.max_behot_time<time){
            this.params.max_behot_time=time;
          }
          if(this.params.min_behot_time>time){
            this.params.min_behot_time=time;
          }
          arr.push(tmp);
        }
        let newList = [...Array(this.tabTitles.length).keys()].map(i => []);
        if(this.params.loaddir!=0){
          arr = this.tabList[this.params.index].concat(arr);
        }else{
          arr=arr.concat(this.tabList[this.params.index]);
        }
        newList[this.params.index] = arr;
        this.tabList = newList;
        this.showmore=false;
        this.shownew=false;
      },
      // 频道页切换事件
      wxcTabPageCurrentTabSelected (e) {
        this.params.loaddir=1
        this.params.index=e.page
        this.params.tag = Config.tabTitles[e.page]['id'];
        this.params.max_behot_time=0
        this.params.min_behot_time=20000000000000
        this.shownew=true
        this.load();
      },
      // 兼容回调
      wxcPanItemPan (e) {
        if (BindEnv.supportsEBForAndroid()) {
          this.$refs['wxc-tab-page'].bindExp(e.element);
        }
      },
      // 列表项点击事件
      wxcPanItemClicked(item){
        this.$router.push({
          name:'article-info',
          params:item
        })
      }
    }
  };
</script>

<style lang="less" scoped>
  @import '../../styles/article';
  .wrapper{
    background-color: @body-background;
    font-size: @font-size;
    font-family: @font-family;
    flex-direction : column;
    flex-wrap:wrap;
  }
  .top-body{
    position: fixed;
    left: 0;
    top: 0;
  }
  .content-body{
    flex: 1;
    flex-direction : column;
    margin-top: 90px;
  }
  .item-container {
    width: 750px;
    background-color: #ffffff;
  }
  .cell {
    background-color: #ffffff;
  }
</style>
