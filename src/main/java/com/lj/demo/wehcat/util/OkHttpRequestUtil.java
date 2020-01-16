/**
 * 
 */
package com.lj.demo.wehcat.util;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;
import java.util.Objects;

/**
 * Okhttp方法封装
 * @author lv
 */
public class OkHttpRequestUtil {

	private OkHttpRequestUtil() {
	}

	/**
	 * 根据请求地址、请求参数、请求头构建post类型的Request对象
	 * @param url 请求地址
	 * @param jsonParam 请求参数
	 * @param header 请求头
	 * @return Request对象
	 */
	public static Request getRequestByPost(String url, String jsonParam, Map<String,Object> header) {
		Request.Builder requestBuilder = getRequestBuilder(url,header);
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), (null == jsonParam) ? "" : jsonParam);
		Request request = requestBuilder.post(requestBody).build();
		return request;
	}

	/**
	 * 根据请求地址、请求参数、请求头构建post类型的Request对象
	 * @param url 请求地址
	 * @param header 请求头
	 * @return Request对象
	 */
	public static Request getRequestByPost(String url, Map<String,Object> header) {
		return getRequestByPost(url,null,header);
	}

	/**
	 * 根据请求地址、请求参数、请求头构建post类型的Request对象
	 * @param url 请求地址
	 * @param jsonParam body参数
	 * @return Request对象
	 */
	public static Request getRequestByPost(String url, String jsonParam) {
		return getRequestByPost(url,jsonParam,null);
	}

	/**
	 * 根据请求地址、请求参数、请求头构建post类型的Request对象
	 * @param url 请求地址
	 * @return Request对象
	 */
	public static Request getRequestByPost(String url) {
		return getRequestByPost(url,null,null);
	}

	/**
	 * 根据请求地址和请求头获取get类型的Request对象
	 * @param url 请求地址
	 * @return Request对象
	 */
	public static Request getRequestByGet(String url, Map<String,Object> header){
		return getRequestBuilder(url,header).get().build();
	}

	/**
	 * 根据请求地址获取get类型的Request对象
	 * @param url 请求地址
	 * @return Request对象
	 */
	public static Request getRequestByGet(String url){
		return getRequestBuilder(url).get().build();
	}

	/**
	 * 根据请求地址获取RequestBuilder对象对象
	 * @param url 请求地址
	 * @return RequestBuilder对象
	 */
	public static Request.Builder getRequestBuilder(String url) {
		return getRequestBuilder(url,null);
	}

	/**
	 * 根据请求地址和请求头参数构建RequestBuilder对象对象
	 * @param url 请求地址
	 * @param header 请求头
	 * @return RequestBuilder对象
	 */
	public static Request.Builder getRequestBuilder(String url, Map<String,Object> header){
		Request.Builder builder = new Request.Builder();
		if (!Objects.equals(header,null)){
			header.forEach((k,v)->builder.addHeader(k,v.toString()));
		}
		return builder.url(url);
	}
}
