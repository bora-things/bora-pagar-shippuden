package com.borathings.borapagar.student;

import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<StudentEntity> findAllStudentsById(List<UserEntity> users){
        return studentRepository.findAllByUserIn(users);
    }
}
