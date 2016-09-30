package com.product.dto.response;

public class MessageResponse {

	private String id;

	private String text;
	private int type;
	private String created_at;
	private String user_id;
	private int is_read;
	private int status;
	private String imageUrl;

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public int getType() {
		return type;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getUser_id() {
		return user_id;
	}

	public int getIs_read() {
		return is_read;
	}

	public int getStatus() {
		return status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
