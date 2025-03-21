package com.borathings.borapagar.student.interest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestSemesterDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentSubjectInterestServiceTest {

    @Mock
    private StudentSubjectInterestRepository studentSubjectInterestRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentSubjectInterestService studentSubjectInterestService;

    private StudentEntity student;
    private StudentSubjectInterestSemesterDTO semesterDTO;
    private StudentSubjectInterestEntity interestEntity;

    @BeforeEach
    void setUp() {
        student = new StudentEntity();
        student.setId(1L);

        semesterDTO = new StudentSubjectInterestSemesterDTO(2023, 1);
        interestEntity = new StudentSubjectInterestEntity(2023, 1, student, 101);
    }

    @Test
    void testListInterest() {
        when(studentSubjectInterestRepository.findAllByStudentId(student.getId()))
                .thenReturn(Arrays.asList(interestEntity));

        List<StudentSubjectInterestEntity> result = studentSubjectInterestService.listInterest(student.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(interestEntity, result.get(0));
        verify(studentSubjectInterestRepository, times(1)).findAllByStudentId(student.getId());
    }

    @Test
    void testCreateInterest() {
        int sigaaSubjectId = 101;

        studentSubjectInterestService.createInterest(sigaaSubjectId, semesterDTO, student);

        verify(studentSubjectInterestRepository, times(1)).save(any(StudentSubjectInterestEntity.class));
    }

    @Test
    void testDeleteInterest() {
        int sigaaSubjectId = 101;

        studentSubjectInterestService.deleteInterest(sigaaSubjectId, student);

        verify(studentSubjectInterestRepository, times(1))
                .deleteBySigaaSubjectIdAndStudentId(sigaaSubjectId, student.getId());
    }
}
