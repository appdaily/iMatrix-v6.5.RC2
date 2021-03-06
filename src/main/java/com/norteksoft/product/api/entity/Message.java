package com.norteksoft.product.api.entity;
import java.io.Serializable;

import javax.persistence.Column;

import com.norteksoft.portal.base.enumeration.MessageType;


/**
 * 消息小窗口
 */
public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sender;           //发件用户
	private String senderLoginName;  //发件用户登陆名
	private Long senderId;  //发件用户登陆名
	private String receiver;         //收件用户名
	private String receiverLoginName;//收件用户登陆名
	private Long receiverId;//收件用户登陆名
	private String category;         //信息类别
	private String systemCode;       //系统code
	private String content;          //信息
	
	private String url;              //访问路径
	private String errorInfo;        //错误 信息
	private MessageType messageType; //消息类型
	private Boolean visible=true;    //消息是否已读  true为已读，false为未读
	private Boolean automatic=true;//true为自动稳藏，，，false为手动处理
	private String uniquely;//唯一标识
	
	public String getSenderLoginName() {
		return senderLoginName;
	}
	public void setSenderLoginName(String senderLoginName) {
		this.senderLoginName = senderLoginName;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiverLoginName() {
		return receiverLoginName;
	}
	public void setReceiverLoginName(String receiverLoginName) {
		this.receiverLoginName = receiverLoginName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public Boolean getAutomatic() {
		return automatic;
	}
	public void setAutomatic(Boolean automatic) {
		this.automatic = automatic;
	}
	public String getUniquely() {
		return uniquely;
	}
	public void setUniquely(String uniquely) {
		this.uniquely = uniquely;
	}
}
