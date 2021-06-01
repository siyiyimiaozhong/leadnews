<template>
    <div class="cell-body">
        <text class="icon">{{icon}}</text>
        <div @click="onClick" v-for="item in getText()">
            <text class="text" :style="{color:getColor(item)}">{{item}}</text>
        </div>
        <text class="skip"> </text>
    </div>
</template>

<script>
    export default {
        name: "search_2",
        props:{
            keyword:{
                type:String,
                default:""
            },
            search:{
                type:String,
                default:""
            },icon:{
                type:String,
                default:'\uf002'
            }
        },
        methods:{
            getText : function(){
                return this.keyword.split('')
            },
            getColor : function(chr){
                return this.search.indexOf(chr)!=-1?'red':'#222222'
            },
            onClick : function(){
                this.$emit("onClick",this.keyword)
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .cell-body{
       flex-direction: row;
        font-size: 36px;
        align-items: center;
        border-bottom-color: #ebebeb;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        padding: 17px 20px;
        background-color: #ffffff;
    }
    .skip{
        flex: 1;
    }
    .icon{
        width: 60px;
        font-size: 28px;
        text-align: center;
        color: #ababab;
    }
</style>
