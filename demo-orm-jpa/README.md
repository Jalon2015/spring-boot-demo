# 持久层技术：Spring Data JPA

> Spring Data JPA 对JPA进行了更加抽象的封装，减少了很多样板代码



### 知识点

- Spring Data JPA

### 问题

- 问题1：
  - 问题描述： Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
  - 原因分析：从报错信息看，是说没有配置`hibernate.dialect`，但实际上yml中有配置；
  - 解决办法：删除JavaConfig配置
  - 参考链接：[stackoverflow](https://stackoverflow.com/questions/26548505/org-hibernate-hibernateexception-access-to-dialectresolutioninfo-cannot-be-null)

- 问题2：

  - 问题描述：No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call

  - 原因分析：删除和更新操作，需要加事务，用来保证数据一致性，
  - 解决办法：在repository的删除和更新操作方法上，加注解`@Modifying`和`@Transactional`