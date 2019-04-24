package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable("user")
    User findByEmail(String email);
    @Cacheable("user")
    Page<User> findAll(Pageable pageable);
    @Cacheable("user")
    @Query("select count(u) from User u")
    int countAll();
}
