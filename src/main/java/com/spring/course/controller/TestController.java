package com.spring.course.controller;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    private int count;

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> map = new HashMap<>();
        map.put("message", "hello");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() throws IOException {
        Resource resource = new ClassPathResource("static/welcome.html");
        byte[] contentBytes = resource.getInputStream().readAllBytes();
        String content = new String(contentBytes, StandardCharsets.UTF_8);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(content);
    }

    @GetMapping("/hello/{name}")
    public ResponseEntity<Map<String, String>> helloWithName(@PathVariable String name) {
        Map<String, String> map = new HashMap<>();
        map.put("message", "hello, " + name);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/addition")
    public ResponseEntity<String> addition(@RequestParam("x") int x, @RequestParam("y") int y) {
        count++;
        if (count >= 10) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests, please try again later");
        }
        return ResponseEntity.ok("Svaret blir: " + (x + y) +  "\nAntalet ggr du ankallat denna endpoint: " + count);
    }
}
