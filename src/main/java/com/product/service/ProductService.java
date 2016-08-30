package com.product.service;

import java.util.List;

import com.product.model.Product;

public interface ProductService {

	String saveProduct(Product product);

	List<Product> getProduct(double latitude, double longitude, String distance_type, int num_results, String country_code);

	void updateProduct(Product user);

	List<Product> findAllProducts();
}