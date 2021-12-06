# 上传文件

## 简介

前端用vue写个简单界面，用来选择文件，进行上传

后端用Spring Boot写个接口，用来接收文件

> PS：注意跨域问题

## 示例

#### 前端：

**文件上传页面** `Home.vue`

```vue
<template>
	<input type="file" @change="upload($event)">
</template>

<script> 
import axios from '../assets/js/axios'
    
function upload(e){
      let file = e.target.files[0]
      let formData = new FormData()
      formData.append('file', file)
      axios.post('http://localhost:8081/upload', formData).then(res=>{
        console.log(res);
      })
    }
```

**知识点**：

- `$event`属性：该属性用在**监听事件的内联语句**中，用来表示原生`DOM`事件，比如这里的`$event`就是**选择的文件信息**
- `FormData`对象：
  - 该对象可以用来表示**表单数据的键值对**`key/value`数据
  - 然后通过`XMLHttpRequest`发送出去(**这里用axios来发送)**；
  - append()方法可以添加file等表单属性（细节见后面的参考链接）
- `axios`库：
  - 一个基于http客户端的支持promise的网络请求库
  - 如果是在浏览器端，则发送 `XMLHttpRequest`请求
  - 如果是用在NodeJS里，则发送`http`请求

#### 后端：

**文件上传控制器** `UploadController.java`

```java
@RestController
public class UploadController {

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(MultipartFile file){
        System.out.println(file.getOriginalFilename());
        return "upload success";
    }
}
```

**配置文件** `MyConfig.java`

```java
@Configuration
public class MyConfig {
	
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}
```

**知识点**

- `consumes`属性：
  - 用来指定接收的内容类型`Content-Type`，例如这里的`MediaType.MULTIPART_FORM_DATA_VALUE`(它的值就是`multipart/form-data`)
  - 这里不指定`consumes`也可以，系统会自动识别，但是建议还是指定，一个是代码更加规范，一个是以防万一
- `MultipartFile`类：该类用来接收前端传来的文件
- `CORS`跨域：配置文件中配置了跨域请求处理，这样不同IP不同端口，都可以请求上传文件

## 参考

- [Vue的$event属性](https://cn.vuejs.org/v2/guide/events.html#%E5%86%85%E8%81%94%E5%A4%84%E7%90%86%E5%99%A8%E4%B8%AD%E7%9A%84%E6%96%B9%E6%B3%95)
- [FormData对象](https://developer.mozilla.org/zh-CN/docs/Web/API/FormData)
- [axios库](https://github.com/axios/axios)
- [consumes属性](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-requestmapping-consumes)
- [MultipartFile类](https://docs.spring.io/spring-framework/docs/1.2.x/javadoc-api/org/springframework/web/multipart/MultipartFile.html)