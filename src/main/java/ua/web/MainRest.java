package ua.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ua.web.servlet.PingServlet;
import ua.web.servlet.ProductServlet;
import ua.util.GenericRepository;
import ua.model.Product;

public class MainRest {

    public static final int PORT = 8080;
    public static final String API_BASE = "/api";

    public static final GenericRepository<Product> PRODUCT_REPO =
            new GenericRepository<>(Product::getName);

    public static void main(String[] args) throws Exception {
        PRODUCT_REPO.add(Product.create("Phone", 500, 10, java.time.LocalDate.of(2024,1,1)));
        PRODUCT_REPO.add(Product.create("TV", 1500, 5, java.time.LocalDate.of(2023,5,10)));

        Server server = new Server(PORT);
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctx.setContextPath("/");
        server.setHandler(ctx);

        ctx.setAttribute("productRepo", PRODUCT_REPO);

        ctx.addServlet(PingServlet.class, API_BASE + "/ping");           // GET
        ctx.addServlet(ProductServlet.class, API_BASE + "/products/*");  // CRUD

        System.out.println("Jetty started at http://localhost:" + PORT + API_BASE);
        try {
            server.start();
            server.join();
        } finally {
            server.stop();
        }
    }
}
