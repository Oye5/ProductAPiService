package com.product.service;

import java.util.List;

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

	@Override
	public void saveProductStatus(ProductStatus productStatus) {
		productStatusDao.saveProductStatus(productStatus);

	}

	@Override
	public List<ProductStatus> getFavouriteProducts() {
		return productStatusDao.getFavouriteProducts();
	}

	@Override
	public void updateProductStatus(ProductStatus status) {
		productStatusDao.updateProductStatus(status);
		
	}

}
