package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestPostComment extends BasePostComment {
    private DocumentReference postId;
    private DocumentReference authorId;

    public RestPostComment(String commentId, String content, Timestamp createdAt, boolean published, Timestamp publishedAt, String title, DocumentReference postId, DocumentReference authorId) {
        super(commentId, content, createdAt, published, publishedAt, title);
        this.postId = postId;
        this.authorId = authorId;
    }

    public void setAuthorId(String authorId) {
        Firestore db = FirestoreClient.getFirestore();
        this.authorId = db.collection("User").document(authorId);
    }

    public void setPostId(String postId) {
        Firestore db = FirestoreClient.getFirestore();
        this.authorId = db.collection("Post").document(postId);
    }

}
