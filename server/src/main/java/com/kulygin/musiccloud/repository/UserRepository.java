package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
