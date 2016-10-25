# Central Authentication Service (CAS) based on jasig cas 4.0.7

## Getting Started
### 证书配置
> 注：
> 默认证书放在cas-server-webapp/src/main/resources/config下。如需更换自己的证书，请替换该证书。
>
> 生成证书：
>
``` bash
keytool -genkeypair -alias auth.imlinlx.com -keyalg RSA -validity 3600  -keystore auth.imlinlx.jks
```

#### 1）导出证书
``` bash
keytool -exportcert -keystore auth.imlinlx.jks -alias auth.imlinlx.com -file auth.imlinlx.com.crt
```

#### 2）导入证书
``` bash
keytool -import -trustcacerts -alias auth.imlinlx.com -keystore cacerts -file auth.imlinlx.com.crt
```

### 运行
mvn jetty:run

### 访问
https://auth.imlinlx.com:8443/login


##登录受限功能：
增加redis版实现。
默认登录错误次数10次，超出后20分钟内禁止登录。

文档参考：
[https://apereo.github.io/cas/4.0.x](https://apereo.github.io/cas/4.0.x)
