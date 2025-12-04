package ua.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.model.Product;
import ua.util.GenericRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProductServlet extends HttpServlet {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private GenericRepository<Product> repo(HttpServletRequest req) {
        return (GenericRepository<Product>) req.getServletContext().getAttribute("productRepo");
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
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"not found\"}");
            } else {
                MAPPER.writeValue(resp.getWriter(), p);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Product p = MAPPER.readValue(req.getInputStream(), Product.class);
        if (repo(req).findByIdentity(p.getName()) != null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"already exists\"}");
            return;
        }
        repo(req).add(p);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        MAPPER.writeValue(resp.getWriter(), p);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"param 'name' required\"}");
            return;
        }
        GenericRepository<Product> r = repo(req);
        Product existed = r.findByIdentity(name);
        if (existed == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"not found\"}");
            return;
        }
        Product updated = MAPPER.readValue(req.getInputStream(), Product.class);
        r.remove(existed);
        r.add(updated);
        MAPPER.writeValue(resp.getWriter(), updated);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"param 'name' required\"}");
            return;
        }
        GenericRepository<Product> r = repo(req);
        Product p = r.findByIdentity(name);
        if (p == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"not found\"}");
            return;
        }
        r.remove(p);
        resp.getWriter().write("{\"deleted\":\"" + name + "\"}");
    }
}