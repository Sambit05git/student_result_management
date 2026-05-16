package com.srms.repository;

import com.srms.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    Optional<Department> findByDepartment(String department);
    Optional<Department> findByDepartmentCode(String departmentCode);
}
