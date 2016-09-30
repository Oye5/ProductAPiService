package com.product.service;

import com.product.model.ThumbNail;

public interface ThumbNailService {
	void saveThumbNail(ThumbNail thumb);

	ThumbNail getThumbByProductId(String productId);

	void deleteThumbNail(String productId);
}
