package com.product.controller;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.request.ProductSaveRequest;
import com.product.dto.request.ProductUpdateRequest;
import com.product.dto.request.UpdateUserProfileRequest;
import com.product.dto.response.GenericResponse;
import com.product.dto.response.GeoResponse;
import com.product.dto.response.ProductGenericResponse;
import com.product.dto.response.ProductImageResponse;
import com.product.dto.response.ProductResponse;
import com.product.dto.response.ProductStatusResponse;
import com.product.dto.response.ProductTransactionResponse;
import com.product.dto.response.SellerResponse;
import com.product.dto.response.ThumbResponse;
import com.product.dto.response.UserResponse;
import com.product.model.FavouriteProducts;
import com.product.model.Geo;
import com.product.model.Product;
import com.product.model.ProductImages;
import com.product.model.ProductStatus;
import com.product.model.ProductTransaction;
import com.product.model.Seller;
import com.product.model.ThumbNail;
import com.product.model.User;
import com.product.service.FavouriteProductService;
import com.product.service.GeoService;
import com.product.service.ProductChatService;
import com.product.service.ProductConversationsService;
import com.product.service.ProductImageService;
import com.product.service.ProductService;
import com.product.service.ProductStatusService;
import com.product.service.ProductTransactionService;
import com.product.service.SellerService;
import com.product.service.ThumbNailService;
import com.product.service.UserService;
import com.product.util.ElasticUtil;
import com.product.util.ImageUploadUtil;
import com.product.util.SetProductResponse;
import com.product.util.UploadImage;

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

	@Autowired
	FavouriteProductService favouriteProductService;

	@Autowired
	ThumbNailService thumbNailService;

	@Autowired
	ImageUploadUtil imageUploadUtil;

	@Autowired
	UploadImage uploadImage;

	@Autowired
	UserService userService;

	@Autowired
	SetProductResponse SetproductResponse;

	@Autowired
	ProductChatService chatService;

	@Autowired
	ProductTransactionService productTransactionService;

	@Autowired
	ProductConversationsService convService;

	private Product product;
	private Geo geo;
	private User user;

	/**
	 * index data in elastic
	 * 
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
	@RequestMapping(value = "/saveProducts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveProducts(@RequestParam("image") MultipartFile[] file, ProductSaveRequest productSaveRequest) {
		GenericResponse response = new GenericResponse();
		ProductGenericResponse productGenericResponse = new ProductGenericResponse();
		// check language code
		if (productSaveRequest.getLanguageCode().isEmpty() || productSaveRequest.getLanguageCode().length() != 2 || productSaveRequest.getLanguageCode().equals(productSaveRequest.getLanguageCode().toLowerCase())) {
			response.setCode("v001");
			response.setMessage("please pass Language code  in upper case like US");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		// check category id
		if (productSaveRequest.getCountry().isEmpty() || productSaveRequest.getCountry().length() != 2 || productSaveRequest.getCountry().equals(productSaveRequest.getLanguageCode().toLowerCase())) {
			response.setCode("v002");
			response.setMessage("please pass country code in upper case like US");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		// check category id
		if (productSaveRequest.getCategoryId() < 0 || productSaveRequest.getCategoryId() > 8) {
			response.setCode("V003");
			response.setMessage("category id not valid.category id must in between 0 to 8");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		String autoGeneratedproductId = null;
		if (productSaveRequest.getProductId() == null || productSaveRequest.getProductId().equals("")) {
			autoGeneratedproductId = UUID.randomUUID().toString();
		} else {
			autoGeneratedproductId = productSaveRequest.getProductId();
		}
		try {
			product = new Product();
			geo = new Geo();
			user = new User();

			geo.setGeo_id(UUID.randomUUID().toString());
			geo.setLattitude(productSaveRequest.getLattitude());
			geo.setLongitude(productSaveRequest.getLongitude());
			geo.setCity(productSaveRequest.getCity());
			geo.setCountryCode(productSaveRequest.getCountry());
			geo.setZipCode(productSaveRequest.getZipCode());

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
			product.setProductId(autoGeneratedproductId);
			product.setStatus(productSaveRequest.getStatus());
			product.setBrand(productSaveRequest.getBrand());
			product.setWarranty(productSaveRequest.getWarranty());
			DateTime createdAt = new DateTime(new Date().getTime(), DateTimeZone.forID(null));
			product.setCreatedAt(createdAt.toString());
			DateTime updatedAt = new DateTime(new Date().getTime(), DateTimeZone.forID(null));
			product.setUpdatedAt(updatedAt.toString());
			product.setQuantity(productSaveRequest.getQuantity());
			String productId = productService.saveProduct(product);

			// save seller check seller is exist or not if not then save
			Seller seller = sellerService.getSellerById(productSaveRequest.getSellerId());
			if (seller == null) {
				seller = new Seller();
				seller.setId(productSaveRequest.getSellerId());
				seller.setActive(1);
				seller.setUserId(user);
				sellerService.saveSeller(seller);
			}

			// upload image
			List<String> keyList = null;
			if (file.length != 0) {
				keyList = imageUploadUtil.uploadImage(file, autoGeneratedproductId);
				productGenericResponse.setImagekeyList(keyList);
			} else {
				keyList = new ArrayList<String>();
			}
			// save image to db directy withoutuploading to s3
			if (productSaveRequest.getImageList().size() != 0) {
				ProductImages productImages = new ProductImages();
				for (int i = 0; i < productSaveRequest.getImageList().size(); i++) {
					productImages = new ProductImages();
					productImages.setId(UUID.randomUUID().toString());

					productImages.setProductId(product);
					productImages.setUrl(productSaveRequest.getImageList().get(i));
					// productImages.setThumbNail(key+"_thumb.jpg");
					productImageService.saveUploadedImage(productImages);
				}
				if (file.length == 0) {
					ThumbNail thumb = new ThumbNail();
					thumb.setId(UUID.randomUUID().toString());
					thumb.setHeight(140);
					thumb.setWidth(90);
					thumb.setProductId(product);
					thumb.setUrl(productSaveRequest.getThumbUrl());
					thumbNailService.saveThumbNail(thumb);
					productGenericResponse.setThumb_nail(productSaveRequest.getThumbUrl());
				}
				keyList.addAll(productSaveRequest.getImageList());

			}

			// Product p = new Product();
			// p.setProductId(productId);

			// Index to elastic
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
			client.execute(index);
			productGenericResponse.setImagekeyList(keyList);
			productGenericResponse.setCode("S001");
			productGenericResponse.setProductId(productId);

			productGenericResponse.setMessage("Product saved sucessfully");

			return new ResponseEntity<ProductGenericResponse>(productGenericResponse, HttpStatus.OK);

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
	 * get product details by differwnt parameters
	 * 
	 * @param lattitude
	 * @param longitude
	 * @param distance_type
	 * @param num_results
	 * @param country_code
	 * @return
	 */
	@RequestMapping(value = "/v1/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProducts(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "categoryId", required = false) String categoryId, @RequestParam("lattitude") Double lattitude, @RequestParam("longitude") Double longitude, @RequestParam(value = "distance", required = false) Double distance, @RequestParam("num_results") Integer num_results, @RequestParam(value = "sortby", required = false) Integer sortby, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "min_price", required = false) Double min_price, @RequestParam(value = "max_price", required = false) Double max_price) {
		GenericResponse response = new GenericResponse();
		if (categoryId != null) {
			if (Integer.parseInt(categoryId) < 0 || Integer.parseInt(categoryId) > 8) {
				response.setCode("C001");
				response.setMessage("category id must be in between 0 and 8");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
		}
		try {
			List<Product> product;

			if (distance == null)
				distance = 15.0d;

			QueryBuilder query = null; // QueryBuilders.boolQuery();

			if (q != null && !q.equals("")) {
				query = QueryBuilders.matchQuery("description", q);

			}
			if (categoryId != null && !categoryId.equals("")) {
				if (query != null)
					query = QueryBuilders.boolQuery().must(query).must(QueryBuilders.matchQuery("categoryId", categoryId));
				else
					query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("categoryId", categoryId));
			}

			if (query != null)
				query = QueryBuilders.boolQuery().must(query).must(QueryBuilders.geoDistanceQuery("location").point(lattitude, longitude).distance(distance, DistanceUnit.KILOMETERS));
			else
				query = QueryBuilders.geoDistanceQuery("location").point(lattitude, longitude).distance(distance, DistanceUnit.KILOMETERS);

			if (max_price != null && min_price != null) {
				if (query == null)
					query = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("price").lt(max_price).gt(min_price));
				else
					query = QueryBuilders.boolQuery().must(query).must(QueryBuilders.rangeQuery("price").lt(max_price).gt(min_price));
			}

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(query);
			// Pagination
			if (start == null)
				start = 0;
			searchSourceBuilder.from(start);
			if (num_results == null)
				num_results = 20;
			searchSourceBuilder.size(num_results);
			if (sortby != null && sortby > 0 && sortby <= 4) {
				if (sortby == 1)
					searchSourceBuilder.sort("price", SortOrder.ASC);
				else if (sortby == 2)
					searchSourceBuilder.sort("price", SortOrder.DESC);
				else if (sortby == 3)
					searchSourceBuilder.sort("createdAt", SortOrder.ASC);
				else if (sortby == 4)
					searchSourceBuilder.sort("createdAt", SortOrder.DESC);
			}

			JestClient client = ElasticUtil.getClient();
			Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("product").addType("Product").build();

			SearchResult result = client.execute(search);
			product = result.getSourceAsObjectList(Product.class);
			// preparing response
			List<ProductResponse> listProductResponse = SetproductResponse.prepareResponse(product);
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

			// delete thumbnail
			thumbNailService.deleteThumbNail(productId);

			// delete favourite product
			favouriteProductService.deleteFavouriteProduct(productId);

			// delete chat messages
			chatService.deleteChatMessages(productId);
			// delete from conversations
			convService.deleteConversations(productId);

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

		// check language code
		if (productUpdateRequest.getLanguage_code().isEmpty() || productUpdateRequest.getLanguage_code().length() != 2 || productUpdateRequest.getLanguage_code().equals(productUpdateRequest.getLanguage_code().toLowerCase())) {
			response.setCode("v001");
			response.setMessage("please pass Language code  in upper case like US");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		// check country code
		if (productUpdateRequest.getGeo().getCountry_code().isEmpty() || productUpdateRequest.getGeo().getCountry_code().length() != 2 || productUpdateRequest.getGeo().getCountry_code().equals(productUpdateRequest.getGeo().getCountry_code().toLowerCase())) {
			response.setCode("v002");
			response.setMessage("please pass country code in  upper case like US");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
		// category id validation
		if (productUpdateRequest.getCategory_id() < 0 || productUpdateRequest.getCategory_id() > 8) {
			response.setCode("V003");
			response.setMessage("category id not valid. category id must in between 0 to 8");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		}
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
			User user = new User();
			user.setUserId(productUpdateRequest.getSeller().getId());
			seller.setUserId(user);
			seller.setBanned(productUpdateRequest.getSeller().getBanned());
			seller.setCity(productUpdateRequest.getSeller().getCity());
			seller.setCountryCode(productUpdateRequest.getSeller().getCountry_code());
			seller.setProfilePic(productUpdateRequest.getSeller().getProfile_pic_url());
			seller.setStatus(productUpdateRequest.getSeller().getStatus());
			seller.setZipCode(productUpdateRequest.getSeller().getZip_code());
			seller.setFirstName(productUpdateRequest.getSeller().getName());
			try {
				sellerService.updateSeller(seller);
			} catch (Exception e) {
				response.setCode("E001");
				response.setMessage("seller id not found: " + e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}
			product.setProductId(productId);
			product.setCategoryId(productUpdateRequest.getCategory_id());
			product.setCondition(productUpdateRequest.getCondition());
			product.setCurrency(productUpdateRequest.getCurrency());
			product.setDescription(productUpdateRequest.getDescription());
			product.setDisplayName(productUpdateRequest.getDisplay_name());
			product.setQuantity(productUpdateRequest.getQuantity());
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
			product.setLanguageCode(productUpdateRequest.getLanguage_code());
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
				response.setMessage("image id not found: " + e.getMessage());
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
				statusRespose.setFavs(productStatus.getFavourites());
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
			if (list.size() > 0) {
				product = list.get(0);
			} else {
				response.setCode("V001");
				response.setMessage("product id not valid");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.NOT_FOUND);
			}

			List<Product> productList = productService.getProductsByCategoryId(product.getCategoryId());
			// List<Product> responseResults = new ArrayList<Product>();
			// for (int i = 0; i < numResults && i < productList.size(); i++) {
			// responseResults.add(productList.get(i));
			// ww
			// }

			List<ProductResponse> listProductResponse = new ArrayList<ProductResponse>();
			ProductResponse productResponse = null;
			for (int i = 0; i < numResults && i < productList.size(); i++) {
				productResponse = new ProductResponse();
				productResponse.setCategory_id(productList.get(i).getCategoryId());
				productResponse.setCreated_at(productList.get(i).getCreatedAt());
				productResponse.setCurrency(productList.get(i).getCurrency());
				productResponse.setDescription(productList.get(i).getDescription());
				productResponse.setCondition(productList.get(i).getCondition());
				productResponse.setQuantity(productList.get(i).getQuantity());
				// geo
				GeoResponse geoResponse = new GeoResponse();
				geoResponse.setCity(productList.get(i).getGeo().getCity());
				geoResponse.setCountry_code(productList.get(i).getGeo().getCountryCode());
				geoResponse.setDistance(10);
				geoResponse.setLat(productList.get(i).getGeo().getLattitude());
				geoResponse.setLng(productList.get(i).getGeo().getLongitude());
				geoResponse.setZip_code(productList.get(i).getGeo().getZipCode());
				productResponse.setGeo(geoResponse);
				productResponse.setProduct_id(productList.get(i).getProductId());
				List<ProductImages> img = productImageService.getProductImagesByProductId(productList.get(i).getProductId());
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
				ThumbNail thumbNail = thumbNailService.getThumbByProductId(productList.get(i).getProductId());
				ThumbResponse thumb = null;
				if (thumbNail != null) {
					thumb = new ThumbResponse();
					thumb.setHeight(thumbNail.getHeight());
					thumb.setUrl(thumbNail.getUrl());
					thumb.setWidth(thumbNail.getWidth());
				}
				productResponse.setThumb(thumb);

				productResponse.setLanguage_code(productList.get(i).getLanguageCode());
				productResponse.setDisplay_name(productList.get(i).getDisplayName());
				// owner
				SellerResponse sellerResponse = new SellerResponse();
				Seller seller = sellerService.getSellerById(productList.get(i).getUser().getUserId());
				if (seller != null) {
					sellerResponse.setProfile_pic_url(seller.getProfilePic());
					sellerResponse.setBanned(seller.getBanned());
					sellerResponse.setCity(seller.getCity());
					sellerResponse.setCountry_code(seller.getCountryCode());
					sellerResponse.setId(seller.getId());
					sellerResponse.setName(seller.getFirstName());
					sellerResponse.setStatus(seller.getStatus());
					sellerResponse.setZip_code(seller.getZipCode());
					productResponse.setSeller(sellerResponse);
				}

				// productResponse.setOwner(sellerResponse);
				productResponse.setPrice(productList.get(i).getPrice());
				productResponse.setStatus(productList.get(i).getStatus());
				productResponse.setUpdated_at(productList.get(i).getUpdatedAt());

				listProductResponse.add(productResponse);

			}

			return new ResponseEntity<List<ProductResponse>>(listProductResponse, HttpStatus.OK);
		} catch (Exception ex) {
			response.setCode("E001");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * make product favourite for given user
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/v1/{userid}/favorites/products/{productid}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> makeProductAsFavourite(@PathVariable("userid") String userId, @PathVariable("productid") String productId) {
		GenericResponse response = new GenericResponse();
		try {
			FavouriteProducts favouriteProducts = new FavouriteProducts();
			Product product = new Product();
			product.setProductId(productId);
			favouriteProducts.setProductId(product);
			User user = new User();
			user.setUserId(userId);
			favouriteProducts.setUserId(user);
			favouriteProducts.setFavourite(true);
			List<FavouriteProducts> listFavouriteProducts = favouriteProductService.getFavouriteProducts(favouriteProducts);
			if (listFavouriteProducts.size() == 0) {
				favouriteProductService.saveFavouriteProduct(favouriteProducts);
				ProductStatus status = productStatusService.getProductStatus(productId);
				if (status == null) {
					status = new ProductStatus();
					status.setFavourites(1);
					status.setId(UUID.randomUUID().toString());
					status.setProductId(product);
					status.setViews(1);
					productStatusService.saveProductStatus(status);
				} else {
					status.setFavourites(status.getFavourites() + 1);
					status.setViews(status.getViews() + 1);
					productStatusService.updateProductStatus(status);
				}
			} else {
				response.setCode("V001");
				response.setMessage("This productId is already marked as favourites by the given userId");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.ALREADY_REPORTED);
			}
			response.setCode("S001");
			response.setMessage("product marked as favourites");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			response.setCode("E001");
			response.setMessage("please check userid and productId: " + ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception ex) {
			response.setCode("E002");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * get favourite item list for particular user
	 * 
	 * @param userId
	 * @return
	 */

	// http://52.43.30.248:8080/productapi/v1//{userid}/favourites/products/{productid}
	@RequestMapping(value = "/v1/{userid}/favorites/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFavouriteProducts(@PathVariable("userid") String userId) {
		GenericResponse response = new GenericResponse();
		try {
			FavouriteProducts favouriteProducts = new FavouriteProducts();
			User user = new User();
			user.setUserId(userId);
			favouriteProducts.setUserId(user);
			List<FavouriteProducts> listFavouriteProducts = favouriteProductService.getFavouriteProductsByUserId(favouriteProducts);

			List<ProductResponse> listProductResponse = new ArrayList<ProductResponse>();
			ProductResponse productResponse = null;
			for (int i = 0; i < listFavouriteProducts.size(); i++) {
				productResponse = new ProductResponse();
				productResponse.setCategory_id(listFavouriteProducts.get(i).getProductId().getCategoryId());
				productResponse.setCreated_at(listFavouriteProducts.get(i).getProductId().getCreatedAt());
				productResponse.setCurrency(listFavouriteProducts.get(i).getProductId().getCurrency());
				productResponse.setDescription(listFavouriteProducts.get(i).getProductId().getDescription());
				productResponse.setCondition(listFavouriteProducts.get(i).getProductId().getCondition());
				productResponse.setQuantity(listFavouriteProducts.get(i).getProductId().getQuantity());
				// geo
				GeoResponse geoResponse = new GeoResponse();
				geoResponse.setCity(listFavouriteProducts.get(i).getProductId().getGeo().getCity());
				geoResponse.setCountry_code(listFavouriteProducts.get(i).getProductId().getGeo().getCountryCode());
				geoResponse.setDistance(10);
				geoResponse.setLat(listFavouriteProducts.get(i).getProductId().getGeo().getLattitude());
				geoResponse.setLng(listFavouriteProducts.get(i).getProductId().getGeo().getLongitude());
				geoResponse.setZip_code(listFavouriteProducts.get(i).getProductId().getGeo().getZipCode());
				productResponse.setGeo(geoResponse);
				productResponse.setProduct_id(listFavouriteProducts.get(i).getProductId().getProductId());
				List<ProductImages> img = productImageService.getProductImagesByProductId(listFavouriteProducts.get(i).getProductId().getProductId());
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
				ThumbNail thumbNail = thumbNailService.getThumbByProductId(listFavouriteProducts.get(i).getProductId().getProductId());
				ThumbResponse thumb = null;
				if (thumbNail != null) {
					thumb = new ThumbResponse();
					thumb.setHeight(thumbNail.getHeight());
					thumb.setUrl(thumbNail.getUrl());
					thumb.setWidth(thumbNail.getWidth());
				}
				productResponse.setThumb(thumb);

				productResponse.setLanguage_code(listFavouriteProducts.get(i).getProductId().getLanguageCode());
				productResponse.setDisplay_name(listFavouriteProducts.get(i).getProductId().getDisplayName());
				// owner
				SellerResponse sellerResponse = new SellerResponse();
				Seller seller = sellerService.getSellerById(listFavouriteProducts.get(i).getProductId().getUser().getUserId());
				if (seller != null) {
					sellerResponse.setProfile_pic_url(seller.getProfilePic());
					sellerResponse.setBanned(seller.getBanned());
					sellerResponse.setCity(seller.getCity());
					sellerResponse.setCountry_code(seller.getCountryCode());
					sellerResponse.setId(seller.getId());
					sellerResponse.setName(seller.getFirstName());
					sellerResponse.setStatus(seller.getStatus());
					sellerResponse.setZip_code(seller.getZipCode());
					productResponse.setSeller(sellerResponse);
				}

				// productResponse.setOwner(sellerResponse);
				productResponse.setPrice(listFavouriteProducts.get(i).getProductId().getPrice());
				productResponse.setStatus(listFavouriteProducts.get(i).getProductId().getStatus());
				productResponse.setUpdated_at(listFavouriteProducts.get(i).getProductId().getUpdatedAt());

				listProductResponse.add(productResponse);

			}

			return new ResponseEntity<List<ProductResponse>>(listProductResponse, HttpStatus.OK);
		} catch (Exception ex) {
			response.setCode("E001");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * unmark favorite product
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/v1/{userid}/unfavorites/products/{productid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> makeProductAsUnFavourite(@PathVariable("userid") String userId, @PathVariable("productid") String productId) {
		GenericResponse response = new GenericResponse();
		try {
			int res = favouriteProductService.deleteFavouriteProductByUserId(userId, productId);
			if (res != 0) {
				ProductStatus status = productStatusService.getProductStatus(productId);
				if (status != null) {
					status.setFavourites(status.getFavourites() - 1);
					productStatusService.updateProductStatus(status);
				}

			}
			response.setCode("S001");
			response.setMessage("product unmarked  as favourites");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			response.setCode("E001");
			response.setMessage("please check userid and productId: " + ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			response.setCode("E002");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * update user profile
	 * 
	 * @param userId
	 * @param file
	 * @param userProfileRequest
	 * @return
	 */
	@Value("${aws.s3.folder.user}")
	public String folder;// = "product";

	@RequestMapping(value = "/v1/updateUser/{userid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserProfile(@PathVariable("userid") String userId, @RequestParam(value = "image", required = false) MultipartFile file, UpdateUserProfileRequest userProfileRequest) {
		GenericResponse response = new GenericResponse();
		try {
			User user = userService.getUserById(userId);
			if (user == null) {
				response.setCode("V001");
				response.setMessage("please check User id");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}
			Seller seller = new Seller();
			seller.setCity(userProfileRequest.getCity());
			seller.setCountryCode(userProfileRequest.getCountryCode());
			seller.setFirstName(userProfileRequest.getFirstName());
			seller.setId(userId);
			seller.setUserId(user);
			seller.setLastName(userProfileRequest.getLastName());
			if (file != null) {
				String ProfilePicUrl = uploadImage.uploadImage(file, folder);
				seller.setProfilePic(ProfilePicUrl);
			}

			seller.setZipCode(userProfileRequest.getZipCode());
			sellerService.updateSeller(seller);

			response.setCode("S001");
			response.setMessage("User updated successfully");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);

		} catch (org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException ex) {

			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("E002");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * get product details by product id from elastic
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/v1/get/product/{productid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProductByProductId(@PathVariable("productid") String productId) {
		GenericResponse response = new GenericResponse();
		try {
			// search product by productId from elasticsearch
			QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("productId", productId));
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(query);

			JestClient client = ElasticUtil.getClient();
			Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("product").addType("Product").build();

			SearchResult result = client.execute(search);
			Product product = result.getSourceAsObject(Product.class);
			if (product != null) {
				ProductResponse productResponse = SetproductResponse.prepareResponse(product);
				return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
			} else {
				response.setCode("V001");
				response.setMessage("No product found for given product Id");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("E001");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * get user profile by user id
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/v1/get/user/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserByUserId(@PathVariable("userid") String userId) {
		GenericResponse response = new GenericResponse();
		try {
			User user = userService.getUserById(userId);
			if (user == null) {
				response.setCode("V001");
				response.setMessage("please check userID");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			Seller seller = sellerService.getSellerById(userId);
			UserResponse userResponse = new UserResponse();
			if (seller != null) {
				userResponse.setBanned(seller.getBanned());
				userResponse.setCity(seller.getCity());
				userResponse.setCountry_code(seller.getCountryCode());
				userResponse.setFirst_name(seller.getFirstName());
				userResponse.setLast_name(seller.getLastName());
				userResponse.setProfile_pic_url(seller.getProfilePic());
				userResponse.setStatus(seller.getStatus());
				userResponse.setZip_code(seller.getZipCode());
			}

			userResponse.setEmail(user.getEmail());
			userResponse.setUser_id(user.getUserId());
			userResponse.setUser_name(user.getUserName());

			return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
		} catch (Exception e) {
			response.setCode("E001");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * get user products by userId
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/v1/get/user/sellingProduct/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserByUserIda(@PathVariable("userid") String userId) {
		GenericResponse response = new GenericResponse();
		try {
			List<Product> productList = productService.getProductByUserId(userId);
			List<ProductResponse> productResponse = SetproductResponse.prepareResponse(productList);
			return new ResponseEntity<List<ProductResponse>>(productResponse, HttpStatus.OK);
		} catch (Exception e) {
			response.setCode("E001");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * buy product
	 * 
	 * @param productId
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value = "/v1/product/{productId}/buyer/{buyerid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buyProduct(@PathVariable("productId") String productId, @PathVariable("buyerid") String buyerId) {
		GenericResponse response = new GenericResponse();
		try {
			List<Product> productList = productService.getProductByProductId(productId);
			Product product = null;
			if (productList.size() != 0) {
				product = productList.get(0);
			} else {
				response.setCode("V001");
				response.setMessage("please check productiD ");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			if (product.getQuantity() <= 0) {
				response.setCode("V002");
				response.setMessage("product is out of stock");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			if (product.getUser().getUserId().equals(buyerId)) {
				response.setCode("V003");
				response.setMessage("seller can't be buyer");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			ProductTransaction productTransaction = new ProductTransaction();
			productTransaction.setCreatedDate(new DateTime(new Date().getTime(), DateTimeZone.forID(null)).toString());
			productTransaction.setId(UUID.randomUUID().toString());
			productTransaction.setProduct(product);
			User user = new User();
			user.setUserId(buyerId);
			productTransaction.setUserId(user);
			String result = productTransactionService.saveBuyingDetails(productTransaction);
			if (result != null) {
				product.setQuantity(product.getQuantity() - 1);
				productService.updateProduct(product);
			}
			ProductTransactionResponse transactionResponse = new ProductTransactionResponse();
			transactionResponse.setBuyerId(buyerId);
			transactionResponse.setDate(productTransaction.getCreatedDate());
			transactionResponse.setProductId(productId);
			transactionResponse.setSellerId(product.getUser().getUserId());
			// index to elastic
			GeoPoint gp = new GeoPoint(product.getGeo().getLattitude(), product.getGeo().getLongitude());
			product.setLocation(gp);
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
			client.execute(index);

			return new ResponseEntity<ProductTransactionResponse>(transactionResponse, HttpStatus.OK);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			ex.printStackTrace();
			response.setCode("E001");
			response.setMessage("please check productiD and userId");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("E002");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}
}