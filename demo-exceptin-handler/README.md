## 知识点

#### 注解

- @ControllerAdvice

  - 用来定义一个全局控制器的增强类，实现全局的异常处理等通用操作
  - 程序中任意一个地方抛出异常，都可以在这里进行捕获；前提是抛出的异常没有在其他地方被捕获
  - 异常处理的顺序：
    - 首先，如果控制器类中定义了与异常类型匹配的 `@ExceptionHandler` 方法，那么该方法将被调用来处理异常。
    - 如果控制器类中未定义能够处理该异常的 `@ExceptionHandler` 方法，那么异常会被传递到全局的 `@ControllerAdvice` 类中，以查找能够处理该异常的方法。

- @ExceptionHandler

  - 用来定义处理异常的方法，搭配@controllerAdvice可以实现全局异常处理

  - 参数：异常类，捕获到指定的异常类就会执行注解下的方法

  - 例子：

  - ```java
    @ControllerAdvice
    public class ExceptionHandlerDemo {
    
        @ExceptionHandler(Exception.class)
        @ResponseBody
        public String handler(Exception e){
            System.out.println("进来了");
            System.out.println(e.getMessage());
            return "haha";
        }
    }
    ```

  - 上面的代码会捕获Exception异常，并执行下面的handler方法，最后把数据返回到前台

- @RestControllerAdvice

  - @ControllerAdvice和@ResponseBody的合体，类似@RestController

#### 运行时异常：RuntimeException

RuntimeException抛出后，不需要在方法签名中声明

比如下面的代码，定义了一个JsonException的运行时异常，在json方法中抛出后，并没有在方法签名中声明 throw JsonException

- ```java
  public class JsonException extends RuntimeException{
  }
  
  public String json(){
      throw new JsonException("-1","服务器有问题啦");
  }
  
  ```