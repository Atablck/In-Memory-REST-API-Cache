package com.example.demo.storage;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HybridHashTable implements KeyValueStore{

    private static final int DEFAULT_CAPACITY = 16;
    private static final int TREEIFY_THRESHOLD = 8;
    private static final int UNTREEIFY_THRESHOLD = 6;
    private static final int NUM_STRIPES = 16;

    private Node<String, String>[] table;
    private final ReentrantReadWriteLock[] locks = new ReentrantReadWriteLock[NUM_STRIPES];
    private int size;

    

    public HybridHashTable() {
        this.table = (Node<String, String>[]) new Node[DEFAULT_CAPACITY];

        for (int i = 0; i <= NUM_STRIPES - 1; i++) {
            locks[i] = new ReentrantReadWriteLock();
        }
    }


    private static class Node<K, V> {
        final K key;
        V value;
        final int hash;


        Node<K, V> next;

        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;
        int height = 1;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }




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
        return this.size;
    }

    @Override 
    public void clear() {

    }
}
