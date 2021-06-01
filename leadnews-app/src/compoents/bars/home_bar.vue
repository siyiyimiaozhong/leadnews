<template>
    <div class="bar_bg">
        <text class="icon"> </text>
        <Search type="search" @onClick="onClick" :icon="icon" left-width="25" rightWidth="20" placeholder="请输入搜索关键字..."/>
        <text class="icon"> </text>
    </div>
</template>

<script>
    import Search from '@/compoents/inputs/search_buttion';
    export default {
        name: "index",
        components: { Search},
        data:()=>{
            return {
                icon:'\uF002'
            }
        },
        mounted(){
            this.backgroundColor = this.$config.style.main_bg;
        },
        methods: {
            onClick : function(){
                this.$router.push('/search')
            },
            minibarLeftButtonClick () {
            },
            minibarRightButtonClick () {
            }
        }
    };
</script>

<style lang="less" scoped>
    @import '../../styles/common';
    .bar_bg{
        width: @screen-width;
        flex-direction: row;
        background-color: @mian-color;
        border-style: solid;
        height: @top-height;
        padding: 7px 15px 10px;
        align-items: center;
    }
    .icon{
        width: 25px;
        color: #ffffff;
        font-family: fontawesome;
        font-size: 32px;
    }
</style>
