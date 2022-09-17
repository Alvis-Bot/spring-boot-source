package com.alvis.springbootsource.repository;


import com.alvis.springbootsource.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
}
