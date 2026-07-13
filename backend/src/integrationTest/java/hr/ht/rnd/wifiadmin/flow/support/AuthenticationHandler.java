package hr.ht.rnd.wifiadmin.flow.support;

import org.springframework.boot.test.context.TestComponent;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestComponent
public class AuthenticationHandler {

    private final AuthenticationRequests requests;
    private final AuthenticationResponses responses;

    public AuthenticationHandler(
            AuthenticationRequests requests,
            AuthenticationResponses responses
    ) {
        this.requests = requests;
        this.responses = responses;
    }

    public String accessToken() throws Exception {
        var result = requests.login().andExpect(status().isOk());
        return responses.login(result).token();
    }

    public AuthenticationRequests requests() {
        return requests;
    }

    public AuthenticationResponses responses() {
        return responses;
    }
}
