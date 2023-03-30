package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostComment extends BasePostComment {
    private Post postId;
    private Post authorId;

    public PostComment(String commentId, String content, Timestamp createdAt, boolean published, Timestamp publishedAt, String title, Post postId, Post authorId) {
        super(commentId, content, createdAt, published, publishedAt, title);
        this.postId = postId;
        this.authorId = authorId;
    }
}
