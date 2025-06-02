package com.borathings.borapagar.student;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentHelperService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentEntity findByIdOrError(Long studentId) {
        return studentRepository
                .findById(studentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Estudante com id: " + studentId + " não foi encontrado"));
    }
}
