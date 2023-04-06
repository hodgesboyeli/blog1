package edu.famu.blog1.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.protobuf.util.Timestamps;
import edu.famu.blog1.model.Category;
import edu.famu.blog1.model.Post;
import edu.famu.blog1.model.RestPost;
import edu.famu.blog1.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostService {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private Firestore db = FirestoreClient.getFirestore();

    private Post getPost(DocumentSnapshot doc) throws ExecutionException, InterruptedException {

        UserService userService = new UserService();
        User user = userService.getUser((DocumentReference) doc.get("authorId") );

        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<DocumentReference> refs = (ArrayList<DocumentReference>) doc.get("categoryId");
        for(DocumentReference ref : refs)
        {
            ApiFuture<DocumentSnapshot> catQuery = ref.get();
            DocumentSnapshot catDoc = catQuery.get();
            Category category = catDoc.toObject(Category.class);
            categories.add(category);
        }
        //logger.info(categories);

        return new Post(doc.getId(),doc.getString("title"),
                doc.getString("content"), doc.getString("summary"), doc.getString("slug"),
                doc.getString("metaTitle"), doc.getBoolean("published"),
                doc.getBoolean("allowComments"), doc.getTimestamp("createdAt"),
                doc.getTimestamp("publishedAt"), (ArrayList<String>) doc.get("tags"), user, categories);

    }


    public ArrayList<Post> getPosts() throws ExecutionException, InterruptedException {


        Query query = db.collection("Post");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        ArrayList<Post> posts = (documents.size() > 0) ?  new ArrayList<>() : null;

        for(QueryDocumentSnapshot doc : documents)
        {

            posts.add(getPost(doc));
        }

        return posts;
    }

    public ArrayList<Post> getPostsByUser(String userId) throws ExecutionException, InterruptedException {


        DocumentReference userRef = db.collection("User").document(userId);
        Query query = db.collection("Post")
                .whereEqualTo("authorId", userRef);

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        ArrayList<Post> posts = documents.size() > 0 ? new ArrayList<>() : null;
        for(QueryDocumentSnapshot doc : documents)
        {
            posts.add(getPost(doc));
        }

        return posts;
    }

    public ArrayList<Post> getPostsByCategory(String category) throws ExecutionException, InterruptedException {
        ArrayList<Post> posts = null;

        DocumentReference catRef = null;
        ApiFuture<QuerySnapshot> snap = db.collection("Category")
                .whereEqualTo("slug", category).get();

        List<QueryDocumentSnapshot> catDocs = snap.get().getDocuments();
        if(catDocs.size() > 0)
            catRef = catDocs.get(0).getReference();

        if(catRef != null) {
            Query query = db.collection("Post")
                    .whereArrayContains("categoryId", catRef);

            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            posts = documents.size() > 0 ? new ArrayList<>() : null;
            for (QueryDocumentSnapshot doc : documents) {
                posts.add(getPost(doc));
            }
        }
        return posts;
    }

    public Post getPostById(String postId) throws ExecutionException, InterruptedException {
        Post post = null;
        DocumentReference postDoc = db.collection("Post").document(postId);
        ApiFuture<DocumentSnapshot> future = postDoc.get();
        DocumentSnapshot doc = future.get();
        if(doc.exists())
            post = getPost(doc);

        return post;
    }

    public String createPost(RestPost post) throws ExecutionException, InterruptedException {
        String postId = null;

        ApiFuture<DocumentReference> future = db.collection("Post").add(post);
        DocumentReference postRef = future.get();
        postId = postRef.getId();

        return postId;
    }

    public void updatePost(String id, Map<String, Object> updateValues) throws ParseException {

        String [] allowed = {"allowComments", "categoryId", "content", "slug", "summary", "tags", "metaTitle", "title", "published", "publishedAt"};
        List<String> list = Arrays.asList(allowed);
        Map<String, Object> formattedValues = new HashMap<>();

        for(Map.Entry<String, Object> entry : updateValues.entrySet())
        {
            String key = entry.getKey();
            if(list.contains(key))
            {
                switch(key)
                {
                    case "publishedAt":
                        formattedValues.put(key, Timestamp.fromProto(Timestamps.parse((String) entry.getValue())));
                        break;
                    case "categoryId":
                        ArrayList<DocumentReference> catRefs = new ArrayList<>();
                        ArrayList<String> categories = (ArrayList<String>) entry.getValue();
                        for(String cat : categories)
                        {
                            DocumentReference catRef = db.collection("Category").document(cat);
                            if(catRef != null)
                                catRefs.add(catRef);
                        }
                        if(catRefs.size() > 0)
                            formattedValues.put(key, catRefs);
                        break;
                    default:
                        formattedValues.put(key, entry.getValue());
                        break;
                }


            }
        }
        //add update statement

    }
    public void deletePost(String postId){
        DocumentReference postDoc = db.collection("Post").document(postId);
        postDoc.delete();
    }

    public Post getPost(DocumentReference postRef) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = postRef.get().get();
        return getPost(doc);
    }

}