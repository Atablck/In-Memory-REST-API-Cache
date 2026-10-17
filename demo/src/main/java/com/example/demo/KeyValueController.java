package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/kv")
public class KeyValueController {

    private final Map<String, String> storage = new ConcurrentHashMap<>();

    @PostMapping
    public ResponseEntity<String> set(@RequestParam String key, @RequestParam String value) {
        storage.put(key, value);
        return ResponseEntity.ok("Saved: " + key);
    }

    
    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key) {
        String value = storage.get(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        if (storage.remove(key) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}