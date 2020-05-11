package com.wirebraincoffee.productapifunctional.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.wirebraincoffee.productapifunctional.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
