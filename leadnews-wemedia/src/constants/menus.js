//导出菜单数据
export  const  MenuData = [
   {
       title:'首页',path : '/index' ,icon:'el-icon-s-home'
   },
   {
        title:'内容管理',path:'/article',icon:'el-icon-edit',
        children:[
          { title:'图文数据' , path : '/material/data'},
        { title:'发布文章' , path : '/article/publish'},
        { title:'内容列表' , path : '/article/list'},
        { title:'素材管理' , path : '/material/list'}
    ]
   },
   {
    title:'粉丝管理', path:'/fans',icon:'el-icon-user',
    children:[
    { title:'粉丝概况' , path : '/fans/index'},
    { title:'粉丝画像' , path : '/fans/info'},
    { title:'粉丝列表' , path : '/fans/list'}
  ]
},
  { title:'账户信息',path:'/user/center',icon:'el-icon-setting'}
]
