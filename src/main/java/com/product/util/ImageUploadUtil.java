package com.product.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.product.model.Product;
import com.product.model.ProductImages;
import com.product.model.ThumbNail;
import com.product.service.ProductImageService;
import com.product.service.ThumbNailService;

@Component
public class ImageUploadUtil {

	@Autowired
	AmazonS3Util amazonS3Util;

	@Autowired
	ProductImageService productImageService;

	@Autowired
	ThumbNailService thumbNailService;
	ProductImages productImages = null;
	String key = null;
	Product product = null;
	String thumbKey = null;
	@Value("${aws.s3.folder}")
	public String folder;// = "product";
	
	public List<String> uploadImage(MultipartFile[] file, String productId) {
		List<String> keyList = new ArrayList<String>();
		for (int j = 0; j < file.length; j++) {
			if (!file[j].isEmpty()) {
				try {
					System.out.println("==folder==="+folder);
					String token = UUID.randomUUID().toString();
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					md.update(token.getBytes());

					byte byteData[] = md.digest();

					// convert the byte to 64 hex format
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < byteData.length; i++) {
						sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
					}

					key = amazonS3Util.generateKey(sb.toString());
					//upload thumb nail
					if(j==0){
	                    thumbKey=key+"_thumb.jpg";
	                    amazonS3Util.uploadFileToS3(thumbKey, file[j].getInputStream(), file[j].getOriginalFilename(),folder);
	                    keyList.add(thumbKey); 
					}
					String keyName = key + ".jpg";
					System.out.println("===keyname===" + keyName);

					// upload file to amazon
					try {
						amazonS3Util.uploadFileToS3(keyName, file[j].getInputStream(), file[j].getOriginalFilename(),folder);
					} catch (Exception e) {
						e.printStackTrace();
					}
					productImages = new ProductImages();
					productImages.setId(UUID.randomUUID().toString());
					product = new Product();
					product.setProductId(productId);
					productImages.setProductId(product);
					productImages.setUrl(keyName);
					// productImages.setThumbNail(key+"_thumb.jpg");
					productImageService.saveUploadedImage(productImages);
					keyList.add(keyName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		ThumbNail thumb = new ThumbNail();
		thumb.setId(UUID.randomUUID().toString());
		thumb.setHeight(140);
		thumb.setWidth(90);
		thumb.setProductId(product);
		thumb.setUrl(thumbKey);
		try {
			thumbNailService.saveThumbNail(thumb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyList;
	}

	
	
}
