package examples;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ExampleSimulation01 extends Simulation {

  // Define HTTP configuration
  // Reference: https://docs.gatling.io/reference/script/protocols/http/protocol/
  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://api.zippopotam.us")
      .acceptHeader("application/json")
      .userAgentHeader(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  // Define scenario
  // Reference: https://docs.gatling.io/reference/script/core/scenario/
  private static final ScenarioBuilder scenario =
          scenario("GET zip code data")
                  .exec(http("Data for US zip code 90210")
                          .get("/us/90210"));

  // Define assertions
  // Reference: https://docs.gatling.io/reference/script/core/assertions/
  private static final Assertion noFailedRequests =
          global().failedRequests().count().lt(1L);

  private static final Assertion maxResponseTimeLessThan100ms =
          global().responseTime().max().lt(100);

  private static final Assertion numberOfRequestsMadeIs60 =
          global().allRequests().count().is(60L);

  // Define injection profile and execute the test
  // Reference: https://docs.gatling.io/reference/script/core/injection/
  {
    setUp(scenario.injectOpen(constantUsersPerSec(2)
            .during(Duration.ofSeconds(30))))
            .assertions(noFailedRequests, maxResponseTimeLessThan100ms, numberOfRequestsMadeIs60)
            .protocols(httpProtocol);
  }
}
