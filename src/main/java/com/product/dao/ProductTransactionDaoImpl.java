package com.product.dao;

import org.springframework.stereotype.Repository;

import com.product.model.ProductTransaction;

@Repository
public class ProductTransactionDaoImpl extends AbstractDao<String, ProductTransaction> implements ProductTransactionDao {

	@Override
	public String saveBuyingDetails(ProductTransaction productTransaction) {
		return save(productTransaction);
	}

	@Override
	public void deleteProductTransaction(String productId) {
		String hql = "delete from ProductTransaction where product= :productId";
		getSession().createQuery(hql).setString("productId", productId).executeUpdate();

	}

}
