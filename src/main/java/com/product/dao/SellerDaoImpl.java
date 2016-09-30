package com.product.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.Seller;

@Repository
public class SellerDaoImpl extends AbstractDao<String, Seller> implements SellerDao {

	@Override
	public Seller getSellerById(String sellerId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("userId.userId", sellerId));
		return (Seller) criteria.uniqueResult();

	}

	@Override
	public void updateSeller(Seller seller) {
		merge(seller);
	}

	@Override
	public void saveSeller(Seller seller) {
		persist(seller);

	}

}
