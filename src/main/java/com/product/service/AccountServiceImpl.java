package com.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.AccountDao;
import com.product.dao.ProductDao;
import com.product.model.Accounts;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Override
	public Accounts getAccountByAuthToken(String authToken) {
		return accountDao.getAccountByAuthToken(authToken);

	}

}
