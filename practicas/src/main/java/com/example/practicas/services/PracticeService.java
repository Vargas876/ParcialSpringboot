package com.example.practicas.services;

import com.example.practicas.model.Practice;
import com.example.practicas.model.Student;
import com.example.practicas.repository.PracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PracticeService {
    @Autowired
    private PracticeRepository practiceRepository;

    public List<Practice> findAll() {
        return practiceRepository.findAll();
    }

    public Optional<Practice> findById(Long id) {
        return practiceRepository.findById(id);
    }

    public Practice save(Practice practice) {
        return practiceRepository.save(practice);
    }

    public List<Practice> findByAcademicPeriod(String period) {
        return practiceRepository.findByAcademicPeriod(period);
    }

    public List<Practice> findByTeacherId(Long teacherId) {
        return practiceRepository.findByTeacherId(teacherId);
    }

    public List<Student> getStudentsByPracticeId(Long practiceId) {
        Optional<Practice> practice = practiceRepository.findById(practiceId);
        return practice.map(Practice::getStudents).orElse(List.of());
    }

    public void deleteById(Long id) {
        practiceRepository.deleteById(id);
    }
}
