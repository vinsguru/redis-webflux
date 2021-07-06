package com.vinsguru.redisperformance.service;

import com.vinsguru.redisperformance.entity.Product;
import com.vinsguru.redisperformance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV1 {

    @Autowired
    private ProductRepository repository;

    public Mono<Product> getProduct(int id){
        return this.repository.findById(id);
    }

    public Mono<Product> updateProduct(int id, Mono<Product> productMono){
        return this.repository.findById(id)
                        .flatMap(p -> productMono.doOnNext(pr -> pr.setId(id)))
                        .flatMap(this.repository::save);
    }

}
