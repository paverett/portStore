package com.portStore;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(VertxUnitRunner.class)
public class TestMainVerticle {

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Mock
  private HttpClient defaultHttpClient;

  @Before
  public void deploy_verticle(TestContext testContext) {
    Vertx vertx = rule.vertx();
    vertx.deployVerticle(new MainVerticle(), testContext.asyncAssertSuccess());
  }

  @Test
  public void verticle_deployed(TestContext testContext) throws Throwable {
    Async async = testContext.async();
    async.complete();
  }

  @Test
  public void returnsSuccess(TestContext testContext) {
  }
}
