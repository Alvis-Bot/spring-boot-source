package com.alvis.springbootsource.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
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
    public void initFirebaseApp() throws IOException {
        var credentials = GoogleCredentials.fromStream(
                new ClassPathResource(FIREBASE_ADMINSDK_FILE).getInputStream());
        var options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();
         FirebaseApp.initializeApp(options);
    }


    @Bean
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }


    @Bean
    public FirebaseMessaging getMessaging() {
        return FirebaseMessaging.getInstance();
   }



}
