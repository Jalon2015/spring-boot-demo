## 简介
SpringSecurity项目学习案例
参考
- [baeldung网站](https://www.baeldung.com/security-spring) 
- [SpringSecurity官网](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-applications)
## 目录
1. 认证机制：有多种
- 基于用户名/密码的认证方式
  - From Login，表单登录认证
  - Basic Authentication，基本认证
  - Digest Authentication，数字认证（已废弃，不再使用这种认证方式，因为它的加密方式不安全，比如md5加密等；现在比较安全的加密方式有BCrypt等）
- 基于OAuth2的认证方式：



#### 基础：
1. Spring Security注册流程
2. SpringSecurity登录表单
3. 基于接口认证(流行)
4. 登录时的异常处理
5. 登出
6. 登录之后重定向页面
7. 记住我
8. SpringSecurity认证提供器
9. 手动管理认证的用户
10. 额外的登录字段
11. 自定义登录失败的处理器
#### 核心：
1. Maven配置SpringSecurity
2. 在Spring Security中检索用户信息
3. 介绍Spring Security的表达式
4. 不加密、不过滤、允许所有的访问请求
5. Session管理
6. 介绍SpringSecurity方法级别的安全（前面介绍的都是请求级别，也就是URL级别）
7. Spring Security自动配置
8. Spring Security5中的密码加密器
9. 查找注册的Spring Security过滤器，比如前端vue，后端rest