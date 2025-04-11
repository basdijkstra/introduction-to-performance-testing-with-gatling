package answers;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class AnswerSimulation01 extends Simulation {

  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://jsonplaceholder.typicode.com")
      .acceptHeader("application/json")
      .userAgentHeader(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  private static final ScenarioBuilder scenario =
          scenario("GET user data")
                  .exec(http("Data for user with ID 1")
                          .get("/users/1"));

  /*
  TODO: Define the following assertions for this test:
    - check that the global number of requests made is greater than 59L (L for long)
    - check that the global percentage of successful tests is 100D (D for double)    -
    - check that the global response time is between 10 and 1000 milliseconds
   */
  private static final Assertion allRequestsSucceeded =
          global().successfulRequests().percent().is(100D);

  private static final Assertion numberOfRequestsMadeIsGreatherThan59 =
          global().allRequests().count().gt(59L);

  private static final Assertion minResponseTimeGreaterThan10ms =
          global().responseTime().min().gt(10);

  private static final Assertion maxResponseTimeLessThan1000ms =
          global().responseTime().max().lt(1000);

  {
    /*
    TODO: add the assertions you defined to the test and run it to see if they all pass.
     */

    setUp(scenario.injectOpen(constantUsersPerSec(2)
            .during(Duration.ofSeconds(30))))
            .assertions(
                    allRequestsSucceeded,
                    numberOfRequestsMadeIsGreatherThan59,
                    minResponseTimeGreaterThan10ms,
                    maxResponseTimeLessThan1000ms
            )
            .protocols(httpProtocol);
  }
}
