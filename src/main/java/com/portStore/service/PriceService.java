package com.portStore.service;

import com.portStore.model.Price;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface PriceService {

  void getPrice(String productId, Handler<AsyncResult<Price>> resultHandler);

  void updatePrice(String productId, JsonObject newPrice, Handler<AsyncResult<String>> resultHandler);

}
