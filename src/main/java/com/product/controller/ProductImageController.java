package com.product.controller;

import java.security.MessageDigest;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.response.GenericResponse;
import com.product.model.ProductImages;
import com.product.service.ProductImageService;
import com.product.util.AmazonS3Util;

@MultipartConfig
@RestController
public class ProductImageController {

	@Autowired
	AmazonS3Util amazonS3Util;

	@Autowired
	ProductImageService productImageService;

	@RequestMapping(value = "/upload", consumes = "multipart/form-data", method = RequestMethod.POST)
	public ResponseEntity<?> uploadImage(@RequestParam("fileName") MultipartFile file) {
		GenericResponse response = new GenericResponse();
		if (!file.isEmpty()) {
			try {
				String token = UUID.randomUUID().toString();
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(token.getBytes());

				byte byteData[] = md.digest();

				// convert the byte to 64 hex format
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}

				String fileName = sb.toString() + ".jpg";

				String keyName = amazonS3Util.generateKey() + fileName;
				// upload file to amazon
				try {
					amazonS3Util.uploadFileToS3(keyName, file.getInputStream(), file.getOriginalFilename());
				} catch (Exception e) {
					response.setCode("E001");
					response.setMessage("failed to upload file");
					return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
				}
				ProductImages productImages = new ProductImages();
				productImages.setId(UUID.randomUUID().toString());
				productImages.setUrl(keyName);
				productImageService.saveUploadedImage(productImages);
				return new ResponseEntity<ProductImages>(productImages, HttpStatus.OK);
			} catch (Exception e) {
				response.setCode("E001");
				response.setMessage(e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setCode("E002");
			response.setMessage("failed to upload file because the file was empty");
			return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
