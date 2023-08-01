package eu.chrost.teefilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Component
@WebFilter(urlPatterns = "/*")
@Order(-999)
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(contentCachingRequestWrapper, response);
        performRequestAudit(contentCachingRequestWrapper);
    }

    private void performRequestAudit(ContentCachingRequestWrapper requestWrapper) {
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest((HttpServletRequest)requestWrapper.getRequest());
        log.info("\n Method:: {} \n URL:: {} \n Headers:: {} \n Request Body:: {}",
                servletServerHttpRequest.getMethod(),
                servletServerHttpRequest.getURI(),
                servletServerHttpRequest.getHeaders(),
                getPayLoadFromByteArray(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding()));
    }

    private String getPayLoadFromByteArray(byte[] requestBuffer, String charEncoding) {
        String payLoad = "";
        try {
            payLoad = new String(requestBuffer, charEncoding);
        } catch (UnsupportedEncodingException unex) {
            payLoad = "Unsupported-Encoding";
        }
        return payLoad;
    }


}
