package rebel.alliance.comm.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.Assert;
import rebel.alliance.comm.QuasarApplication;
import rebel.alliance.comm.dto.AllSatDataDTO;
import rebel.alliance.comm.dto.SatDataDTO;
import rebel.alliance.comm.dto.ShipDataDTO;
import rebel.alliance.comm.dto.SingleSatDataDTO;

import java.util.Arrays;

@SpringBootTest(classes = QuasarApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTests {
    private static final TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;

    @Test
    public void topSecretEndpoint_validData_shipDataIsReturned() {
        final SatDataDTO kenobiSatData = new SatDataDTO("kENOBI", 845, new String[] {"the", "message"});
        final SatDataDTO skywalkerSatData = new SatDataDTO("SKYWALKER", 500, new String[] {"", "message"});
        final SatDataDTO satoSatData = new SatDataDTO("SATO", 500, new String[] {"the", ""});
        final AllSatDataDTO allSatDataDTO = new AllSatDataDTO(Arrays.asList(kenobiSatData, skywalkerSatData, satoSatData));

        final HttpEntity<AllSatDataDTO> entity = new HttpEntity<>(allSatDataDTO, new HttpHeaders());
        final ResponseEntity<ShipDataDTO> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret"), HttpMethod.POST, entity, ShipDataDTO.class);

        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");
        Assert.isTrue(response.getBody() != null, "null response");
        Assert.isTrue(response.getBody().getX() == 92.53125, "invalid x position");
        Assert.isTrue(response.getBody().getY() == 414.9375, "invalid y position");
        Assert.isTrue(response.getBody().getMessage()[0].equals("the"), "invalid message");
        Assert.isTrue(response.getBody().getMessage()[1].equals("message"), "invalid message");
    }

    @Test
    public void topSecretEndpoint_distanceToASatelliteIsInvalid_shipDataIsNotReturned() {
        final SatDataDTO kenobiSatData = new SatDataDTO("kENOBI", -1000, new String[] {"the", "message"}); // invalid distance (negative)
        final SatDataDTO skywalkerSatData = new SatDataDTO("SKYWALKER", 500, new String[] {"", "message"});
        final SatDataDTO satoSatData = new SatDataDTO("SATO", 500, new String[] {"the", ""});
        final AllSatDataDTO allSatDataDTO = new AllSatDataDTO(Arrays.asList(kenobiSatData, skywalkerSatData, satoSatData));

        final HttpEntity<AllSatDataDTO> entity = new HttpEntity<>(allSatDataDTO, new HttpHeaders());
        final ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret"), HttpMethod.POST, entity, Object.class);

        Assert.isTrue(response.getStatusCode().value() == HttpStatus.BAD_REQUEST.value(), "status code is not 400");
    }

    @Test
    public void topScretEndpoint_dataFromOneSatelliteIsMissing_shipDataIsNotReturned() {
        final SatDataDTO kenobiSatData = new SatDataDTO("kENOBI", 845, new String[] {"the", "message"});
        final SatDataDTO skywalkerSatData = new SatDataDTO("SKYWALKER", 500, new String[] {"", "message"});
        final AllSatDataDTO allSatDataDTO = new AllSatDataDTO(Arrays.asList(kenobiSatData, skywalkerSatData));

        final HttpEntity<AllSatDataDTO> entity = new HttpEntity<>(allSatDataDTO, new HttpHeaders());
        final ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret"), HttpMethod.POST, entity, Object.class);

        Assert.isTrue(response.getStatusCode().value() == HttpStatus.BAD_REQUEST.value(), "status code is not 400");
    }

    @Test
    public void topSecretEndpoint_dataFromTwoSatelliteIsMissing_shipDataIsNotReturned() {
        final SatDataDTO kenobiSatData = new SatDataDTO("kENOBI", 845, new String[] {"the", "message"});
        final AllSatDataDTO allSatDataDTO = new AllSatDataDTO(Arrays.asList(kenobiSatData));

        final HttpEntity<AllSatDataDTO> entity = new HttpEntity<>(allSatDataDTO, new HttpHeaders());
        final ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret"), HttpMethod.POST, entity, Object.class);

        Assert.isTrue(response.getStatusCode().value() == HttpStatus.BAD_REQUEST.value(), "status code is not 400");
    }

    @Test
    public void topSecretEndpoint_wrongSatelliteName_shipDataIsNotReturned() {
        final SatDataDTO kenobiSatData = new SatDataDTO("UNKNOWN", 845, new String[] {"the", "message"});   // unknown satellite name
        final SatDataDTO skywalkerSatData = new SatDataDTO("SKYWALKER", 500, new String[] {"", "message"});
        final SatDataDTO satoSatData = new SatDataDTO("SATO", 500, new String[] {"the", ""});
        final AllSatDataDTO allSatDataDTO = new AllSatDataDTO(Arrays.asList(kenobiSatData, skywalkerSatData, satoSatData));

        final HttpEntity<AllSatDataDTO> entity = new HttpEntity<>(allSatDataDTO, new HttpHeaders());
        final ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret"), HttpMethod.POST, entity, Object.class);

        Assert.isTrue(response.getStatusCode().value() == HttpStatus.BAD_REQUEST.value(), "status code is not 400");
    }

    @Test
    public void topSecretSplitEndpoint_validData_shipDataIsReturned() {
        final SingleSatDataDTO kenobiSingleSatDataDTO = new SingleSatDataDTO(845, new String[] {"the", "message"});
        HttpEntity<SingleSatDataDTO> entity = new HttpEntity<>(kenobiSingleSatDataDTO, new HttpHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split/kENOBI"), HttpMethod.POST, entity, Object.class);
        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");

        final SingleSatDataDTO skywalkerSingleSatDataDTO = new SingleSatDataDTO(500, new String[] {"", "message"});
        entity = new HttpEntity<>(skywalkerSingleSatDataDTO, new HttpHeaders());
        response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split/SKYWALKER"), HttpMethod.POST, entity, Object.class);
        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");

        final SingleSatDataDTO satoSingleSatDataDTO = new SingleSatDataDTO(500, new String[] {"the", ""});
        entity = new HttpEntity<>(satoSingleSatDataDTO, new HttpHeaders());
        response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split/SATO"), HttpMethod.POST, entity, Object.class);
        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");

        final ResponseEntity<ShipDataDTO> getResponse = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split"), HttpMethod.GET, null, ShipDataDTO.class);
        Assert.isTrue(getResponse.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");
        Assert.isTrue(getResponse.getBody() != null, "null response");
        Assert.isTrue(getResponse.getBody().getX() == 92.53125, "invalid x position");
        Assert.isTrue(getResponse.getBody().getY() == 414.9375, "invalid y position");
        Assert.isTrue(getResponse.getBody().getMessage()[0].equals("the"), "invalid message");
        Assert.isTrue(getResponse.getBody().getMessage()[1].equals("message"), "invalid message");
    }

    @Test
    public void topSecretSplitEndpoint_dataForOneSatelliteIsMissing_shipDataIsNotReturned() {
        final SingleSatDataDTO kenobiSingleSatDataDTO = new SingleSatDataDTO(845, new String[] {"the", "message"});
        HttpEntity<SingleSatDataDTO> entity = new HttpEntity<>(kenobiSingleSatDataDTO, new HttpHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split/kENOBI"), HttpMethod.POST, entity, Object.class);
        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");

        final SingleSatDataDTO skywalkerSingleSatDataDTO = new SingleSatDataDTO(500, new String[] {"", "message"});
        entity = new HttpEntity<>(skywalkerSingleSatDataDTO, new HttpHeaders());
        response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split/SKYWALKER"), HttpMethod.POST, entity, Object.class);
        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");

        final ResponseEntity<Object> getResponse = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split"), HttpMethod.GET, null, Object.class);
        Assert.isTrue(getResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value(), "status code is not 404");
    }

    @Test
    public void topSecretSplitEndpoint_dataForTwoSatelliteIsMissing_shipDataIsNotReturned() {
        final SingleSatDataDTO kenobiSingleSatDataDTO = new SingleSatDataDTO(845, new String[] {"the", "message"});
        HttpEntity<SingleSatDataDTO> entity = new HttpEntity<>(kenobiSingleSatDataDTO, new HttpHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split/kENOBI"), HttpMethod.POST, entity, Object.class);
        Assert.isTrue(response.getStatusCode().value() == HttpStatus.OK.value(), "status code is not 200");

        final ResponseEntity<Object> getResponse = restTemplate.exchange(buildURL("/api/v1/comm/topsecret_split"), HttpMethod.GET, null, Object.class);
        Assert.isTrue(getResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value(), "status code is not 404");
    }

    private String buildURL(String uri) {
        return "http://localhost:" + port + uri;
    }
}