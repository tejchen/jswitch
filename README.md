# jswitch
配置中心，但不仅仅是配置中心

预览地址：
http://119.23.17.187:8081


### main 方式启动客户端
```java
public class BaseTest {

    @JSwitch(code = "PUSH_FIRST_CONFIG", name = "推送的第一个配置")
    public static String testString = "hi~";
    
    @JSwitch(code = "PUSH_NUMBER_CONFIG", name = "Number配置")
    public static int PUSH_NUMBER_CONFIG = 0;

    public static void main(String[] args) throws InterruptedException {
        JSwitchContext ctx = new JSwitchContext("http://127.0.0.1:8080", JSwitchServer.Default);
        JSwitchManager manager = JSwitchManager.newSwitchManager(ctx);
        manager.init("TEST_APP", BaseTest.class);
        for (;;){
            Thread.sleep(1000);
        }
    }
}

```


### 基于 spring XML 启动客户端
```xml
<bean class="com.tejchen.switchclient.JSwitchSpringManager" init-method="init">
    <property name="url" value="http://127.0.0.1:8080"/>
    <property name="server" value="Default"/>
    <property name="appCode" value="TEST_APP"/>
</bean>
```


### 基于 spring 注解 启动客户端
```java
@Configuration
public class Config {
    
    @Bean(initMethod = "init")
    public JSwitchSpringManager entitlement() {
        JSwitchSpringManager manager = new JSwitchSpringManager();
        manager.setAppCode("TEST_APP");
        manager.setUrl("http://127.0.0.1:8080");
        manager.setServer("Default");
        return manager;
    }    
}
```
