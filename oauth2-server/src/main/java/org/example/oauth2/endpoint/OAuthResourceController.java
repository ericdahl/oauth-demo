package org.example.oauth2.endpoint;

import org.apache.commons.io.IOUtils;
import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.model.Token;
import org.example.oauth2.model.internal.ResolvedResource;
import org.example.oauth2.service.AuthTokenValidationService;
import org.example.oauth2.service.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
public class OAuthResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthResourceController.class);

    private final AuthTokenValidationService tokenValidationService;
    private final ResourceResolver resourceResolver;

    private final RestTemplate restTemplate;

    @Autowired
    public OAuthResourceController(final AuthTokenValidationService tokenValidationService,
                                   final ResourceResolver resourceResolver) {
        this.tokenValidationService = tokenValidationService;
        this.resourceResolver = resourceResolver;

        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });
    }

    @ResponseBody
    @RequestMapping("/go/**")
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       @RequestHeader("Authorization") final String authorizationHeader) throws Exception {

        final String requestUri = request.getRequestURI();
        LOGGER.info("Processing [{}] request for [{}]", request.getMethod(), requestUri);

        final Token token = tokenValidationService.validate(authorizationHeader);

        final ResolvedResource resolvedResource = resourceResolver.resolve(requestUri);
        validateAccess(token, resolvedResource);

        final String target = resolvedResource.getTarget();

        final HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());

        LOGGER.info("[{}] [{}]", httpMethod, target);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-username", token.getUsername());
        HttpEntity<String> requestEntity = new RequestEntity<>(headers, httpMethod, new URI(target));

        // FIXME: Don't look
        final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(target, httpMethod, requestEntity, byte[].class);
        for (Map.Entry<String, List<String>> entry : responseEntity.getHeaders().entrySet()) {
            if (HttpHeaders.CONTENT_TYPE.equals(entry.getKey())) {
                for (String headerValue : entry.getValue()) {

                    response.addHeader(entry.getKey(), headerValue);

                }
            }

        }
        response.setStatus(responseEntity.getStatusCode().value());
        IOUtils.write(responseEntity.getBody(), response.getOutputStream());
    }

    private void validateAccess(Token token, ResolvedResource resolvedResource) {
        token.getScopes();
        final String requiredScope = resolvedResource.getRequiredScope();

        if (requiredScope != null) {
            if (!token.getScopes().contains(requiredScope)) {
                throw new AuthorizationException("Not authorized to access resource with scope [ " + requiredScope + "]");
            }
        }
    }
}
