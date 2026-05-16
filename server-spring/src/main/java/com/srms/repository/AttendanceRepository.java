package com.srms.repository;

import com.srms.models.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByStudent(String student);
    Optional<Attendance> findByStudentAndSubject(String student, String subject);
}
