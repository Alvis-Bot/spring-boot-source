package com.alvis.springbootsource.component;

import com.alvis.springbootsource.entity.User;
import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import com.alvis.springbootsource.exception.ErrorResponse;
import com.alvis.springbootsource.repository.UserRepository;
import com.alvis.springbootsource.util.CodeUtil;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class FirebaseAuthFilter extends OncePerRequestFilter {

    @Value("${app.JWT_AUTH_HEADER}")
    private String JWT_AUTH_HEADER;

    private final Helper helper;

    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        var isContinue = checkAndAuthenticateRequest(request ,response);
        if (isContinue) filterChain.doFilter(request, response);
    }

    private boolean checkAndAuthenticateRequest(
            HttpServletRequest request , HttpServletResponse response) throws IOException {
        if (isAuthenticated()) return true;
        Optional<String> authHeader = Optional.ofNullable(request.getHeader(JWT_AUTH_HEADER));
        if (authHeader.isEmpty()) return true;

        try {
            User user = retrieveUserFromAuthHeader(authHeader.get());
            setAuthenticationForUser(user);
            return true;
        } catch (BadCredentialsException e) {
            var errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED, e.getMessage());
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
        String idToken = helper.extractIdTokenFromAuthHeader(authHeader)
                .orElseThrow(() -> new BadCredentialsException("Missing id token"));
        String uid = helper.extractUidFromIdToken(idToken)
                .orElseThrow(() -> new BadCredentialsException("Invalid id token"));
        return userRepository.findById(uid)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
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
