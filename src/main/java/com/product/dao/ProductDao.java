package com.product.dao;

import java.util.List;

import com.product.model.Product;

public interface ProductDao {

	String saveProduct(Product product);

	List<Product> getProduct(double latitude, double longitude, String distance_type, int num_results, String country_code);

	void updateProduct(Product product);

	List<Product> findAllProducts();
}
