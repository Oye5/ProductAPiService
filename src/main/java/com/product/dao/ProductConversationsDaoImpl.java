package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.ProductChat;
import com.product.model.ProductConversations;

@Repository
public class ProductConversationsDaoImpl extends AbstractDao<String, ProductConversations> implements ProductConversationsDao {

	@Override
	public ProductConversations saveMessages(ProductConversations conv) {
		return (ProductConversations) merge(conv);
	}

	@Override
	public List<ProductConversations> getConversationById(String chatId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("chatId.chatId", chatId));
		return (List<ProductConversations>) criteria.list();
	}

	@Override
	public void deleteConversations(List<ProductChat> list) {
		// // Query q = entityManager.createQuery("SELECT p FROM Peron p WHERE name IN (:names)");
		// // q.setParameter("names", names);
		// Collection<ProductChat> l = new ArrayList<ProductChat>();
		// ProductChat chat=null;
		// for (int i = 0; i < list.size(); i++) {
		// chat = new ProductChat();
		// chat.setChatId(list.get(i).getChatId());
		//
		// }
		// l.add(chat);
		// System.out.println("====li=========" + l);
		// String hql = "delete from ProductConversations where chatId IN (:chatId)";
		// getSession().createQuery(hql).setParameter("chatId", l).executeUpdate();
		// System.out.println("==" + list.get(0).getChatId());

		for (int i = 0; i < list.size(); i++) {
			String hql = "delete from ProductConversations where chatId= :chatId";
			getSession().createQuery(hql).setString("chatId", list.get(i).getChatId()).executeUpdate();
			System.out.println("==" + list.get(0).getChatId());
		}

	}

	@Override
	public List<ProductConversations> getConversationsByUserId(String userId) {
		//orrrr
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.or(Restrictions.eq("receiverId.userId", userId), Restrictions.eq("senderId.userId", userId)));
		return (List<ProductConversations>) criteria.list();
	}

}
