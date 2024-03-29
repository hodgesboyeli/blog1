package edu.famu.blog1.controller;

import com.google.api.client.util.Value;
import edu.famu.blog1.model.RestPostComment;
import edu.famu.blog1.service.PostCommentService;
import edu.famu.blog1.util.ErrorMessage;
import edu.famu.blog1.util.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/comment")
public class PostCommentController {

    PostCommentService postCommentService;
    @Value("${response.status}")
    private int statusCode;
    @Value("${response.name}")
    private String name;
    private Object payload;
    private ResponseWrapper response;
    private static final String CLASS_NAME = "CommentService";

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
        payload = null;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Map<String,Object>> getCommentByPostId(@PathVariable(name="postId") String id){
        try{
            payload = postCommentService.getComments(id);
            statusCode = 200;
            name = "comments";
        } catch (ExecutionException | InterruptedException e) {
            payload = new ErrorMessage("Cannot fetch comments for post with id " + id + " from database.",CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String,Object>> removeComment(@PathVariable(name="commentId") String id){

        try{
            postCommentService.deleteComment(id);
            statusCode = 204;
            name = "message";
            payload = "Delete successful for comment with id " + id;
        }catch (Exception e){
            payload = new ErrorMessage("Cannot delete comment with id " + id, CLASS_NAME, e.toString());
        }
        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

    @PostMapping("/")
    public ResponseEntity<Map<String,Object>> createComment(@RequestBody RestPostComment comment){
        try{

            payload = postCommentService.createComment(comment);
            statusCode = 201;
            name = "commentId";

        }
        catch (Exception e) {
            payload = new ErrorMessage("Cannot create comment in database" ,CLASS_NAME, e.toString());
        }

        response = new ResponseWrapper(statusCode,name, payload);

        return response.getResponse();
    }

}
