package com.product.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

	public Long save(T entity) {
		return (Long) getSession().save(entity);

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

	// protected Criteria createEntityCriteriaWithCondition(double lat , double longitude) {
	//
	// Criteria cr=getSession().createCriteria(persistentClass);
	// cr.add(Restrictions.between("lattitude", lat, lat+2.00023));
	// cr.add(Restrictions.between("longitude", longitude, longitude+2.00023));
	// return cr;
	// }
}
