package com.product.service;

import java.util.List;

import com.product.model.ProductChat;
import com.product.model.ProductConversations;

public interface ProductConversationsService {

	ProductConversations saveMessages(ProductConversations conv);

	List<ProductConversations> getConversationById(String chatId);
	
	void deleteConversations(List<ProductChat> list);

	List<ProductConversations> getConversationsByUserId(String userId);
}
