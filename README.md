# leadnews
头条系统采用当下火热的微服务+大数据技术架构实现。头条系统对海量信息进行搜集，通过系统计算分类，分析用户的兴趣进行推送从而满足用户的需求。



## APP主要功能大纲

![2-1-1](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602101545.png)

- 频道栏：用户可以通过此功能添加自己感兴趣的频道，在添加标签时，系统可依据用户喜好进行推荐
- 文章列表：需要显示文章标题、文章图片、评论数等信息，且需要监控文章是否在APP端展现的行为
- 搜索文章：联想用户想搜索的内容，并记录用户的历史搜索信息
- 个人中心：用户可以在其个人中心查看收藏、关注的人、以及系统设置等功能
- 查看文章：用户点击文章进入查看文章页面，在此页面上可进行点赞、评论、不喜欢、分享等操作；除此之外还需要收集用户查看文章的时间，是否看我等行为信息
- 实名认证：用户可以进行身份证认证和实名认证，实名认证之后即可成为自媒体人，在平台上发布文章
- 注册登录：登录时，验证内容为手机号登录/注册，通过手机号验证码进行登录/注册，首次登录用户自动注册账号。



## APP用例图（主要功能）

![2-1-2](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602101730.png)



## WEMEDIA功能大纲

![2-1-3](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602101825.png)

- 内容管理：自媒体用户管理文章页面，可以根据条件进行筛选，文章包含草稿、已发布、未通过、已撤回状态。用户可以对文章进行修改，上/下架操作、查看文章状态等操作
- 评论管理：管理文章评论页面，显示用户已发布的全部文章，可以查看文章总评论数和粉丝评论数，可以对文章进行关闭评论等操作
- 素材管理：管理自媒体文章发布的图片，便于用户发布带有多张图片的文章
- 图文数据：自媒体人发布文章的数据：阅读数、评论数、收藏了、转发量，用户可以查看对应文章的阅读数据
- 粉丝画像：内容包括：粉丝性别分布、粉丝年龄分布、粉丝终端分布、粉丝喜欢分类分布



## WEMEDIA用例图（主要功能）

![2-1-4](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602101904.png)



## ADMIN功能大纲

![2-1-5](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602101925.png)

- 用户管理：系统后台用来维护用户信息，可以对用户进行增删改查操作，对于违规用户可以进行冻结操
- 用户审核：管理员审核用户信息页面，用户审核分为身份审核和实名审核，身份审核是对用户的身份信息进行审核，包括但不限于工作信息、资质信息、经历信息等；实名认证是对用户实名身份进行认证
- 内容管理：管理员查询现有文章，并对文章进行新增、删除、修改、置顶等操作
- 内容审核：管理员审核自媒体人发布的内容，包括但不限于文章文字、图片、敏感信息等
- 频道管理：管理频道分类界面，可以新增频道，查看频道，新增或修改频道关联的标签
- 网站统计：统计内容包括：日活用户、访问量、新增用户、访问量趋势、热门搜索、用户地区分布等数据
- 内容统计：统计内容包括：文章采集量、发布量、阅读量、阅读时间、评论量、转发量、图片量等数据
- 权限管理：超级管理员对后台管理员账号进行新增或删除角色操作



## ADMIN用例图（主要功能）

![2-1-6](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602101957.png)



## 项目技术介绍

基础六层中包括前端（Weex、Vue、Echarts、WS）、网关（GateWay）、DevOps（单元测试、代码规范）等重难点技术

![3-4](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602102048.png)

- Weex+Vue+WebSocket ：使用Weex跨平台开发工具，整合集成VUE框架，完成黑马头条移动端功能开发，并集成WebSocket实现即时消息（文章推荐、私信）的推送

- Vue+Echarts ： 自媒体系统使用Vue开发关键，集成Echarts图表框架，完成相关粉丝画像、数据分析等功能

- Vue+Echarts+WebSocket ： 管理系统也是使用Vue开发，集成Echarts，完成网站统计、内容统计等功能，集成WebSocket，实现系统看板实时数据自动化更新

- Spring-Cloud-Gateway : 微服务之前架设的网关服务，实现服务注册中的API请求路由，以及控制流速控制和熔断处理都是常用的架构手段，而这些功能Gateway天然支持

- PMD&P3C : 静态代码扫描工具，在项目中扫描项目代码，检查异常点、优化点、代码规范等，为开发团队提供规范统一，提升项目代码质量

- Junit : 在持续集成思想中，单元测试偏向自动化过程，项目通过Junit+Maven的集成实现这种过程



服务四层中包括中间件（Kafka、Mycat）、计算（Spark、Neo4j、Hive）、索引、微服务、大数据存储等重难点技术

![3-5](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602102134.png)

