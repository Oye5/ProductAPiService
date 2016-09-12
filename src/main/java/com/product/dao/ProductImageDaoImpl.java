package com.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.product.model.ProductImages;
import com.product.model.Seller;

@Repository
public class ProductImageDaoImpl extends AbstractDao<String, ProductImages> implements ProductImageDao {

	@Override
	public void updateImageDetails(ProductImages productImages) {
		update(productImages);

	}

	@Override
	public void saveUploadedImage(ProductImages productImages) {
		persist(productImages);

	}

	@Override
	public void deleteProductImages(String productId) {
		deleteProductImagesBasedOnProductId(productId);
	}

	@Override
	public List<ProductImages> getProductImagesByProductId(String productId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productId.productId", productId));
		System.out.println("==-=-=" + criteria);
		return (List<ProductImages>) criteria.list();
	}

}
