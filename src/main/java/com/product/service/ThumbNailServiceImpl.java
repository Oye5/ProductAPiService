package com.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.ThumbNailDao;
import com.product.model.ThumbNail;

@Service
@Transactional
public class ThumbNailServiceImpl implements ThumbNailService {

	@Autowired
	ThumbNailDao thumbNailDao;

	@Override
	public void saveThumbNail(ThumbNail thumb) {
		thumbNailDao.saveThumbNail(thumb);

	}

	@Override
	public ThumbNail getThumbByProductId(String productId) {
		return thumbNailDao.getThumbByProductId(productId);
	}

	@Override
	public void deleteThumbNail(String productId) {
		thumbNailDao.deleteThumbNail(productId);

	}

}
