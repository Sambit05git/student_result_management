package com.srms.repository;

import com.srms.models.Marks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends MongoRepository<Marks, String> {
    List<Marks> findByStudent(String student);
    Optional<Marks> findByExamAndStudent(String exam, String student);
}
