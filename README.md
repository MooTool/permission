


----------

说明
--


这是一款纯原生的权限管理系统，不依靠security或者shiro,但参考其实现方式

上手指南
----

目前只需要安装mysql数据库，运行代码即可




安装步骤
----

项目导入idea或eclipse,执行maven更新
运行init.sql生成数据库表，配置更新数据库连接


项目启动注意事项
----

1、数据库配置：/resources/settings.properties
2、项目登录页：/signin.jsp
3、登录使用用户名和密码：
username: admin@qq.com
password: 12345678

测试
--
目前只有部门用户管理、权限管理、权限点管理、和角色列表管理，其他的功能展示还在优化中

技术框架
--

使用到的框架
项目框架：Spring MVC/Mybatis/Redis
基础工具：Maven/Tomcat/MySQL/JDK1.8
前端技术：jQuery/Bootstrap/Mustache/zTree/Duallistbox
其他技术：Druid/ Jackson/lombok/logback/validation


