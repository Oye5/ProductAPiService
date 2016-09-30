package com.product.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.product.dto.response.GeoResponse;
import com.product.dto.response.ProductImageResponse;
import com.product.dto.response.ProductResponse;
import com.product.dto.response.SellerResponse;
import com.product.dto.response.ThumbResponse;
import com.product.model.Product;
import com.product.model.ProductImages;
import com.product.model.ProductStatus;
import com.product.model.Seller;
import com.product.model.ThumbNail;
import com.product.service.ProductImageService;
import com.product.service.ProductStatusService;
import com.product.service.SellerService;
import com.product.service.ThumbNailService;

@Component
public class SetProductResponse {

	@Autowired
	ProductImageService productImageService;
	@Autowired
	ThumbNailService thumbNailService;
	@Autowired
	SellerService sellerService;
	@Autowired
	ProductStatusService productStatusService;

	public ProductResponse prepareResponse(Product product) {

		ProductResponse productResponse = new ProductResponse();
		productResponse = new ProductResponse();
		productResponse.setCategory_id(product.getCategoryId());
		productResponse.setCreated_at(product.getCreatedAt());
		productResponse.setCurrency(product.getCurrency());
		productResponse.setCondition(product.getCondition());
		productResponse.setDescription(product.getDescription());
		// geo
		GeoResponse geoResponse = new GeoResponse();
		geoResponse.setCity(product.getGeo().getCity());
		geoResponse.setCountry_code(product.getGeo().getCountryCode());
		geoResponse.setDistance(15);
		geoResponse.setLat(product.getGeo().getLattitude());
		geoResponse.setLng(product.getGeo().getLongitude());
		geoResponse.setZip_code(product.getGeo().getZipCode());
		productResponse.setGeo(geoResponse);
		productResponse.setProduct_id(product.getProductId());
		List<ProductImages> img = productImageService.getProductImagesByProductId(product.getProductId());
		// image
		List<ProductImageResponse> listImageRes = new ArrayList<ProductImageResponse>();
		ProductImageResponse imgRes = null;
		for (int j = 0; j < img.size(); j++) {
			imgRes = new ProductImageResponse();
			imgRes.setId(img.get(j).getId());
			imgRes.setUrl(img.get(j).getUrl());
			listImageRes.add(imgRes);
		}
		productResponse.setImages(listImageRes);
		ThumbNail thumbNail = thumbNailService.getThumbByProductId(product.getProductId());
		ThumbResponse thumb = null;
		if (thumbNail != null) {
			thumb = new ThumbResponse();
			thumb.setHeight(thumbNail.getHeight());
			thumb.setUrl(thumbNail.getUrl());
			thumb.setWidth(thumbNail.getWidth());
		}
		productResponse.setThumb(thumb);

		productResponse.setLanguage_code(product.getLanguageCode());
		productResponse.setDisplay_name(product.getDisplayName());
		// owner
		SellerResponse sellerResponse = new SellerResponse();
		Seller seller = sellerService.getSellerById(product.getUser().getUserId());
		if (seller != null) {
			sellerResponse.setProfile_pic_url(seller.getProfilePic());
			sellerResponse.setBanned(seller.getBanned());
			sellerResponse.setCity(seller.getCity());
			sellerResponse.setCountry_code(seller.getCountryCode());
			sellerResponse.setId(seller.getUserId().getUserId());
			sellerResponse.setName(seller.getFirstName());
			sellerResponse.setStatus(seller.getStatus());
			sellerResponse.setZip_code(seller.getZipCode());
			productResponse.setSeller(sellerResponse);
		}

		// productResponse.setOwner(sellerResponse);
		ProductStatus status = productStatusService.getProductStatus(product.getProductId());
		if (status == null) {
			productResponse.setFavorites_count(0);
			productResponse.setViews_count(0);
		} else {
			productResponse.setFavorites_count(status.getFavourites());
			productResponse.setViews_count(status.getViews());
		}

		productResponse.setPrice(product.getPrice());
		productResponse.setStatus(product.getStatus());
		productResponse.setUpdated_at(product.getUpdatedAt());
		productResponse.setQuantity(product.getQuantity());
		return productResponse;

	}

