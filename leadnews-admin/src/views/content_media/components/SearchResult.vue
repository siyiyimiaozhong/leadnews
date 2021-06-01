<template>
  <section class="result">
    <header>{{`共找到${total}条符合条件的内容`}}</header>
    <el-table
      :data="list"
      style="width: 100%">
      <template v-for="item in fileds">
        <template v-if="item.list">
          <el-table-column :label="item.label" v-if="item.type=='radio'">
            <template slot-scope="scope">
              <template v-for="rs in  item.radios">
                <template v-if="rs.value==scope.row[item.name]">
                  <el-tag effect="plain" type="info"  v-if="scope.row[item.name]==false">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type="success"  v-else-if="scope.row[item.name]==true">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type="info"  v-else-if="scope.row[item.name]==0">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type="success"  v-else-if="scope.row[item.name]==1">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type="warning"  v-else-if="scope.row[item.name]==2">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type="danger"  v-else-if="scope.row[item.name]==3">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type=""  v-else-if="scope.row[item.name]==4">{{rs.label}}</el-tag>
                  <el-tag effect="plain" type="info"  v-else-if="scope.row[item.name]==rs.value">{{rs.label}}</el-tag>
                </template>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="item.label" v-else-if="item.name.indexOf('_time')>-1">
            <template slot-scope="scope">
              <span>{{ dateFormat(scope.row[item.name]) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="item.label" v-else>
            <template slot-scope="scope">
              <span>{{ scope.row[item.name] }}</span>
            </template>
          </el-table-column>
        </template>
      </template>
      <el-table-column label="操作"
         width="200" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="operateForView(scope.row)">查看</el-button>
          <el-button
            size="mini"
            type="text"
            @click="operateForDisable(scope.row.id,2,scope.$index )">拒绝</el-button>
          <el-button
            size="mini"
            type="text"
            @click="operateForDisable(scope.row.id,4,scope.$index )">通过</el-button>
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
  props: ['host','list','fileds','table','pageSize','total','changePage','changeStatus','editData','viewData'],
  data() {
    return {
       listPage:{
          currentPage:1
       },
       id: {
         filed: 'id',
         type:'eq',
         value:''
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
    //重新设置页码
    resetPage(){

    },
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
    operateForView(item) {
      this.viewData(item)
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
