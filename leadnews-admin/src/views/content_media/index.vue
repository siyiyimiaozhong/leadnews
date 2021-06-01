<template>
  <div>
    <Editor ref="editor" :fileds="fileds" title="内容" :table="this.params.name" :submitSuccess="submitSuccess"/>
    <search-tool :changeParam="changeParam" :addData="addData" />
    <search-result
      ref='mySearchResult'
      :list="list"
      :host="host"
      :total="total"
      :table="this.params.name"
      :viewData="viewData"
      :changePage="changePage"
      :changeStatus="changeStatus"
      :fileds="fileds"
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
          name:'WM_NEWS',
          page:1,
          size:10,
          base:[{type:'eq',filed:'status',value:'3'}],
          where:[]
        },
        total:0,
        host:'',
        list:[],
        fileds:[
          {list:true,label:'标题',name:'title',type:'input',placeholder:'请输入标题',rule:[
            { required: true, message: '请输入标题', trigger: 'blur' },
              { min: 10, max:20,message: '标题在10~50个字符', trigger: 'blur' }
            ]},
          {list:true,label:'作者',name:'author_name',type:'input'},
          {list:true,label:'类型',name:'type',type:'radio',value:0,radios:[{value:0,label:'无图'},{value:1,label:'单图'},{value:2,label:'多图'}]},
          {list:true,label:'标签',name:'label',type:'input'},
          {list:true,label:'定时时间',name:'publish_time',type:'input'},
          {list:true,label:'创建时间',name:'created_time',type:'hidden',value:DateUtil.format13HH(new Date().getTime())},
          {list:true,label:'提交时间',name:'submited_time',type:'hidden',value:DateUtil.format13HH(new Date().getTime())}
        ]
      }
    },
    mounted() {
      this.loadData()
    },
    components: {SearchTool,SearchResult,Editor},
    methods: {
      // 编辑数据
      viewData : function(item){
        this.$refs['editor'].view(item)
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
        this.loadData()
      },
      changeParam :function(e){
        this.params.page=1
        this.params.where=e
        this.loadData()
      },
      changePage :function(e){
        this.params.page=e.page
        this.loadData()
      },
      async loadData() {
        this.params.where=this.params.base.concat(this.params.where)
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
