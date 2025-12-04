package ua.web.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class BaseServlet extends HttpServlet {
    protected final Logger log = Logger.getLogger(getClass().getName());

    @Override public void init() throws ServletException {
        log.info(getClass().getSimpleName() + " init()");
        super.init();
    }
    @Override protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req.getMethod() + " " + req.getRequestURI());
        super.service(req, resp);
    }
    @Override public void destroy() {
        log.info(getClass().getSimpleName() + " destroy()");
        super.destroy();
    }
}