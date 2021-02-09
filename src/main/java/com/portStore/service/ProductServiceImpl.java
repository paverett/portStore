package com.portStore.service;

import com.portStore.model.Product;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class ProductServiceImpl implements ProductService {

  private static final Logger log = LoggerFactory.getLogger(PriceServiceImpl.class);

  private Vertx vertx;
  private WebClient webClient;

  public ProductServiceImpl(Vertx vertx) {
    this.vertx = vertx;
    this.webClient = WebClient.create(vertx);
  }

  public Future<Product> getProduct(String productId) {
    return Future.future(f -> {
      webClient.get(443, "redsky.target.com", String.format(
        "/v3/pdp/tcin/%s?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate",
        productId))
          .ssl(true)
          .send()
          .onSuccess(handler -> {
          JsonObject productInfo = handler.bodyAsJsonObject();
          f.complete(new Product(productInfo));
        }).onFailure(handler -> {
          f.fail(handler.getCause());
        });
    });
  }

}
