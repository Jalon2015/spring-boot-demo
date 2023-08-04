#### 

## 简介

上传文件到本地路径，前端用postman进行测试

## **知识点**

- @PostMapping的`consumes`属性：

  - 用来指定接收的内容类型`Content-Type`，例如这里的`MediaType.MULTIPART_FORM_DATA_VALUE`(它的值就是`multipart/form-data`)

  - 这里不指定`consumes`也可以，系统会自动识别，但是建议还是指定，一个是代码更加规范，一个是防止其他内容类型的请求

  - 指定consumes的情况下，传入其他类型的数据，比如JSON，返回的报错为不支持的媒体类型

    - ```
      {
          "timestamp": "2023-08-04T07:26:37.512+00:00",
          "status": 415,
          "error": "Unsupported Media Type",
          "message": "",
          "path": "/demo/upload"
      }
      ```

      

  - 没指定consumes的情况下，传入Json数据，返回的报错为500内部错误

    - ```
      {
          "timestamp": "2023-08-04T07:23:36.877+00:00",
          "status": 500,
          "error": "Internal Server Error",
          "message": "",
          "path": "/demo/upload"
      }
      ```

  - 单从报错就可以看出，加了之后报错提示会更加友好

- `MultipartFile`类：该类用来接收前端传来的文件
  - 默认存在Tomcat的临时路径中，通过断点调试，查看file属性，可以看到，我这里临时存的路径为：`C:\Users\Administrator.DESKTOP-RBBHNV9\AppData\Local\Temp\tomcat.8080.5554826482445726824\work\Tomcat\localhost\demo\upload_e6476e89_7026_4c6d_9689_5c12887b4d3d_00000000.tmp`
  - file.transferTo(path)：保存到本地路径；