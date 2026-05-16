package com.srms.controller;

import com.srms.models.*;
import com.srms.repository.*;
import com.srms.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private AdminRepository adminRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("usernameError", "Admin doesn't exist."));
        }
        Admin admin = adminOpt.get();
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return ResponseEntity.status(404).body(Map.of("passwordError", "Invalid Credentials"));
        }
        String token = jwtTokenProvider.generateToken(admin.getUsername(), admin.getId());
        return ResponseEntity.ok(Map.of("result", admin, "token", token));
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<?> updatedPassword(@RequestBody Map<String, String> req) {
        String newPassword = req.get("newPassword");
        String confirmPassword = req.get("confirmPassword");
        String email = req.get("email");
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("mismatchError", "Your password and confirmation password do not match"));
        }
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setPassword(passwordEncoder.encode(newPassword));
            if (!Boolean.TRUE.equals(admin.getPasswordUpdated())) {
                admin.setPasswordUpdated(true);
            }
            adminRepository.save(admin);
            return ResponseEntity.ok(Map.of("success", true, "message", "Password updated successfully", "response", admin));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getallstudent")
    public ResponseEntity<?> getAllStudent() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @PostMapping("/createnotice")
    public ResponseEntity<?> createNotice(@RequestBody Notice noticeReq) {
        Optional<Notice> existing = noticeRepository.findByTopicAndContentAndDate(noticeReq.getTopic(), noticeReq.getContent(), noticeReq.getDate());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("noticeError", "Notice already created"));
        }
        Notice saved = noticeRepository.save(noticeReq);
        return ResponseEntity.ok(Map.of("success", true, "message", "Notice created successfully", "response", saved));
    }

    @GetMapping("/getallfaculty")
    public ResponseEntity<?> getAllFaculty() {
        return ResponseEntity.ok(facultyRepository.findAll());
    }

    @GetMapping("/getalldepartment")
    public ResponseEntity<?> getAllDepartment() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    @GetMapping("/getallsubject")
    public ResponseEntity<?> getAllSubject() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @GetMapping("/getalladmin")
    public ResponseEntity<?> getAllAdmin() {
        return ResponseEntity.ok(adminRepository.findAll());
    }

    @PostMapping("/updateprofile")
    public ResponseEntity<?> updateAdmin(@RequestBody Map<String, Object> req) {
        String email = (String) req.get("email");
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (req.containsKey("name")) admin.setName((String) req.get("name"));
            if (req.containsKey("dob")) admin.setDob((String) req.get("dob"));
            if (req.containsKey("department")) admin.setDepartment((String) req.get("department"));
            if (req.containsKey("contactNumber")) admin.setContactNumber(Long.valueOf(req.get("contactNumber").toString()));
            if (req.containsKey("avatar")) admin.setAvatar((String) req.get("avatar"));
            adminRepository.save(admin);
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addadmin")
    public ResponseEntity<?> addAdmin(@RequestBody Admin adminReq) {
        if (adminRepository.findByEmail(adminReq.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("emailError", "Email already exists"));
        }
        List<Admin> admins = adminRepository.findAll().stream().filter(a -> a.getEmail().equals(adminReq.getEmail())).toList();
        String helper;
        if (admins.size() < 10) helper = "00" + admins.size();
        else if (admins.size() < 100 && admins.size() > 9) helper = "0" + admins.size();
        else helper = String.valueOf(admins.size());
        
        String[] parts = adminReq.getDob().split("-");
        List<String> list = Arrays.asList(parts);
        Collections.reverse(list);
        String newDob = adminReq.getUsername() + "@" + String.join("-", list);
        adminReq.setPassword(passwordEncoder.encode(newDob));
        adminReq.setPasswordUpdated(false);
        Admin saved = adminRepository.save(adminReq);
        return ResponseEntity.ok(Map.of("success", true, "message", "Admin registerd successfully", "response", saved));
    }

    @PostMapping("/adddepartment")
    public ResponseEntity<?> addDepartment(@RequestBody Department departmentReq) {
        if (departmentRepository.findByDepartment(departmentReq.getDepartment()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("departmentError", "Department already added"));
        }
        long count = departmentRepository.count() + 1;
        String code = count < 9 ? "0" + count : String.valueOf(count);
        departmentReq.setDepartmentCode(code);
        Department saved = departmentRepository.save(departmentReq);
        return ResponseEntity.ok(Map.of("success", true, "message", "Department added successfully", "response", saved));
    }

    @PostMapping("/addfaculty")
    public ResponseEntity<?> addFaculty(@RequestBody Faculty facultyReq) {
        if (facultyRepository.findByEmail(facultyReq.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("emailError", "Email already exists"));
        }
        Optional<Department> deptOpt = departmentRepository.findByDepartment(facultyReq.getDepartment());
        String deptCode = deptOpt.map(Department::getDepartmentCode).orElse("");
        
        List<Faculty> faculties = facultyRepository.findByDepartment(facultyReq.getDepartment());
        String helper;
        if (faculties.size() < 10) helper = "00" + faculties.size();
        else if (faculties.size() < 100 && faculties.size() > 9) helper = "0" + faculties.size();
        else helper = String.valueOf(faculties.size());
        
        Calendar cal = Calendar.getInstance();
        String regNo = "FAC" + cal.get(Calendar.YEAR) + deptCode + helper;
        
        String[] dobParts = facultyReq.getDob().split("-");
        List<String> dobList = Arrays.asList(dobParts);
        Collections.reverse(dobList);
        String newDob = facultyReq.getUsername() + "@" + String.join("-", dobList);
        
        facultyReq.setRegistrationNumber(regNo);
        facultyReq.setPassword(passwordEncoder.encode(newDob));
        facultyReq.setPasswordUpdated(false);
        Faculty saved = facultyRepository.save(facultyReq);
        return ResponseEntity.ok(Map.of("success", true, "message", "Faculty registerd successfully", "response", saved));
    }

    @PostMapping("/getfaculty")
    public ResponseEntity<?> getFaculty(@RequestBody Map<String, String> req) {
        List<Faculty> faculties = facultyRepository.findByDepartment(req.get("department"));
        if (faculties.isEmpty()) return ResponseEntity.status(404).body(Map.of("noFacultyError", "No Faculty Found"));
        return ResponseEntity.ok(Map.of("result", faculties));
    }

    @PostMapping("/addsubject")
    public ResponseEntity<?> addSubject(@RequestBody Subject subjectReq) {
        if (subjectRepository.findBySubjectCode(subjectReq.getSubjectCode()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("subjectError", "Given Subject is already added"));
        }
        Subject saved = subjectRepository.save(subjectReq);
        List<Student> students = studentRepository.findByDepartmentAndYearAndSemester(subjectReq.getDepartment(), subjectReq.getYear(), subjectReq.getSemester());
        for (Student s : students) {
            s.getSubjects().add(saved.getId());
            studentRepository.save(s);
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Subject added successfully", "response", saved));
    }

    @PostMapping("/getsubject")
    public ResponseEntity<?> getSubject(@RequestBody Map<String, String> req) {
        List<Subject> subjects = subjectRepository.findByDepartmentAndYearAndSemester(req.get("department"), req.get("year"), req.get("semester"));
        if (subjects.isEmpty()) return ResponseEntity.status(404).body(Map.of("noSubjectError", "No Subject Found"));
        return ResponseEntity.ok(Map.of("result", subjects));
    }

    @PostMapping("/getsubjectcount")
    public ResponseEntity<?> getSubjectCount() {
        return ResponseEntity.ok(Map.of("result", subjectRepository.count()));
    }

    @PostMapping("/addstudent")
    public ResponseEntity<?> addStudent(@RequestBody Student studentReq) {
        if (studentRepository.findByEmail(studentReq.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("emailError", "Email already exists"));
        }
        Optional<Department> deptOpt = departmentRepository.findByDepartment(studentReq.getDepartment());
        String deptCode = deptOpt.map(Department::getDepartmentCode).orElse("");
        
        List<Student> students = studentRepository.findByDepartment(studentReq.getDepartment());
        String helper;
        if (students.size() < 10) helper = "00" + students.size();
        else if (students.size() < 100 && students.size() > 9) helper = "0" + students.size();
        else helper = String.valueOf(students.size());
        
        Calendar cal = Calendar.getInstance();
        String regNo = "STU" + cal.get(Calendar.YEAR) + deptCode + helper;
        
        String[] dobParts = studentReq.getDob().split("-");
        List<String> dobList = Arrays.asList(dobParts);
        Collections.reverse(dobList);
        String newDob = studentReq.getUsername() + "@" + String.join("-", dobList);
        
        studentReq.setRegistrationNumber(regNo);
        studentReq.setPassword(passwordEncoder.encode(newDob));
        studentReq.setPasswordUpdated(false);
        Student saved = studentRepository.save(studentReq);
        
        List<Subject> subjects = subjectRepository.findByDepartmentAndYear(studentReq.getDepartment(), studentReq.getYear());
        for (Subject sub : subjects) {
            saved.getSubjects().add(sub.getId());
        }
        studentRepository.save(saved);
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Student registerd successfully", "response", saved));
    }

    @PostMapping("/getstudent")
    public ResponseEntity<?> getStudent(@RequestBody Map<String, String> req) {
        List<Student> students = studentRepository.findByDepartmentAndYearAndAcademicYear(req.get("department"), req.get("year"), req.get("academicYear"));
        if (students.isEmpty()) return ResponseEntity.status(404).body(Map.of("noStudentError", "No Student Found"));
        return ResponseEntity.ok(Map.of("result", students));
    }

    @PostMapping("/getnotice")
    public ResponseEntity<?> getNotice() {
        List<Notice> notices = noticeRepository.findAll();
        if (notices.isEmpty()) return ResponseEntity.status(404).body(Map.of("noNoticeError", "No Notice Found"));
        return ResponseEntity.ok(Map.of("result", notices));
    }

    @PostMapping("/getadmin")
    public ResponseEntity<?> getAdmin(@RequestBody Map<String, String> req) {
        List<Admin> admins = adminRepository.findByDepartment(req.get("department"));
        if (admins.isEmpty()) return ResponseEntity.status(404).body(Map.of("noAdminError", "No Admins Found"));
        return ResponseEntity.ok(Map.of("result", admins));
    }

    @PostMapping("/deleteadmin")
    public ResponseEntity<?> deleteAdmin(@RequestBody List<String> req) {
        req.forEach(id -> adminRepository.deleteById(id));
        return ResponseEntity.ok(Map.of("message", "Admin Deleted"));
    }

    @PostMapping("/deletefaculty")
    public ResponseEntity<?> deleteFaculty(@RequestBody List<String> req) {
        req.forEach(id -> facultyRepository.deleteById(id));
        return ResponseEntity.ok(Map.of("message", "Faculty Deleted"));
    }

    @PostMapping("/deletestudent")
    public ResponseEntity<?> deleteStudent(@RequestBody List<String> req) {
        req.forEach(id -> studentRepository.deleteById(id));
        return ResponseEntity.ok(Map.of("message", "Student Deleted"));
    }

    @PostMapping("/deletesubject")
    public ResponseEntity<?> deleteSubject(@RequestBody List<String> req) {
        req.forEach(id -> subjectRepository.deleteById(id));
        return ResponseEntity.ok(Map.of("message", "Subject Deleted"));
    }

    @PostMapping("/deletedepartment")
    public ResponseEntity<?> deleteDepartment(@RequestBody Map<String, String> req) {
        departmentRepository.findByDepartment(req.get("department")).ifPresent(d -> departmentRepository.delete(d));
        return ResponseEntity.ok(Map.of("message", "Department Deleted"));
    }
}
