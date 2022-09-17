package com.alvis.springbootsource.services.base;

import com.alvis.springbootsource.entity.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FcmTokenService {


     CompletableFuture<Boolean> addFcmTopicToUser(User user, String fcmTopic);
    CompletableFuture<Boolean> removeFcmTopicFromUser(User user, String fcmTopic);

    CompletableFuture<Boolean> addFcmTokenToUser(User user, String fcmToken);

    CompletableFuture<Boolean> removeFcmTokenFromUser(User user, String fcmToken);

    CompletableFuture<Boolean> removeMultipleFcmTokensFromUser(User user, List<String> fcmTokens);






}
