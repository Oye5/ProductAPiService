package com.product.dto.response;

import java.util.List;

public class ProductConversationResponse {

	private String id;
	private ProductResponse product;
	private int unread_count;
	private String updated_at;
	private List<MessageResponse> messages;
	private SellerResponse user_from;
	private SellerResponse user_to;
	private boolean forbidden;
	private int status;

	public String getId() {
		return id;
	}

	public ProductResponse getProduct() {
		return product;
	}

	public int getUnread_count() {
		return unread_count;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public List<MessageResponse> getMessages() {
		return messages;
	}

	public SellerResponse getUser_from() {
		return user_from;
	}

	public SellerResponse getUser_to() {
		return user_to;
	}

	public boolean isForbidden() {
		return forbidden;
	}

	public int getStatus() {
		return status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProduct(ProductResponse product) {
		this.product = product;
	}

	public void setUnread_count(int unread_count) {
		this.unread_count = unread_count;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public void setMessages(List<MessageResponse> messages) {
		this.messages = messages;
	}

	public void setUser_from(SellerResponse user_from) {
		this.user_from = user_from;
	}

	public void setUser_to(SellerResponse user_to) {
		this.user_to = user_to;
	}

	public void setForbidden(boolean forbidden) {
		this.forbidden = forbidden;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
