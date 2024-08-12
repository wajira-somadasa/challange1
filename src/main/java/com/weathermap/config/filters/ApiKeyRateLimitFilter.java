package com.weathermap.config.filters;

import com.weathermap.exceptions.ApiKeyInvalidException;
import com.weathermap.service.ApiKeyRateLimiterService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyRateLimitFilter implements Filter {

    private final ApiKeyRateLimiterService apikeyRateLimiterService;

    @Autowired
    public ApiKeyRateLimitFilter(ApiKeyRateLimiterService apiKeyService) {
        this.apikeyRateLimiterService = apiKeyService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, jakarta.servlet.FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String apiKey = httpRequest.getHeader("X-API-KEY");

        if (apiKey == null ) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.getWriter().write("Invalid API key");
            throw new ApiKeyInvalidException("Invalid API key : NULL");
        }

        Bucket bucket = apikeyRateLimiterService.resolveBucket(apiKey);
        //ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        //if (probe.isConsumed()) {
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            //long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

            HttpServletResponse  httpServletResponse = (HttpServletResponse) response;
            /* Http Status Code = 429 */
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

            //httpServletResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            //response.setContentType("text/plain");
            //response.getWriter().write("Hourly limit has been exceeded for the key: " + apiKey);
            throw new RuntimeException("Hourly limit has been exceeded for the key : " + apiKey);
        }
        return;
    }

    @Override
    public void destroy() {

    }
}