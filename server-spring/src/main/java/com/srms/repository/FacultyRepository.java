package com.srms.repository;

import com.srms.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, String> {
    Optional<Faculty> findByEmail(String email);
    Optional<Faculty> findByUsername(String username);
    List<Faculty> findByDepartment(String department);
}
