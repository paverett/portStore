package com.portStore;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.portStore.helpers.ResponseFormatter;
import com.portStore.model.Price;
import com.portStore.model.Product;
import com.portStore.service.PriceServiceImpl;
import com.portStore.service.ProductServiceImpl;

@RunWith(VertxUnitRunner.class)
public class TestMainVerticle {

  private Vertx vertx;

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Mock
  private HttpClient defaultHttpClient;

  static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  @Before
  public void deploy_verticle(TestContext testContext) {
    vertx = rule.vertx();
    vertx.deployVerticle(new MainVerticle(), testContext.asyncAssertSuccess());
  }

  @Test
  public void verticle_deployed(TestContext testContext) throws Throwable {
    Async async = testContext.async();
    async.complete();
  }

  @Test
  public void createCorrectResponseForSuccess(TestContext context) {
    JsonObject response = new JsonObject();
    response.put("id", 1010);
    response.put("name", "name");
    response.put("price", new JsonObject());
    JsonObject newResponse = ResponseFormatter.formSuccessResponse("1010", "name", new JsonObject());
    context.assertEquals(response, newResponse);
  }

  @Test
  public void createCorrectResponseForFailure(TestContext context) {
    JsonObject response = new JsonObject();
    response.put("errorMessage", "fail");
    response.put("additionalInformation", "no more info");
    JsonObject newResponse = ResponseFormatter.formErrorResponse("fail", "no more info");
    context.assertEquals(response, newResponse);
  }

  @Test
  public void productServiceReturnsSuccess(TestContext context) throws IOException {
    String productId = "13860428";
    ProductServiceImpl productService = Mockito.mock(ProductServiceImpl.class,
        AdditionalAnswers.delegatesTo(new TestProductService()));
    JsonObject productMock = new JsonObject(
        readFile("src/test/java/com/portStore/resources/redskyMock.json", StandardCharsets.UTF_8));
    Product product = new Product(productMock);
    Mockito.when(productService.getProduct(any())).thenReturn(Future.succeededFuture(new Product(productMock)));
    context.assertEquals(product.getId(), productService.getProduct(productId).result().getId());
  }

  @Test
  public void productServiceReturnsFailure(TestContext context) {
    String productId = "13860428";
    ProductServiceImpl productService = Mockito.mock(ProductServiceImpl.class,
        AdditionalAnswers.delegatesTo(new TestProductService()));
    Mockito.when(productService.getProduct(any())).thenReturn(Future.failedFuture("Failure"));
    context.assertEquals("Failure", productService.getProduct(productId).cause().getMessage());
  }

  @Test
  public void priceServiceReturnsSuccess(TestContext context) throws IOException {
    String productId = "13860428";
    JsonObject priceValues = new JsonObject();
    priceValues.put("currencyCode", "USD");
    priceValues.put("value", 13.82);
    JsonObject priceObject = new JsonObject();
    priceObject.put("price", priceValues);
    Price price = new Price(priceObject);
    PriceServiceImpl priceService = Mockito.mock(PriceServiceImpl.class,
        AdditionalAnswers.delegatesTo(new TestPriceService()));
    Mockito.when(priceService.getPrice(any())).thenReturn(Future.succeededFuture(new Price(priceObject)));
    context.assertEquals(price.getValue(), priceService.getPrice(productId).result().getValue());
  }

  @Test
  public void priceServiceReturnsFailure(TestContext context) {
    String productId = "13860428";
    PriceServiceImpl priceService = Mockito.mock(PriceServiceImpl.class,
        AdditionalAnswers.delegatesTo(new TestPriceService()));
    Mockito.when(priceService.getPrice(any())).thenReturn(Future.failedFuture("Failure"));
    context.assertEquals("Failure", priceService.getPrice(productId).cause().getMessage());
  }
}
