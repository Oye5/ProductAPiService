package com.product.dao;

import com.product.model.Geo;

public interface GeoDao {
	void saveGeoDetails(Geo geo);

	void updateGeo(Geo geo);
}
