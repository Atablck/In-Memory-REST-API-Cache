package com.example.demo.storage;

import java.util.Optional;

public class HybridHashTable implements KeyValueStore{

    @Override 
    public void put(String key, String value) {
        throw new UnsupportedOperationException("In progress...");
    }

    @Override 
    public Optional<String> get(String key) {
        throw new UnsupportedOperationException("In progress...");
    }

    @Override 
    public boolean remove(String key) {
        throw new UnsupportedOperationException("In progress...");
    }

    @Override 
    public int size() {
        return 0;
    }

    @Override 
    public void clear() {
        
    }
}