- 运用Spring Boot快速开发框架，构建项目工程；并结合Spring Cloud全家桶技术，实现后端个人中心、自媒体、管理中心等微服务。
- 运用WebMagic爬虫技术，完善系统内容自动化采集
- 运用Kafka完成内部系统消息通知；与客户端系统消息通知；以及实时数据计算
- 运用MyCat数据库中间件计算，对系统数据进行分开分表，提升系统数据层性能
- 运用Redis缓存技术，实现热数据的计算，NoSession等功能，提升系统性能指标
- 运用Zoookeeper技术，完成大数据节点之后的协调与管理，提升系统存储层高可用
- 使用Mysql存储用户数据，以保证上层数据查询的高性能
- 使用Mongo存储用户热数据，以保证用户热数据高扩展和高性能指标
- 使用FastDFS作为静态资源存储器，在其上实现热静态资源缓存、淘汰等功能
- 运用Habse技术，存储系统中的冷数据，保证系统数据的可靠性
- 运用ES搜索技术，对冷数据、文章数据建立索引，以保证冷数据、文章查询性能
- 运用Sqoop、Kettle等工具，实现大数据的离线入仓；或者数据备份到Hadoop
- 运用Spark+Hive进行离线数据分析，实现系统中各类统计报表
- 运用Spark Streaming + Hive+Kafka实现实时数据分析与应用；比如文章推荐
- 运用Neo4j知识图谱技术，分析数据关系，产出知识结果，并应用到上层业务中，以帮助用户、自媒体、运营效果/能力提升。比如粉丝等级计算
- 运用AI技术，来完成系统自动化功能，以提升效率及节省成本。比如实名认证自动化



## 分库设计

头条项目采用的分库分表设计，因为业务比较复杂，后期的访问量巨大，为了分摊数据库的压力，整个项目用的不只是一个数据库。其中核心库有**5**个，每一个数据库解决的是一个业务点，非常接近与实际项目设计。

![4-2-1](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602102217.png)

- AppInfo  app信息库，主要存储用户信息，文章信息，用户动态，用户评论，用户认证等信息
- Behavior  用户行为库，主要存储用户行为，包括用户的转发，点赞，评论行为等
- WeMedia  多媒体库，主要存储多媒体人图文数据统计，账号信息，粉丝相关信息等。
- Crawlers  爬虫库，主要存储从网络上爬取的文章信息等。
- Admin  后台管理库，主要存储后台管理员的信息。



## 核心数据流转图

![4-3](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602102246.png)

## 模块介绍

* leadnews-article：针对移动端用户文章列表的获取，文章详情的获取，查询记录的操作等。
* leadnews-behavior：针对移动端用户对于文章的行为数据的收集创建一个行为模块，用于保存用户对于文章的阅读时长，点赞，取消点赞等行为。
* leadnews-login：针对移动端用户登录注册等功能。
* leadnews-user：针对移动端用户相互之间的关注以及后续需要实现的我的收藏、我的粉丝等功能单独划分为一个模块，用于分担服务器压力。
* leadnews-media：针对自媒体端用户也创建一个模块，模块中实现的功能包括：作者文章的管理，素材管理，内容管理以及登录。
* leadnews-admin：针对管理员用户也分隔成一个独立的模块，以减轻单台服务器的压力。主要功能为：登录，频道管理，用户管理，内容管理，图文审核等。
* leadnews-crawler：针对爬虫方面，因为爬虫系统不依赖于其他模块，只需要自动化定时爬取数据存储到数据库中，然后给管理模块发送一个消息审核即可。
* leadnews-migration：针对数据迁移方面，只需要定时将MySQL中未被迁移的数据进行全量迁移至HBase即可，无需依赖其他模块。除了定时迁移MySQL中的全量数据以外，还需要接收热文章消息并将其具体信息保存至MongoDB中。
* leadnews-images：针对用户对于热数据的访问，为了增加文章图片的响应速度和用户体验度，创建一个模块用于缓存热数据中的图片。

除了以上功能性的模块分割以外，还需分进行以下分割：

* leadnews-common：针对其他模块所需要使用的框架和技术进行配置。其中包含所有项目中集成的三方框架的配置，比如Redis、Kafka等。此外还包括项目中的常量统一定义。
* leadnews-model：主要是针对项目中所需要的Dto类、Pojo类、Mapper类和配置文件、枚举等进行归纳。
* leadnews-utils：主要是将公共的工具类（加密，解密，时间转换，对象转换等）进行归纳。
* leadnew-apis：针对后续项目迭代更新为微服务架构，该模块主要是为整个项目暴露接口声明所使用，并且按照每个模块进行子包拆分。



## 项目展示

移动APP首页

![1](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103257.png)

文章详情

![2](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103338.png)

搜索文章

![5](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103404.png)

自媒体端登录页面

![6](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103424.png)

自媒体端内容列表

![7](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103501.png)

自媒体端发布文章

![8](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103522.png)

自媒体端素材管理

![9](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103548.png)

admin端登录页面

![10](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103605.png)

admin端管理页面

![11](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103631.png)

![12](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103652.png)

![13](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103658.png)

![14](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103701.png)

![15](https://gitee.com/siyiyimiaozhong/images/raw/master/img/20210602103706.png)