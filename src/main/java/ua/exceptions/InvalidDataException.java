package ua.exceptions;

import java.util.Collections;
import java.util.List;

public class InvalidDataException extends RuntimeException {

    private final List<String> errors;

    public InvalidDataException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = List.copyOf(errors);
    }

    public InvalidDataException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
