package com.product.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.Accounts;

@Repository
public class AccountDaoImpl extends AbstractDao<Long, Accounts> implements AccountDao {

	@Override
	public Accounts getAccountByAuthToken(String authToken) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("provider_token", authToken));
		return (Accounts) criteria.uniqueResult();

	}

}
