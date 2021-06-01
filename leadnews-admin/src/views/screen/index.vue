<template>
<div class="wapper">
  <div class="bg-ann">
    <svg class="triangle-canvas" viewBox="0 0 1000 1000" xmlns="http://www.w3.org/2000/svg">
      <polygon class="triangle triangle-1" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-2" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-3" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-4" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-5" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-6" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-7" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-8" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-9" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-10" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-11" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-12" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-13" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-14" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-15" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-16" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-17" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-18" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-19" points="500,200 759,650 241,650" />
      <polygon class="triangle triangle-20" points="500,200 759,650 241,650" />
    </svg>
  </div>
  <transition mode="in-out" appear
              enter-active-class="animated fadeInUpBig"
              leave-active-class="animated bounceOutRight">
    <div :style="showAnnStyle(false)" class="data-body">
      <div class="left">
        <BTitle text="活跃用户地理位置热点图"/>
        <Map :width="screenWidth" :value="data.map"/>

        <NTitle text="用户行为实时数据"/>
        <div class="pie-body">
          <div class="pie"><NPie :width="screenWidth" color="#37cade" type="注册量" :value="data.userRegister"/></div>
          <div class="pie"><NPie :width="screenWidth" color="#f7503e" type="登录量" :value="data.userLogin"/></div>
          <div class="pie"><NPie :width="screenWidth" color="#6bb582" type="WM登录" value="3000"/></div>
        </div>
        <div class="pie-body">
          <div class="pie"><NPie :width="screenWidth" color="#a7503e" type="关注量" value="3000"/></div>
          <div class="pie"><NPie :width="screenWidth" color="#b53a5e" type="下载量" value="6000"/></div>
          <div class="pie"><NPie :width="screenWidth" color="#5ba5f2" type="反馈量" value="20020"/></div>
        </div>
        <NTitle text="用户终端设备占比" />
        <div class="pie-body">
          <div class="pie"><Pie :width="screenWidth" color="#37cade" type="IOS" value="30"/></div>
          <div class="pie"><Pie :width="screenWidth" color="#f7503e" type="ANDROID" value="60"/></div>
          <div class="pie"><Pie :width="screenWidth" color="#6bb582" type="OTHER" value="20"/></div>
        </div>
        <BTitle text="较昨日用户增长比" :width="screenWidth" style="margin-top: 20px"/>
        <Process :value="data.userRegister" :yes-value="data.userRegisterByYes"/>
      </div>
      <div class="center">
        <div class="ctitle">头条系统可视化大屏</div>
        <BTitle text="全球累计注册用户数"/>
        <HCore :width="screenWidth" :value="data.allUser"/>
        <BTitle text="今日平台访问数据"  style="margin-top: 15px"/>
        <div class="cpie-body" style="margin-top: 15px">
          <div class="pie"><CPie :width="screenWidth" color="#37cade" tip="活跃终端数" :value="data.registerUser" :max="data.maxRegisterUser"/></div>
          <div class="pie"><CPie :width="screenWidth" color="#f7503e" tip="活跃用户数" :value="data.activeUser" :max="data.maxActiveUser"/></div>
          <div class="pie"><CPie :width="screenWidth" color="#6bb582" tip="请求量(PV)"  :value="data.visitorUser" :max="data.maxVisitorUser"/></div>
        </div>
        <BTitle text="系统实时信息台"  style="margin-top: 15px"/>
        <Console  :width="screenWidth" :value="data.console"/>
      </div>
      <div class="right">
      <BTitle text="较昨日发布量增长"/>
      <Triangles :width="screenWidth"></Triangles>
      <NTitle text="文章实时用户数据"/>
      <div class="pie-body">
        <div class="pie"><NPie :width="screenWidth" color="#37cade" type="阅读量" value="2000"/></div>
        <div class="pie"><NPie :width="screenWidth" color="#f7503e" type="点赞量" value="20020"/></div>
        <div class="pie"><NPie :width="screenWidth" color="#6bb582" type="收藏量" value="3000"/></div>
      </div>
      <div class="pie-body">
        <div class="pie"><NPie :width="screenWidth" color="#a7503e" type="评论量" value="3000"/></div>
        <div class="pie"><NPie :width="screenWidth" color="#b53a5e" type="分享量" value="6000"/></div>
        <div class="pie"><NPie :width="screenWidth" color="#5ba5f2" type="转发量" value="20020"/></div>
      </div>
      <NTitle text="文章阅读终端占比" />
      <div class="pie-body">
        <div class="pie"><Pie :width="screenWidth" color="#37cade" type="CRAWLER" value="60"/></div>
        <div class="pie"><Pie :width="screenWidth" color="#f7503e" type="WEMIDE" value="30"/></div>
        <div class="pie"><Pie :width="screenWidth" color="#6bb582" type="OTHER" value="10"/></div>
      </div>
      <BTitle text="较昨日阅读增长比" :width="screenWidth"  style="margin-top: 20px"/>
      <Process value="792439" yes-value="1000000"/>
    </div>
    </div>
  </transition>
  <transition name="fade" mode="out-in" appear enter-active-class="jello rubberBand" leave-active-class="swing fadeOutDownBig">
  <div :style="showAnnStyle(true)" class="ann-body">
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="bomb-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="normal-rocket"></div>
    <div class="text"><div style="font-size: 3vw">注册人数突破</div>{{data.allUser}}</div>
  </div>
  </transition>
</div>
</template>

