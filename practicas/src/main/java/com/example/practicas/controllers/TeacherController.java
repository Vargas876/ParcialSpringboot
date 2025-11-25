package com.example.practicas.controllers;

import com.example.practicas.model.Practice;
import com.example.practicas.model.Teacher;
import com.example.practicas.services.PracticeService;
import com.example.practicas.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private PracticeService practiceService;

    @GetMapping
    public ResponseEntity<List<Teacher>> findAll() {
        return ResponseEntity.ok(teacherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> findById(@PathVariable Long id) {
        return teacherService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // REQUISITO FUNCIONAL 1: Visualizar prácticas de un docente por número de identificación
    @GetMapping("/{identificationNumber}/practices")
    public ResponseEntity<List<Practice>> getPracticesByTeacherIdentification(
            @PathVariable String identificationNumber) {
        return teacherService.findByIdentificationNumber(identificationNumber)
                .map(teacher -> {
                    List<Practice> practices = practiceService.findByTeacherId(teacher.getId());
                    return ResponseEntity.ok(practices);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Teacher> save(@RequestBody Teacher teacher) {
        Teacher savedTeacher = teacherService.save(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (teacherService.findById(id).isPresent()) {
            teacherService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
