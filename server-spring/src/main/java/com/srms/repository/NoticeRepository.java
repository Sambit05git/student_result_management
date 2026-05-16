package com.srms.repository;

import com.srms.models.Notice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends MongoRepository<Notice, String> {
    Optional<Notice> findByTopicAndContentAndDate(String topic, String content, String date);
}
