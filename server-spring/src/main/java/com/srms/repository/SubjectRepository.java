package com.srms.repository;

import com.srms.models.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findBySubjectCode(String subjectCode);
    List<Subject> findByDepartmentAndYearAndSemester(String department, String year, String semester);
    List<Subject> findByDepartmentAndYear(String department, String year);
}
