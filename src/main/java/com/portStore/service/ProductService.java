package com.portStore.service;

import com.portStore.model.Product;

import io.vertx.core.Future;

public interface ProductService {

  Future<Product> getProduct(String productId);

}
