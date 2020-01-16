package com.lj.demo.wehcat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取Spring配置文件变量
 * @author lv
 * @version 1.0.0
 */
@Component
public class SpringValueUtil {

    public static String wechatAppId;

    public static String wechatAppSecret;

    public static String webUrl;

    public static String domain;

    public static String token;

    public static String password;

    @Value("${jasypt.encryptor.password}")
    public static void setPassword(String password) {
        System.out.println("密码："+password);
        SpringValueUtil.password = password;
    }

    @Value("${wechat.config.domain}")
    public void setDomain(String domain) {
        SpringValueUtil.domain = domain;
    }

    @Value("${wechat.config.token}")
    public void setToken(String token) {
        SpringValueUtil.token = token;
    }

    @Value("${wechat.config.webUrl}")
    public void setWebUrl(String webUrl) {
        SpringValueUtil.webUrl = webUrl;
    }

    @Value("${wechat.config.appId}")
    public void setWechatAppId(String wechatAppId) {
        SpringValueUtil.wechatAppId = wechatAppId;
    }

    @Value("${wechat.config.appSecret}")
    public void setWechatAppSecret(String wechatAppSecret) {
        SpringValueUtil.wechatAppSecret = wechatAppSecret;
    }
}
