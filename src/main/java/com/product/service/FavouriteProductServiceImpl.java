package com.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.FavouriteProductDao;
import com.product.model.FavouriteProducts;

@Service
@Transactional
public class FavouriteProductServiceImpl implements FavouriteProductService {

	@Autowired
	FavouriteProductDao favouritesProductDao;

	@Override
	public void saveFavouriteProduct(FavouriteProducts favouriteProducts) {
		favouritesProductDao.saveFavouriteProduct(favouriteProducts);
	}

	@Override
	public List<FavouriteProducts> getFavouriteProducts(FavouriteProducts favouriteProducts) {
		return favouritesProductDao.getFavouriteProducts(favouriteProducts);
	}

	@Override
	public List<FavouriteProducts> getFavouriteProductsByUserId(FavouriteProducts favouriteProducts) {
		return favouritesProductDao.getFavouriteProductsByUserId(favouriteProducts);
	}

	@Override
	public void deleteFavouriteProduct(String productId) {
		favouritesProductDao.deleteFavouriteProduct(productId);
		
	}

	@Override
	public int deleteFavouriteProductByUserId(String userId, String productId) {
		return favouritesProductDao.deleteFavouriteProductByUserId(userId, productId);
		
	}

}
