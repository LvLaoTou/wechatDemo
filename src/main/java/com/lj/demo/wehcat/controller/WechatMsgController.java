package com.lj.demo.wehcat.controller;

import com.google.gson.reflect.TypeToken;
import com.lj.demo.wehcat.entity.WechatEventNoticeXmlEntity;
import com.lj.demo.wehcat.entity.WechatTextMessageXmlEntity;
import com.lj.demo.wehcat.util.Constants;
import com.lj.demo.wehcat.util.OkHttpUtil;
import com.lj.demo.wehcat.util.SpringValueUtil;
import com.lj.demo.wehcat.util.WechatUtil;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 获取微信提供的信息controller
 * @author lv
 * @version 1.0.0
 */
@RestController
@RequestMapping("/wechat")
public class WechatMsgController {

    /**
     * 微信测试公众号测试账号  接口配置页面输入的token保持一致
     */
    public static final String TOKEN = "token";

    /**
     * 微信测试公众号  接口配置认证接口
     * @param signature 微信固定参数,请参考微信开发文档
     * @param timestamp 微信固定参数,请参考微信开发文档
     * @param nonce 微信固定参数,请参考微信开发文档
     * @param echostr 微信固定参数,请参考微信开发文档
     * @param response response对象
     * @throws Exception 未知异常
     */
    @GetMapping("/interfaceConfig")
    public void index(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) throws Exception{
        String[] params = new String[] { TOKEN, timestamp, nonce };
        Arrays.sort(params);
        // 将三个参数字符串拼接成一个字符串进行sha1加密
        String clearText = params[0] + params[1] + params[2];
        //加密算法  与微信一致
        String algorithm = "SHA-1";
        String sign = new String(Hex.encodeHex(MessageDigest.getInstance(algorithm).digest((clearText).getBytes()), true));
        // 获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (Objects.equals(sign,signature)) {
            response.getWriter().print(echostr);
        }
    }

    /**
     * 微信通知处理方法
     * @param entity 微信通知对象
     * @return 消息对象
     */
    @PostMapping(value="/interfaceConfig",consumes = { MediaType.APPLICATION_XML_VALUE,MediaType.TEXT_XML_VALUE}, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public WechatTextMessageXmlEntity wechatEventNotice(@RequestBody WechatEventNoticeXmlEntity entity){
        WechatTextMessageXmlEntity wechatTextMessageXmlEntity = new WechatTextMessageXmlEntity();
        wechatTextMessageXmlEntity.setContent("欢迎关注公众号");
        wechatTextMessageXmlEntity.setFromUserName(entity.getToUserName());
        wechatTextMessageXmlEntity.setCreateTime(System.currentTimeMillis()/1000+"");
        wechatTextMessageXmlEntity.setToUserName(entity.getFromUserName());
        wechatTextMessageXmlEntity.setMsgType("text");
        return wechatTextMessageXmlEntity;
    }

    /**
     * 由服务器重定向到微信授权页面
     * @param response response对象
     * @param request request对象
     * @throws Exception 未知异常
     */
    @RequestMapping("/redirect")
    public void redirect(HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String redirectUriStr = SpringValueUtil.domain+"/wechat/getWechatUserInfo";
        String redirectUriCode= URLEncoder.encode(redirectUriStr, "UTF-8");
        String getCodeUrlStr = "https://open.weixin.qq.com/connect/oauth2/authorize?redirect_uri="+redirectUriCode+"&appid="+ SpringValueUtil.wechatAppId +"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
        response.sendRedirect(getCodeUrlStr);
    }

    /**
     * 获取用户信息（此方法由微信服务器调用，即用户授权后微信会自定重定向到此方法并携带参数code）
     * @param code 微信code码
     * @return 用户信息
     */
    @RequestMapping("/getWechatUserInfo")
    public Map<String,Object> getWechatUserInfo(String code){
        String getAccessTokenUrlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ SpringValueUtil.wechatAppId+"&secret="+SpringValueUtil.wechatAppSecret+"&code="+code+"&grant_type=authorization_code";
        String responseTokenMap = OkHttpUtil.getResponseBodyFromHttpByGet(getAccessTokenUrlStr);
        Map<String,Object> responseMap = Constants.GSON.fromJson(responseTokenMap,new TypeToken<HashMap<String, Object>>() {}.getType());
        String accessToken = responseMap.get("access_token").toString();
        String refreshToken = responseMap.get("refresh_token").toString();
        String openId = responseMap.get("openid").toString();
        //剩余有效时间  单位秒
        String expiresTime = responseMap.get("expires_in").toString();
        String getWechatUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
        String responseUserInfoMap = OkHttpUtil.getResponseBodyFromHttpByGet(getWechatUserInfoUrl);
        Map<String,Object> userInfo = Constants.GSON.fromJson(responseUserInfoMap,new TypeToken<HashMap<String, Object>>() {}.getType());
        System.out.println("用户信息："+ Constants.GSON.toJson(userInfo));
        return userInfo;
    }

    /**
     * 获取js唤醒扫一扫所需参数
     * @return 参数集
     * @throws NoSuchAlgorithmException
     */
    @GetMapping("/getJsSweepQRCodeParam")
    public Map<String,String> getJsSweepQRCodeParam() throws NoSuchAlgorithmException {
        return WechatUtil.getJsSweepQRCodeParam();
    }

    /**
     * 生成微信带事件二维码
     * @param response
     */
    @GetMapping("/getQRCode")
    public void getQRCode(HttpServletResponse response){
        //需要存入二维码的信息  只能是字符串  对象需要先转json字符串
        String param = "test";
        WechatUtil.createQRCode(param,response);
    }
}
