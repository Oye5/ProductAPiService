package com.product.controller;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.common.geo.GeoPoint;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.response.GenericResponse;
import com.product.dto.response.MessageResponse;
import com.product.dto.response.ProductChatResponse;
import com.product.dto.response.ProductConversationResponse;
import com.product.dto.response.ProductResponse;
import com.product.dto.response.SellerResponse;
import com.product.model.Product;
import com.product.model.ProductChat;
import com.product.model.ProductConversations;
import com.product.model.Seller;
import com.product.model.User;
import com.product.service.ProductChatService;
import com.product.service.ProductConversationsService;
import com.product.service.ProductService;
import com.product.service.SellerService;
import com.product.util.ElasticUtil;
import com.product.util.SetProductResponse;
import com.product.util.UploadImage;

@RestController
public class ChatApiController {

	@Autowired
	ProductChatService chatService;

	@Autowired
	ProductConversationsService conversationService;

	@Autowired
	ProductService productService;

	@Autowired
	SetProductResponse SetproductResponse;

	@Autowired
	SellerService sellerService;

	@Autowired
	UploadImage uploadImage;

	@Value("${aws.s3.folder.conversation}")
	public String folder;// = "product";

	/**
	 * send message
	 * 
	 * @param productId
	 * @param type
	 * @param content
	 * @param userTo
	 */

