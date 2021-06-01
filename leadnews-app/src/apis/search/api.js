function Api(){
    this.vue;
}
Api.prototype = {
    setVue : function(vue){
        this.vue = vue;
    },
    // 加载搜索历史
    load_search_history: function(){
        let url = this.vue.$config.urls.get('load_search_history')
        return this.vue.$request.postByEquipmentId(url,{page_size:5})
    },
    // 删除搜索词
    del_search: function(id){
        let url = this.vue.$config.urls.get('del_search')
        return this.vue.$request.postByEquipmentId(url,{his_list:[{id:id}]})
    },
    // 输入联想
    associate_search: function(searchWords){
        let url = this.vue.$config.urls.get('associate_search')
        return this.vue.$request.postByEquipmentId(url,{search_words:searchWords,page_size:10})
    },
    // 加载热词
    load_hot_keywords: function(){
        let url = this.vue.$config.urls.get('load_hot_keywords')
        return this.vue.$request.postByEquipmentId(url,{page_size:6})
    }
}

export default new Api()
