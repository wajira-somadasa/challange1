package com.weathermap.config.filters;

import com.weathermap.config.filters.ApiKeyRateLimitFilter;
import com.weathermap.exceptions.ApiKeyInvalidException;
import com.weathermap.service.ApiKeyRateLimiterService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ApiKeyRateLimitFilterTest {

    @Mock
    private ApiKeyRateLimiterService apiKeyRateLimiterService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private Bucket bucket;

    @Mock
    private ConsumptionProbe probe;

    private ApiKeyRateLimitFilter filter;

    @BeforeEach
    public void setUp() {
        filter = new ApiKeyRateLimitFilter(apiKeyRateLimiterService);
    }

    @Disabled
    @Test
    public void testDoFilterValidApiKeyAllowedRequest() throws IOException, ServletException {
        String apiKey = "validApiKey";
        when(request.getHeader("X-API-KEY")).thenReturn(apiKey);
        when(apiKeyRateLimiterService.resolveBucket(apiKey)).thenReturn(bucket);
        when(probe.isConsumed()).thenReturn(true);

        filter.doFilter(request, response, chain);

        Mockito.verify(chain).doFilter(request, response);
        Mockito.verify(apiKeyRateLimiterService).resolveBucket(apiKey);
        Mockito.verify(bucket).tryConsumeAndReturnRemaining(1);
        Mockito.verifyNoInteractions(response); // No response modifications expected
    }

    @Disabled
    @Test
    public void testDoFilterMissingApiKeyUnauthorized() throws IOException, ServletException {
        when(request.getHeader("X-API-KEY")).thenReturn(null);

        assertThrows(ApiKeyInvalidException.class, () -> filter.doFilter(request, response, chain));

        assertEquals(HttpStatus.UNAUTHORIZED.value(), ((HttpServletResponse) response).getStatus());
        Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        Mockito.verify(response).getWriter().write("Invalid API key");
        Mockito.verifyNoInteractions(apiKeyRateLimiterService, chain); // No service calls or chain execution expected
    }

    @Disabled
    @Test
    public void testDoFilterRateLimitExceededTooManyRequests() throws IOException, ServletException {
        String apiKey = "limitedApiKey";
        when(request.getHeader("X-API-KEY")).thenReturn(apiKey);
        when(apiKeyRateLimiterService.resolveBucket(apiKey)).thenReturn(bucket);
        when(probe.isConsumed()).thenReturn(false);
        when(probe.getNanosToWaitForRefill()).thenReturn(1000000000L); // 1 second refill time

        filter.doFilter(request, response, chain);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), ((HttpServletResponse) response).getStatus());
        Mockito.verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        Mockito.verify(response).getWriter().write("Hourly limit has been exceeded for the key: " + apiKey);
        Mockito.verifyNoInteractions(chain); // Chain execution not expected
        //Mockito.verify(response).setHeader("X-Rate-Limit-Retry-After-Seconds", "1");
    }
}