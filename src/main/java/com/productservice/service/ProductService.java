package com.productservice.service;

import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List ;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final ProductRepository productRepository ;
    private final WebClient.Builder webClientBuilder; // Inject WebClient Builder


    private void decrementProductQuantities(List<String> skuCodes) {
  	  webClientBuilder.build().post()
  	    .uri("http://inventory-service/api/inventory/decrement-stock")  // Use the correct endpoint
  	    .bodyValue(skuCodes)  // Send the entire list in the request body
  	    .retrieve()
  	    .toBodilessEntity()
  	    .block();  
      }
    
    public void createProduct(ProductRequest productRequest) {
//        String url = "http://inventory-service/api/inventory/find-by-sku/" + productRequest.getName();
//        System.out.println(url);
//        boolean isInStock = webClientBuilder.build().get()
//        	      .uri(url)
//        	      .retrieve()
//        	      .bodyToMono(Boolean.class)
//        	      .block();
//
//        	  System.out.println(url);
//        	  System.out.println("Is in stock: " + isInStock);
//
//        	  if (isInStock) {
    	Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build() ;
        productRepository.save(product) ;
//        log.info("Product Created of id {}", product.getId());
//    	 }else {
//    		 throw new RuntimeException("Product with name " + productRequest.getName() + " not found in inventory");
//    	 }
    }

    public List<ProductResponse> fetchAllProducts() {
        List<Product> products = productRepository.findAll() ;
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build() ;
    }
}
