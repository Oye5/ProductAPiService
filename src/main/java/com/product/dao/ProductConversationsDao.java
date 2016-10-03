package com.product.dao;

import java.util.List;

import com.product.model.ProductChat;
import com.product.model.ProductConversations;

public interface ProductConversationsDao {

	ProductConversations saveMessages(ProductConversations conv);

	List<ProductConversations> getConversationById(String chatId);

	void deleteConversations(List<ProductChat> list);

	List<ProductConversations> getConversationsByUserId(String userId);
}
