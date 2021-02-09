package com.portStore.service;

import java.util.Optional;

import com.portStore.model.Price;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class PriceServiceImpl implements PriceService {

  private static final Logger log = LoggerFactory.getLogger(PriceServiceImpl.class);

  private final Vertx vertx;
  private JsonObject mongoConfig;
  private MongoClient mongoClient;

  private final String COLLECTION = "priceCollection";

  public PriceServiceImpl(Vertx vertx, JsonObject mongoConfig) {
    this.vertx = vertx;
    this.mongoConfig = mongoConfig;
    this.mongoClient = MongoClient.createShared(vertx, mongoConfig);
  }

  public Future<Price> getPrice(String productId) {
    return Future.future(f -> {
      JsonObject mongoQuery = new JsonObject().put("id", productId);
      mongoClient.find(COLLECTION, mongoQuery, handler -> {
        if (handler.succeeded()) {
          Optional<JsonObject> result = handler.result().stream().findFirst();
          if (result.isPresent()) {
            log.info(result.get().toString());
            f.complete(new Price(result.get()));
          } else {
            f.fail("Id does not exist");
          }
        } else {
          f.fail(handler.cause());
        }
      });
    });
  }

  public Future<String> updatePrice(String productId, JsonObject newPrice) {
    return Future.future(f -> {
      JsonObject mongoQuery = new JsonObject().put("id", productId);
      newPrice.mergeIn(mongoQuery);
      log.info(newPrice.toString());
      mongoClient.findOneAndReplace(COLLECTION, mongoQuery, newPrice, handler -> {
        if (handler.succeeded()) {
          log.info(handler.result());
          f.complete("OK");
        } else {
          f.fail(handler.cause());
        }
      });
    });
  }

}
