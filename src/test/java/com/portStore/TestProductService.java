package com.portStore;

import com.portStore.model.Product;
import com.portStore.service.ProductService;

import io.vertx.core.Future;

public class TestProductService implements ProductService {

  @Override
  public Future<Product> getProduct(String productId) {
    return Future.future(f -> {
      Product product = new Product();
      product.setId(13860428);
      product.setName("The Big Lebowski (Blu-ray) (Widescreen)");
      f.complete(product);
    });
  }
}
