package com.portStore;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

public class MainVerticle extends AbstractVerticle {

  MongoClient mongoClient;
  WebClient client;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.get("/products/:productId").handler(this::getProducts);
    router.put("/products/:productId").handler(this::updateProduct);

    JsonObject mongoConfig = new JsonObject();
    mongoConfig.put("http.port", 27017);
    mongoConfig.put("db_name", "products");
    mongoConfig.put("connection_string", "mongodb://localhost:27017");

    mongoClient = MongoClient.createShared(vertx, mongoConfig);

    client = WebClient.create(vertx);

    vertx.createHttpServer()
    .requestHandler(router)
    .listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private void getProducts(RoutingContext ctx) {
    String productId = ctx.pathParam("productId");

    CompositeFuture cf = CompositeFuture.join(getProductInfo(productId), getPriceInfo(productId));
    cf.onComplete(cfHandler -> {
      if (cfHandler.succeeded()) {
        String productName = cf.list().get(0).toString();
        JsonArray priceList = new JsonArray(cf.list().get(1).toString());
        JsonObject response = new JsonObject();
        response.put("id", Integer.parseInt(productId));
        response.put("name", productName);
        response.put("prices", priceList);
        ctx.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(response.encode());
      } else if (cfHandler.failed()) {
        JsonObject response = new JsonObject();
        response.put("message", "Error recieving information");
        ctx.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(response.encode());
      }
    });
  }

  private void updateProduct(RoutingContext ctx) {
    String productID = ctx.pathParam("productId");
    System.out.println(productID);
    JsonObject response = new JsonObject();
    response.put("message", "Complete");
    ctx.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(response.encode());
  }

  Future<String> getProductInfo(String productId) {
    return Future.future(f -> {
      client
        .get(443, "redsky.target.com", 
          String.format("/v3/pdp/tcin/%s?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate", productId))
        .ssl(true)
        .send()
        .onSuccess(handler -> {
          JsonObject productInfo = handler.bodyAsJsonObject();
          String productName = productInfo.getJsonObject("product").getJsonObject("item").getJsonObject("product_description").getString("title");
          f.complete(productName);
        })
        .onFailure(handler -> {
          f.fail("Error recieving response from downstream service");
        });
    });
  }

  Future<JsonArray> getPriceInfo(String productId) {
    return Future.future(f -> {
      JsonObject mongoQuery = new JsonObject();
      mongoQuery.put("id", Integer.parseInt(productId));
      mongoClient.find("products", mongoQuery, handler -> {
        if (handler.succeeded()) {
          JsonArray priceList = handler.result().get(0).getJsonArray("price_list");
          f.complete(priceList);
        } else if (handler.failed()) {
          f.fail("Error recieving response from mongodb");
        }
      });
    });
  }
}