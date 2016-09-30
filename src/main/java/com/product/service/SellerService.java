package com.product.service;

import com.product.model.Seller;

public interface SellerService {

	Seller getSellerById(String sellerId);

	void updateSeller(Seller seller);

	void saveSeller(Seller seller);
}
