package com.borathings.borapagar.student.interest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.SubjectSigaaClient;
import com.borathings.borapagar.core.exception.subjectInterest.InterestInCompletedSubjectException;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentHelperService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.student.transcript.TranscriptComponentEntity;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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

    @InjectMocks
    private StudentSubjectInterestService studentSubjectInterestService;

    @Mock
    private ComponentService componentService;

    @Mock
    private StudentHelperService studentHelperService;

    @Mock
    private SubjectSigaaClient subjectSigaaClient;

    @Mock
    private UserMapper userMapper;

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
        when(studentHelperService.findByIdOrError(anyLong())).thenReturn(studentMock);
        when(studentMock.getUser()).thenReturn(userMock);
        when(userMock.getFriends()).thenReturn(Set.of());
        when(subjectSigaaClient.getComponentByCode(any(String.class))).thenReturn(null);

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
    void shouldCreateInterestSuccessfully() {
        ComponentEntity component = new ComponentEntity();
        component.setComponentId(10);
        component.setCode("63313");

        // Student não tem histórico, nem turma, nem interesse
        student.setTranscriptComponents(List.of());
        student.setClassrooms(List.of());

        when(componentService.findByCode("63313")).thenReturn(Optional.of(component));
        when(studentSubjectInterestRepository.findBySubjectCodeAndStudentId("63313", student.getId()))
                .thenReturn(Optional.empty());

        studentSubjectInterestService.createInterest(semesterDTO, student);

        verify(studentSubjectInterestRepository, times(1)).save(any(StudentSubjectInterestEntity.class));
    }

    @Test
    void shouldThrowWhenComponentNotFound() {
        when(componentService.findByCode("63313")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> studentSubjectInterestService.createInterest(semesterDTO, student));

        assertEquals("Componente não encontrado", ex.getMessage());
        verify(studentSubjectInterestRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAlreadyInTranscript() {
        ComponentEntity component = new ComponentEntity();
        component.setComponentId(10);
        component.setCode("63313");

        TranscriptComponentEntity transcript = new TranscriptComponentEntity();
        transcript.setComponentId(10);
        student.setTranscriptComponents(List.of(transcript));
        student.setClassrooms(List.of());

        when(componentService.findByCode("63313")).thenReturn(Optional.of(component));

        assertThrows(
                InterestInCompletedSubjectException.class,
                () -> studentSubjectInterestService.createInterest(semesterDTO, student));

        verify(studentSubjectInterestRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAlreadyInClassroom() {
        ComponentEntity component = new ComponentEntity();
        component.setComponentId(10);
        component.setCode("63313");

        ClassroomEntity classroom = new ClassroomEntity();
        classroom.setComponentCode("63313");
        student.setClassrooms(List.of(classroom));
        student.setTranscriptComponents(List.of());

        when(componentService.findByCode("63313")).thenReturn(Optional.of(component));

        assertThrows(
                InterestInCompletedSubjectException.class,
                () -> studentSubjectInterestService.createInterest(semesterDTO, student));

        verify(studentSubjectInterestRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAlreadyInterested() {
        ComponentEntity component = new ComponentEntity();
        component.setComponentId(10);
        component.setCode("63313");

        student.setClassrooms(List.of());
        student.setTranscriptComponents(List.of());

        when(componentService.findByCode("63313")).thenReturn(Optional.of(component));
        when(studentSubjectInterestRepository.findBySubjectCodeAndStudentId("63313", student.getId()))
                .thenReturn(Optional.of(mock(StudentSubjectInterestEntity.class)));

        assertThrows(
                InterestInCompletedSubjectException.class,
                () -> studentSubjectInterestService.createInterest(semesterDTO, student));

        verify(studentSubjectInterestRepository, never()).save(any());
    }

    @Test
    void testDeleteInterest() {
        String sigaaSubjectId = "IMD0001";

        studentSubjectInterestService.deleteInterest(sigaaSubjectId, student);

        verify(studentSubjectInterestRepository, times(1))
                .deleteBySigaaSubjectIdAndStudentId(sigaaSubjectId, student.getId());
    }
}
