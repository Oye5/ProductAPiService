package com.product.service;

import com.product.model.ProductImages;

public interface ProductImageService {

	void updateImageDetails(ProductImages productImages);
	
	void saveUploadedImage(ProductImages productImages);
	
}
