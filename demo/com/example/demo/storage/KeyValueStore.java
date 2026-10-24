package com.example.demo.storage;

import java.util.Optional;

public interface KeyValueStore {
    void put(String key, String value);

    Optional<String> get(String key);

    boolean remove(String key);

    int size();

    void clear();
}
