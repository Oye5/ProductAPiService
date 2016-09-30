package com.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "product_conversations")
@Entity
public class ProductConversations implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "chat_Id")
	private ProductChat chatId;

	@Column(name = "message", length = 1024)
	private String message;

	@Column(name = "is_read")
	private int isRead;

	@Column(name = "type")
	private int type;

	@Column(name = "imageUrl")
	private String imageUrl;

	@Column(name = "date", length = 50)
	private String date;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User senderId;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiverId;

	public String getId() {
		return id;
	}

	public ProductChat getChatId() {
		return chatId;
	}

	public String getMessage() {
		return message;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setChatId(ProductChat chatId) {
		this.chatId = chatId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public User getSenderId() {
		return senderId;
	}

	public User getReceiverId() {
		return receiverId;
	}

	public void setSenderId(User senderId) {
		this.senderId = senderId;
	}

	public void setReceiverId(User receiverId) {
		this.receiverId = receiverId;
	}

}
