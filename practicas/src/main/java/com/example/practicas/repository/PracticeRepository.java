package com.example.practicas.repository;

import com.example.practicas.model.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
    List<Practice> findByAcademicPeriod(String academicPeriod);
    List<Practice> findByTeacherId(Long teacherId);
}
