package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class RestPost extends BasePost{

    private DocumentReference authorId;
    private ArrayList<DocumentReference> categoryId;

    public RestPost(@Nullable String postId, String title, String content, String summary, String slug, String metaTitle, boolean published, boolean allowComments, @Nullable Timestamp createdAt, @Nullable Timestamp publishedAt, ArrayList<String> tags, DocumentReference authorId, ArrayList<DocumentReference> categoryId) {
        super(postId,title,content,summary,slug,metaTitle,published,allowComments,createdAt,publishedAt,tags);
        this.authorId = authorId;
        this.categoryId = categoryId;
    }

    public void setAuthorId(String author) {
        Firestore db = FirestoreClient.getFirestore();
        this.authorId = db.collection("User").document(author);
    }

    public void setCategoryId(ArrayList<String> categoryId) {
        Firestore db = FirestoreClient.getFirestore();
        this.categoryId = new ArrayList<>();
        for(String cat : categoryId) {
            this.categoryId.add(db.collection("Category").document(cat));
        }
    }
}
