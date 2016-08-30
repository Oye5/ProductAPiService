package com.product.controller;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.product.dto.request.ProductRequest;
import com.product.dto.response.GenericResponse;
import com.product.model.Geo;
import com.product.model.Product;
import com.product.model.User;
import com.product.service.ProductService;
import com.product.util.ElasticUtil;

/**
 * @author Nitesh
 */

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

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
				Index index = new Index.Builder(product).index("product").type("product").id(product.getProduct_id().toString()).build();
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
				product.setUserId(user);
			}
			user.setUserId(productRequest.getSellerId());
			geo.setGeo_id(productRequest.getGeoId());
			product.setCategoryId(productRequest.getCategoryId());
			product.setCondition(productRequest.getCondition());
			product.setCurrency(productRequest.getCurrency());
			product.setDescription(productRequest.getDescription());
			product.setDisplay_name(productRequest.getDisplayName());
			product.setLanguageCode(productRequest.getLanguageCode());
			product.setPrice(productRequest.getPrice());
			product.setProduct_id(productRequest.getProductId());
			product.setStatus(productRequest.getStatus());
			product.setCreated_at(new Date());
			product.setUpdated_at(new Date());
			productService.saveProduct(product);

			// Index to elastic
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("product").id(product.getProduct_id().toString()).build();
			client.execute(index);
			response.setCode("S001");
			response.setMessage("Product saved sucessfully");

			return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);

		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			e.printStackTrace();
			response.setCode("E001");
			response.setMessage("please provde unique ProductId. given productId is already registered");

			return new ResponseEntity<GenericResponse>(response, HttpStatus.CONFLICT);

		} catch (Exception ex) {

			ex.printStackTrace();
			response.setCode("E002");
			response.setMessage(ex.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

}
