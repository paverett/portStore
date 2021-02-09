package com.portStore;

import java.util.List;

import com.portStore.helpers.ResponseFormatter;
import com.portStore.model.Product;
import com.portStore.service.PriceService;
import com.portStore.service.PriceServiceImpl;
import com.portStore.service.ProductService;
import com.portStore.service.ProductServiceImpl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

  private static final int PORT = 8888;

  private PriceService priceService;
  private ProductService productService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.route("/products/:productId").handler(BodyHandler.create());

    router.get("/products/:productId").handler(this::getProduct);
    router.put("/products/:productId").handler(this::updatePrice);

    priceService = initService();
    productService = initProductService();

    vertx.createHttpServer().requestHandler(router).listen(PORT, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.info("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private PriceServiceImpl initService() {
    JsonObject mongoConfig = new JsonObject();
    mongoConfig.put("http.port", 27017);
    mongoConfig.put("db_name", "prices");
    mongoConfig.put("connection_string", "mongodb://localhost:27017");
    return new PriceServiceImpl(vertx, mongoConfig);
  }

  private ProductServiceImpl initProductService() {
    return new ProductServiceImpl(vertx);
  }

  private void getProduct(RoutingContext ctx) {
    String productId = ctx.pathParam("productId");

    CompositeFuture cf = CompositeFuture.join(priceService.getPrice(productId), productService.getProduct(productId));
    cf.onComplete(cfHandler -> {
      if (cfHandler.succeeded()) {
        List<Object> futures = cf.list();
        log.info(futures.toString());
        Product product = (Product) futures.get(1);
        String productName = product.getName();
        new JsonObject();
        JsonObject priceList = JsonObject.mapFrom(futures.get(0));
        ctx.response().putHeader("content-type", "application/json;")
            .end(ResponseFormatter.formSuccessResponse(productId, productName, priceList).encode());
      } else if (cfHandler.failed()) {
        log.info(cfHandler.cause());
        ctx.response().putHeader("content-type", "application/json;")
            .end(ResponseFormatter.formErrorResponse(cfHandler.cause().toString(), "").encode());
      }
    });
  }

  private void updatePrice(RoutingContext ctx) {
    String productId = ctx.pathParam("productId");
    JsonObject newPrice = ctx.getBodyAsJson();
    priceService.updatePrice(productId, newPrice).onSuccess(handler -> {
      JsonObject response = new JsonObject();
      response.put("message", "OK");
      ctx.response().putHeader("content-type", "application/json;").end(response.encode());
    }).onFailure(handler -> {
      ctx.response().putHeader("content-type", "application/json;")
          .end(ResponseFormatter.formErrorResponse("Failed", handler.getMessage()).encode());
    });
  }
}