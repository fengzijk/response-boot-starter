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
    - com.fengzijk.springstudy
~~~


### 3. 由于String的converters问题 需要加入以下配置

~~~java
   @Configuration
   public class WebMvcConfiguration implements WebMvcConfigurer {
   
       /**
        * 交换MappingJackson2HttpMessageConverter与第一位元素 让返回值类型为String的接口能正常返回包装结果
        *
        * @param converters initially an empty list of converters
        */
       @Override
       public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
           for (int i = 0; i < converters.size(); i++) {
               if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                   MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converters.get(i);
                   converters.set(i, converters.get(0));
                   converters.set(0, mappingJackson2HttpMessageConverter);
                   break;
               }
           }
       }
   }
~~~

### 4.返回结果包装示例

~~~json
{
  "code": 200,
  "msg": "请求成功",
  "data": "1111",
  "timestamp": 1663399471767,
  "success": true
}
~~~

### 5. 异常信息处理
~~~json

{
  "code": 400,
  "msg": "请求失败",
  "data": "方法调用方式异常，Get、Post请求不匹配",
  "timestamp": 1663400082513,
  "success": false
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
