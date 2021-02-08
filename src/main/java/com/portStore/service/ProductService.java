package com.portStore.service;

import com.portStore.model.Product;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface ProductService {

  void getProduct(String productId, Handler<AsyncResult<Product>> resultHandler);

}
