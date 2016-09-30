package com.product.dto.response;

import java.util.List;

public class ProductResponse {

	private String product_id;
	private String description;
	private String display_name;
	private int quantity;
	private int category_id;
	private String language_code;
	private double price;
	private String currency;
	private String status;
	private String condition;
	private GeoResponse geo;
	private SellerResponse seller;
	private List<ProductImageResponse> images;
	private ThumbResponse thumb;
	private int favorites_count;
	private int views_count;
	private String created_at;
	private String updated_at;

	public String getProduct_id() {
		return product_id;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getCategory_id() {
		return category_id;
	}

	public String getLanguage_code() {
		return language_code;
	}

	public double getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public String getStatus() {
		return status;
	}

	public String getCondition() {
		return condition;
	}

	public GeoResponse getGeo() {
		return geo;
	}

	public SellerResponse getSeller() {
		return seller;
	}

	public List<ProductImageResponse> getImages() {
		return images;
	}

	public ThumbResponse getThumb() {
		return thumb;
	}

	public int getFavorites_count() {
		return favorites_count;
	}

	public int getViews_count() {
		return views_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public void setLanguage_code(String language_code) {
		this.language_code = language_code;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setGeo(GeoResponse geo) {
		this.geo = geo;
	}

	public void setSeller(SellerResponse seller) {
		this.seller = seller;
	}

	public void setImages(List<ProductImageResponse> images) {
		this.images = images;
	}

	public void setThumb(ThumbResponse thumb) {
		this.thumb = thumb;
	}

	public void setFavorites_count(int favorites_count) {
		this.favorites_count = favorites_count;
	}

	public void setViews_count(int views_count) {
		this.views_count = views_count;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

}
