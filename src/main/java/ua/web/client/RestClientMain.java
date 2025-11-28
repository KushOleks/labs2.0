package ua.web.client;

import ua.model.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ua.web.client.JsonUtil.MAPPER;

public class RestClientMain {

    private static final String BASE = "http://localhost:8080/api/products";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        // 1) CREATE
        Product p = Product.create("Phone", 799.0, 7, java.time.LocalDate.of(2024,1,1));
        postJson("/", MAPPER.writeValueAsString(p));

        // 2) READ all
        get("/");

        // 3) READ one (за identity — у тебе це name)
        get("/Phone");

        // 4) UPDATE
        Product updated = Product.create("Phone", 899.0, 9, java.time.LocalDate.of(2024,1,1));
        putJson("/Phone", MAPPER.writeValueAsString(updated));

        // 5) DELETE
        delete("/Phone");

        // 6) READ all (порожній список або без “Phone”)
        get("/");
    }

    // ---------------- helpers ----------------

    private static void get(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .GET()
                .build();
        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        print("GET " + path, resp);
    }

    private static void postJson(String path, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        print("POST " + path, resp);
    }

    private static void putJson(String path, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        print("PUT " + path, resp);
    }

    private static void delete(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .DELETE()
                .build();
        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        print("DELETE " + path, resp);
    }

    private static void print(String title, HttpResponse<String> resp) {
        System.out.println("\n=== " + title + " | status " + resp.statusCode() + " ===");
        try {
            // prettify JSON if possible
            String body = resp.body();
            if (body != null && !body.isBlank() && body.trim().startsWith("{") || body.trim().startsWith("[")) {
                Object tree = MAPPER.readTree(body);
                System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
            } else {
                System.out.println(body);
            }
        } catch (Exception e) {
            System.out.println(resp.body());
        }
    }
}