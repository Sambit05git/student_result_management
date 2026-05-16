package com.srms.repository;

import com.srms.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    List<Attendance> findByStudent(String student);
    Optional<Attendance> findByStudentAndSubject(String student, String subject);
}
