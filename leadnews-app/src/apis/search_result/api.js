function Api(){
    this.vue;
}
Api.prototype = {
    setVue : function(vue){
        this.vue = vue;
    },
    // 加载
    article_search: function(parms){
        let url = this.vue.$config.urls.get('article_search')
        return this.vue.$request.postByEquipmentId(url,{
            search_words:parms.keyword,
            page_num:parms.pageNum,
            tag:parms.tag,
            page_size:20
        })
    }
}

export default new Api()
