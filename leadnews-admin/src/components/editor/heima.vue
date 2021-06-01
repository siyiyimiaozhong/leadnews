<template>
  <div class="item-wapper">
    <div class="item" v-for="(item,key) in content">
      <div class="item-t-bar">
        <li @click="deleteItem(key)" title="删除">&#xf00d;</li>
        <li @click="clearStyle(key)" title="恢复样式"  v-if="item.type=='text'">&#xf122;</li>
        <li @click="enditorText(key)" title="编辑内容"  v-if="item.type=='text'">&#xf044;</li>
        <li @click="editImg(key)" title="重新选择"  v-if="item.type=='image'">&#xf044;</li>
        <li @click="bold(key)" title="加粗"  v-if="item.type=='text'">&#xf032;</li>
        <li @click="up(key)" title="上移">&#xf062;</li>
        <li @click="down(key)" title="下移">&#xf063;</li>
        <li @click="upFontSize(key)" title="加大字号"  v-if="item.type=='text'">&#xf15d;</li>
        <li @click="downFontSize(key)" title="减小字号"  v-if="item.type=='text'">&#xf15e;</li>
        <li @click="addText(key)" title="添加文字" style="float: left">&#xf034;</li>
        <li @click="addImg(key)" title="添加图片" style="float: left">&#xf03e;</li>
      </div>
      <div class="item-t" v-if="item.type=='text'" :style="item.style">{{item.value}}</div>
      <div class="item-i" v-if="item.type=='image'"><img style="max-width: 290px" :src="item.value"></div>
    </div>
    <el-dialog :title="controller.editorTitle" :visible.sync="controller.dialogTextVisible">
      <el-form>
        <el-input
          type="textarea"
          :rows="5"
          placeholder="请输入内容"
          v-model="controller.editorText">
        </el-input>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveText('cancel')">取 消</el-button>
        <el-button type="primary" @click="saveText('ok')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "heima",
    props:{
      data : {
        type:Array,
        default:function () {
          return [
            {
              type: 'text',
              value: '这个暑期档被灭霸打了响指之后就显得非常暗淡。易烊千玺的首部大荧幕男主角作品《少年的你》撤档，管虎的战争片《八佰》也因“技术问题”没法如期上映，《伟大的梦想》萎缩成《小小的愿望》，《悲伤逆流成河》不得不强颜欢笑，化作《流淌的美好时光》。'
            },
            {
              type: 'text',
              value: '唯一振奋人心的大概就是“复活”的这部《长安十二时辰》，它突然上线给人带来的惊讶不小于前阵子突然消失的《九州缥缈录》。'
            },
            {
              type: 'image',
              value: 'https://p3.pstatp.com/large/pgc-image/RVFRw8xCiUeTbd',
              style:{
                width:'100%',
                maxWidth:'290px'
              }
            },
            {
              type: 'text',
              value: '6月27日，雷佳音和易烊千玺主演的《长安十二时辰》上线，播出一周，讨论声众多，连身边不少把国产剧放在鄙视链最底端的朋友都追起剧来。'
            },
            {
              type: 'text',
              value: '但我怎么也没想到，和同事关于这部剧的讨论是从吃开始的。罪魁祸首是可以吸的火晶柿子。糙汉张小敬吃柿子的套路太骚气，又红又圆的小柿子，把精致吸管往里一插，手指肚捧着柿子，就这么喝起来了。大家忍不住就柿子品种来了一轮南北方大讨论，琢磨着去哪能骚气地喝一回小柿子。'
            },
            {
              type: 'image',
              value: 'https://p3.pstatp.com/large/pgc-image/RVFRw9gDn1CAGc',
              style:{
                height:'176PX'
              }
            },
            {
              type: 'image',
              value: 'https://p3.pstatp.com/large/pgc-image/RVFRwBeGmhQHL8',
              style:{
                height:'211PX'
              }
            },
            {
              type: 'image',
              value: 'https://p3.pstatp.com/large/pgc-image/RVFRwEM7cyRgyz',
              style:{
                height:'211PX'
              }
            },
            {
              type: 'text',
              value: '《长安十二时辰》的开场简直就是雷佳音的大型吃喝直播，我至今在帮他数着，在顺手忙活解救长安城的前提下，就这十二时辰里，雷佳音到底能吃多少东西。',
              style: {
                fontWeight: 'bold',
                fontSize:'36px'
              }
            },
            {
              type: 'image',
              value: 'https://p3.pstatp.com/large/pgc-image/RVFRwHoGEipU4R',
              style:{
                height:'211px'
              }
            },
          ]
        }
      }
    },
    data(){
      return {
        text:{
          addText:'添加文字',
          editText:'编辑文字'
        },
        controller:{
          editorKey : 0,
          editorTitle:'',
          editorText : "",
          dialogTextVisible:false
        },
        content:[]
      }
    },
    created(){
      this.content=this.data;
    },
    methods:{
      deleteItem:function(key){
        this.$confirm('删除后不可找回，确认删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.content.splice(key,1)
          this.$message({type: 'success',message: '删除成功!'});})
      },
      clearStyle:function(key){
        this.getStyle(key,'w',"0")
        this.$set(this.content[key],'style',{})
      },
      // 编辑
      editImg : function(key){
        this.$emit("addImg",{key:key,type:'edit'});
      },
      // 增加或者删除
      addImg : function(key){
        this.$emit("addImg",{key:key,type:'add'});
      },
      // 添加文字
      addText:function(key){
        this.controller.editorTitle=this.text.addText
        this.controller.editorKey=key
        this.controller.editorText=''
        this.controller.dialogTextVisible=true
      },
      // 编辑文本
      enditorText:function(key){
        this.controller.editorTitle=this.text.editText
        this.controller.editorKey=key
        this.controller.editorText=this.content[key].value
        this.controller.dialogTextVisible=true
      },
      // 保存图片
      saveImage: function(data,image){
        console.log(image)
        if(data.type=='add'){
          let value = {
            type:'image',
            value:image
          }
          this.content.splice(data.key,0,value)
        }else{
          this.$set(this.content[data.key], 'type', "image")
          this.$set(this.content[data.key], 'value', image)
        }
      },
      // 保存编辑和新增的文字
      saveText: function(button){
        if(button=='ok'){
          if(this.controller.editorText!='') {
            if (this.controller.editorTitle == this.text.editText) {
              this.$set(this.content[this.controller.editorKey], 'value', this.controller.editorText)
              this.controller.dialogTextVisible = false
            }else{
              this.content.splice(this.controller.editorKey,0,{type:'text',value:this.controller.editorText});
              this.controller.dialogTextVisible = false
            }
          }else{
            alert('文字内容不能为空！')
          }
        }else{
          this.controller.dialogTextVisible=false
        }
      },
      bold:function (key) {
        let temp = this.getStyle(key,'fontWeight',"normal")
        if(temp!='bold'){temp='bold';}else{temp='normal';}
        this.$set(this.content[key]['style'],'fontWeight',temp)
      },
      up : function(key){
        let i = key-1
        if(i>=0){
          this.content[i] = this.content.splice(key, 1, this.content[i])[0];
        }
      },
      down : function(key){
        let i = key+1
        if(i<this.content.length){
          this.content[i] = this.content.splice(key, 1, this.content[i])[0];
        }
      },
      upFontSize:function (key) {
        let temp = this.getStyle(key,'fontSize','12')
        this.$set(this.content[key]['style'],'fontSize',(parseInt(temp)+1)+"px")
      },
      downFontSize:function (key) {
        let temp = this.getStyle(key,'fontSize','12')
        this.$set(this.content[key]['style'],'fontSize',(parseInt(temp)-1)+"px")
      },
      // 获取一个样式
      getStyle:function(key,name,defValue){
        let style=this.content[key]['style']
        if(style==undefined){
          style = {}
          this.$set(this.content[key],'style',style)
        }
        let temp = style[name]
        if(temp==undefined){
          temp=defValue
          this.$set(this.content[key]['style'],name,defValue)
        }else{
          temp=temp.toLowerCase()
        }
        return temp.replace(';','').replace('px','')
      },
      // 过滤空样式
      getItemStyle:function (style) {
        if(style!=undefined){
          return style
        }
        return {}
      },
      getContent:function () {
        return JSON.stringify(this.content)
      }
    }
  }
