<template>
  <div class="navbar">
    <hamburger :toggle-click="toggleSideBar" class="hamburger-container"/>
    <span class="company-container">头条系统</span>
    <div class="right-menu">
      <el-tooltip content="搜索" effect="dark" placement="bottom">
       <el-input v-model="searchText" type="text" @keyup.enter.native="searchInfo" size="small" style='width:180px;margin-right:20px' placeholder="请输入搜索的文章内容"  prefix-icon="el-icon-search"  >
     </el-input>
      </el-tooltip>
      <el-tooltip content="消息" effect="dark" placement="bottom">
        <span>消息</span>
      </el-tooltip>
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <img :src="headImg" class="user-avatar">
          <span class="user-name">{{userName}}</span>
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/center">
            <el-dropdown-item>
              个人信息
            </el-dropdown-item>
          </router-link>
          <a target="_blank" href="https://github.com/PanJiaChen/vue-element-admin/">
            <el-dropdown-item @click="goToGit">
              git地址
            </el-dropdown-item>
          </a>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import Hamburger from '@/components/Hamburger/index.vue'
import { getUser , clearUser } from '@/utils/store'
import emitter from  '@/utils/event'
export default {
  components: {
    Hamburger
  },
  data () {
    return {
       user:{},
       searchText:''
    }
  },
  created () {
     this.user = getUser() //获取用户信息
      emitter.$on('userChange',() => {
           this.user = getUser() //用户信息发生改变时 触发重新拉取新的内容
      })
  },
  computed: {
     userName (){
       return this.user.name ? this.user.name : '未登录'
     },
     headImg () {
       return this.user.photo ? this.user.photo : require('@/assets/avatar.jpg')
     }
  },
  methods: {
    toggleSideBar() {
      //收缩或者展开左侧菜单
      emitter.$emit('changeCollapse')
    },
    logout() {
      clearUser() //退出前要清除掉用户的信息
      this.$router.replace({path:'/login'})
    },
    goToGit () {
       //去掉git的地址
       window.location.href = 'http://git.itcast.cn/dual-front/heima-toutiao-meiti-admin'
    },
    searchInfo () {
      if(this.searchText) {
         //搜索内容
           this.$router.push({path:'/article/list',query:{searchText:this.searchText}})
      }else{
           this.$router.push({path:'/article/list',query:{}})
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  .hamburger-container {
    float: left;
    padding-top: 5px;
    margin-left: 10px;
  }
  .company-container {
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container{
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus{
     outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international{
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        position: relative;
        .user-avatar {
          width: 35px;
          height: 35px;
          border-radius: 50%;
          vertical-align: middle;
        }
        .user-name {
          margin-left: 10px;
        }
        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 20px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
