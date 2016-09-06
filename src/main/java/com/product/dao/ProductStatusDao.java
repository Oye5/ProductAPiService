package com.product.dao;

import com.product.model.ProductStatus;

public interface ProductStatusDao {

	ProductStatus getProductStatus(String productId);

	void deleteProductStatus(String productId);
}
