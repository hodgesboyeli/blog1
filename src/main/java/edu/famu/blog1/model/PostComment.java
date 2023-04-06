package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostComment extends BasePostComment{
    private Post postId;
    private User authorId;

    public PostComment(@Nullable String commentId, String content, String title, boolean published, Timestamp createdAt, Timestamp publishedAt, User authorId, @Nullable Post postId) {
        super(commentId,content,title,published,createdAt,publishedAt);
        this.postId = postId;
        this.authorId = authorId;
    }
}
