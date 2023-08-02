package eu.chrost.teefilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
@Order(1)
@Slf4j
public class TeeingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().contains("old")) {
            ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
            filterChain.doFilter(contentCachingRequestWrapper, response);
            ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest((HttpServletRequest) contentCachingRequestWrapper.getRequest());
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
            ResponseEntity<String> exchange = restTemplate.exchange(servletServerHttpRequest.getURI().toString().replace("old", "new"),
                    servletServerHttpRequest.getMethod(),
                    new HttpEntity<>(contentCachingRequestWrapper.getContentAsByteArray(), servletServerHttpRequest.getHeaders()),
                    String.class,
                    request.getParameterMap());
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
