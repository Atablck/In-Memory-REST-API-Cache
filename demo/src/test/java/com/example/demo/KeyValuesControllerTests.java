package com.example.demo;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Order;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc 
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KeyValuesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void testGetKeyNotFound() throws Exception {
        mockMvc.perform(get("/api/kv/missing_key"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void testPostKeyFound() throws Exception {
        mockMvc.perform(post("/api/kv")
                .param("key", "testKey")
                .param("value", "hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Saved: testKey"));
    }

    @Test 
    @Order(3)
    void testGetKeyFound() throws Exception {
        mockMvc.perform(get("/api/kv/testKey"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test 
    @Order(4)
    void testDeleteKeyFound() throws Exception {
        mockMvc.perform(delete("/api/kv/testKey"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    void testGetKeyAfterDelete() throws Exception {
        mockMvc.perform(get("/api/kv/testKey"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void testOverwriteKey() throws Exception {
        mockMvc.perform(post("/api/kv")
                .param("key", "overwrite_key")
                .param("value", "v1"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/kv")
                .param("key", "overwrite_key")
                .param("value", "v2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/kv/overwrite_key"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("v2"));
        
    }

    @Test 
    @Order(7)
    void testBadRequest() throws Exception {
        mockMvc.perform(post("/api/kv")
                .param("key", "invalid_param_key"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void testSaveAndRetrieveComplexValue() throws Exception {
        mockMvc.perform(post("/api/kv")
                .param("key", "comlex_key")
                .param("value", "{\"user\": \"Emir\", \"role\": \"Senior\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/kv/complex_key"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"user\": \"Emir\", \"role\": \"Senior\"}"));
    }

    
}
