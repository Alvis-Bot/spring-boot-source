package com.alvis.springbootsource.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {
    @Value("${app.FIREBASE_ADMINSDK_FILE}")
    private String FIREBASE_ADMINSDK_FILE;



    @PostConstruct
    public void onStart() {
        log.info("Initializing Firebase App...");
        try {
            this.initFirebaseApp();
        } catch (IOException e) {
            log.error("Initializing Firebase App {0}", e);
        }
    }


    public void initFirebaseApp() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource(FIREBASE_ADMINSDK_FILE).getInputStream());
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();
         FirebaseApp.initializeApp(options);
    }


    @Bean
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

//    @Bean
//    public FirebaseDatabase firebaseDatabase() {
//        return FirebaseDatabase.getInstance();
//    }
//
//    @Bean
//    public Firestore getDatabase() throws IOException {
//        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
//                .setCredentials(GoogleCredentials.getApplicationDefault()).build();
//        return firestoreOptions.getService();
//    }
//
//    @Bean
//    public FirebaseMessaging getMessaging() {
//        return FirebaseMessaging.getInstance();
//    }
//
//    @Bean
//    public FirebaseRemoteConfig getRemoteConfig() {
//        return FirebaseRemoteConfig.getInstance();
//    }
}
