<template>
  <div class="menu-wrapper">
      <template v-if=" item.children && item.children.length > 1">
          <el-submenu :index="item.path" :key="item.path">
              <template  slot="title">
                <i :class="item.icon"></i><span>{{item.title}}</span>
              </template>
              <template v-for="subItem in item.children">
                  <el-menu-item  :index="subItem.path" :key="subItem.path">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{subItem.title }}
                  </el-menu-item>
              </template>
          </el-submenu>
      </template>
      <template v-else>
          <el-menu-item :index="item.path" :key="item.path" >
              <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
      </template>
  </div>

</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    },
  },
  data() {
    return {
      onlyOneChild: null
    }
  }
}
</script>
<style lang="postcss" scoped>
  .menu-wrapper{
    overflow-x: hidden;
  }
#app .sidebar-container .el-submenu  .el-menu-item.is-active {
     background-color: #409EFF !important;
     color:#FFFFFF;
  }
</style>
