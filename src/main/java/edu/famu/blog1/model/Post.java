package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Post extends BasePost {
   private User authorId;
   private ArrayList<Category> categoryId;

    public Post(String postId, String title, String content, String summary, String slug, String metaTitle, boolean published, boolean allowComments, Timestamp createAt, Timestamp publishedAt, ArrayList<String> tags, User authorId, ArrayList<Category> categoryId) {
        super(postId, title, content, summary, slug, metaTitle, published, allowComments, createAt, publishedAt, tags);
        this.authorId = authorId;
        this.categoryId = categoryId;
    }
}
