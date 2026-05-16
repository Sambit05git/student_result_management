package com.srms.repository;

import com.srms.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByUsername(String username);
    List<Student> findByDepartment(String department);
    List<Student> findByDepartmentAndYearAndSemester(String department, String year, String semester);
    List<Student> findByDepartmentAndYearAndAcademicYear(String department, String year, String academicYear);
    List<Student> findByDepartmentAndYear(String department, String year);
}
