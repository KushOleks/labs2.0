package ua.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class RequestLoggingFilter implements Filter {
    private static final Logger log = Logger.getLogger(RequestLoggingFilter.class.getName());

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        long t0 = System.currentTimeMillis();
        try {
            chain.doFilter(req, res);
        } finally {
            long dt = System.currentTimeMillis() - t0;
            log.info(r.getMethod() + " " + r.getRequestURI() + " [" + dt + " ms]");
        }
    }
}