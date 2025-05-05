package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSubjectInterestService {

    @Autowired
    StudentSubjectInterestRepository studentSubjectInterestRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    SubjectSigaaClient subjectClient;

    public List<StudentSubjectInterestDTO> listInterests(Long studentId) {
        List<StudentSubjectInterestEntity> studentSubjectInterests =
                studentSubjectInterestRepository.findAllByStudentId(studentId);
        return studentSubjectInterests.stream()
                .map(interest -> {
                    ComponentDTO component = subjectClient.getComponentById(interest.getSigaaSubjectId());
                    return new StudentSubjectInterestDTO(
                            interest.getId(), component, interest.getYear(), interest.getPeriod());
                })
                .collect(Collectors.toList());
    }

    public void createInterest(StudentSubjectAddInterestDTO semesterDTO, StudentEntity student) {

        StudentSubjectInterestEntity interestEntity = new StudentSubjectInterestEntity(
                semesterDTO.year(), semesterDTO.period(), student, semesterDTO.subjectSigaaId());

        studentSubjectInterestRepository.save(interestEntity);
    }
    ;

    public void deleteInterest(int sigaaSubjectId, StudentEntity student) {
        studentSubjectInterestRepository.deleteBySigaaSubjectIdAndStudentId(sigaaSubjectId, student.getId());
    }
}
