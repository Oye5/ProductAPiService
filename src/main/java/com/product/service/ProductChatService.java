package com.product.service;

import java.util.List;

import com.product.model.ProductChat;

public interface ProductChatService {

	ProductChat saveChatMessages(ProductChat productChat);

	void deleteChatMessages(String productID);

	ProductChat getChatId(String productId, String sellerId, String buyerId);

	ProductChat getChatByConversationId(String conversationId);

	List<ProductChat> getChatIdByProductId(String productId);

	List<ProductChat> getChatIdByBuyerId(String userId);
}
