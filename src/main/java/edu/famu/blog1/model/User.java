package edu.famu.blog1.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @DocumentId
    private @Nullable String userId;
    private @Nullable String uid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String intro;
    private @Nullable String middleName;
    private @Nullable String mobile;
    private String profile;
    private @Nullable Timestamp registeredAt;
    private @Nullable Timestamp lastLogin;
}
