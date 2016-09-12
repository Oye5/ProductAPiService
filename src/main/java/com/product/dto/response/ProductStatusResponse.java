package com.product.dto.response;

public class ProductStatusResponse {

	private int views;

	private boolean favs;

	private int offers;

	public int getViews() {
		return views;
	}

	public boolean isFavs() {
		return favs;
	}

	public int getOffers() {
		return offers;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public void setFavs(boolean favs) {
		this.favs = favs;
	}

	public void setOffers(int offers) {
		this.offers = offers;
	}

}
