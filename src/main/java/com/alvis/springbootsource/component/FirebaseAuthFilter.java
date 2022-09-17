package com.alvis.springbootsource.component;

import com.alvis.springbootsource.entity.User;
import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import com.alvis.springbootsource.exception.ErrorResponse;
import com.alvis.springbootsource.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseAuthFilter extends OncePerRequestFilter {

    @Value("${app.JWT_AUTH_HEADER}")
    private String JWT_AUTH_HEADER;

    private final Helper helper;

    private FirebaseAuth firebaseAuth;

    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        boolean isLoginRoute = request.getRequestURI().equals("/api/auth/login");
        var isContinue = isLoginRoute || checkAndAuthenticateRequest(request ,response);
        if (isContinue) filterChain.doFilter(request, response);
    }

    private boolean checkAndAuthenticateRequest(
            HttpServletRequest request , HttpServletResponse response) throws IOException {
        if (isAuthenticated()) return true;
        var authHeader = Optional.ofNullable(request.getHeader(JWT_AUTH_HEADER));
        if (authHeader.isEmpty()) return true;

        try {
            var user = retrieveUserFromAuthHeader(authHeader.get());
            setAuthenticationForUser(user);
            return true;
        } catch (ApiException ex ) {
            var errorResponse = new ErrorResponse(ex.getCode());
            responseUnauthorized(response, errorResponse);
            return false;
        }
    }



    private void setAuthenticationForUser(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getUid(), null,
                helper.buildAuthorities(user.getRole()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean isAuthenticated() {
        return Objects.nonNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private User retrieveUserFromAuthHeader(String authHeader) {
        var idToken = helper.extractIdTokenFromAuthHeader(authHeader)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_ID_TOKEN));
        var uid = helper.extractUidFromIdToken(idToken);
        return userRepository.findById(uid)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private void responseUnauthorized(
            HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().print(new Gson().toJson(errorResponse));
        // Still different response headers from default response
        // Transfer-Encoding: chunked and no Content-Length
    }

}
