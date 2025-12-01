package com.devwiki.leafy.repository.user;

import com.devwiki.leafy.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotNull String email);
}
