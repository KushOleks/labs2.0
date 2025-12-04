package ua.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.model.Product;
import ua.util.DataSerializer;
import ua.util.GenericRepository;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ProductServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ProductServlet.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    /* ---------- lifecycle logging ---------- */
    @Override
    public void init() throws ServletException {
        log.info(getClass().getSimpleName() + " init()");
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req.getMethod() + " " + req.getRequestURI() +
                (req.getQueryString() != null ? ("?" + req.getQueryString()) : ""));
        super.service(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getClass().getSimpleName() + " destroy()");
        super.destroy();
    }
    /* -------------------------------------- */

    private GenericRepository<Product> repo(HttpServletRequest req) {
        return (GenericRepository<Product>) req.getServletContext().getAttribute("productRepo");
    }

    private String dataFile(HttpServletRequest req) {
        return (String) req.getServletContext().getAttribute("productDataFile");
    }

    private void persist(HttpServletRequest req) {
        DataSerializer.toJSON(repo(req).getAll(), dataFile(req));
    }

    private static void jsonError(HttpServletResponse resp, int code, String msg) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json; charset=UTF-8");
        MAPPER.writeValue(resp.getWriter(), java.util.Map.of("error", msg));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String name = req.getParameter("name");
        if (name == null) {
            List<Product> all = repo(req).getAll();
            MAPPER.writeValue(resp.getWriter(), all);
        } else {
            Product p = repo(req).findByIdentity(name);
            if (p == null) {
                jsonError(resp, HttpServletResponse.SC_NOT_FOUND, "not found");
            } else {
                MAPPER.writeValue(resp.getWriter(), p);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Product p = MAPPER.readValue(req.getInputStream(), Product.class);
            if (p.getName() == null || p.getName().isBlank()) {
                jsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "name is required");
                return;
            }
            if (repo(req).findByIdentity(p.getName()) != null) {
                jsonError(resp, HttpServletResponse.SC_CONFLICT, "already exists");
                return;
            }
            repo(req).add(p);
            persist(req);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json; charset=UTF-8");
            MAPPER.writeValue(resp.getWriter(), p);
        } catch (Exception ex) {
            jsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "invalid JSON: " + ex.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name == null || name.isBlank()) {
            jsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "param 'name' required");
            return;
        }
        Product existed = repo(req).findByIdentity(name);
        if (existed == null) {
            jsonError(resp, HttpServletResponse.SC_NOT_FOUND, "not found");
            return;
        }
        try {
            Product updated = MAPPER.readValue(req.getInputStream(), Product.class);
            // upsert/replace
            repo(req).remove(existed);
            repo(req).add(updated);
            persist(req);
            resp.setContentType("application/json; charset=UTF-8");
            MAPPER.writeValue(resp.getWriter(), updated);
        } catch (Exception ex) {
            jsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "invalid JSON: " + ex.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name == null || name.isBlank()) {
            jsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "param 'name' required");
            return;
        }
        Product p = repo(req).findByIdentity(name);
        if (p == null) {
            jsonError(resp, HttpServletResponse.SC_NOT_FOUND, "not found");
            return;
        }
        repo(req).remove(p);
        persist(req);
        resp.setContentType("application/json; charset=UTF-8");
        MAPPER.writeValue(resp.getWriter(), java.util.Map.of("deleted", name));
    }
}