package com.portStore.service;

import com.portStore.model.Price;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface PriceService {

  Future<Price> getPrice(String productId);

  Future<String> updatePrice(String productId, JsonObject newPrice);

}
