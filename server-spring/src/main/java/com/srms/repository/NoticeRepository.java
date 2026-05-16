package com.srms.repository;

import com.srms.models.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String> {
    Optional<Notice> findByTopicAndContentAndDate(String topic, String content, String date);
}
