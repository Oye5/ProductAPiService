package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.FavouriteProducts;

@Repository
public class FavouriteProductDaoImpl extends AbstractDao<Long, FavouriteProducts> implements FavouriteProductDao {

	@Override
	public void saveFavouriteProduct(FavouriteProducts favouriteProducts) {
		persist(favouriteProducts);

	}

	@Override
	public List<FavouriteProducts> getFavouriteProducts(FavouriteProducts favouriteProducts) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productId", favouriteProducts.getProductId()));
		criteria.add(Restrictions.eq("userId", favouriteProducts.getUserId()));
		return (List<FavouriteProducts>) criteria.list();

	}

	@Override
	public List<FavouriteProducts> getFavouriteProductsByUserId(FavouriteProducts favouriteProducts) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("favourite", true));
		criteria.add(Restrictions.eq("userId", favouriteProducts.getUserId()));
		return (List<FavouriteProducts>) criteria.list();
	}

	@Override
	public void deleteFavouriteProduct(String productId) {
		String hql = "delete from FavouriteProducts where productId= :productId";
		getSession().createQuery(hql).setString("productId", productId).executeUpdate();

	}

	@Override
	public int deleteFavouriteProductByUserId(String userId, String productId) {
		String hql = "delete from FavouriteProducts where userId= :userId and productId=:productId";
		return getSession().createQuery(hql).setString("userId", userId).setString("productId", productId).executeUpdate();
	}

}
