package com.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_favourites")
public class FavouriteProducts implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

	@Column(name = "favourite")
	private boolean favourite;

	public Long getId() {
		return id;
	}

	public Product getProductId() {
		return productId;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public User getUserId() {
		return userId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

}
