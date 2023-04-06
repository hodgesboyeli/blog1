package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Post extends BasePost{

    private User authorId;
    private ArrayList<Category> categoryId;

    public Post(@Nullable String postId, String title, String content, String summary, String slug, String metaTitle, boolean published, boolean allowComments, @Nullable Timestamp createdAt, @Nullable  Timestamp publishedAt, ArrayList<String> tags, User authorId, ArrayList<Category> categoryId) {
        super(postId,title,content,summary,slug,metaTitle,published,allowComments,createdAt,publishedAt,tags);
        this.authorId = authorId;
        this.categoryId = categoryId;
    }
}
