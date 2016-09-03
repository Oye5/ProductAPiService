package com.product.dao;

import com.product.model.ProductImages;

public interface ProductImageDao {

	void updateImageDetails(ProductImages productImages);
	
	void saveUploadedImage(ProductImages productImages);
}
