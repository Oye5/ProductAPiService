package com.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.ProductChatDao;
import com.product.model.ProductChat;

@Transactional
@Service
public class ProductChatServiceImpl implements ProductChatService {

	@Autowired
	ProductChatDao chatDao;

	@Override
	public ProductChat saveChatMessages(ProductChat productChat) {
		return chatDao.saveChatMessages(productChat);

	}

	@Override
	public void deleteChatMessages(String productID) {
		chatDao.deleteChatMessages(productID);

	}

	@Override
	public ProductChat getChatId(String productId, String sellerId, String buyerId) {
		return chatDao.getChatId(productId, sellerId, buyerId);
	}

	@Override
	public ProductChat getChatByConversationId(String conversationId) {
		return chatDao.getChatByConversationId(conversationId);
	}

	@Override
	public List<ProductChat> getChatIdByProductId(String productId) {
		return chatDao.getChatIdByProductId(productId);
	}

	@Override
	public List<ProductChat> getChatIdByBuyerId(String userId) {
		return chatDao.getChatIdByBuyerId(userId);
	}

}
