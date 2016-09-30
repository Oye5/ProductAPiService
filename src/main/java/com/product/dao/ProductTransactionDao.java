package com.product.dao;

import com.product.model.ProductTransaction;

public interface ProductTransactionDao {

	String saveBuyingDetails(ProductTransaction productTransaction);

	void deleteProductTransaction(String productId);
}
