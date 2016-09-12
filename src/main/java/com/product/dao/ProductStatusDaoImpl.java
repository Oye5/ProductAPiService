package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.ProductStatus;

@Repository
public class ProductStatusDaoImpl extends AbstractDao<Long, ProductStatus> implements ProductStatusDao {

	@Override
	public ProductStatus getProductStatus(String productId) {
		Criteria criteria = createEntityCriteria();
		// Integer v = Integer.parseInt(productId);
		criteria.add(Restrictions.eq("productId.productId", productId));
		System.out.println("criteria=====" + criteria);
		return (ProductStatus) criteria.uniqueResult();
	}

	@Override
	public void deleteProductStatus(String productId) {
		// delete(entity);
		deleteProductStatusBasedOnProductId(productId);
	}

	@Override
	public void saveProductStatus(ProductStatus productStatus) {
		persist(productStatus);

	}

	@Override
	public List<ProductStatus> getFavouriteProducts() {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("favourites", true));
		System.out.println("criteria=====" + criteria);
		return (List<ProductStatus>) criteria.list();
	}

}
