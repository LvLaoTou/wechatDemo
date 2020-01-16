# wechat-demo

#### 介绍
学习对接微信公众号相关功能

#### 使用技术说明
Java+gradle+springboot

#### 功能列表
    1、公众号获取用户微信公开信息
    2、前端JS调取微信扫一扫功能
    3、公众号创建带事件的二维码
    4、接收微信推送消息通知并响应
#### 使用方法（微信公众号测试账号）
    1、注册微信公众号测试账号https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
    2、注册完成后获取appID和appsecret
    3、拉取代码并完善application.yml里面的appID和appsecret以及domain(微信能回调的地址，即公网能访问的地址)和webUrl(无前端页面可不填，但是注意修改涉及到的相关代码)
    4、微信公众号测试账号配置开发者，接口配置信息-->修改--->URL(controller里面的interfaceConfig(get)请求地址)--->token(与application.yml里面的wechat.config.token一致)
    5、修改JS安全域名即调用微信js的服务器的地址(不需要能回调，即非公网地址也行)
    6、修改网页授权获取用户基本信息--->只需要域名 不要带http和端口 参考（www.gitee.com）
    7、配置完成，即可体验功能列表相关功能