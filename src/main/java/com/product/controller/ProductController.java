package com.product.controller;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.dto.request.ProductSaveRequest;
import com.product.dto.request.ProductUpdateRequest;
import com.product.dto.response.GenericResponse;
import com.product.dto.response.GeoResponse;
import com.product.dto.response.ProductImageResponse;
import com.product.dto.response.ProductResponse;
import com.product.dto.response.ProductStatusResponse;
import com.product.dto.response.SellerResponse;
import com.product.model.Geo;
import com.product.model.Product;
import com.product.model.ProductImages;
import com.product.model.ProductStatus;
import com.product.model.Seller;
import com.product.model.User;
import com.product.service.GeoService;
import com.product.service.ProductImageService;
import com.product.service.ProductService;
import com.product.service.ProductStatusService;
import com.product.service.SellerService;
import com.product.util.ElasticUtil;

/**
 * @author Nitesh
 */

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	ProductImageService productImageService;

	@Autowired
	ProductStatusService productStatusService;

	@Autowired
	GeoService geoService;

	@Autowired
	SellerService sellerService;

	private Product product;
	private Geo geo;
	private User user;

	/**
	 * @return
	 */

	@RequestMapping(value = "/indexData", method = RequestMethod.GET)
	public ResponseEntity<Void> indexData() {

		try {
			// Fetching all data from the table
			List<Product> productsList = productService.findAllProducts();
			// Creating Elastic Client
			JestClient client = ElasticUtil.getClient();
			// Iterating over Retrived data from database
			for (Product product : productsList) {
				GeoPoint gp = new GeoPoint(product.getGeo().getLattitude(), product.getGeo().getLongitude());
				product.setLocation(gp);
				Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
				client.execute(index);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * save product
	 * 
	 * @param productRequest
	 * @return
	 */
	@RequestMapping(value = "/saveProducts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveProducts(@RequestBody ProductSaveRequest productSaveRequest) {
		GenericResponse response = new GenericResponse();

		try {
			product = new Product();
			geo = new Geo();
			user = new User();
			String id = UUID.randomUUID().toString();
			geo.setGeo_id(id);
			geo.setLattitude(productSaveRequest.getLattitude());
			geo.setLongitude(productSaveRequest.getLongitude());

			GeoPoint gp = new GeoPoint(productSaveRequest.getLattitude(), productSaveRequest.getLongitude());
			product.setLocation(gp);

			// if (productRequest.getGeoId() != 0) {
			// geo.setGeo_id(productRequest.getGeoId());
			// product.setGeo(geo);
			// }
			geoService.saveGeoDetails(geo);
			product.setGeo(geo);
			if (productSaveRequest.getSellerId() != null) {
				user.setUserId(productSaveRequest.getSellerId());
				product.setUser(user);
			}

			product.setCategoryId(productSaveRequest.getCategoryId());
			product.setCondition(productSaveRequest.getCondition());
			product.setCurrency(productSaveRequest.getCurrency());
			product.setDescription(productSaveRequest.getDescription());
			product.setDisplayName(productSaveRequest.getDisplayName());
			product.setLanguageCode(productSaveRequest.getLanguageCode());
			product.setPrice(productSaveRequest.getPrice());
			product.setProductId(UUID.randomUUID().toString());
			product.setStatus(productSaveRequest.getStatus());
			product.setCountry(productSaveRequest.getCountry());
			product.setAddress(productSaveRequest.getAddress());
			product.setBrand(productSaveRequest.getBrand());
			product.setWarranty(productSaveRequest.getWarranty());
			product.setZipCode(productSaveRequest.getZipCode());
			product.setState(productSaveRequest.getState());
			product.setImageInformation(productSaveRequest.getImageInformation());
			DateTime createdAt = new DateTime(productSaveRequest.getCreatedAt(), DateTimeZone.forID(null));
			product.setCreatedAt(createdAt.toString());
			DateTime updatedAt = new DateTime(productSaveRequest.getUpdatedAt(), DateTimeZone.forID(null));
			product.setUpdatedAt(updatedAt.toString());

			String productId = productService.saveProduct(product);
			Product p = new Product();
			p.setProductId(productId);
			// save image
			for (int i = 0; i < productSaveRequest.getImageList().size(); i++) {
				ProductImages productImages = new ProductImages();
				productImages.setId(productSaveRequest.getImageList().get(i).toString());
				productImages.setProductId(p);
				productImageService.saveUploadedImage(productImages);
			}
			// Index to elastic
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
			client.execute(index);
			response.setCode("S001");
			response.setMessage("Product saved sucessfully");

			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);

		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			e.printStackTrace();
			response.setCode("E001");
			// response.setMessage("please provde unique ProductId. given productId is already registered");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.CONFLICT);
		} catch (org.apache.http.conn.HttpHostConnectException ex) {
			ex.printStackTrace();
			response.setCode("E002");
			response.setMessage(ex.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setCode("E003");
			response.setMessage(ex.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * @param lattitude
	 * @param longitude
	 * @param distance_type
	 * @param num_results
	 * @param country_code
	 * @return
	 */
	@RequestMapping(value = "/v1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProducts(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "categoryId", required = false) String categoryId, @RequestParam("lattitude") Double lattitude, @RequestParam("longitude") Double longitude, @RequestParam("distance_type") String distance_type, @RequestParam(value = "distance", required = false) Double distance, @RequestParam("num_results") Integer num_results, @RequestParam(value = "sortby", required = false) String sortby) {
		GenericResponse response = new GenericResponse();
		if (categoryId != null) {
			if (Integer.parseInt(categoryId) < 0 || Integer.parseInt(categoryId) > 8) {
				response.setCode("C001");
				response.setMessage("category id must be in between 0 and 8");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}
		}
		try {
			List<Product> product;

			// if(categoryId<0)

			DistanceUnit distanceUnit = DistanceUnit.MILES;

			if (distance_type != null && distance_type.equalsIgnoreCase("mile")) {
				distanceUnit = DistanceUnit.MILES;
			} else if (distance_type != null && distance_type.equalsIgnoreCase("km")) {
				distanceUnit = DistanceUnit.KILOMETERS;
			}

			if (distance == null)
				distance = 10.0d;

			QueryBuilder query = null;// =QueryBuilders.boolQuery();

			if (q != null && !q.equals("")) {
				query = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("displayName", q));
			}
			if (categoryId != null && !categoryId.equals("")) {
				if (query != null)
					QueryBuilders.boolQuery().should(query).should(QueryBuilders.matchQuery("categoryId", categoryId));
				else
					query = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("categoryId", categoryId));
			}

			if (query != null)
				query = QueryBuilders.boolQuery().should(query).should(QueryBuilders.geoDistanceQuery("location").point(longitude, lattitude).distance(distance, distanceUnit));
			else
				query = QueryBuilders.geoDistanceQuery("location").point(lattitude, longitude).distance(distance, DistanceUnit.MILES);

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(query).size(num_results);

			JestClient client = ElasticUtil.getClient();
			Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("product").addType("Product").build();

			SearchResult result = client.execute(search);
			product = result.getSourceAsObjectList(Product.class);
			// preparing response
			List<ProductResponse> listProductResponse = new ArrayList<ProductResponse>();
			ProductResponse productResponse = null;
			for (int i = 0; i < product.size(); i++) {
				productResponse = new ProductResponse();
				productResponse.setProductId(product.get(i).getProductId());
				productResponse.setBrand(product.get(i).getBrand());
				productResponse.setCategoryId(product.get(i).getCategoryId());
				productResponse.setCondition(product.get(i).getCondition());
				productResponse.setCountry(product.get(i).getCountry());
				productResponse.setCreatedAt(product.get(i).getCreatedAt());
				productResponse.setCurrency(product.get(i).getCurrency());
				productResponse.setDescription(product.get(i).getDescription());
				productResponse.setDisplayName(product.get(i).getDisplayName());
				productResponse.setDistance(distance);
				productResponse.setLanguageCode(product.get(i).getLanguageCode());
				productResponse.setPrice(product.get(i).getPrice());

				productResponse.setState(product.get(i).getState());
				productResponse.setStatus(product.get(i).getStatus());
				productResponse.setUpdatedAt(product.get(i).getUpdatedAt());
				productResponse.setWarranty(product.get(i).getWarranty());
				productResponse.setZipCode(product.get(i).getZipCode());
				productResponse.setImageInformation(product.get(i).getImageInformation());
				// set geo details
				GeoResponse geoResponse = new GeoResponse();
				geoResponse.setCity(product.get(i).getGeo().getCity());
				geoResponse.setCountryCode(product.get(i).getGeo().getCountryCode());

				geoResponse.setLattitude(product.get(i).getGeo().getLattitude());
				geoResponse.setLongitude(product.get(i).getGeo().getLongitude());
				geoResponse.setZipCode(product.get(i).getGeo().getZipCode());
				productResponse.setGeo(geoResponse);
				// productResponse.setSeller(seller);
				Seller seller = sellerService.getSellerById(product.get(i).getUser().getUserId());
				if (seller != null) {
					SellerResponse sellerResponse = new SellerResponse();
					sellerResponse.setAvatar_url(seller.getProfilePic());
					sellerResponse.setBanned(seller.getBanned());
					sellerResponse.setCountry_code(seller.getCountryCode());
					sellerResponse.setName(seller.getFirstName() + " " + seller.getLastName());
					sellerResponse.setSellerId(product.get(i).getUser().getUserId());
					sellerResponse.setStatus(seller.getStatus());
					sellerResponse.setCity(seller.getCity());
					productResponse.setSeller(sellerResponse);
				}
				// image
				List<ProductImages> img = productImageService.getProductImagesByProductId(product.get(i).getProductId());
				System.out.println("---size of image===" + img.size());
				List<ProductImageResponse> listImageRes = new ArrayList<ProductImageResponse>();
				ProductImageResponse imgRes = null;
				for (int j = 0; j < img.size(); j++) {
					imgRes = new ProductImageResponse();
					imgRes.setId(img.get(j).getId());
					imgRes.setUr(img.get(j).getUrl());
					imgRes.setThumb_nail(img.get(j).getThumbNail());
					listImageRes.add(imgRes);
				}
				productResponse.setImages(listImageRes);
				listProductResponse.add(productResponse);

			}
			System.out.println("Elastic query " + searchSourceBuilder.toString());

			return new ResponseEntity<List<ProductResponse>>(listProductResponse, HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			response.setCode("E001");
			response.setMessage(ex.getMessage());

			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * delete product from product table and also from the elastic search
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/v1/{productId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteProducts(@PathVariable("productId") String productId) {
		GenericResponse response = new GenericResponse();
		try {
			Product product = new Product();
			product.setProductId(productId);

			// delete product_status table on the basis of productId
			productStatusService.deleteProductStatus(productId);

			// delete product_images table based on productID
			productImageService.deleteProductImages(productId);

			// delect product from product table
			productService.deleteProduct(product);

			// Delete from Elastic Index
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
			client.execute(index);
			response.setCode("S002");
			response.setMessage("Product deleted successfully");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setCode("E001");
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);

		}

	}

	/**
	 * update product details
	 * 
	 * @param productId
	 * @param productUpdateRequest
	 * @return
	 */
	@RequestMapping(value = "/v1/{productId}/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> updateProducts(@PathVariable("productId") String productId, @RequestBody ProductUpdateRequest productUpdateRequest) {
		GenericResponse response = new GenericResponse();
		try {
			Product product = null;
			List<Product> productList = productService.getProductByProductId(productId);
			if (productList.size() != 0) {
				product = productList.get(0);
			} else {
				response.setCode("E001");
				response.setMessage("productid not valid");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}

			// seller details
			Seller seller = new Seller();
			seller.setId(productUpdateRequest.getSeller().getId());
			seller.setBanned(productUpdateRequest.getSeller().getBanned());
			seller.setCity(productUpdateRequest.getSeller().getCity());
			seller.setCountryCode(productUpdateRequest.getSeller().getCountry_code());
			seller.setProfilePic(productUpdateRequest.getSeller().getProfile_pic_url());
			seller.setStatus(productUpdateRequest.getSeller().getStatus());
			seller.setZipCode(productUpdateRequest.getSeller().getZip_code());
			try {
				sellerService.updateSeller(seller);
			} catch (Exception e) {
				response.setCode("E001");
				response.setMessage("seller id not found: " + e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}
			product.setProductId(productId);
			product.setCategoryId(productUpdateRequest.getCategoryId());
			product.setCondition(productUpdateRequest.getCondition());
			product.setImageInformation(productUpdateRequest.getImageInformation());
			product.setCurrency(productUpdateRequest.getCurrency());
			product.setDescription(productUpdateRequest.getDescription());
			product.setDisplayName(productUpdateRequest.getDisplayName());
			geo = new Geo();
			geo.setGeo_id(product.getGeo().getGeo_id());
			geo.setCity(productUpdateRequest.getGeo().getCity());
			geo.setCountryCode(productUpdateRequest.getGeo().getCountry_code());
			geo.setLattitude(productUpdateRequest.getGeo().getLat());
			geo.setLongitude(productUpdateRequest.getGeo().getLongitude());
			geo.setZipCode(productUpdateRequest.getGeo().getZip_code());
			geoService.updateGeo(geo);

			GeoPoint gp = new GeoPoint(productUpdateRequest.getGeo().getLat(), productUpdateRequest.getGeo().getLongitude());
			product.setLocation(gp);

			product.setGeo(geo);
			product.setLanguageCode(productUpdateRequest.getLanguageCode());
			product.setPrice(productUpdateRequest.getPrice());
			product.setStatus(productUpdateRequest.getStatus());
			DateTime updatedAt = new DateTime(productUpdateRequest.getUpdatedAt(), DateTimeZone.forID(null));

			product.setUpdatedAt(updatedAt.toString());
			user = new User();
			user.setUserId(productUpdateRequest.getSeller().getId());
			product.setUser(user);

			// set image details to Imageclass and save to database
			ProductImages productImages = new ProductImages();
			productImages.setId(productUpdateRequest.getImages().getId());
			productImages.setUrl(productUpdateRequest.getImages().getUrl());
			productImages.setProductId(product);
			try {
				productImageService.updateImageDetails(productImages);
			} catch (Exception e) {
				response.setCode("E002");
				response.setMessage(e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}
			productService.updateProduct(product);
			// Index to elastic
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
			client.execute(index);

			response.setCode("S001");
			response.setMessage("product modified sucessfully");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setCode("E003");
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * get product status by productId
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/v1/{productId}/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> productFavourites(@PathVariable("productId") String productId) {
		GenericResponse response = new GenericResponse();
		ProductStatusResponse statusRespose = new ProductStatusResponse();
		try {
			ProductStatus productStatus = productStatusService.getProductStatus(productId);
			if (productStatus != null) {
				statusRespose.setFavs(productStatus.isFavourites());
				statusRespose.setOffers(productStatus.getOffers());
				statusRespose.setViews(productStatus.getViews());
				return new ResponseEntity<ProductStatusResponse>(statusRespose, HttpStatus.OK);
			} else {
				response.setCode("E001");
				response.setMessage("product status for given ProductId is not found");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.setCode("E002");
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * get similar product
	 * 
	 * @param productId
	 * @param numResults
	 * @return
	 */
	@RequestMapping(value = "/v1/{productId}/similar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSimilarProducts(@PathVariable("productId") String productId, @RequestParam("num_results") Integer numResults) {
		GenericResponse response = new GenericResponse();
		try {
			List<Product> list = productService.getProductByProductId(productId);
			Product product = null;
			if (list.size() != 0) {
				product = list.get(0);
			}

			List<Product> productList = productService.getProductsByCategoryId(product.getCategoryId());
			List<Product> responseResults = new ArrayList<Product>();
			for (int i = 0; i < numResults && i < productList.size(); i++) {
				responseResults.add(productList.get(i));
			}

			return new ResponseEntity<List<Product>>(responseResults, HttpStatus.OK);
		} catch (Exception ex) {
			response.setCode("E001");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * mark product as favourites
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/v1/makeFavourite", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> makeProductAsFavourite(@RequestParam("productId") String productId) {
		GenericResponse response = new GenericResponse();
		try {
			ProductStatus productStatus = new ProductStatus();
			Product product = new Product();
			product.setProductId(productId);
			productStatus.setProductId(product);
			productStatus.setFavourites(true);
			productStatusService.saveProductStatus(productStatus);
			response.setCode("S001");
			response.setMessage("product marked as favourites");
			return new ResponseEntity<ProductStatus>(productStatus, HttpStatus.OK);
		} catch (Exception ex) {
			response.setCode("E001");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	//
	@RequestMapping(value = "/v1/getFavouriteProducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFavouriteProducts() {
		GenericResponse response = new GenericResponse();
		try {
			List<ProductStatus> productStatusList = productStatusService.getFavouriteProducts();
			response.setCode("S001");
			response.setMessage("product marked as favourites");
			return new ResponseEntity<List<ProductStatus>>(productStatusList, HttpStatus.OK);
		} catch (Exception ex) {
			response.setCode("E001");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}
}
