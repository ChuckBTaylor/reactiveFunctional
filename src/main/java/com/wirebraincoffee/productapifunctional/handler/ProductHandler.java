package com.wirebraincoffee.productapifunctional.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		return productMono
				.flatMap(product -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(product)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> postProduct(ServerRequest request) {
		Mono<Product> productMono = request.bodyToMono(Product.class);
		return productMono.flatMap(product -> ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON)
				.body(repository.save(product), Product.class));
	}

	public Mono<ServerResponse> updateProduct(ServerRequest request) {
		String id = request.pathVariable("id");
		Mono<Product> existingProductMono = repository.findById(id);
		Mono<Product> productMono = request.bodyToMono(Product.class);
		return productMono
				.zipWith(existingProductMono,
						(product, existingProduct) -> new Product(existingProduct.getId(), product.getName(),
								product.getPrice()))
				.flatMap(product -> ServerResponse.ok().contentType(APPLICATION_JSON).body(repository.save(product),
						Product.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> deleteProduct(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Product> productMono = repository.findById(id);
		return productMono.flatMap(product -> ServerResponse.ok().build(repository.delete(product)).switchIfEmpty(ServerResponse.notFound().build()));
	}

}
