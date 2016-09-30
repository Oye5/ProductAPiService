package com.product.dao;

import org.springframework.stereotype.Repository;

import com.product.model.Geo;

@Repository
public class GeoDaoImpl extends AbstractDao<String, Geo> implements GeoDao {

	@Override
	public void saveGeoDetails(Geo geo) {
		persist(geo);

	}

	@Override
	public void updateGeo(Geo geo) {
		update(geo);
		
	}

}
