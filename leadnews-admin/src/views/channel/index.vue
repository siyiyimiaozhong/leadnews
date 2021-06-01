<template>
  <div>
    <Editor ref="editor" :fileds="fileds" title="频道" :table="this.params.name" :submitSuccess="submitSuccess"/>
    <search-tool :changeParam="changeParam" :addData="addData" />
    <search-result
      ref='mySearchResult'
      :list="list"
      :host="host"
      :total="total"
      :table="this.params.name"
      :editData="editData"
      :changePage="changePage"
      :changeStatus="changeStatus"
      :pageSize="params.size"/>
  </div>
</template>

<script>
  import SearchTool from './components/SearchTool.vue'
  import SearchResult from './components/SearchResult.vue'
  import Editor from '@/components/CommEditor.vue'
  import {loadList} from '@/api/common'
  import DateUtil from '@/utils/date'
  export default {
    name: "ChannelManager",
    data() {
      return {
        params:{
          name:'AD_CHANNEL',
          page:1,
          size:10,
          where:[]
        },
        total:0,
        host:'',
        list:[],
        fileds:[
          {label:'频道名称',name:'name',type:'input',placeholder:'请输入频道名称',rule:[
            { required: true, message: '请输入频道名称', trigger: 'blur' },
              { min: 2, max:4,message: '频道名称在2~4个字符', trigger: 'blur' }
            ]},
          {label:'频道描述',name:'description',type:'input',placeholder:'请输入描述信息'},
          {label:'默认频道',name:'is_default',type:'radio',value:0,radios:[{value:0,label:'否'},{value:1,label:'是'}]},
          {label:'是否有效',name:'status',type:'radio',value:1,radios:[{value:0,label:'否'},{value:1,label:'是'}]},
          {label:'排序',name:'ord',type:'number',value:0},
          {label:'创建时间',name:'created_time',type:'hidden',value:DateUtil.format13HH(new Date().getTime())}
        ]
      }
    },
    mounted() {
      this.loadData()
    },
    components: {SearchTool,SearchResult,Editor},
    methods: {
      // 编辑数据
      editData : function(item){
        this.$refs['editor'].edit(item)
      },
      // 新增数据
      addData : function(item){
        this.$refs['editor'].add()
      },
      // 新增或者修改后的操作方法
      submitSuccess:function(){
        this.loadData()
      },
      changeStatus:function(index,status){
        this.$set(this.list[index],'status',status)
      },
      changeParam :function(e){
        this.params.page=1
        this.params.where[0]=e
        this.loadData()
      },
      changePage :function(e){
        this.params.page=e.page
        this.loadData()
      },
      async loadData() {
        let res = await loadList({...this.params});
        if (res.code == 0) {
          this.list = res.data.list
          this.host = res.host
          this.total = res.data.total //总记录数
        } else {
          this.$message({type: 'error', message: res.error_message})
        }
      }
    }
  }
</script>

<style scoped>

</style>
