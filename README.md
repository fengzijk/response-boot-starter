# springboot 统一返回结果 以及异常信息封装 

## 使用示例


### 1. 依赖
### Latest Version: [![Maven Central](https://img.shields.io/maven-central/v/com.fengzijk/response-spring-boot-starter.svg)](https://search.maven.org/search?q=g:com.fengzijka:response-spring-boot-starter*)


``` xml
        <dependency>
            <groupId>com.fengzijk</groupId>
            <artifactId>response-spring-boot-starter</artifactId>
            <version>Latest Version</version>
        </dependency>
```


### 2. config 配置文件
~~~yml
global-response:
  enabled: true
  # 请求响应日志开关
  request-log-url-pattern: /*
  # 忽略的请求头
  ignore-header-list:
    - feign
  # 需要过滤包装的类   
  advice-filter-class-list:
    - com.fengzijk.demo.common.advice.GlobalResponseAdvice
    - com.fengzijk.demo.common.advice.GlobalRequestLogAdvice
    # 需要过滤的包
  advice-filter-package-list: 
    - com.fengzijk.demo.common.advice

~~~


### 3.返回结果包装示例

~~~json
{"statusCode":"20000","statusMessage":"请求成功","data":"aaaaa","timestamp":"1735724275333"}
~~~

### 4. 异常信息处理
~~~json

{
  "code": "21000",
  "statusMessage": "参数错误",
  "data": [
    "ss不能为空",
    "ss2不能为空"
  ],
  "timestamp": "1663430017151",
}


~~~