</script>

<style scoped>
  .item-wapper{
    border: 1px solid #dbdbdb;
    width: 310px;
    max-height: 550px;
    overflow-x: hidden;
    overflow-y: auto;
    padding: 15px 10px;
    border-radius: 10px;
  }
  .item-wapper::-webkit-scrollbar {/*滚动条整体样式*/
    width: 10px;     /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
  }
  .item-wapper::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
    background: #dbdbdb;
  }
  .item-wapper::-webkit-scrollbar-track {/*滚动条里面轨道*/
    background: transparent;
  }
  .item{
    position: relative;
    overflow: hidden;
    text-align: left;
    border: 1px solid #ffffff;
  }
  .item-t{
    min-height: 30px;
    font-size: 12px;
    line-height: 150%;
    margin: 5px 0px;
  }
  .item-i{
    margin: 5px 0px;
  }
  .item-i img{
    padding: 0px;
    margin: 0px;
    display: block;
    border: none;
  }
  .item:hover{
    border: 1px solid #dbdbdb;
    border-radius: 10px;
  }
  .item:hover .item-t-bar{
    display: inherit;
  }
  .item-t-bar{
    display: none;
    position: absolute;
    background-color: red;
    opacity: 0.9;
    width: 100%;
    color: #FFFFFF;
    z-index: 999;
    overflow: hidden;
  }
  .item-t-bar li{
    list-style: none;
    float: right;
    line-height: 30px;
    background-color: red;
    font-size: 10px;
    font-family: "FontAwesome";
    padding: 0px 5px;
    cursor: pointer;
  }

</style>
