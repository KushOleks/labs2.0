package ua.util;

import org.junit.jupiter.api.Test;
import ua.model.Product;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataSerializerTest {

    @Test
    void testJsonSerializationAndDeserialization() {
        String path = "data/test_products.json";

        List<Product> products = List.of(
                new Product("Phone", 500, 10, LocalDate.now())
        );

        // serialize
        DataSerializer.toJSON(products, path);

        // file must exist
        assertTrue(new File(path).exists());

        // deserialize
        List<Product> restored = DataSerializer.fromJSON(path, Product.class);

        // same size
        assertEquals(products.size(), restored.size());

        // check values
        assertEquals(products.get(0).getName(), restored.get(0).getName());
        assertEquals(products.get(0).getPrice(), restored.get(0).getPrice());
        assertEquals(products.get(0).getStock(), restored.get(0).getStock());
        assertEquals(products.get(0).getCreatedDate(), restored.get(0).getCreatedDate());
    }
}