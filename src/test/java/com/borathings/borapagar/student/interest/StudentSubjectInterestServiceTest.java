package com.borathings.borapagar.student.interest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import java.util.List;
import java.util.Set;
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

    @Mock
    private UserMapper userMapper;

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
        StudentEntity studentMock = mock(StudentEntity.class);
        UserEntity userMock = mock(UserEntity.class);

        // Mock da cadeia de chamadas
        when(studentService.findByIdOrError(anyLong())).thenReturn(studentMock);
        when(studentMock.getUser()).thenReturn(userMock);
        when(userMock.getFriends()).thenReturn(Set.of());

        // Mock do repository
        when(studentSubjectInterestRepository.findAllByStudentId(anyLong())).thenReturn(List.of(interestEntity));

        List<StudentSubjectInterestDTO> result = studentSubjectInterestService.listInterests(student.getId());

        assertNotNull(result);
        assertEquals(1, result.size());

        // Ajustar conforme assinatura do DTO
        StudentSubjectInterestDTO expected = new StudentSubjectInterestDTO(
                interestEntity.getId(),
                null, // Aqui você pode ajustar conforme getComponentByCode mockado ou não.
                interestEntity.getYear(),
                interestEntity.getPeriod(),
                List.of());

        assertEquals(expected, result.get(0));

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
