# switch
动态切换（还在编码中，请静候）

client
分模块
1. manager 管理器
    1. 负责初始化
    2. 连接注册中心并初始化监听器
    3. 拉取配置，并更新到本地静态类里面
        1. 没有存量配置时，推送默认配置持久化
2. listener 监听器
    1. 监听变化
    2. 更新到本地平台类里面
3. @interface 自定义注解套件
4. 自定义注解解析helper
    1. 解析并存储到一个中间模型类
5. 


服务端原型

![服务端原型](jswitch-server/src/main/resource/frontend/image/temp.jpg)