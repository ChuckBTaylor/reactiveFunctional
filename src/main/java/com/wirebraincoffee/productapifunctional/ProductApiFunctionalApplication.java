package com.wirebraincoffee.productapifunctional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.wirebraincoffee.productapifunctional.handler.ProductHandler;
import com.wirebraincoffee.productapifunctional.model.Product;
import com.wirebraincoffee.productapifunctional.repository.ProductRepository;

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
	// @formatter:off
		return nest(path("/products"),
				nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON).or(accept(TEXT_EVENT_STREAM))),
						route(GET("/"), handler::getAllProducts)
						.andRoute(method(HttpMethod.POST), handler::postProduct)
						.andRoute(DELETE("/"), handler::deleteAllProducts)
						.andRoute(GET("/events"), handler::getProductEvents)
						.andNest(path("/{id}"),
								route(method(HttpMethod.GET), handler::getProduct)
								.andRoute(method(HttpMethod.PUT), handler::updateProduct)
								.andRoute(method(HttpMethod.DELETE), handler::deleteProduct)
						)
				)
		);
    // @formatter:on
	}
}