<script>
  import BTitle from "./components/btitle"
  import NTitle from "./components/ntitle"
  import Map from "./components/map"
  import Pie from "./components/pie"
  import NPie from "./components/npie"
  import Process from "./components/process"
  import HCore from "./components/core"
  import CPie from "./components/cpie"
  import Console from "./components/console"
  import Triangles from "./components/triangles"
  import WSApi from "./components/data/webSocket"
  import {getAuthWs} from "@/api/ws"

  export default {
        name: "index",
        components: {BTitle,NTitle,Map,Pie,NPie,Process,HCore,CPie,Console,Triangles},
        data() {
          return {
            showAnn:false,
            screenWidth : document.documentElement.clientWidth,
            screenHeight : document.documentElement.clientHeight,
            // 自动重连定时器
            connect : {
              status : false,
              timer : null
            },
            // 显示的数据
            data:{
              console:{s:'',t:''},//输出到控制台的内容
              map:null,// 增加的地图点
              allUser:32434342,//总用户数量
              userLogin:328,//当日登录量
              userRegister:689,//当日注册量
              userRegisterByYes:700,//昨日注册量
              activeUser:74923000,//当前活跃用户
              maxActiveUser:100000000,//最大的单日活跃用户
              registerUser:32423000,//当前注册用户
              maxRegisterUser:100000000,//最大的单日注册用户
              visitorUser:54923000,//当日访问量
              maxVisitorUser:100000000//最大的单日访问量
            },
            // 消息处理函数
            funs:{
              'user-login':[
                {attr:'userLogin',fun:'increment'},
                {attr:'activeUser',fun:'increment'},
                {attr:'map',fun:'incrMap'},
                {attr:'console',fun:'printConsole',path:'name',tmpl:{s:{color:'#fe4f3c'},t:'\nAPP用户#登录\n'}}
                ],
              'user-register':[
                {attr:'allUser',fun:'increment'},
                {attr:'userRegister',fun:'increment'},
                {attr:'registerUser',fun:'increment'},
                {attr:'console',fun:'printConsole',path:'name',tmpl:{s:{color:'#6efffc'},t:'\n新用户[#]完成注册\n'}}
              ]
            }
          }
        },
        watch:{
          'data.allUser':function(){
            if(this.data.allUser%5==0){
              this.showAnn=true;
            }else{
              this.showAnn=false;
            }
          }
        },
        mounted() {
          document.title='欢迎进入头条系统可视化大屏'
          document.querySelector('body').setAttribute('style', 'background-color:#090f1d')
          this.connect.timer=setInterval(this.connectFun,3000)
        },
        destroyed(){
          if(this.connect.timer){
            clearInterval(this.connect.timer)
          }
        },
        methods:{
          showAnnStyle : function(bool){
            if(!bool) {
              if (!this.showAnn) {
                return { display: 'block'  }
              }else{
                return { display: 'none'  }
              }
            }else{
              if (this.showAnn) {
                return { display: 'block'  }
              }else{
                return { display: 'none'  }
              }
            }
          },
          async connectFun(){
            if(!this.connect.status) {
              let result = await getAuthWs();
              if (result.code == 0) {
                WSApi.init(result.data.client_id, this)
              } else {
                this.$message({message: '授权自动数据刷新错误', type: 'warning'});
              }
            }
          },
          message : function(ev){
            let data = eval("("+ev.data+")")
            if(data){
              let processor = this.funs[data.type]
              if(processor){
                for (let i = 0; i < processor.length; i++) {
                   let fun = this[processor[i]['fun']]
                  if(fun){
                    fun(processor[i],data)
                  }
                }
              }
            }
          },
          open : function(ev){
            this.connect.status = true
            this.$message({message: '成功连接大脑中心',type: 'success'});
          },
          close : function(ev){
            this.connect.status = false
            let reason = ev.reason
            if(reason=="") reason="服务器断开"
            this.$message({message: '连接被断开：'+reason,type: 'warning'});
          },
          error : function(ev){
            console.log(ev)
          },
          // 自增计算函数
          increment : function(parm,data){
            let tmp = this.data[parm.attr]
            if(tmp==undefined)tmp=0;
            tmp++;
            this.data[parm.attr]=tmp
          },
          // 随机生成地图点
          incrMap : function(parm,data){
            let tmp = this.data[parm.attr]
            if(tmp==undefined)tmp={name:data.data.name};
            // 经度
            let longitude = Math.random()*90
            // 纬度
            let latitude = Math.random()*45
            let pos1 = ((parseInt(Math.random()*90)%2==1)?1:-1)
            let pos2 = ((parseInt(Math.random()*45)%2==1)?1:-1)*pos1
            tmp['value']=[longitude*pos1,latitude*pos2]
            this.data[parm.attr]=tmp
          },
          // 打印信息去控制台
          printConsole : function(parm,data) {
            let arrs = parm.path.split(".")
            let log = data.data;
            for (let i = 0; i < arrs.length; i++) {
              log = log[arrs[i]]
            }
            log += Math.random()
            this.data[parm.attr] = {s:parm.tmpl.s,t:parm.tmpl.t.replace("#", log)}
          }
        }
  }
</script>

<style scoped>
  @import "./screen.css";
  @import "./ann.css";
  .wapper{
    margin: 0px 20px;
    height: 99%;
  }
  .left{
    width: 20%;
    height: 100%;
    padding-right: 20px;
    padding-top: 80px;
    float: left;
  }
  .center{
    float: left;
    width: 60%;
    padding: 0px 20px;
  }
  .ctitle{
    font-size: 36px;
    width: 100%;
    text-align: center;
    font-weight: bold;
    line-height: 80px;
  }
  .right{
    width: 20%;
    height: 100%;
    padding-left: 20px;
    padding-top: 80px;
    float: right;
  }
</style>
