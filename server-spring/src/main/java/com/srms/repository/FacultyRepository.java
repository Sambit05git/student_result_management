package com.srms.repository;

import com.srms.models.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends MongoRepository<Faculty, String> {
    Optional<Faculty> findByEmail(String email);
    Optional<Faculty> findByUsername(String username);
    List<Faculty> findByDepartment(String department);
}
