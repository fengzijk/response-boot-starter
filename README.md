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
api-response:
  enabled: true
  # 有些请求不需要封装  可以根据header不增强
  ignore-header-list:
    - feign
  # 响应增强过滤类列表   比如有些接口不要 输出image等
  advice-filter-class-list:
    - com.fengzijk.common.advice.ApiResponseAdvice
  # 响应增强过滤包列表
  adviceFilterPackageList:
    - com.fengzijk.common.advice
      
  # 请求响应日志配置
  request-log:
    # 是否开启请求响应日志
    enabled: true
    # 响应内容可见类型列表
    visible-content-type-list: application/json
    # 忽略的url列表
    ignore-url-list:
      - /open/dict/all
     # 敏感header列表  
    sensitive-headers-list:
      - authorization
    # 最大响应体大小  
    max-body-size: 2048
    # 响应日志url匹配模式列表
    request-log-url-pattern-list:
      - ^/open/dict/all


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

