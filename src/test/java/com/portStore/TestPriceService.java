package com.portStore;

import com.portStore.model.Price;
import com.portStore.service.PriceService;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class TestPriceService implements PriceService {

  @Override
  public Future<Price> getPrice(String productId) {
    return Future.future(f -> {
      Price price = new Price();
      price.setValue(13.89);
      price.setCurrencyCode("USD");
      f.complete(price);
    });
  }

  @Override
  public Future<String> updatePrice(String productId, JsonObject newPrice) {
    return Future.future(f -> {
      f.complete("OK");
    });
  }
}
