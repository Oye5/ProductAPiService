package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.ProductChat;

@Repository
public class ProductChatDaoImpl extends AbstractDao<String, ProductChat> implements ProductChatDao {

	@Override
	public void deleteChatMessages(String productId) {

		String hql = "delete from ProductChat where productId= :productId";
		getSession().createQuery(hql).setString("productId", productId).executeUpdate();

	}

	@Override
	public ProductChat saveChatMessages(ProductChat productChat) {

		return merge(productChat);
	}

	@Override
	public ProductChat getChatId(String productId, String sellerId, String buyerId) {

		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productId.productId", productId));
		criteria.add(Restrictions.or(Restrictions.eq("buyer.userId", buyerId), Restrictions.eq("buyer.userId", sellerId)));
		criteria.add(Restrictions.or(Restrictions.eq("seller.userId", buyerId), Restrictions.eq("seller.userId", sellerId)));
		return (ProductChat) criteria.uniqueResult();

	}

	@Override
	public ProductChat getChatByConversationId(String conversationId) {
		// TODO Auto-generated method stub
		return getByKey(conversationId);
	}

	@Override
	public List<ProductChat> getChatIdByProductId(String productId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productId.productId", productId));
		return (List<ProductChat>) criteria.list();

	}

	@Override
	public List<ProductChat> getChatIdByBuyerId(String userId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("buyer.userId", userId));
		return (List<ProductChat>) criteria.list();
	}

}
