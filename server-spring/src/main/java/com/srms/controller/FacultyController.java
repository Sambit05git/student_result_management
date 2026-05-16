package com.srms.controller;

import com.srms.models.Attendance;
import com.srms.models.Faculty;
import com.srms.models.Marks;
import com.srms.models.Student;
import com.srms.models.Subject;
import com.srms.models.Test;
import com.srms.repository.AttendanceRepository;
import com.srms.repository.FacultyRepository;
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

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private StudentRepository studentRepository;
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
    public ResponseEntity<?> facultyLogin(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        Map<String, String> errors = new HashMap<>();

        Optional<Faculty> facultyOpt = facultyRepository.findByUsername(username);
        if (facultyOpt.isEmpty()) {
            errors.put("usernameError", "Faculty doesn't exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

        Faculty faculty = facultyOpt.get();
        if (!passwordEncoder.matches(password, faculty.getPassword())) {
            errors.put("passwordError", "Invalid Credentials");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

        String token = jwtTokenProvider.generateToken(faculty.getEmail(), faculty.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("result", faculty);
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
            Optional<Faculty> facultyOpt = facultyRepository.findByEmail(email);
            if (facultyOpt.isPresent()) {
                Faculty faculty = facultyOpt.get();
                faculty.setPassword(passwordEncoder.encode(newPassword));
                if (!Boolean.TRUE.equals(faculty.getPasswordUpdated())) {
                    faculty.setPasswordUpdated(true);
                }
                facultyRepository.save(faculty);

                Map<String, Object> res = new HashMap<>();
                res.put("success", true);
                res.put("message", "Password updated successfully");
                res.put("response", faculty);
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/updateprofile")
    public ResponseEntity<?> updateFaculty(@RequestBody Map<String, Object> req) {
        try {
            String email = (String) req.get("email");
            Optional<Faculty> facultyOpt = facultyRepository.findByEmail(email);
            if (facultyOpt.isPresent()) {
                Faculty faculty = facultyOpt.get();
                if (req.containsKey("name")) faculty.setName((String) req.get("name"));
                if (req.containsKey("dob")) faculty.setDob((String) req.get("dob"));
                if (req.containsKey("department")) faculty.setDepartment((String) req.get("department"));
                if (req.containsKey("contactNumber")) faculty.setContactNumber(Long.valueOf(req.get("contactNumber").toString()));
                if (req.containsKey("designation")) faculty.setDesignation((String) req.get("designation"));
                if (req.containsKey("avatar")) faculty.setAvatar((String) req.get("avatar"));

                facultyRepository.save(faculty);
                return ResponseEntity.ok(faculty);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/createtest")
    public ResponseEntity<?> createTest(@RequestBody Test testReq) {
        try {
            List<Test> existingTests = testRepository.findAll().stream()
                .filter(t -> t.getSubjectCode().equals(testReq.getSubjectCode()) &&
                             t.getDepartment().equals(testReq.getDepartment()) &&
                             t.getYear().equals(testReq.getYear()) &&
                             t.getSemester().equals(testReq.getSemester()) &&
                             t.getDivision().equals(testReq.getDivision()) &&
                             t.getTest().equals(testReq.getTest()))
                .toList();

            if (!existingTests.isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                errors.put("testError", "Given Test is already created");
                return ResponseEntity.badRequest().body(errors);
            }

            Test newTest = testRepository.save(testReq);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test added successfully");
            response.put("response", newTest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/gettest")
    public ResponseEntity<?> getTest(@RequestBody Map<String, String> req) {
        try {
            String department = req.get("department");
            String year = req.get("year");
            String semester = req.get("semester");
            String division = req.get("division");

            List<Test> tests = testRepository.findAll().stream()
                .filter(t -> t.getDepartment().equals(department) &&
                             t.getYear().equals(year) &&
                             t.getSemester().equals(semester) &&
                             t.getDivision().equals(division))
                .toList();

            return ResponseEntity.ok(Map.of("result", tests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/getstudent")
    public ResponseEntity<?> getStudent(@RequestBody Map<String, String> req) {
        try {
            String department = req.get("department");
            String year = req.get("year");
            String semester = req.get("semester");
            String division = req.get("division");

            List<Student> students = studentRepository.findAll().stream()
                .filter(s -> s.getDepartment().equals(department) &&
                             s.getYear().equals(year) &&
                             s.getSemester().equals(semester) &&
                             s.getDivision().equals(division))
                .toList();

            if (students.isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                errors.put("noStudentError", "No Student Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
            }

            return ResponseEntity.ok(Map.of("result", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/uploadmarks")
    public ResponseEntity<?> uploadMarks(@RequestBody Map<String, Object> req) {
        try {
            String department = (String) req.get("department");
            String year = (String) req.get("year");
            String division = (String) req.get("division");
            String testName = (String) req.get("test");
            String subjectCode = (String) req.get("subjectCode");
            List<Map<String, Object>> marksList = (List<Map<String, Object>>) req.get("marks");

            Optional<Test> testOpt = testRepository.findAll().stream()
                .filter(t -> t.getDepartment().equals(department) &&
                             t.getYear().equals(year) &&
                             t.getDivision().equals(division) &&
                             t.getTest().equals(testName) &&
                             t.getSubjectCode().equals(subjectCode))
                .findFirst();

            if (testOpt.isEmpty()) return ResponseEntity.badRequest().body("Test not found");
            Test existingTest = testOpt.get();

            List<Marks> isAlready = marksRepository.findAll().stream().filter(m -> m.getExam().equals(existingTest.getId())).toList();
            if (!isAlready.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("examError", "You have already uploaded marks of given exam"));
            }

            for (Map<String, Object> markObj : marksList) {
                Marks m = new Marks();
                m.setStudent((String) markObj.get("_id"));
                m.setExam(existingTest.getId());
                m.setMarks(Integer.parseInt(markObj.get("value").toString()));
                marksRepository.save(m);
            }

            return ResponseEntity.ok(Map.of("message", "Marks uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/markattendance")
    public ResponseEntity<?> markAttendance(@RequestBody Map<String, Object> req) {
        try {
            List<String> selectedStudents = (List<String>) req.get("selectedStudents");
            String subjectName = (String) req.get("subjectName");
            String department = (String) req.get("department");
            String year = (String) req.get("year");
            String division = (String) req.get("division");

            Optional<Subject> subOpt = subjectRepository.findAll().stream()
                .filter(s -> s.getSubjectName().equals(subjectName)).findFirst();
            
            if (subOpt.isEmpty()) return ResponseEntity.badRequest().body("Subject not found");
            Subject sub = subOpt.get();

            List<Student> allStudents = studentRepository.findAll().stream()
                .filter(s -> s.getDepartment().equals(department) &&
                             s.getYear().equals(year) &&
                             s.getDivision().equals(division))
                .toList();

            for (Student student : allStudents) {
                Optional<Attendance> preOpt = attendanceRepository.findByStudentAndSubject(student.getId(), sub.getId());
                Attendance att;
                if (preOpt.isEmpty()) {
                    att = new Attendance();
                    att.setStudent(student.getId());
                    att.setSubject(sub.getId());
                } else {
                    att = preOpt.get();
                }
                att.setTotalLecturesByFaculty(att.getTotalLecturesByFaculty() + 1);
                attendanceRepository.save(att);
            }

            for (String studentId : selectedStudents) {
                Optional<Attendance> preOpt = attendanceRepository.findByStudentAndSubject(studentId, sub.getId());
                if (preOpt.isPresent()) {
                    Attendance att = preOpt.get();
                    att.setLectureAttended(att.getLectureAttended() + 1);
                    attendanceRepository.save(att);
                }
            }

            return ResponseEntity.ok(Map.of("message", "Attendance Marked successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
