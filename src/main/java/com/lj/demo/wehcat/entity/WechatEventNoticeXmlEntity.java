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
public class WechatEventNoticeXmlEntity implements Serializable {

    private static final long serialVersionUID = -8945426860406760064L;
    @JacksonXmlProperty(localName ="ToUserName")
    private String toUserName;

    @JacksonXmlProperty(localName ="FromUserName")
    private String FromUserName;

    @JacksonXmlProperty(localName ="CreateTime")
    private String CreateTime;

    @JacksonXmlProperty(localName ="MsgType")
    private String MsgType;

    @JacksonXmlProperty(localName ="EventKey")
    private String EventKey;

    @JacksonXmlProperty(localName ="Ticket")
    private String Ticket;

    @JacksonXmlProperty(localName ="Event")
    private String Event;

    private String URL;

}
