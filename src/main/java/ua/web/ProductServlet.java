package ua.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import ua.model.Product;
import ua.service.ProductService;

import static ua.web.JsonUtil.MAPPER;

public class ProductServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(ProductServlet.class.getName());
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // дістаємо із контексту, який поклав MainRest
        this.productService = (ProductService) config.getServletContext().getAttribute("productService");
        if (productService == null) {
            throw new ServletException("ProductService not found in context");
        }
        log.info("ProductServlet initialized");
    }

    // GET /api/products         -> список
    // GET /api/products/{name}  -> один елемент
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo(); // may be null or "/{name}"
        if (path == null || "/".equals(path)) {
            List<Product> all = productService.findAll();
            MAPPER.writeValue(resp.getOutputStream(), all);
            return;
        }
        String name = path.substring(1);
        Product p = productService.findByName(name);
        if (p == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            MAPPER.writeValue(resp.getOutputStream(), new ErrorDto("Product not found"));
        } else {
            MAPPER.writeValue(resp.getOutputStream(), p);
        }
    }

    // POST /api/products  (body: Product JSON)
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Product p = MAPPER.readValue(req.getInputStream(), Product.class);
            productService.add(p);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            MAPPER.writeValue(resp.getOutputStream(), p);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getOutputStream(), new ErrorDto(e.getMessage()));
        }
    }

    // PUT /api/products/{name}
    @Override protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getOutputStream(), new ErrorDto("Name is required in path"));
            return;
        }
        String name = path.substring(1);
        try {
            Product updated = MAPPER.readValue(req.getInputStream(), Product.class);
            Product result = productService.update(name, updated);
            if (result == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                MAPPER.writeValue(resp.getOutputStream(), new ErrorDto("Product not found"));
            } else {
                MAPPER.writeValue(resp.getOutputStream(), result);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getOutputStream(), new ErrorDto(e.getMessage()));
        }
    }

    // DELETE /api/products/{name}
    @Override protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getOutputStream(), new ErrorDto("Name is required in path"));
            return;
        }
        String name = path.substring(1);
        boolean removed = productService.delete(name);
        if (!removed) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            MAPPER.writeValue(resp.getOutputStream(), new ErrorDto("Product not found"));
        } else {
            MAPPER.writeValue(resp.getOutputStream(), new StatusDto("deleted"));
        }
    }

    // DTO для помилок/статусів
    public record ErrorDto(String error) {}
    public record StatusDto(String status) {}
}