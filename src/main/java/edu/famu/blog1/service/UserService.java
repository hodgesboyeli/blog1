package edu.famu.blog1.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.blog1.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private Firestore db = FirestoreClient.getFirestore();

    /**
     * getUsers
     * @return ArrayList<User>
     */
    public ArrayList<User>getUsers() throws ExecutionException, InterruptedException {
        Query query = db.collection("User");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        ArrayList<User> users = (documents.size() > 0) ? new ArrayList<>() : null;

        for (QueryDocumentSnapshot doc : documents)
            users.add(doc.toObject(User.class));

        return users;


    }

    /**
     * getUserById
     * @param String id
     * @return User
     */

    public User getUserById(String id) throws ExecutionException, InterruptedException {
        User user = null;

        DocumentReference doc = db.collection("User").document(id);
        ApiFuture<DocumentSnapshot> future = doc.get();
        user = future.get().toObject(User.class);

        return user;
    }

    /**
     * createUser
     * @param User
     * @return String
     */

    public String createUser(User user) throws ExecutionException, InterruptedException {

        String userId = null;
        user.setRegisteredAt(Timestamp.now());

        ApiFuture<DocumentReference> future = db.collection("User").add(user);
        DocumentReference userRef = future.get();
        userId = userRef.getId();

        return userId;
    }

    /**
     * updateUser
     * @param String id
     * @param Map<String,String> updateValues
     * @return none
     */

    public void updateUser(String id, Map<String, String> updateValues) {
        String [] allowed = {"firstname", "lastname", "middlename", "intro",
                                "mobile", "profile"};

        List<String> allowList = Arrays.asList(allowed);
        Map<String, Object> formattedValues = new HashMap<>();

        for(Map.Entry<String, String> entry: updateValues.entrySet())
        {
            String key = entry.getKey();
            if(allowList.contains(key))
                formattedValues.put(key, entry.getValue());
        }
        DocumentReference userDoc = db.collection("User").document(id);
        userDoc.update(formattedValues);
    }

    /**
     * getUser - never made publicly available
     * @param userRef
     * @return User
     */
    public User getUser(DocumentReference userRef) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> userQuery = userRef.get();
        DocumentSnapshot userDoc = userQuery.get();
        return userDoc.toObject(User.class);
    }
}
