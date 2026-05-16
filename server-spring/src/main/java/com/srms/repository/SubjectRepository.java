package com.srms.repository;

import com.srms.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    Optional<Subject> findBySubjectCode(String subjectCode);
    List<Subject> findByDepartmentAndYearAndSemester(String department, String year, String semester);
    List<Subject> findByDepartmentAndYear(String department, String year);
}
