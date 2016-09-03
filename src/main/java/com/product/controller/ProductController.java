package com.product.controller;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.Date;
import java.util.List;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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

import com.product.dto.request.ProductRequest;
import com.product.dto.response.GenericResponse;
import com.product.dto.response.ProductStatusResponse;
import com.product.model.Geo;
import com.product.model.Product;
import com.product.model.ProductImages;
import com.product.model.ProductStatus;
import com.product.model.User;
import com.product.service.ProductImageService;
import com.product.service.ProductService;
import com.product.service.ProductStatusService;
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
	 * @param productRequest
	 * @return
	 */
	@RequestMapping(value = "/saveProducts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveProducts(@RequestBody ProductRequest productRequest) {
		GenericResponse response = new GenericResponse();

		try {
			product = new Product();
			geo = new Geo();
			user = new User();
			if (productRequest.getGeoId() != 0) {
				product.setGeo(geo);
			}
			if (productRequest.getSellerId() != null) {
				product.setUser(user);
			}
			user.setUserId(productRequest.getSellerId());
			geo.setGeo_id(productRequest.getGeoId());
			product.setCategoryId(productRequest.getCategoryId());
			product.setCondition(productRequest.getCondition());
			product.setCurrency(productRequest.getCurrency());
			product.setDescription(productRequest.getDescription());
			product.setDisplayName(productRequest.getDisplayName());
			product.setLanguageCode(productRequest.getLanguageCode());
			product.setPrice(productRequest.getPrice());
			product.setProductId(productRequest.getProductId());
			product.setStatus(productRequest.getStatus());
			product.setCreatedAt(new Date());
			product.setUpdatedAt(new Date());
			productService.saveProduct(product);

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
			response.setMessage("please provde unique ProductId. given productId is already registered");
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
	 * @param latitude
	 * @param longitude
	 * @param distance_type
	 * @param num_results
	 * @param country_code
	 * @return
	 */
	@RequestMapping(value = "/v1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProducts(@RequestParam("lattitude") Double lattitude, @RequestParam("longitude") Double longitude, @RequestParam("distance_type") String distance_type, @RequestParam("num_results") Integer num_results, @RequestParam("country_code") String country_code) {
		GenericResponse response = new GenericResponse();

		try {

			DistanceUnit distanceUnit = DistanceUnit.MILES;

			if (distance_type != null && distance_type.equals("mile")) {
				distanceUnit = DistanceUnit.MILES;
			} else if (distance_type != null && distance_type.equals("km")) {
				distanceUnit = DistanceUnit.KILOMETERS;
			}
			List<Product> product;
			QueryBuilder query = QueryBuilders.geoDistanceQuery("location").point(lattitude, longitude).distance(10, distanceUnit);

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(query);

			JestClient client = ElasticUtil.getClient();
			Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("product").addType("Product").build();

			SearchResult result = client.execute(search);
			product = result.getSourceAsObjectList(Product.class);
			System.out.println("Elastic query " + searchSourceBuilder.toString());

			return new ResponseEntity<List<Product>>(product, HttpStatus.OK);

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
	 * upadate product details
	 * 
	 * @param productId
	 * @param productRequest
	 * @return
	 */
	@RequestMapping(value = "/v1/{productId}/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> updateProducts(@PathVariable("productId") String productId, @RequestBody ProductRequest productRequest) {
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

			product.setProductId(productId);
			product.setCategoryId(productRequest.getCategoryId());
			product.setCondition(productRequest.getCondition());
			// product.setCreatedAt(createdAt);
			product.setCurrency(productRequest.getCurrency());
			product.setDescription(productRequest.getDescription());
			product.setDisplayName(productRequest.getDisplayName());
			geo = new Geo();
			geo.setGeo_id(product.getGeo().getGeo_id());
			geo.setCity(productRequest.getGeo().getCity());
			geo.setCountryCode(productRequest.getGeo().getCountry_code());
			geo.setDistance(productRequest.getGeo().getDistance());
			geo.setLattitude(productRequest.getGeo().getLat());
			geo.setLongitude(productRequest.getGeo().getLongitude());
			geo.setZipCode(productRequest.getGeo().getZip_code());

			product.setGeo(geo);
			product.setLanguageCode(productRequest.getLanguageCode());
			product.setPrice(productRequest.getPrice());
			product.setStatus(productRequest.getStatus());
			product.setUpdatedAt(new Date());
			user = new User();
			user.setUserId(productRequest.getSeller().getId());
			product.setUser(user);

			// set image details to Imageclass and save to database
			ProductImages productImages = new ProductImages();
			productImages.setId(productRequest.getImages().getId());
			productImages.setUrl(productRequest.getImages().getUrl());
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
				statusRespose.setFavs(productStatus.getFavourites());
				statusRespose.setOffers(productStatus.getOffers());
				statusRespose.setViews(productStatus.getViews());
				return new ResponseEntity<ProductStatusResponse>(statusRespose, HttpStatus.OK);
			} else {
				response.setCode("E001");
				response.setMessage("ProductId is not found");
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

			return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);
		} catch (Exception ex) {
			response.setCode("E001");
			response.setMessage(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}
}
