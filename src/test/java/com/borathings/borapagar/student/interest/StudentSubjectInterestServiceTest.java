package com.borathings.borapagar.student.interest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentHelperService;
import com.borathings.borapagar.student.interest.dto.StudentSubjectAddInterestDTO;
import com.borathings.borapagar.student.interest.dto.StudentSubjectInterestDTO;
import com.borathings.borapagar.student.interest.exception.InterestInCompletedSubjectException;
import com.borathings.borapagar.student.takenComponent.TakenComponentEntity;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
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
    private ComponentMapper componentMapper;

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
    void listInterests_SimpleTest_ShouldReturnStudentInterests() {

        Long studentId = 1L;
        String subjectCode = "PROG1";

        StudentSubjectInterestEntity interest = new StudentSubjectInterestEntity();
        interest.setId(101L);
        interest.setSubjectCode(subjectCode);
        interest.setYear(2025);
        interest.setPeriod(1);
        when(studentSubjectInterestRepository.findAllByStudentId(studentId)).thenReturn(List.of(interest));

        ComponentEntity component = new ComponentEntity();
        component.setCode(subjectCode);
        component.setName("Programação I");
        when(componentService.findAllDiscinctByCodeIn(List.of(subjectCode))).thenReturn(List.of(component));

        ComponentDTO componentDto = null;
        when(componentMapper.toDto(any(ComponentEntity.class))).thenReturn(componentDto);

        StudentEntity student = new StudentEntity();
        UserEntity user = new UserEntity();
        user.setFriends(Collections.emptySet()); // O ponto chave para simplificar!
        student.setUser(user);
        when(studentHelperService.findByIdOrError(studentId)).thenReturn(student);

        List<StudentSubjectInterestDTO> result = studentSubjectInterestService.listInterests(studentId);

        StudentSubjectInterestDTO resultDto = result.get(0);
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.interestId()).isEqualTo(interest.getId());
    }

    @Test
    void shouldCreateInterestSuccessfully() {
        ComponentEntity component = new ComponentEntity();
        component.setComponentId(10);
        component.setCode("63313");

        // Student não tem histórico, nem turma, nem interesse
        student.setTakenComponents(List.of());
        student.setClassrooms(Set.of());

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

        assertEquals("Componente não encontrado: 63313", ex.getMessage());
        verify(studentSubjectInterestRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenAlreadyInTranscript() {
        StudentEntity student = new StudentEntity();
        student.setId(1L);
        student.setClassrooms(Set.of());

        ComponentEntity component = new ComponentEntity();
        component.setComponentId(10);
        component.setCode("63313");

        TakenComponentEntity transcript = new TakenComponentEntity();
        transcript.setComponentId(10);

        student.setTakenComponents(List.of(transcript));

        StudentSubjectAddInterestDTO semesterDTO = new StudentSubjectAddInterestDTO("63313", 2025, 1);

        when(componentService.findByCode("63313")).thenReturn(Optional.of(component));
        when(componentService.findAllByComponentId(List.of(10))).thenReturn(List.of(component));

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
        student.setClassrooms(Set.of(classroom));
        student.setTakenComponents(List.of());

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

        student.setClassrooms(Set.of());
        student.setTakenComponents(List.of());

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