	@RequestMapping(value = "/v1/products/{productid}/messages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserByUserIda(@RequestParam(value = "image", required = false) MultipartFile file, @PathVariable("productid") String productId, @RequestParam("type") Integer type, @RequestParam(value = "content", required = false) String content, @RequestParam("userFrom") String userFrom, @RequestParam("userTo") String userTo) {
		GenericResponse response = new GenericResponse();
		try {
			List<Product> listProduct = productService.getProductByProductId(productId);
			if (listProduct.size() == 0) {
				response.setCode("V001");
				response.setMessage("please check product Id");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}

			Product product = listProduct.get(0);
			if (userFrom.equalsIgnoreCase(userTo)) {
				response.setCode("V002");
				response.setMessage("UserTo and userFom id can't be same");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			if (!product.getUser().getUserId().equalsIgnoreCase(userTo) && !product.getUser().getUserId().equalsIgnoreCase(userFrom)) {
				response.setCode("V003");
				response.setMessage("UserTo and userFom id one of them must contain seller id");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			ProductChat productChat = chatService.getChatId(productId, userFrom, userTo);

			if (productChat == null) {
				productChat = new ProductChat();
				productChat.setChatId(UUID.randomUUID().toString());
				// productChat.setMessage(content);
				productChat.setProductId(product);

				productChat.setSeller(product.getUser());
				User buyer = new User();
				if (product.getUser().getUserId().equalsIgnoreCase(userTo)) {
					buyer.setUserId(userFrom);
				} else {
					buyer.setUserId(userTo);
				}

				productChat.setBuyer(buyer);
				productChat.setDate(new DateTime(new Date().getTime(), DateTimeZone.forID(null)).toString());
				chatService.saveChatMessages(productChat);
			}
			ProductConversations conv = new ProductConversations();
			String imageUrl = null;
			if (file != null) {
				imageUrl = uploadImage.uploadImage(file, folder);
				conv.setImageUrl(imageUrl);
			}
			conv.setId(UUID.randomUUID().toString());
			conv.setChatId(productChat);
			conv.setMessage(content);
			User receiver = new User();
			receiver.setUserId(userTo);
			conv.setReceiverId(receiver);
			User sender = new User();
			sender.setUserId(userFrom);
			conv.setSenderId(sender);
			conv.setDate(new DateTime(new Date().getTime(), DateTimeZone.forID(null)).toString());
			conversationService.saveMessages(conv);
			// change product status to buying and update product
			if (!product.getStatus().equalsIgnoreCase("buying")) {
				product.setStatus("buying");
				productService.updateProduct(product);
			}

			ProductChatResponse chatResponse = new ProductChatResponse();
			chatResponse.setDate(productChat.getDate());
			chatResponse.setImageUrl(imageUrl);
			chatResponse.setProductId(productId);
			chatResponse.setReceiverId(userTo);
			chatResponse.setSenderId(userFrom);
			chatResponse.setText(content);
			chatResponse.setConversationId(productChat.getChatId());
			// Index to elastic
			GeoPoint gp = new GeoPoint(product.getGeo().getLattitude(), product.getGeo().getLongitude());
			product.setLocation(gp);
			JestClient client = ElasticUtil.getClient();
			Index index = new Index.Builder(product).index("product").type("Product").id(product.getProductId().toString()).build();
			client.execute(index);
			return new ResponseEntity<ProductChatResponse>(chatResponse, HttpStatus.OK);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			e.printStackTrace();
			response.setCode("E001");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("E002");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * get conversation by conversation id
	 * 
	 * @param conversationId
	 * @param offset
	 * @param numResults
	 * @return
	 */
	@RequestMapping(value = "/v1/conversations/{conversationId}/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getConversations(@PathVariable("conversationId") String conversationId, @RequestParam("offset") Integer offset, @RequestParam("num_results") Integer numResults) {
		GenericResponse response = new GenericResponse();
		try {
			ProductChat productChat = chatService.getChatByConversationId(conversationId);
			if (productChat == null) {
				response.setCode("V001");
				response.setMessage("invalid conversation ID");
				return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);
			}
			List<ProductConversations> conv = conversationService.getConversationById(productChat.getChatId());
			// sort the conversation message
			Collections.sort(conv, new Comparator<ProductConversations>() {
				@Override
				public int compare(final ProductConversations object1, final ProductConversations object2) {
					return object1.getDate().compareTo(object2.getDate());
				}
			});

			ProductConversationResponse conversationResponse = new ProductConversationResponse();
			conversationResponse.setId(productChat.getChatId());
			conversationResponse.setForbidden(productChat.isForbidden());
			MessageResponse msgResponse = null;
			List<MessageResponse> listMsg = new ArrayList<MessageResponse>();
			if (offset < conv.size()) {
				for (int i = offset; i < conv.size() && i < numResults; i++) {
					msgResponse = new MessageResponse();
					msgResponse.setId(conv.get(i).getId());
					msgResponse.setCreated_at(conv.get(i).getDate());
					msgResponse.setIs_read(conv.get(i).getIsRead());
					msgResponse.setStatus(productChat.getStatus());
					msgResponse.setText(conv.get(i).getMessage());
					msgResponse.setType(conv.get(i).getType());
					msgResponse.setUser_id(conv.get(i).getReceiverId().getUserId());
					msgResponse.setImageUrl(conv.get(i).getImageUrl());
					listMsg.add(msgResponse);
				}
			}
			conversationResponse.setMessages(listMsg);

			List<Product> product = productService.getProductByProductId(productChat.getProductId().getProductId());
			ProductResponse productResponse = null;
			if (product.size() != 0) {
				productResponse = SetproductResponse.prepareResponse(product.get(0));
			}
			conversationResponse.setProduct(productResponse);

			conversationResponse.setStatus(productChat.getStatus());
			conversationResponse.setUnread_count(productChat.getUnreadCount());
			conversationResponse.setUpdated_at(productChat.getDate());
			// senderID
			Seller seller = sellerService.getSellerById(productChat.getSeller().getUserId());
			SellerResponse userFromResponse = null;
			if (seller != null) {
				userFromResponse = new SellerResponse();
				userFromResponse.setBanned(seller.getBanned());
				userFromResponse.setCity(seller.getCity());
				userFromResponse.setCountry_code(seller.getCountryCode());
				userFromResponse.setName(seller.getFirstName() + " " + seller.getLastName());
				userFromResponse.setProfile_pic_url(seller.getProfilePic());
				userFromResponse.setStatus(seller.getStatus());
				userFromResponse.setId(seller.getUserId().getUserId());
				userFromResponse.setZip_code(seller.getZipCode());
			}
			conversationResponse.setUser_from(userFromResponse);
			// /receiver
			Seller receiver = sellerService.getSellerById(productChat.getBuyer().getUserId());
			SellerResponse userToResponse = null;
			if (receiver != null) {
				userToResponse = new SellerResponse();
				userToResponse.setBanned(receiver.getBanned());
				userToResponse.setCity(receiver.getCity());
				userToResponse.setCountry_code(receiver.getCountryCode());
				userToResponse.setName(receiver.getFirstName() + " " + receiver.getLastName());
				userToResponse.setProfile_pic_url(receiver.getProfilePic());
				userToResponse.setStatus(receiver.getStatus());
				userToResponse.setZip_code(receiver.getZipCode());
				userToResponse.setId(receiver.getUserId().getUserId());
			}
			conversationResponse.setUser_to(userToResponse);

			return new ResponseEntity<ProductConversationResponse>(conversationResponse, HttpStatus.OK);
		} catch (org.hibernate.ObjectNotFoundException e) {
			e.printStackTrace();
			response.setCode("E001");
			response.setMessage("user id not found");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.EXPECTATION_FAILED);

		} catch (Exception e) {
			e.printStackTrace();
			response.setCode("E002");
			response.setMessage(e.getMessage());
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}

	}

}
