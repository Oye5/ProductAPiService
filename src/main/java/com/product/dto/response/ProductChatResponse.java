package com.product.dto.response;

public class ProductChatResponse {
	private String productId;
	private String senderId;
	private String receiverId;
	private String text;
	private String conversationId;
	private String imageUrl;
	private String date;

	public String getProductId() {
		return productId;
	}

	public String getSenderId() {
		return senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public String getText() {
		return text;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
