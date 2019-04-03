package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
