package com.product.service;

import com.product.model.Accounts;

public interface AccountService {

	Accounts getAccountByAuthToken(String authToken);

}
