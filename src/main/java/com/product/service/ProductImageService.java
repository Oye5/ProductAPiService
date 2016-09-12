package com.product.service;

import java.util.List;

import com.product.model.ProductImages;

public interface ProductImageService {

	void updateImageDetails(ProductImages productImages);

	void saveUploadedImage(ProductImages productImages);

	void deleteProductImages(String productId);

	List<ProductImages> getProductImagesByProductId(String productId);

}
