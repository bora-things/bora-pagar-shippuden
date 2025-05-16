package com.borathings.borapagar.student.interest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
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

    @Mock
    private SubjectSigaaClient subjectClient;

    @InjectMocks
    private StudentSubjectInterestService studentSubjectInterestService;

    private StudentEntity student;
    private StudentSubjectAddInterestDTO semesterDTO;
    private StudentSubjectInterestEntity interestEntity;

    @BeforeEach
    void setUp() {
        student = new StudentEntity();
        student.setId(1L);

        semesterDTO = new StudentSubjectAddInterestDTO("63313", 2023, 1);
        interestEntity = new StudentSubjectInterestEntity(2023, 1, student, "101");
    }

    @Test
    void testListInterest() {
        when(studentSubjectInterestRepository.findAllByStudentId(student.getId()))
                .thenReturn(Arrays.asList(interestEntity));

        List<StudentSubjectInterestDTO> result = studentSubjectInterestService.listInterests(student.getId());
        var actual = new StudentSubjectInterestDTO(null, null, semesterDTO.year(), semesterDTO.period());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(actual, result.get(0));
        verify(studentSubjectInterestRepository, times(1)).findAllByStudentId(student.getId());
    }

    @Test
    void testCreateInterest() {
        studentSubjectInterestService.createInterest(semesterDTO, student);

        verify(studentSubjectInterestRepository, times(1)).save(any(StudentSubjectInterestEntity.class));
    }

    @Test
    void testDeleteInterest() {
        String sigaaSubjectId = "IMD0001";

        studentSubjectInterestService.deleteInterest(sigaaSubjectId, student);

        verify(studentSubjectInterestRepository, times(1))
                .deleteBySigaaSubjectIdAndStudentId(sigaaSubjectId, student.getId());
    }
}
