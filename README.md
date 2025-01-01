e# springboot 统一返回结果 以及异常信息封装 

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
  "msg": "参数错误",
  "data": [
    "ss不能为空",
    "ss2不能为空"
  ],
  "timestamp": "1663430017151",
}


~~~

## 验签
##### 继承验签基础类
~~~java

public class UserInfoDTO extends BaseSignDTO {
    /**
     * 姓名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

}
~~~

##### 加签验签
~~~ java


 // appid
String appId="fgdfgdg33ghfghf";
// APP秘钥 
String secretKey="jhghjffgdfgdgyy";

//对象 加签名
UserInfoDTO userInfoDTO= new UserInfoDTO();
userInfoDTO.setUsername("11111");
userInfoDTO.setNickname("test");
userInfoDTO.setAppId(appId);
// 加签类型
userInfoDTO.setSignType(SignatureUtils.SignType.MD5.name());
// 时间戳
userInfoDTO.setTimestamp(SignUtils.getTodayDateTime());
// 随机字符串
userInfoDTO.setNonce("hgghfgsdfdfsf");
// 设置签名
userInfoDTO.setSign(SignatureUtils.getSignV2(userInfoDTO,secretKey));

// 对象方式验签
boolean b = SignatureUtils.validateSignV2(userInfoDTO, secretKey);

System.out.println(b);
~~~
