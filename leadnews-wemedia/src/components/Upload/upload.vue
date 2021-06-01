<template>
     <div class="upload_pic" >
              <el-form  status-icon label-width="100px">
                <img :src="upload_img_url" class="upload_pic_show" />
                <el-form-item label="用户图片" prop="logo">
                  <el-upload ref="myUpload" action="" :auto-upload="false">
                    <el-button size="small" type="primary">点击选择图片</el-button>
                  </el-upload>
                </el-form-item>
                <el-form-item>
              <el-button type="primary" @click="fnUpload" size="small">开始上传</el-button>
            </el-form-item>
        </el-form>
     </div>
</template>
<script>
import { uploadImg } from  '@/api/publish'
export default {
  name:"upload",
  props:['imgChange'],
  data () {
      return  {
         upload_img_url:require('@/assets/pic_bg.png'),
      }
  },
  methods:{
      //上传图片
     async fnUpload () {
        let files = document.querySelector('.el-upload .el-upload__input').files ;
        if(files && files.length) {
          let fd = new FormData();
          fd.append('file', files[0],files[0].name);
          let result = await uploadImg(fd)
          this.$message({message:'上传成功',type:'success'}) && (this.upload_img_url = result.data.url)
          debugger;
          this.imgChange && this.imgChange(result.url) //调用上层的方法 通知数据变化
        }else{
           this.$message({message:"请选择一张图片",type:"warning"})
        }
      }
  }
}
</script>

<style>
 .upload_pic_show{
    display:block;
    width:240px;
    height:180px;
    margin:15px auto 10px;
  }
</style>
