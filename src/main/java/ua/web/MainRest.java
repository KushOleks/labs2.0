package ua.web;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ua.model.Product;
import ua.service.ProductService;
import ua.util.GenericRepository;
import ua.util.IdentityExtractor;

import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

import static ua.web.JsonUtil.MAPPER;

public class MainRest {
    private static final Logger log = Logger.getLogger(MainRest.class.getName());

    public static void main(String[] args) throws Exception {
        // 1) In-memory repo + сервіс
        IdentityExtractor<Product> id = Product::getName;
        GenericRepository<Product> repo = new GenericRepository<>(id);
        ProductService productService = new ProductService(repo);

        // 2) Початкове завантаження з JSON (файли з твоєї папки data/)
        //    Якщо файл відсутній/порожній — просто стартуємо з пустим репозиторієм
        String dataFile = Paths.get("data", "products_concurrency.json").toString();
        try {
            List<Product> initial = List.of(MAPPER.readValue(
                    Paths.get(dataFile).toFile(),
                    MAPPER.getTypeFactory().constructArrayType(Product.class)
            ));
            repo.addAll(initial); // див. метод нижче (ми додамо його у твій GenericRepository)
            log.info("Loaded products: " + initial.size());
        } catch (Exception e) {
            log.warning("No initial products loaded (" + e.getMessage() + ")");
        }

        // 3) Jetty + servlet context
        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctx.setContextPath("/");

        // 4) Фільтр логування
        ctx.addFilter(RequestLoggingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        // 5) Реєструємо сервлети під /api
        ctx.addServlet(ProductServlet.class, "/api/products/*")
                .setInitParameter("bean", "productService");
        // кладемо сервіс у context, щоб сервлет дістав його
        ctx.setAttribute("productService", productService);

        // 6) Запуск
        server.setHandler(ctx);
        log.info("Starting Jetty on http://localhost:8080 …");
        server.start();
        server.join();
    }
}