package com.wirebraincoffee.productapifunctional;

import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.wirebraincoffee.productapifunctional.handler.ProductHandler;
import com.wirebraincoffee.productapifunctional.model.Product;
import com.wirebraincoffee.productapifunctional.repository.ProductRepository;

import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProductApiFunctionalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiFunctionalApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ProductRepository repository) {
		return args -> {
			Flux<Product> productFlux = Flux.just(new Product(null, "Big Latte", 2.99),
					new Product(null, "Big Decaf", 2.49), new Product(null, "Green Tea", 1.99))
					.flatMap(repository::save);
			productFlux.thenMany(repository.findAll()).subscribe(System.out::println);
		};
	}

	@Bean
	public RouterFunction<ServerResponse> routes(ProductHandler handler) {
		return route(GET("/products").and(accept(APPLICATION_JSON)), handler::getAllProducts)
				.andRoute(POST("/products").and(contentType(APPLICATION_JSON)), handler::postProduct)
				.andRoute(DELETE("/products").and(accept(APPLICATION_JSON)), handler::deleteAllProducts)
				.andRoute(GET("/products/events").and(accept(TEXT_EVENT_STREAM)), handler::getProductEvents)
				.andRoute(GET("/products/{id}").and(accept(APPLICATION_JSON)), handler::getProduct)
				.andRoute(PUT("products/{id}").and(contentType(APPLICATION_JSON)), handler::updateProduct)
				.andRoute(DELETE("/products/{id}").and(accept(APPLICATION_JSON)), handler::deleteProduct);
	}
}
