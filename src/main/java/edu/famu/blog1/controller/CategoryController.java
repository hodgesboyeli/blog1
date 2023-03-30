package edu.famu.blog1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.famu.blog1.service.CategoryService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> getCategories() {
        Map<String,Object> returnVal = new HashMap<>();
        int statusCode = 500;

        try {
            Object payload = categoryService.getCategories();
            statusCode = 200;
            returnVal.put("categories", payload);
        }
        catch (ExecutionException | InterruptedException e) {
            returnVal.put("error", e.getStackTrace());
        }
        return ResponseEntity.status(statusCode).body(returnVal);
    }
}
