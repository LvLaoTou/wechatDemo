package com.lj.demo.wehcat.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;

/**
 * 处理微信xml请求
 * @author lv
 * @version 1.0.0
 */
@JacksonXmlRootElement(localName ="xml")
@Data
public class WechatTextMessageXmlEntity implements Serializable {

    private static final long serialVersionUID = -8945426860406760064L;
    @JacksonXmlProperty(localName ="ToUserName")
    private String toUserName;

    @JacksonXmlProperty(localName ="FromUserName")
    private String fromUserName;

    @JacksonXmlProperty(localName ="CreateTime")
    private String createTime;

    @JacksonXmlProperty(localName ="MsgType")
    private String msgType;

    @JacksonXmlProperty(localName ="Content")
    private String content;


}
