package ua.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ua.model.Product;
import ua.util.DataSerializer;
import ua.util.GenericRepository;
import ua.web.servlet.PingServlet;
import ua.web.servlet.ProductServlet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class MainRest {

    public static final int PORT = 8080;
    public static final String API_BASE = "/api";

    public static final String PRODUCTS_FILE = "data/products.json";

    public static final GenericRepository<Product> PRODUCT_REPO =
            new GenericRepository<>(Product::getName);

    public static void main(String[] args) throws Exception {

        ensureDataDir();
        List<Product> fromFile = DataSerializer.fromJSON(PRODUCTS_FILE, Product.class);
        if (fromFile == null || fromFile.isEmpty()) {
            fromFile = List.of(
                    Product.create("Phone", 500, 10, LocalDate.of(2024, 1, 1)),
                    Product.create("TV", 1500, 5, LocalDate.of(2023, 5, 10))
            );
            DataSerializer.toJSON(fromFile, PRODUCTS_FILE); // одразу створимо файл
        }
        PRODUCT_REPO.clear();
        PRODUCT_REPO.addAll(fromFile);

        Server server = new Server(PORT);
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctx.setContextPath("/");
        server.setHandler(ctx);

        ctx.setAttribute("productRepo", PRODUCT_REPO);
        ctx.setAttribute("productDataFile", PRODUCTS_FILE);

        ctx.addServlet(PingServlet.class, API_BASE + "/ping");            // GET /api/ping
        ctx.addServlet(ProductServlet.class, API_BASE + "/products/*");   // CRUD /api/products

        System.out.println("Jetty started at http://localhost:" + PORT + API_BASE);
        try {
            server.start();
            server.join();
        } finally {
            server.stop();
        }
    }

    private static void ensureDataDir() throws Exception {
        Path dir = Path.of("data");
        if (!Files.exists(dir)) Files.createDirectories(dir);
    }
}