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
	public void updateProduct(Product product) {
		productDao.updateProduct(product);

	}

	@Override
	public List<Product> findAllProducts() {
		return productDao.findAllProducts();
	}

	@Override
	public void deleteProduct(Product product) {
		productDao.deleteProduct(product);

	}

	@Override
	public List<Product> getProductByProductId(String productId) {
		return productDao.getProductByProductId(productId);
	}

	@Override
	public List<Product> getProductsByCategoryId(int categoryId) {
		return productDao.getProductsByCategoryId(categoryId);
	}


	@Override
	public List<Product> getProductByUserId(String sellerId) {
		return productDao.getProductByUserId(sellerId);
	}

}
