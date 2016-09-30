package com.product.service;

import com.product.model.ProductTransaction;

public interface ProductTransactionService {

	String saveBuyingDetails(ProductTransaction productTransaction);

	void deleteProductTransaction(String productId);

}
