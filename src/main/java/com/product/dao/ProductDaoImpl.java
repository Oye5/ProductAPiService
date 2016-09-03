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
		persist(product);
		return "success";
	}

	@Override
	public List<Product> getProduct(double latitude, double longitude, String distance_type, int num_results, String country_code) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("geo.lat", latitude));
		// return (User)criteria.uniqueResult();
		return null;
	}

	@Override
	public void updateProduct(Product product) {
		update(product);
		System.out.println("===update==");
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

}
