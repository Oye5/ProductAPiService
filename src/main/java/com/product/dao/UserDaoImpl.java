package com.product.dao;

import org.springframework.stereotype.Repository;

import com.product.model.User;

@Repository
public class UserDaoImpl extends AbstractDao<String, User> implements UserDao {

	@Override
	public User getUserById(String userId) {
		return getByKey(userId);
	}

}
