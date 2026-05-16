package com.srms.repository;

import com.srms.models.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends MongoRepository<Test, String> {
    List<Test> findByDepartmentAndYearAndSemester(String department, String year, String semester);
}
