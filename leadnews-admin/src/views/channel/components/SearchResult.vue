<template>
  <section class="result">
    <header>{{`共找到${total}条符合条件的内容`}}</header>
    <el-table
      :data="list"
      style="width: 100%">
      <el-table-column
        label="频道名称">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="描述">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="是否默认频道">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.is_default==1">是</el-tag>
          <el-tag type="info" v-else >否</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="是否有效">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status==1">是</el-tag>
          <el-tag type="info" v-else >否</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="排序">
        <template slot-scope="scope">
          <span>{{ scope.row.ord }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间">
        <template slot-scope="scope">
          <span>{{ dateFormat(scope.row.created_time) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作"
         width="200" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="operateForEditor(scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            v-if="scope.row.status == 1"
            @click="operateForDisable(scope.row.id,0,scope.$index )">无效</el-button>
          <el-button
            size="mini"
            v-else
            @click="operateForDisable(scope.row.id,1,scope.$index )">有效</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        layout="total,prev, pager, next"
        @current-change='pageChange'
        :current-page.sync='listPage.currentPage'
        :page-size="pageSize"
        :total="total">
      </el-pagination>
    </div>
  </section>
</template>

<script>
import DateUtil from '@/utils/date'
import {updateData} from '@/api/common'
const avatar = require('@/assets/avatar.jpg')
export default {
  props: ['host','list','table','pageSize','total','changePage','changeStatus','editData'],
  data() {
    return {
       listPage:{
          currentPage:1
       },
       id: {
         filed: 'id',
         type:'eq',
         value:''
       },
      setForStatus: {
        status: ''
      }
    }
  },
  methods: {
    getImage : function(item, key){
      if(item[key]){
        return this.host+item[key];
      }
      return avatar
    },
    //页码变化 调用上层组件的方法
    pageChange (newPage) {
        this.changePage && this.changePage({page:newPage})
    },
    resetPage(){

    },
    //重新设置页码
    dateFormat (time) {
      return DateUtil.format13(time)
    },
    async operateForDisable(id,status,index) {
      this.id.value = id;
      let params = {
        name:this.table,
        where:[this.id],
        sets:[{filed:'status',value:status}]
      }
      let res = await updateData(params)
      if(res.code==0){
        this.changeStatus(index,status);
        this.$message({type:'success',message:'操作成功！'});
      }else{
        this.$message({type:'error',message:res.errorMessage});
      }
    },
    operateForEditor(item) {
      this.editData(item)
    },
    open(msg) {
      this.$prompt(msg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        this.params.msg = value;
        this.updateData(this.params)
      }).catch(() => {

      });
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .result {
    background-color: #ffffff;
    text-align: left;
    border: 1px solid #e7e7e9;
    margin-top: 10px;
    header {
      border-bottom: 1px dashed #cccccc;
      margin: 0 5px;
      padding: 0 10px;
      font-size: 14px;
      height: 55px;
      line-height: 55px;
      color: #323745;
    }
    li {
      overflow: hidden;
      padding: 15px 5px;
      margin: 0 10px;
      border-bottom: 1px solid #f2f3f5;
       .draft {
           color:#FDC2A9;
           border-color: #FDC2A9;
           background: #FFFFFF;
        }
        .delete {
           color:rgb(243, 24, 8);
           border-color: rgb(243, 24, 8);
           background: #FFFFFF;
        }
        .audit {
           color:rgb(53, 160, 17);
           border-color: rgb(53, 160, 17);
           background: #FFFFFF;
        }
        .unaudit {
           color:#FFAEAE;
           border:none;
        }
    }
    .article-img {
      float: left;
      width: 150px;
      height: 100px;
    }
    .article-content {
      margin-left: 160px;
      text-align: left;
      dt {
        color: #333333;
        font-size: 14px;
        margin: 0 0 7px;
        div {
          float: right;
          i {
            margin-left: 20px;
            font-size: 12px;
            cursor: pointer;
          }
        }
      }
      .time {
        margin-bottom: 10px;
        span {
          color:#3296fa;
          margin-right: 8px;
        }
      }
      dd {
        font-size: 12px;
        margin: 5px 0;
        color: #999999;
        b {
          color: #333333;
        }
        span {
          margin-right: 10px;
        }
      }
    }
  }
  .el-pagination {
    text-align: center;
    margin: 20px 0 40px;
  }
</style>
