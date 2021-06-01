<template>
  <div class="login">
    <div class="container">
      <img src="@/assets/logo_index.png" class="user-avatar">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" class="login-ruleForm">
        <el-form-item prop="name">
          <el-input type="text" v-model="ruleForm.name" autocomplete="off" placeholder="请输入账户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item class="loginBtn">
          <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { loginByUsername } from '@/api/login'
export default {
  data() {
    var validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入登录用户名'));
      } else {
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        callback();
      }
    };
    return {
      checked: true,
      ruleForm: {
        name: '',
        password: '',
      },
      rules: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ],
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
      }
    };
  },
  components: {
  },
  computed: {

  },
  methods: {
  async submitForm () {
       let {password , name} = this.ruleForm;
       if(!name || !password){
           this.$message({
              message:'用户名和密码不能为空',
              type:'warning'
            })
            return
       }
       //登录
       let result = await loginByUsername(name,password) //登录
        if(result.code==0){
          this.$router.replace({path:'/index'}) //跳转
        }else{
          this.$message({
            message:result.error_message,
            type:'error'
          })
        }
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  background-image: url('../../assets/login_bg.jpg');
  background-size: cover;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  .container {
    background-color: #ffffff;
    width: 30%;
    padding: 30px 0;
    img {
      width: 40%;
    }
  }
  .login-ruleForm {
    padding: 25px 40px 0;
    .allow {
      text-align: left;
      font-size: 14px;
      margin-bottom: 20px;
      color: #999999;
      a {
        color: #3296fa;
      }
      .el-checkbox {
        margin-right: 10px;
      }
    }
    .el-form-item {
      margin-bottom: 20px;
    }
    .checkCode {
      .el-input {
        width: 60%;
        float: left;
      }
      .el-button {
        width: 35%;
        float: right;
        span{
          width: 100%;
          display: inline-block;
        }
      }
    }
    .loginBtn {
      .el-button {
        width: 100%;
      }
    }
  }
}
</style>
