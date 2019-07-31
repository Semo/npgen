package dev.semo.npgen.service;

import dev.semo.npgen.model.CamImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PostClient {

    @Value("${target.uri}")
    private String uri;

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    public PostClient() {}

    public HttpStatus postNumberPlate(CamImage camImage) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("numplate", camImage.getIdentifier());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String,
                        Object>>(map, headers);

        ByteArrayResource resource = new ByteArrayResource(camImage.getData()) {
            /**
             * IMPORTANT! Otherwise I receive a BAD REQUEST
             * @return
             */
            @Override
            public String getFilename() {
                return camImage.getIdentifier() + ".png";
            }
        };
        map.add("image", resource);

        System.out.println(this.uri);

        ResponseEntity<String> result = restTemplate.exchange(this.uri,
                HttpMethod.POST,
                requestEntity, String.class);

        return result.getStatusCode();
    }
}