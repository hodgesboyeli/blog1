package edu.famu.blog1.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.blog1.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryService {

    private Firestore db = FirestoreClient.getFirestore();

    public ArrayList<Category> getCategories() throws ExecutionException, InterruptedException {

        Query query = db.collection("Category")
                .orderBy("title", Query.Direction.ASCENDING);

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents =
                future.get().getDocuments();

        ArrayList<Category> categories = (documents.size() > 0) ?

                new ArrayList<>() : null;

        for(QueryDocumentSnapshot doc : documents)
            categories.add(doc.toObject(Category.class));

        return categories;
    }
}
