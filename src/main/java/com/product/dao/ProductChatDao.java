package com.product.dao;

import java.util.List;

import com.product.model.ProductChat;

public interface ProductChatDao {
	ProductChat saveChatMessages(ProductChat productChat);

	void deleteChatMessages(String productId);

	ProductChat getChatId(String productId, String sellerId, String buyerId);

	ProductChat getChatByConversationId(String conversationId);

	List<ProductChat> getChatIdByProductId(String productId);

	List<ProductChat> getChatIdByBuyerId(String userId);
}
