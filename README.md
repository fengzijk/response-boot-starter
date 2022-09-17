# springboot 统一返回结果 以及异常信息封装 

## 使用示例


### 1. 依赖
### Latest Version: [![Maven Central](https://img.shields.io/maven-central/v/com.fengzijk/response-boot-starter.svg)](https://search.maven.org/search?q=g:com.fengzijka:response-boot-starter*)


``` xml
        <dependency>
            <groupId>com.fengzijk</groupId>
            <artifactId>response-boot-starter</artifactId>
            <version>Latest Version</version>
        </dependency>
```


### 2. config 配置文件
~~~yml
global-response:
  ## feign 接口暂时有问题也过滤 feign 根据Header过滤
  feign-header: feign
  ## 过滤的类
  advice-filter-class: 
    - com.fengzijk.springstudy.TestController
  ## 需要过滤的包
  advice-filter-package:
    com.fengzijk.springstudy
~~~

### 3.返回结果包装示例

~~~json
{
  "code": 200,
  "msg": "请求成功",
  "data": "1111",
  "timestamp": 1663399471767,
  "success": true
}
~~~

### 4. 异常信息处理
~~~json

{
  "code": 400,
  "msg": "请求失败",
  "data": "方法调用方式异常，Get、Post请求不匹配，或Form、Body参数不匹配",
  "timestamp": 1663400082513,
  "success": false
}
~~~
