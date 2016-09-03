package com.product.util;

import java.io.InputStream;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AmazonS3Util {

	@Value("${aws.s3.bucket}")
	public String bucketName; // = "oye5";
	@Value("${aws.s3.access.key.id}")
	public String accessKeyId;// = "AKIAJLLUP2ENUSVLV46A";
	@Value("${aws.s3.access.key}")
	public String secretKey;// = "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";
	@Value("${aws.s3.folder}")
	public String folder;// = "product";

	public String uploadFileToS3(String key, InputStream ins, String fileName) throws AmazonServiceException, AmazonClientException {

		AmazonS3Client s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretKey));

		ObjectMetadata meta = new ObjectMetadata();
		meta.addUserMetadata("name", fileName);

		s3client.putObject(new PutObjectRequest(bucketName, folder + "/" + key, ins, meta));

		return key;
	}

	public String generateKey() {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		int N = alphabet.length();
		Random r = new Random();
		String result = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				result += alphabet.charAt(r.nextInt(N));

			}
			result = result + "/";
		}
		return result;

	}
}
