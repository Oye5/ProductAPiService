package com.product.dto.response;

public class ProductTransactionResponse {
	private String productId;
	private String sellerId;
	private String buyerId;
	private String date;

	public String getProductId() {
		return productId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public String getDate() {
		return date;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
