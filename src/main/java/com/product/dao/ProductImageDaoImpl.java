package com.product.dao;

import org.springframework.stereotype.Repository;

import com.product.model.ProductImages;

@Repository
public class ProductImageDaoImpl extends AbstractDao<String, ProductImages> implements ProductImageDao {

	@Override
	public void updateImageDetails(ProductImages productImages) {
		update(productImages);

	}

	@Override
	public void saveUploadedImage(ProductImages productImages) {
		persist(productImages);

	}

	@Override
	public void deleteProductImages(String productId) {
		deleteProductImagesBasedOnProductId(productId);
	}

}
