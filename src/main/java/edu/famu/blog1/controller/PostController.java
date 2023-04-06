package edu.famu.blog1.controller;

import com.google.api.client.util.Value;
import edu.famu.blog1.model.RestPost;
import edu.famu.blog1.service.PostService;
import edu.famu.blog1.util.ErrorMessage;
import edu.famu.blog1.util.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;
    @Value("${response.status}")
    private int statusCode;
    @Value("${response.name}")
    private String name;
    private Object payload;
    private ResponseWrapper response;
    private static final String CLASS_NAME = "PostService";

    public PostController(PostService postService) {
        this.postService = postService;
        payload = null;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllPosts(){
        try {
            payload = postService.getPosts();
            statusCode = 200;
            name = "posts";

        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch posts from database", CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String,Object>> getPostByUser(@PathVariable(name="userId") String id){
        try{
            payload = postService.getPostsByUser(id);
            statusCode = 200;
            name = "posts";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch posts for user " + id + " from database.",CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Map<String,Object>> getPostById(@PathVariable(name="postId") String id){
        try{
            payload = postService.getPostById(id);
            statusCode = 200;
            name = "post";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch post with id " + id + " from database.",CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @GetMapping("/c/{catId}")
    public ResponseEntity<Map<String,Object>> getPostByCategory(@PathVariable(name="catId") String id){
        try{
            payload = postService.getPostsByCategory(id);
            statusCode = 200;
            name = "posts";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch posts for category " + id + " from database.",CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @PostMapping("/")
    public ResponseEntity<Map<String,Object>> createPost(@RequestBody RestPost post){
        try{
            payload = postService.createPost(post);
            statusCode = 201;
            name = "postId";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot create new post in database.", CLASS_NAME, e.toString());
        }

        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Map<String,Object>> updatePost(@PathVariable(name="postId") String id, @RequestBody Map<String, Object> updateValues){


        try{

            postService.updatePost(id, updateValues);
            statusCode = 201;
            name = "message";
            payload = "Update successful for post with id " + id;

        }catch (ParseException e){
            statusCode = 400;
            payload = new ErrorMessage("Cannot parse JSON",CLASS_NAME, e.toString());
        }
        catch (Exception e) {
            payload = new ErrorMessage("Cannot update post with id " + id,CLASS_NAME, e.toString());
        }

        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String,Object>> removePost(@PathVariable(name="postId") String id){

        try{
            postService.deletePost(id);
            statusCode = 204;
            name = "message";
            payload = "Delete successful for post with id " + id;
        }catch (Exception e){
            payload = new ErrorMessage("Cannot delete post with id " + id, CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }


}

