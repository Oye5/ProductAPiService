package com.product.service;

import java.util.List;

import com.product.model.ProductStatus;

public interface ProductStatusService {

	ProductStatus getProductStatus(String productId);

	void deleteProductStatus(String productId);

	void saveProductStatus(ProductStatus productStatus);

	List<ProductStatus> getFavouriteProducts();
}
