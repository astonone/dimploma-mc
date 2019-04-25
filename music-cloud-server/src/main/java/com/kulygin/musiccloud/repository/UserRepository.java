package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Cacheable("user")
    Page<User> findAll(Pageable pageable);

    @Cacheable("user")
    Page<User> findAllByUserDetails_FirstNameOrUserDetails_LastNameOrUserDetails_NickName(Pageable pageable, String firstName, String lastName, String nickName);

    @Cacheable("user")
    int countAllByUserDetails_FirstNameOrUserDetails_LastNameOrUserDetails_NickName(String firstName, String lastName, String nickName);

    @Cacheable("user")
    @Query("select count(u) from User u")
    int countAll();
}
