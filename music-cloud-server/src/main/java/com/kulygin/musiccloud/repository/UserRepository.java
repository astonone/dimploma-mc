package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAll();
    Page<User> findAllByIdNotOrderByUserDetails_LastNameAsc(Pageable pageable, Long userId);
    Page<User> findAllByUserDetails_FirstNameOrUserDetails_LastNameOrUserDetails_NickNameOrderByUserDetails_LastNameAsc(Pageable pageable, String firstName, String lastName, String nickName);
    int countAllByUserDetails_FirstNameOrUserDetails_LastNameOrUserDetails_NickName(String firstName, String lastName, String nickName);
    @Query("select count(u) from User u")
    int countAll();
    int countAllByIdNot(Long userId);
}
