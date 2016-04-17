package org.example.oauth2.endpoint;

import org.apache.commons.io.IOUtils;
import org.example.oauth2.exception.ResourceNotFoundException;
import org.example.oauth2.model.Token;
import org.example.oauth2.model.internal.ResourceConfig;
import org.example.oauth2.service.AuthTokenValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class OAuthResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthResourceController.class);

    private final ResourceConfig resourceConfig;
    private final AuthTokenValidationService tokenValidationService;

    private RestTemplate restTemplate = new RestTemplate(); // TODO: configure

    @Autowired
    public OAuthResourceController(ResourceConfig resourceConfig, AuthTokenValidationService tokenValidationService) {
        this.resourceConfig = resourceConfig;
        this.tokenValidationService = tokenValidationService;
    }

    @ResponseBody
    @RequestMapping("/go/**")
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       @RequestHeader("Authorization") final String authorizationHeader) throws Exception {

        final String requestUri = request.getRequestURI();
        LOGGER.info("Processing [{}] request for [{}]", request.getMethod(), requestUri);

        final Token token = tokenValidationService.validate(authorizationHeader);

        final Optional<String> target = resourceConfig.findTarget(requestUri);
        if (target.isPresent()) {
            final String url = target.get();
            final HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());

            LOGGER.info("[{}] [{}]", httpMethod, url);
            HttpEntity<String> requestEntity = new RequestEntity<>(httpMethod, new URI(target.get()));

            // FIXME
            // Don't look
            final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(target.get(), httpMethod, requestEntity, byte[].class);
            for (Map.Entry<String, List<String>> entry : responseEntity.getHeaders().entrySet()) {
                if (HttpHeaders.CONTENT_TYPE.equals(entry.getKey())) {
                    for (String headerValue : entry.getValue()) {

                        response.addHeader(entry.getKey(), headerValue);

                    }
                }

            }
            IOUtils.write(responseEntity.getBody(), response.getOutputStream());


        } else {
            throw new ResourceNotFoundException("No target for path [" + request.getRequestURI() + "]");
        }
    }
}