	public List<ProductResponse> prepareResponse(List<Product> product) {
		List<ProductResponse> listProductResponse = new ArrayList<ProductResponse>();
		ProductResponse productResponse = null;
		for (int i = 0; i < product.size(); i++) {
			productResponse = new ProductResponse();
			productResponse.setCategory_id(product.get(i).getCategoryId());
			productResponse.setCreated_at(product.get(i).getCreatedAt());
			productResponse.setCurrency(product.get(i).getCurrency());
			productResponse.setCondition(product.get(i).getCondition());
			productResponse.setDescription(product.get(i).getDescription());
			// geo
			GeoResponse geoResponse = new GeoResponse();
			geoResponse.setCity(product.get(i).getGeo().getCity());
			geoResponse.setCountry_code(product.get(i).getGeo().getCountryCode());
			geoResponse.setDistance(15);
			geoResponse.setLat(product.get(i).getGeo().getLattitude());
			geoResponse.setLng(product.get(i).getGeo().getLongitude());
			geoResponse.setZip_code(product.get(i).getGeo().getZipCode());
			productResponse.setGeo(geoResponse);
			productResponse.setProduct_id(product.get(i).getProductId());
			List<ProductImages> img = productImageService.getProductImagesByProductId(product.get(i).getProductId());
			// image
			List<ProductImageResponse> listImageRes = new ArrayList<ProductImageResponse>();
			ProductImageResponse imgRes = null;
			for (int j = 0; j < img.size(); j++) {
				imgRes = new ProductImageResponse();
				imgRes.setId(img.get(j).getId());
				imgRes.setUrl(img.get(j).getUrl());
				listImageRes.add(imgRes);
			}
			productResponse.setImages(listImageRes);
			ThumbNail thumbNail = thumbNailService.getThumbByProductId(product.get(i).getProductId());
			ThumbResponse thumb = null;
			if (thumbNail != null) {
				thumb = new ThumbResponse();
				thumb.setHeight(thumbNail.getHeight());
				thumb.setUrl(thumbNail.getUrl());
				thumb.setWidth(thumbNail.getWidth());
			}
			productResponse.setThumb(thumb);

			productResponse.setLanguage_code(product.get(i).getLanguageCode());
			productResponse.setDisplay_name(product.get(i).getDisplayName());
			// owner
			SellerResponse sellerResponse = new SellerResponse();
			Seller seller = sellerService.getSellerById(product.get(i).getUser().getUserId());
			if (seller != null) {
				sellerResponse.setProfile_pic_url(seller.getProfilePic());
				sellerResponse.setBanned(seller.getBanned());
				sellerResponse.setCity(seller.getCity());
				sellerResponse.setCountry_code(seller.getCountryCode());
				sellerResponse.setId(seller.getUserId().getUserId());
				sellerResponse.setName(seller.getFirstName());
				sellerResponse.setStatus(seller.getStatus());
				sellerResponse.setZip_code(seller.getZipCode());
				productResponse.setSeller(sellerResponse);
			}

			// productResponse.setOwner(sellerResponse);
			ProductStatus status = productStatusService.getProductStatus(product.get(i).getProductId());
			if (status == null) {
				productResponse.setFavorites_count(0);
				productResponse.setViews_count(0);
			} else {
				productResponse.setFavorites_count(status.getFavourites());
				productResponse.setViews_count(status.getViews());
			}

			productResponse.setPrice(product.get(i).getPrice());
			productResponse.setStatus(product.get(i).getStatus());
			productResponse.setUpdated_at(product.get(i).getUpdatedAt());
			productResponse.setQuantity(product.get(i).getQuantity());
			listProductResponse.add(productResponse);

		}
		return listProductResponse;
	}
}
