package com.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.ProductTransactionDao;
import com.product.model.ProductTransaction;

@Service
@Transactional
public class ProductTransactionServiceImpl implements ProductTransactionService {

	@Autowired
	ProductTransactionDao transactionDao;

	@Override
	public String saveBuyingDetails(ProductTransaction productTransaction) {
		return transactionDao.saveBuyingDetails(productTransaction);

	}

	@Override
	public void deleteProductTransaction(String productId) {
		transactionDao.deleteProductTransaction(productId);

	}

}
