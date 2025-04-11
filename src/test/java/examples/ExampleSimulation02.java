package examples;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ExampleSimulation02 extends Simulation {

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

  // Define injection profile and execute the test
  // Reference: https://docs.gatling.io/reference/script/core/injection/
  {
    setUp(scenario.injectOpen(
            nothingFor(4),
            atOnceUsers(10),
            rampUsers(10).during(5),
            constantUsersPerSec(20).during(15),
            constantUsersPerSec(20).during(15).randomized(),
            rampUsersPerSec(10).to(20).during(10),
            rampUsersPerSec(10).to(20).during(10).randomized(),
            stressPeakUsers(1000).during(20)
    ))
            .assertions(noFailedRequests)
            .protocols(httpProtocol);
  }
}
