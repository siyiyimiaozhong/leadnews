<template>
    <div class="botton" @click="click">
        <text class="icon" v-if="icon!=''" :style="{color:active?'#3296fa':'#636363'}">{{icon}}</text>
        <text class="text">{{getText()}}</text>
    </div>
</template>

<script>
    export default {
        name: "button",
        props:{
            text : {
                type:String,
                default : ''
            },
            icon : {
                type:String,
                default: '\uf164'
            },
            active : {
                type:Boolean,
                default: false
            },
            activeText : {
                type:String,
                default : ''
            }
        },
        data(){
            return {
                color:''
            }
        },
        methods : {
            click : function(){
                this.$emit('onClick', {});
            },
            getText : function(){
                if(this.active){
                    return this.activeText==''?this.text:this.activeText;
                }else{
                    return this.text;
                }
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .botton{
        border-width: 1px;
        border-radius: 30px;
        border-color: @border-color;
        line-height: 60px;
        flex-direction: row;
        align-items: center;
        vertical-align: center;
        padding: 10px 15px;
        margin: 0px 10px;
    }
    .icon{
        font-size: 32px;
        color: #636363;
        width: 38px;
        margin-left: 10px;
        margin-right: 6px;
    }
    .text{
        color: #636363;
        font-size: 24px;
    }

</style>
