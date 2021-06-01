//导出菜单数据
export  const  MenuData = [
   {
       title:'首页',path : '/index' ,icon:'el-icon-s-home'
   },
   {
       title:'用户管理',path : '/auth/index' ,icon:'el-icon-user',
       children:[
           { title:'用户列表' , path : '/users/index'},
           { title:'用户审核' , path : '/auth/index'}
       ]
   },
   {
        title:'图文管理', path:'/article',icon:'el-icon-edit',
        children:[
          { title:'频道管理' , path : '/channel/index'},
          { title:'内容管理' , path : '/content/index'},
          { title:'爬虫审核' , path : '/content/auth_crawler'},
          { title:'媒体审核' , path : '/content/auth_media'},
          { title:'敏感词设置' , path : '/sensitive/index'}
        ]
   },
   {
      title:'数据统计', path:'/fans',icon:'el-icon-data-line',
      children:[
      { title:'网站统计' , path : '/fans/index'},
      { title:'内容统计' , path : '/fans/info'}
    ]
  },
  { title:'系统管理',path:'/user/center',icon:'el-icon-setting',
    children:[
      { title:'账号管理' , path : '/fans/index'},
      { title:'角色管理' , path : '/fans/info1'},
      { title:'权限管理' , path : '/fans/info2'},
      { title:'操作日志' , path : '/fans/info3'}
    ]
  },
  { title:'头条大屏',path:'/screen',icon:'el-icon-full-screen'}
]
