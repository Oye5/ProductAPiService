package com.product.util;

import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AmazonS3Util {

	// @Value("${aws.s3.bucket}")
	public String bucketName = "oye5";
	// @Value("${aws.s3.access.key.id}")
	public String accessKeyId = "AKIAI3FIXXHK4HA47UMQ";
	// @Value("${aws.s3.access.key}")
	public String secretKey = "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";
	// @Value("${aws.s3.folder}")
	public String folder = "product";

	public String uploadFileToS3(String key, InputStream ins, String fileName) throws AmazonServiceException, AmazonClientException {

		AmazonS3Client s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretKey));

		System.out.println("Uploading a new object to S3 from a file\n");
		ObjectMetadata meta = new ObjectMetadata();
		meta.addUserMetadata("name", fileName);

		s3client.putObject(new PutObjectRequest(bucketName, folder + "/" + key, ins, meta));

		System.out.println("Uploaded file-- successfully");

		System.out.println("Key " + key);

		return key;
	}
}
