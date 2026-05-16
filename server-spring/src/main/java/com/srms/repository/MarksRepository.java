package com.srms.repository;

import com.srms.models.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, String> {
    List<Marks> findByStudent(String student);
    Optional<Marks> findByExamAndStudent(String exam, String student);
}
