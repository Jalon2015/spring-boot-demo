## 知识点

#### 注解

- @Mapper

  - 将当前类注册为一个Mapper接口

- @MapperScan

  - 配置扫描Dao接口的路径，扫描到会将其注册为Mapper Bean
  - 配置该扫描路径后，对应的Mapper类就不需要添加@Mapper注解

- @Select

  - 配置SQL语句，用在Mapper类的方法上

  - 例子

    - ```java
      @Select("select * from orm_user where name like concat('%',#{name},'%')")
      List<User> selectAllUser(String name);

    - 这里的 %#{name}%，需要通过concat函数进行连接；

    - 如果直接用 '%#{name}%'，那么#{name}不会被解析，而是直接被当做字符串处理

      - 处理后为 like '%#{name}%'

- @Param

  - 配置参数名称，用在Mapper接口的方法形参中，跟xml映射文件中的参数名进行匹配

  - 例子

    - ```java
      int saveUser(@Param("user")User user);
      ```

    - ```xml
      <insert id="saveUser">
          INSERT INTO `orm_user` (`name`,
          `password`)
          VALUES (
          #{user.name},
          #{user.password})
      </insert>
      ```

  - 该注解是否可以省略：

    - 如果是基本类型（Integer, String）的参数，那么@Param可以省略，只要名字匹配即可

    - 如果是对象类型，要看情况；

      - 如果xml映射文件中的语句使用了 paramType，那么变量#{name}就会直接通过属性来访问，而不需要 对象前缀#{user.name}这种形式

        - 此时就可以省略@Param

        - 例子

          - ```java
            int saveUser(User user);
            ```

          - ```xml
            <insert id="saveUser" parameterType="com.jalon.entity.User">
                INSERT INTO `orm_user` (`name`,
                `password`)
                VALUES (#{name},
                #{password})
            </insert>
            ```

      - 如果映射文件中没有使用paramType，那么就是上面的情况，此时就需要@param("user")

  - 建议：

    - 省略，会更加简洁高效