package com.product.dto.request;

public class ProductSaveRequest {

	private String description;
	private String displayName;
	private int categoryId;
	private String languageCode;
	private double price;
	private String currency;
	private int status;
	private String condition;
	private String sellerId;
	// private String createdAt;
	// private String updatedAt;
	private double lattitude;
	private double longitude;
	private String country;

	private String address;

	private String city;

	private String zipCode;

	private String state;

	private String warranty;

	private String brand;

	// private List<String> imageList = new ArrayList<String>();

	private String imageInformation;

	public String getDescription() {
		return description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public double getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public String getCondition() {
		return condition;
	}

	public String getSellerId() {
		return sellerId;
	}

	public double getLattitude() {
		return lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public String getAddress() {
		return address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getState() {
		return state;
	}

	public String getWarranty() {
		return warranty;
	}

	public String getBrand() {
		return brand;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getImageInformation() {
		return imageInformation;
	}

	public void setImageInformation(String imageInformation) {
		this.imageInformation = imageInformation;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
