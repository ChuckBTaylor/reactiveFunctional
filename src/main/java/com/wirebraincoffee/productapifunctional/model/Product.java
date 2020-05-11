package com.wirebraincoffee.productapifunctional.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product {

	@Id
	private String id;
	
	public Product(String id, String name, Double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public Product() {
	}

	private String name;
	
	private Double price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product {\"id\":\"" + id + "\", name\":\"" + name + "\", price\":\"" + price + "}";
	}
}
