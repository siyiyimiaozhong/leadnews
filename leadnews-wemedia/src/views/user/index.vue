<template>
  <div class="user-container">
    <header>账号信息</header>
    <div class="wrap">
      <div class="form-item username">
        <label>
          <div>
          <img :src="headImg" class="user-avatar" /><br />
          <a @click="showHead" style="color:#3296fa"  href="javascript:;">更换头像</a>
          </div>
        </label>
        <div v-if="!editUser" class="rightContent">
          <dl>
            <dt>{{user.name}}</dt>
            <dd>{{user.intro}}</dd>
          </dl>
            <a style="color:#3296fa" @click="beginEdit('user')" href="javascript:;">修改</a>
        </div>
           <div v-if="editUser" class="clause edituser">
             <el-form>
               <el-form-item label-width="120px" label="名称">
                  <el-input style='width:30%' placeholder="请输入头条号名称" v-model="userData.name"></el-input>
               </el-form-item>
                <el-form-item  label-width="120px" label="简介">
                  <el-input  style='width:30%'  placeholder="请输入头条号简介" v-model="userData.intro"></el-input>
               </el-form-item>
             </el-form>  
            <div class="btn-group">
              <el-button @click="saveEdit('user')" size="small" type="primary">保存</el-button>
              <el-button @click="cancelEdit('user')" size="small">取消</el-button>
            </div>
          </div>
      </div>
      <div class="form-item userinfo">
        <label>账号信息</label>
        <div class="rightContent">
          <div class="clause">
            <span>头条号类型</span>个人
          </div>
          <div class="clause">
            <span>头条号ID</span>{{user.id}}
          </div>
        </div>
      </div>
      <div class="form-item userinfo">
        <label>登录方式</label>
        <div class="rightContent">
          <div  class="clause">
            <span>绑定手机</span>{{user.mobile}}
          </div>
        </div>
      </div>
      <div class='form-item userinfo'>
          <label>邮箱</label>
          <div class="rightContent">
          <div v-if="!editEmail" class="clause">
             <span>{{user.email}}</span>
             <a href="javascript:;" @click="beginEdit('email')">修改邮箱</a>
          </div>
          <div v-if="editEmail" class="clause">  
            <span>邮箱</span>
            <el-input placeholder="请输入邮箱地址" v-model="emailData"></el-input>
            <div class="btn-group">
              <el-button @click="saveEdit('email')" size="small" type="primary">保存</el-button>
              <el-button @click="cancelEdit('email')" size="small">取消</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog   
      :visible.sync="showHeadUpload"
       title="上传头像"
      >
      <img  class='localimg' v-if="showLocalImg" :src="localImg" alt="">
      <el-upload  :on-change="fileChange" ref="myUpload" class="avatar-uploader" :auto-upload="false" :limit="1">
         <i  class="el-icon-plus avatar-uploader-icon"></i>
       </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showHeadUpload = false">取 消</el-button>
        <el-button type="primary" @click="uploadHead">确 定</el-button>
     </span>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProfile , updateUserProfile , updateUserHead } from '@/api/user' 
