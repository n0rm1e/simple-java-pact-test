package testproject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class UserServiceClient {
    private final String endpoint;

    public UserServiceClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public User getUser(long userId) {
        String url = endpoint + "/users/" + userId;
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        User response = target.request(MediaType.APPLICATION_JSON_TYPE).get(User.class);
        return response;
    }
}
