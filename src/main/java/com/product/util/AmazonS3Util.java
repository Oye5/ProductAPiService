package com.product.util;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
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
	

	public String uploadFileToS3(String key, InputStream ins, String fileName,String folder) throws AmazonServiceException, AmazonClientException {

		AmazonS3Client s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretKey));

		ObjectMetadata meta = new ObjectMetadata();
		meta.addUserMetadata("name", fileName);

		s3client.putObject(new PutObjectRequest(bucketName, folder + "/" + key, ins, meta).withCannedAcl(CannedAccessControlList.PublicRead));
		// .withCannedAcl(CannedAccessControlList.PublicRead));
		return key;
	}

	public String generateKey(String key) {
		String x = key;
		String y = x.substring(0, 2) + "/" + x.substring(2, x.length());
		String z = y.substring(0, 5) + "/" + x.substring(4, x.length());
		String a = z.substring(0, 8) + "/" + x.substring(6, x.length());
		String b = a.substring(0, 11) + "/" + key;
		return b;

	}
}
