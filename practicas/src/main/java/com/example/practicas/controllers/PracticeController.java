package com.example.practicas.controllers;

import com.example.practicas.model.Company;
import com.example.practicas.model.Practice;
import com.example.practicas.model.Student;
import com.example.practicas.model.Teacher;
import com.example.practicas.services.CompanyService;
import com.example.practicas.services.PracticeService;
import com.example.practicas.services.StudentService;
import com.example.practicas.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/practices")
public class PracticeController {

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Practice>> findAll() {
        return ResponseEntity.ok(practiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practice> findById(@PathVariable Long id) {
        return practiceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // REQUISITO FUNCIONAL 2: Obtener listado de estudiantes de una práctica
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByPractice(@PathVariable Long id) {
        List<Student> students = practiceService.getStudentsByPracticeId(id);
        if (students.isEmpty() && practiceService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    // REQUISITO FUNCIONAL 3: Resumen de prácticas por periodo académico
    @GetMapping("/summary")
    public ResponseEntity<List<Practice>> getSummaryByPeriod(@RequestParam String period) {
        List<Practice> practices = practiceService.findByAcademicPeriod(period);
        return ResponseEntity.ok(practices);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody PracticeRequest request) {
        // Validar que existan teacher y company
        Teacher teacher = teacherService.findById(request.getTeacherId())
                .orElse(null);
        Company company = companyService.findById(request.getCompanyId())
                .orElse(null);

        if (teacher == null || company == null) {
            return ResponseEntity.badRequest()
                    .body("Teacher or Company not found");
        }

        Practice practice = new Practice();
        practice.setDestination(request.getDestination());
        practice.setDepartureDate(request.getDepartureDate());
        practice.setReturnDate(request.getReturnDate());
        practice.setAcademicPeriod(request.getAcademicPeriod());
        practice.setTeacher(teacher);
        practice.setCompany(company);

        // Agregar estudiantes si se proporcionaron
        if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
            List<Student> students = studentService.findAll().stream()
                    .filter(s -> request.getStudentIds().contains(s.getId()))
                    .toList();
            practice.setStudents(students);
        }

        Practice savedPractice = practiceService.save(practice);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPractice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (practiceService.findById(id).isPresent()) {
            practiceService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTO para la creación de prácticas
    public static class PracticeRequest {
        private String destination;
        private java.time.LocalDate departureDate;
        private java.time.LocalDate returnDate;
        private String academicPeriod;
        private Long teacherId;
        private Long companyId;
        private List<Long> studentIds;

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public java.time.LocalDate getDepartureDate() { return departureDate; }
        public void setDepartureDate(java.time.LocalDate departureDate) { this.departureDate = departureDate; }
        public java.time.LocalDate getReturnDate() { return returnDate; }
        public void setReturnDate(java.time.LocalDate returnDate) { this.returnDate = returnDate; }
        public String getAcademicPeriod() { return academicPeriod; }
        public void setAcademicPeriod(String academicPeriod) { this.academicPeriod = academicPeriod; }
        public Long getTeacherId() { return teacherId; }
        public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
        public Long getCompanyId() { return companyId; }
        public void setCompanyId(Long companyId) { this.companyId = companyId; }
        public List<Long> getStudentIds() { return studentIds; }
        public void setStudentIds(List<Long> studentIds) { this.studentIds = studentIds; }
    }
}
