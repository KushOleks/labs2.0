package ua.util;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String extractIdentity(T obj);
}