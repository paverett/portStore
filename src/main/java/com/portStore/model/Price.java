package com.portStore.model;

import io.vertx.core.json.JsonObject;

public class Price {

  private double value;

  private String currencyCode;

  public Price(JsonObject response) {
    value = response.getJsonObject("price").getDouble("value");
    currencyCode = response.getJsonObject("price").getString("currencyCode");
  }

  public Price() {

  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}