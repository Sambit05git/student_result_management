package com.srms.repository;

import com.srms.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, String> {
    List<Test> findByDepartmentAndYearAndSemester(String department, String year, String semester);
}
