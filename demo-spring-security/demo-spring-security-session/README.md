

## 简介

前面我们介绍了基于SpringSecurity的两种认证方式：[表单认证](https://juejin.cn/post/7030306851762176007)和[基本认证](https://juejin.cn/post/7031077013393768484)；

本篇我们介绍下认证后如何获取用户的基本信息;

这里的核心就是`SecurityContextHolder`类。

## 目录

1. 从SecurityContextHolder中获取
2. 从控制器中获取
3. 从自定义接口获取

## 正文

### 1. 从SecurityContextHolder中获取

代码如下所示：

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (!(authentication instanceof AnonymousAuthenticationToken)) {
    String currentUserName = authentication.getName();
    return currentUserName;
}else{
    return "";
}
```

这里我们加了一个校验，当获取的认证用户存在时我们才去访问；

这种方法最大的好处就是方便，直接通过静态方法就可以获取；

但是缺点也很明显，其中最大的缺点就是不方便测试；

### 2. 从控制器中获取

通过控制器来获取用户信息，就很灵活了；

- 我们可以通过Principal参数获取：这里的Principal其实就是一个实体类，用来代表用户，可以是个人，也可以是公司

```java
@GetMapping("/userinfo-principal")
public String userinfoByPrincipal(Principal principal) {
    return principal.getName();
}
```

- 也可以通过Authentication参数获取：

```java
@GetMapping("/userinfo-authentication")
public String userinfoByAuthentication(Authentication authentication) {
    return authentication.getName();
}
```

上面我们主要获取了用户名，用户的其他信息也是可以获取的；

这里我们可以试着获取用户的权限：通过 UserDetails 类来获取:

```java
@GetMapping("/userinfo-authentication")
public String userinfoByAuthentication(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    System.out.println("User has authorities: " + userDetails.getAuthorities());
    return authentication.getName();
}
```

> 需要注意的是， Principal 是不能转换成UserDetails的，因为他俩之间没关系；
>
> 而这里的authentication.getPrincipal()返回的是一个Object对象，在请求接口时，Object传入的是一个UserDetails对象，所以获取时可以通过UserDetails强转；

通过debug我们可以看到，在访问`/userinfo-authentication`时，getPrincipal返回的实际上就是一个User对象(User实现了UserDetails)，所以转换是没问题的

![image-20211116172359093](https://i.loli.net/2021/11/16/xogBnKGP5kcCeIJ.png)

- 还可以通过HttpServletRequest获取：这个其实本质还是通过 Principal 获取

```java
@GetMapping("/userinfo-request")
public String userinfoByRequest(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    return principal.getName();
}
```

### 3. 自定义接口获取

前面我们体验了从控制器获取，这种方式有点局限，因为如果想在其他类中获取，就无能为力了；

其实更灵活的方式是自己定义一个Bean，然后在需要的地方注入来获取；

这里其实是对第一种方式的升级，将 SecurityContextHolder包装到一个Bean中，然后在需要的地方进行注入即可；

接口类：**IAuthenticationFacade**

```java
public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
```

实现类：**AuthenticationFacade**

```java
@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
```

这样一来，我们就可以在需要的地方通过@Autowired注入IAuthenticationFacade即可：

```java
@RestController
public class UserController {

    @Autowired
    IAuthenticationFacade authenticationFacade;
        
    @GetMapping("/userinfo-custom-interface")
    public String userinfoByCustomInterface() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }

}
```

可以看到，这次我们没有用到任何的参数，只通过自定义的Bean来获取，这样不仅充分利用了Spring的依赖注入功能，还使得获取信息变得更加灵活。

## 总结

上面介绍了三种获取方式：

1. 直接通过 SecurityContextHolder 的静态方法获取
2. 从控制器中获取：通过各种参数，比如Principal、Authentication、HttpServletRequest
3. 从自定义的Bean中获取：该方法是对方法1的升级，通过将 SecurityContextHolder包装到 Bean 中，使得获取信息更加灵活



[源码地址](https://github.com/Jalon2015/spring-boot-demo/tree/master/demo-spring-security/demo-spring-security-userinfo)
