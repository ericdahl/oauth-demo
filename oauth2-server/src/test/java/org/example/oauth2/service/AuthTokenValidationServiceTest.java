package org.example.oauth2.service;

import org.example.oauth2.exception.MissingAuthorizationHeaderException;
import org.example.oauth2.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class AuthTokenValidationServiceTest {


    private static final Token GOOD_TOKEN = Mockito.mock(Token.class);

    private AuthTokenValidationService service;

    @Before
    public void setUp() throws Exception {
        final TokenService mockTokenService = Mockito.mock(TokenService.class);
        service = new AuthTokenValidationService(mockTokenService);

        when(mockTokenService.validate("123")).thenReturn(GOOD_TOKEN);
    }

    @Test
    public void shouldParseGoodHeader() throws Exception {
        assertThat(service.validate("Bearer 123"), is(GOOD_TOKEN));
    }

    @Test(expected = MissingAuthorizationHeaderException.class)
    public void shouldGiveErrorForMissingHeader() throws Exception {
        service.validate(null);
    }

    @Test(expected = MissingAuthorizationHeaderException.class)
    public void shouldGiveErrorForMissingToken() throws Exception {
        service.validate("Bearer");
    }

    @Test(expected = MissingAuthorizationHeaderException.class)
    public void shouldGiveErrorForMalformedToken() throws Exception {
        service.validate("Bear one");
    }

    @Test(expected = MissingAuthorizationHeaderException.class)
    public void shouldGiveErrorForMalformedTokenWithExtraStuff() throws Exception {
        service.validate("Bearer 1234 567");
    }

}