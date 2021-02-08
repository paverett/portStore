package com.portStore.model;

import io.vertx.core.json.JsonObject;

public class Product {

  private Integer id;

  private String name;

  public Product(JsonObject response) {
    id = Integer.parseInt(response.getJsonObject("product").getJsonObject("item").getString("tcin"));
    name = response.getJsonObject("product").getJsonObject("item").getJsonObject("product_description")
        .getString("title");
  }

  public Product() {
    
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
