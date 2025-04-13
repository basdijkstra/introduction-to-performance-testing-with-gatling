package examples;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ExampleSimulation03 extends Simulation {

  // Define HTTP configuration
  // Reference: https://docs.gatling.io/reference/script/protocols/http/protocol/
  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://api.zippopotam.us")
      .acceptHeader("application/json")
      .userAgentHeader(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  // Define scenario
  // Reference: https://docs.gatling.io/reference/script/core/scenario/
  private static final ScenarioBuilder scenarioForUsZipCode90210 =
          scenario("GET zip code data for US zip code 90210")
                  .exec(http("Data for US zip code 90210")
                          .get("/us/90210"));

  private static final ScenarioBuilder scenarioForCaZipCodeY1A =
          scenario("GET zip code data for CA zip code Y1A")
                  .exec(http("Data for CA zip code Y1A")
                          .get("/ca/Y1A"));

  // Define assertions
  // Reference: https://docs.gatling.io/reference/script/core/assertions/
  private static final Assertion noFailedRequests =
          global().failedRequests().count().lt(1L);

  // Define injection profile and execute the test
  // Reference: https://docs.gatling.io/reference/script/core/injection/
  {
    setUp(scenarioForUsZipCode90210.injectOpen(
            constantUsersPerSec(2).during(60).randomized()
            ),
            scenarioForCaZipCodeY1A.injectOpen(
                    constantUsersPerSec(1).during(60).randomized()
            ))
            .assertions(noFailedRequests)
            .protocols(httpProtocol);
  }
}
