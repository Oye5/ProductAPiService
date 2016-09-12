package com.product.dao;

import java.util.List;

import com.product.model.ProductStatus;

public interface ProductStatusDao {

	ProductStatus getProductStatus(String productId);

	void deleteProductStatus(String productId);

	void saveProductStatus(ProductStatus productStatus);

	List<ProductStatus> getFavouriteProducts();
}
