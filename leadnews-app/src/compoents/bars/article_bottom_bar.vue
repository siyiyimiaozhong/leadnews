<template>
    <div class="bar_bg">
        <Search icon="" rightWidth=25 placeholder="写评论" />
        <text class="icon">&#xf075;</text>
        <text @click="clickCollection" class="icon" :style="{color:collection?'#3296fa':'#a5a5a5'}">&#xf005;</text>
        <text @click="clickForward" class="icon" :style="{color:forward?'#3296fa':'#a5a5a5'}">&#xf14d;</text>
    </div>
</template>

<script>
    import Search from '@/compoents/inputs/search';
    export default {
        name: "article_bottom_bar",
        components: {Search},
        props:{
            collection:{
                type:Boolean,
                default:false
            },
            forward:{
                type:Boolean,
                default:false
            }
        },
        methods:{
            clickCollection : function(){
                this.$emit("clickCollection",{})
            },
            clickForward : function(){
                this.$emit("clickForward",{})
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .bar_bg{
        width: @screen-width;
        flex-direction: row;
        align-items:center;
        border-width:1px;
        border-color: @border-color;
        background-color: @bg-white;
        border-style: solid;
        height: @top-height;
        padding-top: 4px;
        padding-left: 5px;
    }
    .icon{
        color: #a5a5a5;
        font-size: 48px;
        width: 80px;
    }
</style>
