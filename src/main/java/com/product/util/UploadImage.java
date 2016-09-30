package com.product.util;

import java.security.MessageDigest;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadImage {

	@Autowired
	AmazonS3Util amazonS3Util;

	String keyName = null;

	public String uploadImage(MultipartFile file, String folder) {
		if (!file.isEmpty()) {
			try {
				System.out.println("==folder===" + folder);
				String token = UUID.randomUUID().toString();
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(token.getBytes());

				byte byteData[] = md.digest();

				// convert the byte to 64 hex format
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}

				String key = amazonS3Util.generateKey(sb.toString());
				keyName = key + ".jpg";
				System.out.println("===keyname===" + keyName);

				// upload file to amazon
				try {
					amazonS3Util.uploadFileToS3(keyName, file.getInputStream(), file.getOriginalFilename(), folder);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {

			}
		}

		return keyName;
	}
}
