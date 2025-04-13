package answers;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class AnswerSimulation03 extends Simulation {

  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("https://jsonplaceholder.typicode.com")
          .acceptHeader("application/json")
          .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  private static final ScenarioBuilder scenarioGetDataForUser1 =
          scenario("GET user data")
                  .exec(http("Data for user with ID 1")
                          .get("/users/1"));

  /*
  TODO: add a second scenario, one that first retrieves data for a post with ID 1 (GET /posts/1),
    then retrieves data for a comment with ID 4 (GET /comments/4)
    These calls should be made sequentially. Examples: https://docs.gatling.io/reference/script/core/scenario/#exec
   */
  private static final ScenarioBuilder scenarioGetDataForPost1ThenForComment4 =
          scenario("GET post and comment data")
                  .exec(http("Data for post with ID 1").get("/posts/1"),
                          http("Data for comment with ID 4").get("/comments/4"));

  private static final Assertion noFailedRequests =
          global().failedRequests().count().lt(1L);

  /*
  TODO: define two more assertions:
    - one that verifies that response times for all calls is under 1000 ms
    - one that verifies that the GET call to /comments/4 is made exactly 60 times
        Examples: https://docs.gatling.io/reference/script/core/assertions/#putting-it-all-together
   */
  private static final Assertion allResponseTimesUnder1000ms =
          global().responseTime().max().lt(1000);

  private static final Assertion commentsRetrieved60Times =
          details("Data for comment with ID 4").allRequests().count().is(60L);

  /*
  TODO: complete the test so that:
    - both scenarios are run with a workload model
        injecting 2 users per second for 30 seconds, at randomized intervals
    - all assertions are executed
    Run the test and inspect the report.
   */

  {
    setUp(scenarioGetDataForUser1.injectOpen(
            constantUsersPerSec(2).during(30).randomized()
            ),
            scenarioGetDataForPost1ThenForComment4.injectOpen(
                    constantUsersPerSec(2).during(30).randomized()
            ))
            .assertions(noFailedRequests,
                    allResponseTimesUnder1000ms,
                    commentsRetrieved60Times
            )
            .protocols(httpProtocol);
  }
}