import { setUser } from '@/utils/store'
export default {
  name: 'ContentManage',
  data() {
    return {
       user:{},
       emailData:null, //邮箱
       userData:null,  //用户信息
       editEmail:false,//编辑邮箱
       editUser:false, //编辑用户信息
       showHeadUpload:false,
       showLocalImg:false, //显示本地图片
       localImg:null
    }
  },
  created () {
     this.getUser() //获取用户个人资料
  },
  computed: {
     headImg () {
        return  this.user.photo ? this.user.photo : require('@/assets/avatar.jpg')
     }
  },
  methods: {
    fileChange () {
         let file = document.querySelector('.el-upload .el-upload__input').files[0] ;
         this.localImg = URL.createObjectURL(file)
         this.showLocalImg = true //显示图片
    },
    /***
     * 显示上传头像的图层
     * ***/
    showHead () {
      this.$refs.myUpload && this.$refs.myUpload.clearFiles()  //清除垃圾数据
      this.showHeadUpload = true //显示弹层
    },
    async getUser () {
      let  result = await getUserProfile()  //获取用户数据
      setUser(result) //更新数据到缓存中
      this.user = result;  //设置用户数据
    },
    //开始进入编辑态 编辑的是某个信息
    beginEdit (type) {
       if(this.checkOtherClose(type)) {
            this.$message({type:'warning',message:'请关掉其他正在编辑的内容'})
            return;
       }
       if(type == 'email') {
          this.editEmail = true //编辑邮箱
          this.emailData = this.user.email //读取邮箱
       }
       else if(type == 'user') {
          this.userData = {...this.user} //解构方式的赋值 因为如果是对象 可能造成数据会自动同步到原有数据
          this.editUser = true //用户信息
       }
    },
    //检查其他的编辑状态功能是否已关闭
    checkOtherClose (type) {
        if(type == 'email'){
          return this.editUser 
       }
       else if(type == 'user'){
          return  this.editEmail 
       }
    },
    //取消编辑 通用方法
    cancelEdit (type) {
        if(type == 'email') {
          this.editEmail = false //取消编辑邮箱
       }
       else if(type == 'user') {
          this.editUser = false //取消编辑用户
       }
    },
    //保存编辑态
  async saveEdit (type) {
      if(type == 'email') {
        let pattrn = /^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+\.)+[A-Za-z]{2,4}$/
        if(this.emailData.match(pattrn)) {
         await updateUserProfile({...this.user,email:this.emailData})
         this.getUser () //重新加载数据
        this.editEmail = false  //保存成功关闭
        }else{
           this.$message({ type:'warning',message:'邮箱格式不正确!'})
        }
     }
     else if (type == 'user') {
        if(this.userData.name){
            await updateUserProfile(this.userData)
            this.getUser () //重新加载数据
            this.editUser = false  //保存成功关闭
        }else{
           this.$message({type:'warning', message:'头条号名称不能为空' })
        }
     }
    },
    //修改头像
   async uploadHead () {
        let files = document.querySelector('.el-upload .el-upload__input').files ;
        if(files && files.length) {
          let fd = new FormData();
          fd.append('photo', files[0], files[0].name);
           await updateUserHead(fd)
          this.$message({message:'上传成功',type:'success'}) && this.getUser () //重新加载数据
        }else{
           this.$message({message:"请选择一张图片",type:"warning"})
        }   
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.user-container {
  background-color: #ffffff;
  text-align: left;
  border: 1px solid #e7e7e9;
  font-size: 14px;
  .avatar-uploader  {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    width: 178px;
    height: 178px;
    margin: 0 auto;
  }
  .localimg {
    width: 178px;
    height: 178px;
    border-radius: 4px;
    position: absolute;
  }
  .avatar-uploader:hover {
    border-color: #409EFF;
  }
   .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  header {
    color: #323745;
    height: 55px;
    line-height: 55px;
    padding: 0 15px;
    background-color: #fbfbfb;
    border-bottom: 1px solid #e8e8e8;
  }
  .wrap {
    padding: 0 30px 80px;
    .form-item {
      overflow: hidden;
      border-bottom: 1px solid #e8e8e8;
      padding: 25px 0;
      &:last-child {
        border: none;
      }
      label {
        float: left;
        width: 80px;
        text-align: right;
        font-weight: normal;
        color: #323745;
        img {
          width: 60px;
          height: 60px;
          border-radius: 50%;
        }
      }
      .rightContent {
        margin-left: 120px;
        overflow: hidden;
      }
    } 
  }
  .username {
     .edituser {
        margin-left:100px; 
        .btn-group {
          margin-left: 120px;
          margin-top: 20px;
        }
      }
    .rightContent {
      display: flex;
      justify-content: space-between;
      margin-top: 8px;
      dl {
        dt {
          font-size: 16px;
        }
        dd {
          color: #999999;
          font-size: 12px;
          margin-top: 7px;
        }
      }
    }
  }
  .userinfo {
    padding-bottom: 0 !important;
    .rightContent {
      margin-top: -20px;
    }
    
    .clause {

      color: #666666;
      border-bottom: 1px solid #e8e8e8;
      line-height: 60px;
      .btn-group {
        margin-left: 120px;
        line-height: 40px;
        margin-bottom: 10px;
      }
      &:last-child {
        border: none;
      }
      a {
        float: right;
        color: #3296fa;
      }
      span {
        color: #333333;
        width: 120px;
        display: inline-block;
      }
    }
  }
  .el-input {
    width: auto;
  }
}

</style>