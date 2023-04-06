package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class RestPostComment extends BasePostComment{

    private DocumentReference authorId;
    private DocumentReference postId;

    public RestPostComment(@Nullable String commentId, String content, String title, boolean published, Timestamp createdAt, Timestamp publishedAt, DocumentReference authorId, DocumentReference postId) {
        super(commentId, content, title,published, createdAt,publishedAt);
        this.authorId = authorId;
        this.postId = postId;
    }
    public void setAuthorId(String author) {
        Firestore db = FirestoreClient.getFirestore();
        this.authorId = db.collection("User").document(author);
    }

    public void setPostId(String post) {
        Firestore db = FirestoreClient.getFirestore();
        this.postId = db.collection("Post").document(post);
    }
}
