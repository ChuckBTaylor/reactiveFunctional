package com.wirebraincoffee.productapifunctional.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.wirebraincoffee.productapifunctional.model.Product;
import com.wirebraincoffee.productapifunctional.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

	@Autowired
	private ProductRepository repository;

	public Mono<ServerResponse> getAllProducts(ServerRequest request) {
		Flux<Product> products = repository.findAll();
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(products, Product.class);
	}

	public Mono<ServerResponse> getProduct(ServerRequest request) {
		String id = request.pathVariable("id");
		Mono<Product> productMono = repository.findById(id);
		return productMono.flatMap(product -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(product)));
	}

}
