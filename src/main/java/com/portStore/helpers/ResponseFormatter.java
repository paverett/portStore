package com.portStore.helpers;

import io.vertx.core.json.JsonObject;

public class ResponseFormatter {
  public static JsonObject formSuccessResponse(String productId, String productName, JsonObject price) {
    JsonObject response = new JsonObject();
    response.put("id", Integer.parseInt(productId));
    response.put("name", productName);
    response.put("price", price);
    return response;
  }

  public static JsonObject formErrorResponse(String message, String additionalInformation) {
    JsonObject response = new JsonObject();
    response.put("errorMessage", message);
    response.put("additionalInformation", additionalInformation);
    return response;
  }
}
