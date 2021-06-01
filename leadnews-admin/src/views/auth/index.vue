<template>
  <div>
    <search-tool
      :changeParam="searchAuthList"
    />
    <search-result
      ref='mySearchResult'
      :authList="authList"
      :host="host"
      :total="total"
      :changePage="searchAuthList"
      :pageSize="params.size"
      :authPassRealName="authPassRealName"
      :authFailRealName="authFailRealName"
    />
  </div>
</template>

<script>
  import SearchTool from './components/SearchTool.vue'
  import SearchResult from './components/SearchResult.vue'
  import {findAuthList, authPass, authFail} from '@/api/auth'
  export default {
    name: "AuthManage",
    data() {
      return {
        params:{
          page:1,
          size:10
        },
        total:0,
        host:'',
        authList:[]
      }
    },
    created() {
      this.searchAuthList();
    },
    components: {
      SearchTool,
      SearchResult
    },
    methods: {
      async searchAuthList(newParams) {
        let res = await findAuthList({...this.params, ...newParams});
        if (res.code == 200) {
          this.authList = res.data
          this.host = res.host
          this.total = res.total //总记录数
        } else {
          this.$message({type: 'error', message: res.error_message})
        }
      },
      async authPassRealName(params) {
        let res = await authPass(params)
        if (res.code == 200)
          this.$message({type: 'success', message: '操作成功'})
        else
          this.$message({type: 'success', message: res.error_message})
        this.searchAuthList();
      },
      async authFailRealName(params) {
        let res = await authFail(params)
        this.searchAuthList();
      },
    }
  }
</script>

<style scoped>

</style>
