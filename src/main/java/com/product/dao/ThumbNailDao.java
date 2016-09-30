package com.product.dao;

import com.product.model.ThumbNail;

public interface ThumbNailDao {

	void saveThumbNail(ThumbNail thumb);

	ThumbNail getThumbByProductId(String productId);
	void deleteThumbNail(String productId);
}
