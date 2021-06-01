<template>
 <div class="filter">
     <header>图片管理</header>
     <div class="container">
          <el-radio-group size='small' @change="loadData" v-model="activeSelect"  style="margin-bottom: 30px;">
             <el-radio-button  label="0">全部</el-radio-button>
             <el-radio-button  label="1">收藏</el-radio-button>
          </el-radio-group>
          <el-button @click="showPicDialog = true" class="upload_btn" type="primary">上传图片</el-button>
          <div class="img_list">
              <div class="img_list_item" v-for="img in imgData" :key="img.id">
                  <img :src="img.url" />
                  <div v-if="activeSelect == '0'" class="operate">
                     <img @click="collectOrCancel(img)" :src="img.is_collection ? collectSelectedIcon : collectIcon" alt="" />
                     <img @click="delImg(img)" :src="delIcon" alt="">
                  </div>
              </div>
          </div>
           <div class="pagination">
                 <el-pagination
                       background
                       layout="prev, pager, next, jumper"
                       :total="imgPage.total"
                       :page-count="imgPage.pageCount"
                       :page-size="imgPage.pageSize"
                       :current-page="imgPage.currentPage"
                        @current-change="pageChange"
                        >
                 </el-pagination>
                </div>
     </div>
       <el-dialog
         :visible.sync="showPicDialog"
          width="50%"
         :show-close="false"
         :center="true"
         :before-close="closeModal"
         title="上传图片">
           <upload v-if="showPicDialog" :imgChange="imgChangeCall" />
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="closeModal">关闭</el-button>
         </span>
      </el-dialog>
 </div>
 </template>
<script>
import { getAllImgData , delImg , collectOrCancel} from '@/api/publish'
import Upload from  '@/components/Upload/upload.vue'
export default {
    name:'material',
    data () {
        return {
            collectIcon:require('@/assets/collect.png'),//收藏图标
            collectSelectedIcon:require('@/assets/collect_select.png'),//收集图标
            delIcon:require('@/assets/del.png'),//删除图标
            imgPage:{
               total:0,
               currentPage:1,
               pageCount:0,
               pageSize:15
            },
            imgChange:false,//是否上传过图片导致图片数据变化 此状态用来控制是否在关闭后要进行重新加载
            showPicDialog:false,
            activeSelect:'0',
            imgData:[],//存储图片的数据 同时作为收藏数据和全部数据的引用
        }
    },
    components : {
        Upload
    },
    mounted () {
        this.loadData();
    },
    methods:{
      loadData : function(){
        //初始化时加载数据
        this.getImgData({
          page:this.imgPage.currentPage,
          size:this.imgPage.pageSize,
          is_collected:this.activeSelect
        })
      },
      //页面发生变化
      pageChange (newPage) {
        this.imgPage.currentPage = newPage
        this.loadData();
      },
      //获取图片素材
      async  getImgData (params) {
        let result = await getAllImgData(params)
        this.imgData = result.data.list
        this.imgPage.total = result.data.total
        this.imgPage.pageCount = Math.ceil(this.imgPage.total / this.imgPage.pageSize)
      },
      //取消或者收藏图片
      async collectOrCancel (img) {
          let isCollected = img.is_collection;
          if(isCollected==1){ isCollected = 0; }else{ isCollected=1; }
          //取相反状态
         await collectOrCancel(img.id , {isCollected:isCollected})
         img.is_collection = isCollected //取相反状态
         this.$forceUpdate() //强制更新
         this.$message({type:'success',message:'操作成功'})
      },
      //删除图片
      async delImg (img) {
        let result =  await  this.$confirm('确认删除该素材?');
         result ? await delImg(img.id) : null //删除数据
        //写多了if  else 写个三元表达式 换换口味
         this.$message({type:'success',message:'删除成功'}) &&
         this.loadData();
      },
     imgChangeCall () {
         //图片变化了 记录改变的状态 用于关闭弹层时 重新加载数据
         this.imgChange = true
     },
     //关闭弹层时触发
     //注意 这里 为什么不在click用表达式赋值的方式去关掉弹层呢
     //因为发现在click="dialog = false" 模式下 不能触发关闭的回调 应该是实现机制的顺序问题
     closeModal () {
        if(this.imgChange){
           this.loadData()
           this.imgChange = false
        }
        this.showPicDialog = false
     }
  }
}
</script>
<style lang="scss" scoped>
 .filter {
    background-color: #ffffff;
    text-align: left;
    border: 1px solid #e7e7e9;
    header {
      border-bottom: 1px dashed #cccccc;
      margin: 0 5px;
      padding: 0 10px;
      font-size: 14px;
      height: 55px;
      line-height: 55px;
      color: #323745;
    }
    .container {
        padding: 20px;
        .upload_btn {
            position:absolute;
            right:40px;
            top:80px;
        }
    }
    .img_list {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-content: center;
        .img_list_item {
            margin:30px 40px;
            width: 150px;
            height:150px;
            position:relative;
            img {
                width: 100%;
                height:100%;
                border-radius: 5px;
            }
            .operate {
                position: absolute;
                width: 100%;
                height:30px;
                background: #f4f5f6;
                bottom: 0;
                left:0;
                display: flex;
                flex-direction: row;
                justify-content: space-around;
                align-items: center;
                img {
                    width: 16px;
                    height:16px;
                    cursor: pointer;
                }
            }
        }
    }
     .pagination {
            width: 100%;
            text-align: center
     }
 }
</style>
