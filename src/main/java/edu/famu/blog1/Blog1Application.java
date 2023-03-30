package edu.famu.blog1;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Blog1Application {

    public static void main(String[] args) throws IOException {
        ClassLoader loader = Blog1Application.class.getClassLoader();

        //opens the file stored in resources
        File file = new File(loader.getResource("serviceAccountKey.json").getFile());

        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        //connect to Firebase
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

        FirebaseApp.initializeApp(options);

        SpringApplication.run(Blog1Application.class, args);
    }

}
