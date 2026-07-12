package hr.ht.rnd.wifiadmin.flow;

import hr.ht.rnd.wifiadmin.domain.wifi.TestWifiConfigurations;
import hr.ht.rnd.wifiadmin.domain.wifi.WifiConfiguration;
import hr.ht.rnd.wifiadmin.flow.support.WifiConfigurationRequests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventFlowTest extends WifiConfigurationFlowTest {

    private static final String EVENTS_ENDPOINT = "/admin/events";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CONFIGURATIONS_CHANGED_EVENT = "event:configurations-changed";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Returns event stream")
    void should_ReturnEventStream_when_EventsAreSubscribed() throws Exception {
        subscribeToEvents()
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM))
                .andExpect(request().asyncStarted());
    }

    @Test
    @DisplayName("Emits configuration change event when configuration is updated")
    void should_EmitConfigurationChangeEvent_when_ConfigurationIsUpdated() throws Exception {
        var subscription = subscribeToEvents()
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();
        var configuration = TestWifiConfigurations.forCpeId("CPE_EVENT_UPDATE");

        updateConfiguration(configuration).andExpect(status().isOk());

        assertThat(subscription.getResponse().getContentAsString())
                .contains(CONFIGURATIONS_CHANGED_EVENT);
    }

    @Test
    @DisplayName("Emits configuration change event when configuration is retrieved from platform")
    void should_EmitConfigurationChangeEvent_when_ConfigurationIsRetrievedFromPlatform() throws Exception {
        var subscription = subscribeToEvents()
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();
        var configuration = TestWifiConfigurations.forCpeId("CPE_EVENT_RETRIEVE");

        platformClient.addConfiguration(configuration);
        wifi.requests().retrieveConfiguration(auth.accessToken(), configuration.cpeId())
                .andExpect(status().isOk());

        assertThat(subscription.getResponse().getContentAsString())
                .contains(CONFIGURATIONS_CHANGED_EVENT);
    }

    private ResultActions subscribeToEvents() throws Exception {
        return mockMvc.perform(get(EVENTS_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + auth.accessToken()));
    }

    private ResultActions updateConfiguration(WifiConfiguration configuration) throws Exception {
        return wifi.requests().updateConfiguration(
                auth.accessToken(),
                WifiConfigurationRequests.from(configuration)
        );
    }
}
