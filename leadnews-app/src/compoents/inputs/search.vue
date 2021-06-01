<template>
    <div class="body" :style="getBodyStyle">
        <text v-if="icon!=''" class="icon" :style="getIconStyle">{{icon}}</text>
        <input @blur="onBlur"
               @focus="onFocus"
               @input="onInput"
               @return="onSubmit"
               @change="onChange"
               :return-key-type="returnType"
               :autofocus="autofocus"
               autocomplete="off"
               :type="type"
               :placeholder="placeholder"
               class="input"
               :value="value"
               :style="getInputStyle"
               />
        <text class="skin">&nbsp;</text>
    </div>
</template>

<script>
    export default {
        props:{
            flex:{
                type:Number,
                default:1
            },
            height:{
                type:Number,
                default:70
            },
            autofocus:{
                type:Boolean,
                default:false
            },
            leftWidth:{
                type:String,
                default:"5"
            },
            icon:{
              type:String,
              default:'\uf044'
            },
            rightWidth:{
                type:String,
                default:"5"
            },
            type:{
              type:String,
              default:'text'
            },
            returnType:{
                type:String,
                default:'search'
            },
            color:{
              type:String,
              default:'#efefef'
            },
            iconColor:{
                type:String,
                default:'#a5a5a5'
            },
            borderWidth:{
                type:Number,
                default:1
            },
            backgroundColor:{
              type:String,
              default:'#ffffff'
            },
            placeholder:{
                type:String,
                default:'请输入...'
            },
            value:{
                type:String,
                default:''
            },
            radius:{
                type:Number,
                default:30
            }
        },
        data(){
          return {
              inpValue:''
          }
        },
        computed:{
            getInputStyle:function(){
                return {
                    marginLeft:(this.icon==''?'20px':'1px'),
                    height: (this.height - 10) + "px",
                    lineHeight: (this.height - 10) + "px",
                    fontSize: (this.height / 70) * 32 + "px"
                }
            },
            getBodyStyle:function(){
                console.log("======"+(this.height-this.radius))
                return {
                    borderRadius:(this.height-this.radius)+"px",
                    marginLeft:this.leftWidth+'px',
                    marginRight:this.rightWidth+'px',
                    borderColor:this.color,
                    borderWidth:this.borderWidth+"px",
                    backgroundColor: this.backgroundColor,
                    'flex':this.flex,
                    'height':this.height+'px'
                }
            },
            getIconStyle:function(){
                return {
                    color:this.iconColor,
                    width:(this.height-10)+"px",
                    height:(this.height-10)+"px",
                    lineHeight:(this.height-10)+"px",
                    fontSize:(this.height/70)*32+"px"
                }
            }
        },
        methods:{
            onBlur() {
                const self = this;
                setTimeout(() => {
                    self.$emit('onBlur', { value: self.inpValue });
                }, 10);
            },
            onFocus : function(){
                this.$emit('onFocus', {});
            },
            onChange : function(){
                this.$emit('onChange', this.inpValue);
            },
            onSubmit : function(){
                if(this.inpValue!=''){
                    this.$emit('onSubmit', this.inpValue);
                }
            },
            onInput : function(e){
                this.inpValue = e.value;
                this.$emit('onInput', this.inpValue);
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .body{
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        border-width:1px;
        border-color: @border-color;
        border-style: solid;
    }
    .icon{
        font-size: 28px;
        text-align: center;
    }
    .input{
        flex:1;
        border: none;
        placeholder-color:'a5a5a5'
    }
    .skin{
        width: 20px;
    }
</style>
