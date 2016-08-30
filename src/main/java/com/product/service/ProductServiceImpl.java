package com.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.ProductDao;
import com.product.model.Product;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public String saveProduct(Product product) {
		return productDao.saveProduct(product);
	}

	@Override
	public List<Product> getProduct(double latitude, double longitude, String distance_type, int num_results, String country_code) {
		List<Product> productList = productDao.getProduct(latitude, longitude, distance_type, num_results, country_code);
		return productList;
	}

	@Override
	public void updateProduct(Product product) {
		productDao.updateProduct(product);

	}

	@Override
	public List<Product> findAllProducts() {
		return productDao.findAllProducts();
	}

}
