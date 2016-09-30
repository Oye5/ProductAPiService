package com.product.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_chat")
public class ProductChat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "chat_Id")
	private String chatId;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User seller;

	@ManyToOne
	@JoinColumn(name = "buyer_id")
	private User buyer;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	@Column(name = "date", length = 45)
	private String date;

	@Column(name = "unread_count")
	private int unreadCount;

	@Column(name = "forbidden")
	private boolean forbidden;

	@Column(name = "status")
	private int status;

	public String getChatId() {
		return chatId;
	}

	public User getSeller() {
		return seller;
	}

	public User getBuyer() {
		return buyer;
	}

	public Product getProductId() {
		return productId;
	}

	public String getDate() {
		return date;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public boolean isForbidden() {
		return forbidden;
	}

	public int getStatus() {
		return status;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public void setForbidden(boolean forbidden) {
		this.forbidden = forbidden;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
