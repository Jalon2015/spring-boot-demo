## 知识点
- 注解：获取yml配置的
  - @ConfigurationProperties：设置属性名的前缀，前缀+字段名就是yml中的配置属性名；
    - ```java
      @ConfigurationProperties(prefix = "application")
      public class ApplicationProperty {
          // name 自动对应 yml 中的 application.name 属性
          private String name;
          // version 自动对应 yml 中的 application.version 属性
          private String version;
      }
  
  - @Value：通过占位符直接设置属性名，占位符为 ${xxx.yyy}格式
  
    - ```java
      // name 对应 yml 中的 developer.name 属性
      @Value("${developer.name}")
      private String name;
      ```
  
      
  
    - 注意：@Value不能搭配 @ConfigurationProperties 使用，只能单独使用；

- application.yml中获取pom.xml中属性值

  - 先在pom.xml中配置资源路径

    - ```xml
      <build>
          <finalName>demo-properties</finalName>
      
          <resources>
              <resource>
                  <directory>src/main/resources</directory>
                  <filtering>true</filtering>
              </resource>
          </resources>
      </build>
      ```

  - 通过占位符 @xxx@ 获取

    - ```yaml
      application:
        name: prod环境  @artifactId@
      ```

    - 这里的 @artifactId@ 就是pom.xml中的配置

- 去掉yml配置中的红线警告，并对自定义配置进行提示

  - 先在pom.xml中添加依赖

    - ```xml
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-configuration-processor</artifactId>
          <optional>true</optional>
      </dependency>
      ```

  - 再添加json格式的提示文件 `resources/META-INF/additional-spring-configuration-metadata.json`

    - ```json
      {
      	"properties": [
      		{
      			"name": "application.name",
      			"description": "Default value is artifactId in pom.xml.",
      			"type": "java.lang.String"
      		},
      		{
      			"name": "application.version",
      			"description": "Default value is version in pom.xml.",
      			"type": "java.lang.String"
      		},
      		{
      			"name": "developer.name",
      			"description": "The Developer Name.",
      			"type": "java.lang.String"
      		},
      		{
      			"name": "developer.website",
      			"description": "The Developer Website.",
      			"type": "java.lang.String"
      		},
      		{
      			"name": "developer.nickname",
      			"description": "The Developer NickName.",
      			"type": "java.lang.String"
      		}
      	]
      }
      ```

  - 此时yml中就会有提示，而且没有红线警告了

    - ![image-20230802170742569](https://raw.githubusercontent.com/Jalon2015/pic/main/win10-company/image-20230802170742569.png)