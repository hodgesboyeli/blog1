package edu.famu.blog1.controller;

import com.google.api.client.util.Value;
import edu.famu.blog1.util.ErrorMessage;
import edu.famu.blog1.util.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.famu.blog1.service.CategoryService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController //identified this class a controller used for REST API class.
@RequestMapping("/api/category")//sets up the base resource url for all calls to methods in this file
public class CategoryController {

    CategoryService categoryService;

    @Value("${response.status}")
    private int statusCode;

    @Value("${response.name}")
    private String name;

    private Object payload;
    private ResponseWrapper response;
    private static final String CLASS_NAME = "CategoryService";

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        payload = null;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> getCategories(){
        try{
            payload = categoryService.getCategories();
            statusCode = 200;
            name = "categories";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch categories from database.",CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }
}
