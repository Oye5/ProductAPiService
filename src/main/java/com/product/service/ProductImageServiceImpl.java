package com.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.ProductImageDao;
import com.product.model.ProductImages;

@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	ProductImageDao productImageDao;

	@Override
	public void updateImageDetails(ProductImages productImages) {
		productImageDao.updateImageDetails(productImages);

	}

	@Override
	public void saveUploadedImage(ProductImages productImages) {
		productImageDao.saveUploadedImage(productImages);

	}

	@Override
	public void deleteProductImages(String productId) {
		productImageDao.deleteProductImages(productId);

	}

	@Override
	public List<ProductImages> getProductImagesByProductId(String productId) {
		return productImageDao.getProductImagesByProductId(productId);
	}

}
