package com.lj.demo.wehcat.util;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信工具类
 * @author lv
 * @version 1.0.0
 */
@Slf4j
public class WechatUtil {

    /**
     * 工具类私有无惨构造
     */
    private WechatUtil() {

    }


    /**
     * 全局微信access_token存储容器  access_token有效期是7200秒 频繁请求微信服务器获取access_token会被微信限制 微信官方建议全局缓存
     */
    private static final ConcurrentHashMap<String,String> wechatAccessTokenMap = new ConcurrentHashMap(4);

    /**
     * 全局微信access_token存储容器  access_token有效期是7200秒 频繁请求微信服务器获取access_token会被微信限制 微信官方建议全局缓存
     */
    private static final ConcurrentHashMap<String,String> wechatJsapiTicketMap = new ConcurrentHashMap(8);

    /**
     * 获取微信AccessToken
     * @return AccessToken
     */
    public synchronized static String getWechatAccessToken() {
        if (wechatJsapiTicketMap.size() < 1) {
            wechatAccessTokenMap.putAll(getWechatAccessTokenFromWechat());
        }
        String expires = wechatAccessTokenMap.get("expires_in");
        long now = System.currentTimeMillis() / 1000;
        if (now < Long.parseLong(expires)) {
            String accessToken = wechatAccessTokenMap.get("access_token");
            return accessToken;
        } else {
            wechatAccessTokenMap.putAll(getWechatAccessTokenFromWechat());
        }
        return wechatAccessTokenMap.get("access_token");
    }

    /**
     * 从微信服务器获取AccessToken
     * @return AccessToken
     */
    public static Map<String, String> getWechatAccessTokenFromWechat() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + SpringValueUtil.wechatAppId + "&secret=" + SpringValueUtil.wechatAppSecret;
        String token = OkHttpUtil.getResponseBodyFromHttpByGet(url);
        Map<String, String> tokenMap = Constants.GSON.fromJson(token, new TypeToken<HashMap<String, String>>() {
        }.getType());
        long nowTime = System.currentTimeMillis() / 1000;
        tokenMap.put("expires_in", (nowTime + Long.parseLong(tokenMap.get("expires_in"))) + "");
        return tokenMap;
    }

    /**
     * 从微信服务器获取JsapiTicket
     * @return JsApiTicket
     */
    public static Map<String, String> getJsApiTicketFromWechat() {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getWechatAccessToken() + "&type=jsapi";
        String ticket = OkHttpUtil.getResponseBodyFromHttpByGet(url);
        Map<String, String> tokenMap = Constants.GSON.fromJson(ticket, new TypeToken<HashMap<String, String>>() {
        }.getType());
        long nowTime = System.currentTimeMillis() / 1000;
        tokenMap.put("expires_in", (nowTime + Long.parseLong(tokenMap.get("expires_in"))) + "");
        return tokenMap;
    }

    /**
     * 获取微信JsapiTicket
     * @return 微信JsapiTicket
     */
    public synchronized static String getJsApiTicket() {
        if (wechatJsapiTicketMap.size() < 1) {
            wechatJsapiTicketMap.putAll(getJsApiTicketFromWechat());
        }
        String expires = wechatJsapiTicketMap.get("expires_in");
        long now = System.currentTimeMillis() / 1000;
        if (now < Long.parseLong(expires)) {
            String ticket = wechatJsapiTicketMap.get("ticket");
            return ticket;
        } else {
            wechatJsapiTicketMap.putAll(getJsApiTicketFromWechat());
        }
        return wechatJsapiTicketMap.get("ticket");

    }

    /**
     * 前端js调取扫一扫参数
     * @return 扫一扫需要的所有参数
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> getJsSweepQRCodeParam() throws NoSuchAlgorithmException {
        final String noncestr = "flyaudio";
        String jsapi_ticket = getJsApiTicket();
        final String timestamp = "1414587457";
        Map<String, String> map = Maps.newHashMapWithExpectedSize(3);
        StringBuffer sb = new StringBuffer();
        //顺序不能变  参考微信说明https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html  附录1-JS-SDK使用权限签名算法
        sb.append("jsapi_ticket=").append(jsapi_ticket).append("&");
        sb.append("noncestr=").append(noncestr).append("&");
        sb.append("timestamp=").append(timestamp).append("&");
        sb.append("url=").append(SpringValueUtil.webUrl);
        String sign = new String(Hex.encodeHex(MessageDigest.getInstance("SHA-1").digest(sb.toString().getBytes()), true));
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        return map;
    }


    /**
     * 获取创建微信带参数二维码所需参数
     * @param param 需要存入二维码中的参数
     * @return 参数集合
     */
    public static Map<String,Object> getCreateQRCodeParam(String param){
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+getWechatAccessToken();
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
        //该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
        map.put("expire_seconds",604800);
        map.put("action_name","QR_STR_SCENE");
        Map<String, Object> sceneMap = Maps.newHashMapWithExpectedSize(1);
        sceneMap.put("scene_str",param);
        Map<String, Object> actionInfoMap = Maps.newHashMapWithExpectedSize(1);
        actionInfoMap.put("scene",sceneMap);
        map.put("action_info",actionInfoMap);
        String body = OkHttpUtil.getResponseBodyFromHttpByPost(url, Constants.GSON.toJson(map));
        Map<String,Object> resultMap = Constants.GSON.fromJson(body,new TypeToken<HashMap<String, Object>>() {}.getType());
        return resultMap;
    }

    /**
     * 获取创建微信带参数二维码所需ticket
     * @param param 需要存入二维码中的参数
     * @return 参数集合
     */
    public static String getCreateQRCodeTicket(String param){
        Map<String, Object> resultMap = getCreateQRCodeParam(param);
        return (Objects.equals(resultMap.get("ticket"),null)) ? null : resultMap.get("ticket").toString();
    }

    /**
     * 获取创建微信带参数二维码所需ticket
     * @param param 需要存入二维码中的参数
     * @return 参数集合
     */
    public static String getQRCodeUrl(String param){
        Map<String, Object> resultMap = getCreateQRCodeParam(param);
        return (Objects.equals(resultMap.get("url"),null)) ? null : resultMap.get("url").toString();
    }

    /**
     * 创建二维码
     * @param param 需要存入二维码的参数
     * @param response response对象
     */
    public static void createQRCode(String param, HttpServletResponse response){
        response.setContentType("image/jpeg");
        try(ServletOutputStream outputStream = response.getOutputStream()){
            BufferedImage image = QRCodeUtil.getQRCodeBufferedImage(getQRCodeUrl(param));
            ImageIO.write(image,"jpg",outputStream);
        } catch (Exception e) {
            log.error("创建微信带参数二维码失败：{}",e);
        }
    }
}
