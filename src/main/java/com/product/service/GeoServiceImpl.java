package com.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dao.GeoDao;
import com.product.model.Geo;

@Service
@Transactional
public class GeoServiceImpl implements GeoService {

	@Autowired
	GeoDao geoDao;
	
	
	@Override
	public void saveGeoDetails(Geo geo) {
		geoDao.saveGeoDetails(geo); 
		
	}


	@Override
	public void updateGeo(Geo geo) {
		geoDao.updateGeo(geo);
		
	}

}
