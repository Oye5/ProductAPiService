package com.product.dto.response;

import java.util.List;

public class ProductGenericResponse {
	private List<String> imagekeyList;
	private String thumb_nail;
	private String productId;
	private String code;
	private String message;

	public List<String> getImagekeyList() {
		return imagekeyList;
	}

	public String getProductId() {
		return productId;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setImagekeyList(List<String> imagekeyList) {
		this.imagekeyList = imagekeyList;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getThumb_nail() {
		return thumb_nail;
	}

	public void setThumb_nail(String thumb_nail) {
		this.thumb_nail = thumb_nail;
	}

}
