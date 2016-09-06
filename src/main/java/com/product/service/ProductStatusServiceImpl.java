package com.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.ProductStatusDao;
import com.product.model.ProductStatus;

@Repository
@Transactional
public class ProductStatusServiceImpl implements ProductStatusService {

	@Autowired
	ProductStatusDao productStatusDao;
	
	@Override
	public ProductStatus getProductStatus(String productId) {
		
		return productStatusDao.getProductStatus(productId);
	}

	@Override
	public void deleteProductStatus(String productId) {
		productStatusDao.deleteProductStatus(productId);
		
	}

}
