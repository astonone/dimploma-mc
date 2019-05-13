package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
