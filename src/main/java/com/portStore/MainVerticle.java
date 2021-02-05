package com.target.portStore;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.get("/products/:productId").handler(this::getProducts);
    router.put("/products/:productId").handler(this::updateProduct);

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

  private void getProducts(RoutingContext routingContext) {
    JsonObject response = new JsonObject();
    response.put("message", "hello");
    routingContext.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(response.encode());
  }

  private void updateProduct(RoutingContext routingContext) {
    JsonObject response = new JsonObject();
    response.put("message", "Complete");
    routingContext.response()
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(response.encode());
  }
}
