package testproject;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Rule;
import org.junit.Test;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserServiceClientTest {
    private final int testUserId = 1;
    private final User testUser = new User("Bob", "Smith");

    @Rule
    // Wrap all the tests with PactVerification annotation
    public PactProviderRule provider = new PactProviderRule("my-provider-name", "localhost", 8080, this);

    @Test
    // Set up a mock server for the test
    @PactVerification (fragment = "getUserFragment")
    public void shouldGetUsers() {
        UserServiceClient client = new UserServiceClient("http://localhost:8080");
        User user = client.getUser(testUserId);
        assertThat(user, is(testUser));
    }

    // Describe the interaction between a provider and a consumer
    @Pact (consumer = "my-consumer-name", provider = "my-provider-name")
    public PactFragment getUserFragment(PactDslWithProvider builder) throws JsonProcessingException {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String testUserJsonString = objectMapper.writeValueAsString(testUser);

        return builder
                .uponReceiving("A request for user " + testUserId)
                .path("/users/" + testUserId)
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(responseHeaders)
                .body(testUserJsonString)
                .toFragment();
    }
}
