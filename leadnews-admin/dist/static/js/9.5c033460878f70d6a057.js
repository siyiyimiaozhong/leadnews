webpackJsonp([9],{"+b4D":function(t,e){},"3kCj":function(t,e){},t9vl:function(t,e){},ziI6:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a("lC5x"),r=a.n(s),n=a("4YfN"),l=a.n(n),u=a("J0Oq"),o=a.n(u),i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("section",{staticClass:"filter"},[a("div",{staticClass:"filter-container"},[a("el-form",{ref:"form"},[a("el-form-item",{attrs:{label:"审核状态：","label-width":"110px"}},t._l(t.stateList,function(e){return a("a",{key:e.value,class:["state_label",e.value===t.selectState.value?"active":""],attrs:{href:"javascript:;"},on:{click:function(a){return t.changeState(e)}}},[t._v(t._s(e.label))])}),0)],1)],1)])},staticRenderFns:[]};var c=a("C7Lr")({props:["changeParam"],data:function(){return{stateList:[{label:"待审核",value:1},{label:"审核通过",value:9},{label:"审核失败",value:2}],date:null,selectState:{label:"待审核",value:1},userName:""}},methods:{queryData:function(){var t={status:this.selectState.value};this.changeParam(t)},changeState:function(t){this.selectState=t,this.queryData()}}},i,!1,function(t){a("t9vl")},"data-v-74a3a074",null).exports,h=a("xT6B"),f=a("lS+k"),m={props:["host","authList","pageSize","total","changePage","authPassRealName","authFailRealName"],data:function(){return{listPage:{currentPage:1},params:{id:"",msg:""}}},methods:{getImage:function(t,e){return t[e]?this.host+t[e]:f},pageChange:function(t){this.changePage&&this.changePage({page:t})},resetPage:function(){},dateFormat:function(t){return h.a.format13HH(t)},operateForPass:function(t){this.params.id=t,this.authPassRealName(this.params)},operateForFail:function(t){this.params.id=t,this.open("请输入驳回审核原因")},open:function(t){var e=this;this.$prompt(t,"提示",{confirmButtonText:"确定",cancelButtonText:"取消"}).then(function(t){var a=t.value;e.params.msg=a,e.authFailRealName(e.params)}).catch(function(){})}}},p={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("section",{staticClass:"result"},[a("header",[t._v(t._s("共找到"+t.total+"条符合条件的内容"))]),t._v(" "),a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.authList}},[a("el-table-column",{attrs:{label:"姓名"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.name))])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"身份证号"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.idno))])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",["1"==e.row.status?a("el-tag",{staticClass:"audit"},[t._v("待审核")]):t._e(),t._v(" "),"2"==e.row.status?a("el-tag",{staticClass:"publish"},[t._v("驳回审核")]):t._e(),t._v(" "),"9"==e.row.status?a("el-tag",{staticClass:"publish"},[t._v("审核通过")]):t._e()],1)]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"正面照"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[a("img",{staticClass:"article-img",attrs:{src:t.getImage(e.row.font_image,"back_image")}})])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"背面照"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[a("img",{staticClass:"article-img",attrs:{src:t.getImage(e.row.back_image,"back_image")}})])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"手持照"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[a("img",{staticClass:"article-img",attrs:{src:t.getImage(e.row.hold_image,"hold_image")}})])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"原因"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.reason))])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"提交时间"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[a("dd",{staticClass:"time"},[t._v(t._s(t.dateFormat(e.row.submited_time)))])])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"操作",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[1==e.row.status?a("el-button",{attrs:{size:"mini"},on:{click:function(a){return t.operateForPass(e.row.id)}}},[t._v("通过")]):t._e(),t._v(" "),1==e.row.status?a("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(a){return t.operateForFail(e.row.id)}}},[t._v("驳回")]):t._e()]}}])})],1),t._v(" "),a("div",{staticClass:"pagination"},[a("el-pagination",{attrs:{layout:"total,prev, pager, next","current-page":t.listPage.currentPage,"page-size":t.pageSize,total:t.total},on:{"current-change":t.pageChange,"update:currentPage":function(e){return t.$set(t.listPage,"currentPage",e)},"update:current-page":function(e){return t.$set(t.listPage,"currentPage",e)}}})],1)],1)},staticRenderFns:[]};var d=a("C7Lr")(m,p,!1,function(t){a("3kCj")},"data-v-1f42bd7c",null).exports,g=a("vLgD"),v=a("2EN7");var _={name:"AuthManage",data:function(){return{params:{page:1,size:10},total:0,host:"",authList:[]}},created:function(){this.searchAuthList()},components:{SearchTool:c,SearchResult:d},methods:{searchAuthList:function(t){var e=this;return o()(r.a.mark(function a(){var s;return r.a.wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return a.next=2,r=l()({},e.params,t),new g.a({url:v.b,method:"post",data:r});case 2:200==(s=a.sent).code?(e.authList=s.data,e.host=s.host,e.total=s.total):e.$message({type:"error",message:s.error_message});case 4:case"end":return a.stop()}var r},a,e)}))()},authPassRealName:function(t){var e=this;return o()(r.a.mark(function a(){var s;return r.a.wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return a.next=2,r=t,new g.a({url:v.c,method:"post",data:r});case 2:200==(s=a.sent).code?e.$message({type:"success",message:"操作成功"}):e.$message({type:"success",message:s.error_message}),e.searchAuthList();case 5:case"end":return a.stop()}var r},a,e)}))()},authFailRealName:function(t){var e=this;return o()(r.a.mark(function a(){return r.a.wrap(function(a){for(;;)switch(a.prev=a.next){case 0:return a.next=2,s=t,new g.a({url:v.a,method:"post",data:s});case 2:a.sent,e.searchAuthList();case 4:case"end":return a.stop()}var s},a,e)}))()}}},b={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("search-tool",{attrs:{changeParam:t.searchAuthList}}),t._v(" "),a("search-result",{ref:"mySearchResult",attrs:{authList:t.authList,host:t.host,total:t.total,changePage:t.searchAuthList,pageSize:t.params.size,authPassRealName:t.authPassRealName,authFailRealName:t.authFailRealName}})],1)},staticRenderFns:[]};var w=a("C7Lr")(_,b,!1,function(t){a("+b4D")},"data-v-0da55d53",null);e.default=w.exports}});
//# sourceMappingURL=9.5c033460878f70d6a057.js.map