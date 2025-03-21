package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestSemesterDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSubjectInterestService {

    @Autowired
    StudentSubjectInterestRepository studentSubjectInterestRepository;

    @Autowired
    StudentService studentService;

    public List<StudentSubjectInterestEntity> listInterest(Long studentId) {

        return studentSubjectInterestRepository.findAllByStudentId(studentId);
    }

    public void createInterest(
            int sigaaSubjectId, StudentSubjectInterestSemesterDTO semesterDTO, StudentEntity student) {

        StudentSubjectInterestEntity interestEntity =
                new StudentSubjectInterestEntity(semesterDTO.year(), semesterDTO.period(), student, sigaaSubjectId);

        studentSubjectInterestRepository.save(interestEntity);
    }
    ;

    public void deleteInterest(int sigaaSubjectId, StudentEntity student) {
        studentSubjectInterestRepository.deleteBySigaaSubjectIdAndStudentId(sigaaSubjectId, student.getId());
    }
}
