package answers;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class AnswerSimulation02 extends Simulation {

  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://jsonplaceholder.typicode.com")
          .acceptHeader("application/json")
          .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  private static final ScenarioBuilder scenario =
          scenario("GET user data")
                  .exec(http("Data for user with ID 1")
                          .get("/users/1"));

  private static final Assertion noFailedRequests =
          global().failedRequests().count().lt(1L);

  private static final Assertion numberOfRequestsIs60 =
          global().allRequests().count().is(60L);

  /*
  TODO: run the same test three times, with a different workload model for each run:
    - first, inject 2 users per second for 30 seconds, at randomized intervals
    - then, inject 2 users per second for 30 seconds at regular intervals
    - then, inject 60 users distributed evenly across time over 30 seconds
    Inspect the report for each test run and compare the results.
    Pay special attention to the user rate and the assertions.
   */

  {
    setUp(scenario.injectOpen(
            // constantUsersPerSec(2).during(30).randomized()
            // constantUsersPerSec(2).during(30)
            // rampUsers(60).during(30)
    ))
            .assertions(noFailedRequests, numberOfRequestsIs60)
            .protocols(httpProtocol);
  }
}
