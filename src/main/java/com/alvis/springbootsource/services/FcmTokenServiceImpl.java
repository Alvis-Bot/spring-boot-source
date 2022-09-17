package com.alvis.springbootsource.services;

import com.alvis.springbootsource.component.Helper;
import com.alvis.springbootsource.entity.User;
import com.alvis.springbootsource.services.base.FcmTokenService;
import com.alvis.springbootsource.util.CodeUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmTokenServiceImpl implements FcmTokenService {

    private final Helper helper;

    private final FirebaseMessaging firebaseMessaging;

    @Override
    @Async
    public CompletableFuture<Boolean> addFcmTopicToUser(User user, String fcmTopic) {
        if (!helper.isValidFcmTopic(fcmTopic)) return CompletableFuture.completedFuture(false);
        String[] updatedFcmTopics = CodeUtil.addStringToArray(user.getFcmTopics(), fcmTopic);
        user.setFcmTopics(updatedFcmTopics);

        // Subscribe all user tokens to the new topic
        List<String> fcmTokens = CodeUtil.arrayToList(user.getFcmTokens());
        if (!fcmTokens.isEmpty())
            firebaseMessaging.subscribeToTopicAsync(fcmTokens, fcmTopic);

        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> removeFcmTopicFromUser(User user, String fcmTopic) {
        if (!helper.isValidFcmTopic(fcmTopic)) return CompletableFuture.completedFuture(false);

        String[] updatedFcmTopics = CodeUtil.removeStringFromArray(user.getFcmTopics(), fcmTopic);
        user.setFcmTopics(updatedFcmTopics);

        // Unsubscribe all user tokens from the new topic
        List<String> fcmTokens = CodeUtil.arrayToList(user.getFcmTokens());
        if (!fcmTokens.isEmpty())
            firebaseMessaging.unsubscribeFromTopicAsync(fcmTokens, fcmTopic);

        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> addFcmTokenToUser(User user, String fcmToken) {
        if (!helper.isValidFcmToken(fcmToken)) return CompletableFuture.completedFuture(false);

        String[] updatedFcmTokens = CodeUtil.addStringToArray(user.getFcmTokens(), fcmToken);
        user.setFcmTokens(updatedFcmTokens);

        // Subscribe the new token from all user topics
        List<String> fcmTopics = CodeUtil.arrayToList(user.getFcmTopics());
        // No need to check each FCM topic (checked when adding)
        for (String fcmTopic : fcmTopics)
            firebaseMessaging.subscribeToTopicAsync(List.of(fcmToken), fcmTopic);

        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> removeFcmTokenFromUser(User user, String fcmToken) {
        if (!helper.isValidFcmToken(fcmToken)) return CompletableFuture.completedFuture(false);

        String[] updatedFcmTokens = CodeUtil.removeStringFromArray(user.getFcmTokens(), fcmToken);
        user.setFcmTokens(updatedFcmTokens);

        // Unsubscribe the new token from all user topics
        List<String> fcmTopics = CodeUtil.arrayToList(user.getFcmTopics());
        for (String fcmTopic : fcmTopics)
            if (helper.isValidFcmTopic(fcmTopic))
                firebaseMessaging.unsubscribeFromTopicAsync(List.of(fcmToken), fcmTopic);

        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> removeMultipleFcmTokensFromUser(User user, List<String> fcmTokens) {
        String[] updatedFcmTokens = CodeUtil.removeAllStringsFromArray(
                user.getFcmTokens(), fcmTokens);
        user.setFcmTokens(updatedFcmTokens);

        // Unsubscribe all given tokens from all user topics
        List<String> fcmTopics = CodeUtil.arrayToList(user.getFcmTopics());
        List<String> validFcmTokens = helper.getValidFcmTokens(fcmTokens);
        if (!validFcmTokens.isEmpty())
            for (String fcmTopic : fcmTopics)
                if (helper.isValidFcmTopic(fcmTopic))
                    firebaseMessaging.unsubscribeFromTopicAsync(validFcmTokens, fcmTopic);

        return CompletableFuture.completedFuture(true);
    }
}
