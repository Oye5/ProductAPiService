package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.Product;

@Repository
public class ProductDaoImpl extends AbstractDao<Long, Product> implements ProductDao {

	@Override
	public String saveProduct(Product product) {
		return save(product);
	}

	@Override
	public void updateProduct(Product product) {
		update(product);
	}

	@Override
	public List<Product> findAllProducts() {
		Criteria criteria = createEntityCriteria();
		return (List<Product>) criteria.list();
	}

	@Override
	public void deleteProduct(Product product) {
		delete(product);

	}

	@Override
	public List<Product> getProductByProductId(String productId) {
		// getByKey(productId);
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productId", productId));
		return (List<Product>) criteria.list();
	}

	@Override
	public List<Product> getProductsByCategoryId(int categoryId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("categoryId", categoryId));
		return (List<Product>) criteria.list();
	}

	@Override
	public List<Product> getProductByUserId(String sellerId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("user.userId", sellerId));
		return (List<Product>) criteria.list();
	}

}
