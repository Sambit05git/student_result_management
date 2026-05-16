package com.srms.controller;

import com.srms.models.Attendance;
import com.srms.models.Marks;
import com.srms.models.Student;
import com.srms.models.Subject;
import com.srms.models.Test;
import com.srms.repository.AttendanceRepository;
import com.srms.repository.MarksRepository;
import com.srms.repository.StudentRepository;
import com.srms.repository.SubjectRepository;
import com.srms.repository.TestRepository;
import com.srms.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private MarksRepository marksRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> studentLogin(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        Map<String, String> errors = new HashMap<>();

        Optional<Student> studentOpt = studentRepository.findByUsername(username);
        if (studentOpt.isEmpty()) {
            errors.put("usernameError", "Student doesn't exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

        Student student = studentOpt.get();
        if (!passwordEncoder.matches(password, student.getPassword())) {
            errors.put("passwordError", "Invalid Credentials");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

        String token = jwtTokenProvider.generateToken(student.getEmail(), student.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("result", student);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<?> updatedPassword(@RequestBody Map<String, String> req) {
        String newPassword = req.get("newPassword");
        String confirmPassword = req.get("confirmPassword");
        String email = req.get("email");
        Map<String, String> errors = new HashMap<>();

        if (!newPassword.equals(confirmPassword)) {
            errors.put("mismatchError", "Your password and confirmation password do not match");
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(email);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                student.setPassword(passwordEncoder.encode(newPassword));
                if (!Boolean.TRUE.equals(student.getPasswordUpdated())) {
                    student.setPasswordUpdated(true);
                }
                studentRepository.save(student);

                Map<String, Object> res = new HashMap<>();
                res.put("success", true);
                res.put("message", "Password updated successfully");
                res.put("response", student);
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/updateprofile")
    public ResponseEntity<?> updateStudent(@RequestBody Map<String, Object> req) {
        try {
            String email = (String) req.get("email");
            Optional<Student> studentOpt = studentRepository.findByEmail(email);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                if (req.containsKey("name")) student.setName((String) req.get("name"));
                if (req.containsKey("dob")) student.setDob((String) req.get("dob"));
                if (req.containsKey("department")) student.setDepartment((String) req.get("department"));
                if (req.containsKey("contactNumber")) student.setContactNumber(Long.valueOf(req.get("contactNumber").toString()));
                if (req.containsKey("batch")) student.setBatch((String) req.get("batch"));
                if (req.containsKey("division")) student.setDivision((String) req.get("division"));
                if (req.containsKey("year")) student.setYear((String) req.get("year"));
                if (req.containsKey("motherName")) student.setMotherName((String) req.get("motherName"));
                if (req.containsKey("fatherName")) student.setFatherName((String) req.get("fatherName"));
                if (req.containsKey("fatherContactNumber")) student.setFatherContactNumber(Long.valueOf(req.get("fatherContactNumber").toString()));
                if (req.containsKey("avatar")) student.setAvatar((String) req.get("avatar"));

                studentRepository.save(student);
                return ResponseEntity.ok(student);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/testresult")
    public ResponseEntity<?> testResult(@RequestBody Map<String, String> req) {
        try {
            String username = req.get("username");
            String department = req.get("department");
            String year = req.get("year");
            String division = req.get("division");

            Optional<Student> studentOpt = studentRepository.findByUsername(username); // simplified find
            if(studentOpt.isEmpty()) return ResponseEntity.notFound().build();
            Student student = studentOpt.get();

            List<Test> tests = testRepository.findAll().stream()
                .filter(t -> t.getDepartment().equals(department) && t.getYear().equals(year) && t.getDivision().equals(division))
                .toList();
            if (tests.isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                errors.put("notestError", "No Test Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
            }

            List<Map<String, Object>> result = new ArrayList<>();
            for (Test test : tests) {
                Optional<Subject> subjectOpt = subjectRepository.findBySubjectCode(test.getSubjectCode());
                Optional<Marks> marksOpt = marksRepository.findByExamAndStudent(test.getId(), student.getId());
                
                if (marksOpt.isPresent() && subjectOpt.isPresent()) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("marks", marksOpt.get().getMarks());
                    temp.put("totalMarks", test.getTotalMarks());
                    temp.put("subjectName", subjectOpt.get().getSubjectName());
                    temp.put("subjectCode", test.getSubjectCode());
                    temp.put("test", test.getTest());
                    result.add(temp);
                }
            }
            Map<String, Object> res = new HashMap<>();
            res.put("result", result);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> attendance(@RequestBody Map<String, String> req) {
        try {
            String department = req.get("department");
            String year = req.get("year");
            String division = req.get("division");

            // Need to match how nodejs find student by dept, year, division
            List<Student> students = studentRepository.findByDepartmentAndYear(department, year);
            Student student = students.stream().filter(s -> division.equals(s.getDivision())).findFirst().orElse(null);

            if (student == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Student not found"));
            }

            List<Attendance> attendances = attendanceRepository.findByStudent(student.getId());

            List<Map<String, Object>> result = attendances.stream().map(att -> {
                Map<String, Object> res = new HashMap<>();
                Optional<Subject> subjectOpt = subjectRepository.findById(att.getSubject());
                
                double percentage = att.getTotalLecturesByFaculty() > 0 ? 
                        ((double) att.getLectureAttended() / att.getTotalLecturesByFaculty()) * 100 : 0.0;
                
                res.put("percentage", String.format("%.2f", percentage));
                if(subjectOpt.isPresent()){
                    res.put("subjectCode", subjectOpt.get().getSubjectCode());
                    res.put("subjectName", subjectOpt.get().getSubjectName());
                }
                res.put("attended", att.getLectureAttended());
                res.put("total", att.getTotalLecturesByFaculty());
                return res;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
