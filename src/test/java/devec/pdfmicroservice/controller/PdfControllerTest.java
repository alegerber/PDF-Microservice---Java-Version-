package devec.pdfmicroservice.controller;

import devec.pdfmicroservice.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class PdfControllerTest extends AbstractIntegrationTest {
    private static final String STREAM_URL = "http://localhost:8080/pdf/rendering/stream/";

    @Test
    void testStream() {
        if (!testConnection(STREAM_URL)) {
            System.out.println("skipp test no connection to localhost");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        String html = "<!DOCTYPE html><html><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";

        RestTemplate restTemplate       = new RestTemplate();
        HttpEntity<String> httpEntity   = new HttpEntity<>(html, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(STREAM_URL, httpEntity, String.class);
        assertThat(response).isNotNull();
        String body = response.getBody();
        assertThat(body).isNotNull();
    }
}
