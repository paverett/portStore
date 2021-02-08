package com.portStore.service;

import java.util.Optional;

import com.portStore.model.Price;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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

  public void getPrice(String productId, Handler<AsyncResult<Price>> resultHandler) {
    JsonObject mongoQuery = new JsonObject().put("id", productId);
    mongoClient.find(COLLECTION, mongoQuery, handler -> {
      if (handler.succeeded()) {
        Optional<JsonObject> result = handler.result().stream().findFirst();
        if (result.isPresent()) {
          log.info(result.get().toString());
          resultHandler.handle(Future.succeededFuture(new Price(result.get())));
        } else {
          resultHandler.handle(Future.failedFuture("id does not exist"));
        }
      } else {
        resultHandler.handle(Future.failedFuture(handler.cause()));
      }
    });
  }

  public void updatePrice(String productId, JsonObject newPrice, Handler<AsyncResult<String>> resultHandler) {
    JsonObject mongoQuery = new JsonObject().put("id", productId);
    mongoClient.findOneAndUpdate(COLLECTION, mongoQuery, newPrice, handler -> {
      if (handler.succeeded()) {
        resultHandler.handle(Future.succeededFuture("OK"));
      } else {
        resultHandler.handle(Future.failedFuture(handler.cause()));
      }
    });
  }

}
