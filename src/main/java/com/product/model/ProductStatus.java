package com.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_status")
public class ProductStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private int Id;

	@Column(name = "views")
	private int views;

	@Column(name = "favs")
	private int favourites;

	@Column(name = "offers")
	private int offers;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	public int getId() {
		return Id;
	}

	public int getViews() {
		return views;
	}

	public int getFavourites() {
		return favourites;
	}

	public int getOffers() {
		return offers;
	}

	public Product getProductId() {
		return productId;
	}

	public void setId(int id) {
		Id = id;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public void setFavourites(int favourites) {
		this.favourites = favourites;
	}

	public void setOffers(int offers) {
		this.offers = offers;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

}
