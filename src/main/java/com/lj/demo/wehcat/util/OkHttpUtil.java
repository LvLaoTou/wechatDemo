/**
 *
 */
package com.lj.demo.wehcat.util;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Okhttp工具类
 * @author lv
 */
public class OkHttpUtil {
    private static OkHttpClient singleton;
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtil.class);

    private OkHttpUtil() {
    }

    /**
     * 获取httpClient对象
     * @return httpClient对象
     */
    public synchronized static OkHttpClient getInstance() {
        if (null == singleton) {
            singleton = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS).build();
        }
        return singleton;
    }

    /**
     * 通过get方式获取responseBody值
     * @param url 请求地址
     * @return responseBody的值
     */
    public static String getResponseBodyFromHttpByGet(String url) {
        return getResponseBodyFromHttpByGet(url,null);
    }

    /**
     * 通过get方式获取body值
     * @param url 请求地址
     * @param headers responseBody的值
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpByGet(String url, Map<String,Object> headers) {
        String result = "";
        ResponseBody body;
        try {
            Response response = getInstance()
                    .newCall(OkHttpRequestUtil.getRequestByGet(url,headers)).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误:" + response);
            }
            body = response.body();
            result = body.string();
            body.close();
        } catch (IOException e) {
            LOGGER.error("http get  ResponseBody请求失败: url:{}   headers:{}", url,headers);
            LOGGER.error("http get  ResponseBody请求错误: ", e);
        }
        return result;
    }

    /**
     * 通过post请求获取 ResponseBody的值
     * @param url 请求地址
     * @param jsonParam  参数  json类型
     * @param headers  请求头
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpByPost(String url, String jsonParam,Map<String,Object> headers) {
        String result = "";
        ResponseBody body;
        try {
            Response response = getInstance()
                    .newCall(OkHttpRequestUtil.getRequestByPost(url, jsonParam, headers)).execute();

            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误:" + response);
            }
            body = response.body();
            result = body.string();
            body.close();
        } catch (IOException e) {
            LOGGER.error("http post ResponseBody请求失败: url:{}   jsonParam:{}    headers:{}", url,jsonParam,headers);
            LOGGER.error("http post ResponseBody请求错误: ", e);
        }
        return result;
    }

    /**
     * 通过post请求获取 ResponseBody的值
     * @param url 请求地址
     * @param headers  请求头
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpByPost(String url,Map<String,Object> headers){
        return getResponseBodyFromHttpByPost(url,null,headers);
    }

    /**
     * 通过post请求获取 ResponseBody的值
     * @param url 请求地址
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpByPost(String url,String jsonParam){
        return getResponseBodyFromHttpByPost(url,jsonParam,null);
    }

    /**
     * 通过post请求获取 ResponseBody的值
     * @param url 请求地址
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpByPost(String url){
        return getResponseBodyFromHttpByPost(url,null,null);
    }

    /**
     * 通过post获取返回指定请求头的值
     * @param url 请求地址
     * @param jsonParam 参数  json格式
     * @param headers  请求头
     * @param headerKey  指定头key
     * @return 指定头value
     */
    public static String getResponseHeaderFromHttpByPost(String url,String jsonParam, Map<String,Object> headers,String headerKey) {
        String result = "";
        try {
            Response response = getInstance()
                    .newCall(OkHttpRequestUtil.getRequestByPost(url, jsonParam, headers)).execute();

            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误:" + response);
            }
            result = response.header(headerKey);
        } catch (IOException e) {
            LOGGER.error("http post getHeader请求失败: url:{}   jsonParam:{}    headers:{}", url,jsonParam,headers);
            LOGGER.error("http post getHeader请求错误: ", e);
        }
        return result;
    }

    /**
     * 通过post获取返回指定请求头的值
     * @param url 请求地址
     * @param jsonParam 参数  json格式
     * @param headers  请求头
     * @return 指定头value
     */
    public static String getTokenFromHttpByPost(String url,String jsonParam, Map<String,Object> headers){
        String headerKey = "X-Auth-Token";
        return getResponseHeaderFromHttpByPost(url,jsonParam,headers,headerKey);
    }

    /**
     * 通过post获取返回指定请求头的值
     * @param url 请求地址
     * @param headers  请求头
     * @return 指定头value
     */
    public static String getTokenFromHttpByPost(String url, Map<String,Object> headers){
        String headerKey = "X-Auth-Token";
        return getResponseHeaderFromHttpByPost(url,null,headers,headerKey);
    }

    /**
     * 通过post获取返回指定请求头的值
     * @param url 请求地址
     * @param jsonParam  参数 json格式
     * @return 指定头value
     */
    public static String getTokenFromHttpByPost(String url, String jsonParam){
        String headerKey = "X-Auth-Token";
        return getResponseHeaderFromHttpByPost(url,jsonParam,null,headerKey);
    }

    /**
     * 通过post获取返回指定请求头的值
     * @param url 请求地址
     * @return 指定头value
     */
    public static String getTokenFromHttpByPost(String url){
        String headerKey = "X-Auth-Token";
        return getResponseHeaderFromHttpByPost(url,null,null,headerKey);
    }

    /**
     * 通过Https发送get请求并获ResponseBody的值
     * @param url 请求地址
     * @param headers 请求头
     * @return ResponseBody
     */
    public static String getResponseBodyFromHttpsByGet(String url,Map<String,Object> headers){
        OkHttpClient httpsClient = getHttpsClient();
        ResponseBody body;
        try {
            Response response = httpsClient
                    .newCall(OkHttpRequestUtil.getRequestByGet(url,headers)).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误:" + response);
            }
            body = response.body();
            String result = body.string();
            body.close();
            return result;
        } catch (IOException e) {
            LOGGER.error("https  get  ResponseBody请求失败: URL:{}   headers:{}",url,headers);
            LOGGER.error("https  get  ResponseBody请求错误: {}",e);
        }
        return null;
    }

    /**
     * 通过Https发送get请求并获ResponseBody的值
     * @param url 请求地址
     * @return ResponseBody
     */
    public static String getResponseBodyFromHttpsByGet(String url){
        return getResponseBodyFromHttpsByGet(url,null);
    }

    /**
     * 通过https发送post请求获取ResponseBody的值
     * @param url 请求地址
     * @param jsonParam 参数  json格式
     * @param headers 请求头
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpsByPost(String url,String jsonParam,Map<String,Object> headers){
        OkHttpClient httpsClient = getHttpsClient();
        ResponseBody body;
        try {
            Response response = httpsClient
                    .newCall(OkHttpRequestUtil.getRequestByPost(url,jsonParam,headers)).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误:" + response);
            }
            body = response.body();
            String result = body.string();
            body.close();
            return result;
        } catch (IOException e) {
            LOGGER.error("https  post  ResponseBody请求失败: URL:{}   jsonParam:{}  headers:{}",url,jsonParam,headers);
            LOGGER.error("https  post  ResponseBody请求错误: {}",e);
        }
        return null;
    }

    /**
     * 通过https发送post请求获取ResponseBody的值
     * @param url 请求地址
     * @param headers 请求头
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpsByPost(String url,Map<String,Object> headers){
        return getResponseBodyFromHttpsByPost(url,null,headers);
    }

    /**
     * 通过https发送post请求获取ResponseBody的值
     * @param url 请求地址
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpsByPost(String url){
        return getResponseBodyFromHttpsByPost(url,null,null);
    }

    /**
     * 通过https发送post请求获取ResponseBody的值
     * @param url 请求地址
     * @param jsonParam 参数  json格式
     * @return ResponseBody的值
     */
    public static String getResponseBodyFromHttpsByPost(String url,String jsonParam){
        return getResponseBodyFromHttpsByPost(url,jsonParam,null);
    }

    /**
     * 获取httpsclient
     * @return 获取httpsclient
     */
    public static OkHttpClient getHttpsClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                //配置
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                //配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
    }

}
