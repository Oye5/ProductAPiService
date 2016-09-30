package com.product.dao;

import com.product.model.Seller;

public interface SellerDao {

	Seller getSellerById(String sellerId);

	void updateSeller(Seller seller);
	
	void saveSeller(Seller seller);

}
