package com.portStore.model;

import io.vertx.core.json.JsonObject;

public class Price {

  private Float value;

  private String currencyCode;

  public Price(JsonObject response) {
    value = response.getJsonObject("price").getFloat("value");
    currencyCode = response.getJsonObject("price").getString("currency_code");
  }

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}