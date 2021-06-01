<template>
    <div class="list-item">
        <text class="title">{{formatTitle(data.title)}}</text>
        <div class="item-image">
            <image class="image" v-for="img in data.image" :src="img"></image>
        </div>
        <div class="item-l">
            <div class="tags">
                <text class="tags-text tags-icon">{{data.icon}}</text>
                <text class="tags-text">{{data.source}}</text>
                <text class="tags-text">评论 {{data.commit}}</text>
                <text class="tags-text">{{formatDate(data.date)}}</text>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "article_3",
        props:{
            data:{
                type:Object
            }
        },
        methods : {
            formatDate:function(time){
                return this.$date.format13(time);
            },
            formatTitle:function(title){
                if(title.length>20){
                    return title.substring(0,19);
                }
                return title;
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../styles/article';
    .list-item{
        height: @list-2-height;
    }
    .item-image{
        flex-direction: row;
        padding: 6px 10px 0px;
        justify-content: space-around;
    }
    .image{
        width: 240px;
    }
</style>
