

## 简介

SpringSecurity的认证机制有多种，比如基于用户名/密码的认证，基于OAuth2.0的认证（OAuth已废弃）。。。

而基于用户名/密码的认证方式，又分多种，比如：

- Form Login，表单登录认证（单体应用，比如SpringMVC）
- Basic Authentication，基本的http认证（前后端分离应用）
- 【已废弃】Digest Authentication，数字认证（已废弃，不再使用这种认证方式，因为它的加密方式不安全，比如md5加密等；现在比较安全的加密方式有BCrypt等）

本节介绍的就是第二种：**基本认证的方式**

## 目录

1. maven配置
2. security配置
3. controller控制器
4. web界面
5. 启动运行

## 正文

在开始之前，需要先了解两个词

- Authenticate认证：就是通过用户名/密码等方式，登入到系统，这个过程就是认证；类似于进入景区的大门
- Authorize授权：就是登入到系统之后，校验用户是否有权限操作某个模块，这个过程就是授权；类似于进入景区后，各个收费区域，只有交了钱（有权限），才能进入指定区域；

项目背景：Spring Boot + Vue

项目结构如下：src/main目录就是后端接口，src/web目录就是前端界面

![image-20211112170900370](https://i.loli.net/2021/11/12/XirCJog4VSQWTcH.png)

#### 1.maven配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo</artifactId>
        <groupId>com.jalon</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>demo-spring-security</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    <dependencies>
        <!-- ... other dependency elements ... -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
</project>
```

#### 2. security配置

这里面主要包含两部分：

- authenticate 认证配置：主要配置用户名，密码，角色（这里基于内存来保存，为了简化）
- authorize 授权配置：主要配置各个角色的权限，即可以访问哪些页面

```java
@Configuration
@EnableWebSecurity
public class CustomConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    // 全局配置：用户名+密码，角色（基于内存）
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("javalover").password( passwordEncoder().encode("123456"))
                .authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                // 这里的httpBasic就是说明这里的认证不是通过登录表单，而是基于http形式的请求来认证
                .httpBasic();
    }

    // 这里的密码加密器需注入，不然会提示缺少默认的密码加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
```

#### 3. controller控制器

控制器主要任务就是处理请求，这里我们只写了一个方法，用来测试认证和不认证的区别

```java
@RestController
public class UserController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}

```

#### 4. 命令行请求测试

下面我们对上面的接口进行测试；

先执行下面的请求命令：不传用户名/密码

```bash
curl -i http://localhost:8090/home
```

不出意料，会报401未授权的错误，如下所示：

```bash
HTTP/1.1 401
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Set-Cookie: JSESSIONID=37DBDC431DCA48BEBBEE1A65B82582C1; Path=/; HttpOnly
WWW-Authenticate: Basic realm="Realm"
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 12 Nov 2021 09:31:09 GMT

{"timestamp":"2021-11-12T09:31:09.788+00:00","status":401,"error":"Unauthorized","message":"","path":"/home"}
```

接下来，我们传入配置中设置的用户名/密码（javalover/123456），如下所示：

```bash
curl -i --user javalover:123456 http://localhost:8090/home
```

此时，返回200，而且返回结果为home，表示请求成功，如下所示：

```bash
HTTP/1.1 200
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Set-Cookie: JSESSIONID=1A81AF17E7EB380E16F0E8854DA59452; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/plain;charset=UTF-8
Content-Length: 4
Date: Fri, 12 Nov 2021 09:31:44 GMT

home
```

如果我们没用curl进行请求，而是在浏览器，那么浏览器会自动弹出一个登录窗口，让我们填写用户名密码，如下所示：

![image-20211112173718774](https://i.loli.net/2021/11/12/3hgtFJZUiR1AzBn.png)我们填入`javalover/123456`，就可以看到返回的数据了：

![image-20211112173806301](https://i.loli.net/2021/11/12/TOrkU1wMLdqQbKS.png)

#### 5. vue界面请求测试

刚才我们用curl测了请求，现在我们用vue来试下，核心文件如下：HelloWorld.vue

```vue
<template>
  <div>
      <button @click="home">主页</button>
  </div>
</template>

<script>
import axios from 'axios'
export default {
    data: function (){
        return {
            axiosInstance: {}
        }
    },
    mounted() {
        this.axiosInstance = axios.create({
            baseURL: 'http://localhost:8090',
            auth: {
                username: 'javalover',
                password: '123456'
            }
        })
    },
    methods:{
      home(){
          this.axiosInstance.get('/home').then(res=> {
              console.log('success')
              console.log(res)
          }).catch(err=>{
              console.log('fail')
              console.log(err)
          })
      }
    }
}
</script>
```

下面我们启动vue`yarn run serve`，访问主界面`http://localhost:8080/`，点击主页：可以看到打印了 success

![image-20211112175126444](https://i.loli.net/2021/11/12/JjARF12CPwNdXHY.png)



同理，如果我们把HelloWorld.vue中的用户名/密码配置拿掉，那么就会打印fail，报401错误:

![image-20211112175337702](https://i.loli.net/2021/11/12/CO8Qm5NAVtP2bwI.png)

## 总结

SpringSecurity的基本认证方式跟表单认证方式，后端的代码其实差不多，就是配置的地方不一样；

这俩的核心都是通过用户名/密码的方式进行认证，只是适用的场景不同：

- Form Login，表单登录认证，适用于单体应用

- Basic Authentication，基本的http认证，适用于前后端分离的应用



源码地址：[demo-spring-security-basic-auth](https://github.com/Jalon2015/spring-boot-demo/tree/master/demo-spring-security/demo-spring-security-basic-auth)
