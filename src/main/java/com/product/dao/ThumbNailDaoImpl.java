package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.Product;
import com.product.model.ThumbNail;

@Repository
public class ThumbNailDaoImpl extends AbstractDao<String, ThumbNail> implements ThumbNailDao {

	@Override
	public void saveThumbNail(ThumbNail thumb) {
		persist(thumb);

	}

	@Override
	public ThumbNail getThumbByProductId(String productId) {
		Criteria criteria = createEntityCriteria();
		System.out.println("productId=="+productId);
		criteria.add(Restrictions.eq("productId.productId", productId));
		return (ThumbNail) criteria.uniqueResult();
	}

	@Override
	public void deleteThumbNail(String productId) {
		deleteProductImagesThumnailBasedOnProductId(productId);
		
	}

}
