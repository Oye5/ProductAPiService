package com.product.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.product.model.ProductStatus;

public class AbstractDao<PK extends Serializable, T> {

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T getByKey(PK key) {
		return (T) getSession().get(persistentClass, key);
	}

	public void persist(T entity) {
		getSession().persist(entity);
	}

	public String save(T entity) {
		return (String) getSession().save(entity);

	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	protected Criteria createEntityCriteria() {
		return getSession().createCriteria(persistentClass);
	}

	protected void update(T entity) {
		getSession().update(entity);

	}

	protected void deleteProductStatusBasedOnProductId(String productId) {
		String hql = "delete from ProductStatus where productId= :productId";
		getSession().createQuery(hql).setString("productId", productId).executeUpdate();
	}

	protected void deleteProductImagesBasedOnProductId(String productId) {
		String hql = "delete from ProductImages where productId= :productId";
		getSession().createQuery(hql).setString("productId", productId).executeUpdate();
	}
	protected void deleteProductImagesThumnailBasedOnProductId(String productId) {
		String hql = "delete from ThumbNail where productId= :productId";
		getSession().createQuery(hql).setString("productId", productId).executeUpdate();
	}


}
