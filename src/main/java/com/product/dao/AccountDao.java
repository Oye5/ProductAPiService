package com.product.dao;

import com.product.model.Accounts;

public interface AccountDao {
	Accounts getAccountByAuthToken(String authToken);

}
